package cn.tedu.store.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ExceptionHandler;

import cn.tedu.store.controller.ex.FileEmptyException;
import cn.tedu.store.controller.ex.FileSizeException;
import cn.tedu.store.controller.ex.FileStateException;
import cn.tedu.store.controller.ex.FileTypeException;
import cn.tedu.store.controller.ex.FileUploadException;
import cn.tedu.store.controller.ex.FileUploadIOException;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.AddressCountLimitException;
import cn.tedu.store.service.ex.AddressNotFoundException;
import cn.tedu.store.service.ex.CartNotFoundException;
import cn.tedu.store.service.ex.DeleteException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.ProductNotFoundException;
import cn.tedu.store.service.ex.ServiceException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameDuplicateException;
import cn.tedu.store.util.JsonResult;

/**
 * 控制器类的基类
 */
public class BaseController {
	
	/**
	 * 操作成功的状态码
	 */
	public static final int OK = 2000;
	
	/**
	 * 从Session中获取uid
	 * @param session HttpSession对象
	 * @return 当前登录的用户的id
	 */
	protected final Integer getUidFromSession(HttpSession session) {
		return Integer.valueOf(
			session.getAttribute("uid").toString());
	}
	
	/**
	 * 从Session中获取用户名
	 * @param session HttpSession对象
	 * @return 当前登录的用户名
	 */
	protected final String getUsernameFromSession(HttpSession session) {
		return session.getAttribute("username").toString();
	}
	
	@ExceptionHandler({ServiceException.class, FileUploadException.class})
	public JsonResult<Void> handleException(Throwable e) {
		JsonResult<Void> jr = new JsonResult<>(e);
		
		if (e instanceof UsernameDuplicateException) {
			jr.setState(4000);
		} else if (e instanceof UserNotFoundException) {
			jr.setState(4001);
		} else if (e instanceof PasswordNotMatchException) {
			jr.setState(4002);
		} else if (e instanceof AddressCountLimitException) {
			jr.setState(4003);
		} else if (e instanceof AddressNotFoundException) {
			jr.setState(4004);
		} else if (e instanceof AccessDeniedException) {
			jr.setState(4005);
		} else if (e instanceof ProductNotFoundException) {
			jr.setState(4006);
		} else if (e instanceof CartNotFoundException) {
			jr.setState(4007);
		} else if (e instanceof InsertException) {
			jr.setState(5000);
		} else if (e instanceof UpdateException) {
			jr.setState(5001);
		} else if (e instanceof DeleteException) {
			jr.setState(5002);
		} else if (e instanceof FileEmptyException) {
			jr.setState(6000);
		} else if (e instanceof FileSizeException) {
			jr.setState(6001);
		} else if (e instanceof FileTypeException) {
			jr.setState(6002);
		} else if (e instanceof FileStateException) {
			jr.setState(6003);
		} else if (e instanceof FileUploadIOException) {
			jr.setState(6004);
		}
		
		return jr;
	}

}
