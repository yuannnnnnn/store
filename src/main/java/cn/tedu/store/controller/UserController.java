package cn.tedu.store.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.tedu.store.controller.ex.FileEmptyException;
import cn.tedu.store.controller.ex.FileSizeException;
import cn.tedu.store.controller.ex.FileStateException;
import cn.tedu.store.controller.ex.FileTypeException;
import cn.tedu.store.controller.ex.FileUploadIOException;
import cn.tedu.store.entity.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.util.JsonResult;

/**
 * 处理用户相关请求的控制器类
 */
@RequestMapping("users")
@RestController
public class UserController extends BaseController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping("reg")
	public JsonResult<Void> reg(User user) {
		// 调用业务对象执行注册
		userService.reg(user);
		
		// 返回
		return new JsonResult<>(OK);
	}
	
	@RequestMapping("login")
	public JsonResult<User> login(
			String username, String password,
			HttpSession session) {
		// 调用业务对象的方法执行登录，并获取返回值
		User data = userService.login(username, password);
		System.err.println("UserController.login()");
		System.err.println("\t成功登录的用户：" + data);
		// 登录成功，将uid和username存入到Session中
		session.setAttribute("uid", data.getUid());
		session.setAttribute("username", data.getUsername());
		Integer uid = getUidFromSession(session);
		System.err.println("\tSession中的uid=" + uid);
		// 将以上返回值和状态码OK封装到响应结果中，并返回
		return new JsonResult<>(OK, data);
	}
	
	@RequestMapping("change_password")
	public JsonResult<Void> changePassword(
			String oldPassword, String newPassword, 
			HttpSession session) {
		// 调用session.getAttribute()获取uid和username
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session); 
		// 调用业务对象执行修改密码
		userService.changePassword(uid, username, oldPassword, newPassword);
		// 返回成功
		return new JsonResult<>(OK);
	}
	                                         
	@GetMapping("get_by_uid")
	public JsonResult<User> getByUid(HttpSession session) {
		// 从Session中获取uid
		Integer uid = getUidFromSession(session);
		// 调用业务对象执行获取数据
		User data = userService.getByUid(uid);
		// 响应成功和数据
		return new JsonResult<>(OK, data);
	}
	
	@RequestMapping("change_info")
	public JsonResult<Void> changeInfo(User user, HttpSession session) {
		// 从Session中获取uid和username
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		// 调用业务对象执行修改用户资料
		userService.changeInfo(uid, username, user);
		// 响应成功
		return new JsonResult<>(OK);
	}
	
	/**
	 * 头像文件大小的上限值
	 */
	public static final int AVATAR_MAX_SIZE = 600 * 1024;
	/**
	 * 允许上传的头像的文件类型
	 */
	public static final List<String> AVATAR_TYPES = new ArrayList<String>();
	 
	/**
	 * 初始化允许上传的头像的文件类型
	 */
	static {
		AVATAR_TYPES.add("image/jpeg");
		AVATAR_TYPES.add("image/png");
		AVATAR_TYPES.add("image/bmp");
		AVATAR_TYPES.add("image/gif");
	}
	
	@PostMapping("change_avatar")
	public JsonResult<String> changeAvatar(
			@RequestParam("file") MultipartFile file,
			HttpSession session) {
		// 判断上传的文件是否为空
		if (file.isEmpty()) {
			// 是：抛出异常
			throw new FileEmptyException("上传的头像文件不允许为空");
		}
		
		// 判断上传的文件大小是否超出限制值
		if (file.getSize() > AVATAR_MAX_SIZE) {
			// 是：抛出异常
			throw new FileSizeException("不允许上传超过" + (AVATAR_MAX_SIZE / 1024) + "KB的头像文件");
		}
		
		// 判断上传的文件类型是否超出限制
		String contentType = file.getContentType();
		if (!AVATAR_TYPES.contains(contentType)) {
			// 是：抛出异常
			throw new FileTypeException("不支持使用该类型的文件作为头像，允许的文件类型：\n" + AVATAR_TYPES);
		}
		
		// 保存头像文件的文件夹
		String parent = session.getServletContext().getRealPath("upload");
		File dir = new File(parent);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		// 保存的头像文件的文件名
		String suffix = "";
		String originalFilename = file.getOriginalFilename();
		int i = originalFilename.lastIndexOf(".");
		if (i > 0) {
			suffix = originalFilename.substring(i);
		}
		String filename = UUID.randomUUID().toString() + suffix;
		
		// 创建文件对象，表示保存的头像文件
		File dest = new File(dir, filename);
		// 执行保存头像文件
		try {
			file.transferTo(dest);
		} catch (IllegalStateException e) {
			// 抛出异常
			throw new FileStateException(
				"文件状态异常，可能文件已被移动或删除");
		} catch (IOException e) {
			// 抛出异常
			throw new FileUploadIOException(
				"上传文件时读写错误，请稍后再次尝试");
			
		}
		
		// 头像路径
		String avatar = "/upload/" + filename;
		// 从Session中获取uid和username
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		// 将头像写入到数据库中
		userService.changeAvatar(uid, username, avatar);
		
		// 返回成功与头像路径
		return new JsonResult<>(OK, avatar);
	}
	
}






