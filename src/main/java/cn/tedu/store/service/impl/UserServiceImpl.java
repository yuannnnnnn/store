package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.tedu.store.entity.User;
import cn.tedu.store.mapper.UserMapper;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameDuplicateException;

/**
 * 处理用户数据的业务层实现类
 */
@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public void reg(User user) {
		// 打桩输出
		System.err.println("UserServiceImpl.reg()");
		System.err.println("\t客户端提交的用户：" + user);
		// 根据参数user获取尝试注册的用户名
		String username = user.getUsername();
		// 调用持久层的User findByUsername(String username)方法，根据用户名查询用户数据
		User result = userMapper.findByUsername(username);
		// 判断查询结果是否不为null
		if (result != null) {
			// 是：表示用户名已经被占用，抛出UsernameDuplicateException
			throw new UsernameDuplicateException(
				"尝试注册的用户名(" + username + ")已经被占用");
		}
		
		// 创建当前时间对象
		Date now = new Date();
		// 补全数据：加密后的密码
		String salt = UUID.randomUUID().toString().toUpperCase();
		String md5Password = getMd5Password(user.getPassword(), salt);
		user.setPassword(md5Password);
		// 补全数据：盐值
		user.setSalt(salt);
		// 补全数据：isDelete(0)
		user.setIsDelete(0);
		// 补全数据：4项日志属性
		user.setCreatedUser(username);
		user.setCreatedTime(now);
		user.setModifiedUser(username);
		user.setModifiedTime(now);
		
		// 打桩输出
		System.err.println("\t即将注册的用户：" + user);
		// 表示用户名没有占用，允许注册
		// 调用持久层的Integer insert(User user)方法执行注册，并获取返回值(受影响的行数)
		Integer rows = userMapper.insert(user);
		// 判断受影响的行数是否不为1
		if (!rows.equals(1)) {
			// 是：插入数据时出现某种错误，抛出InsertException
			throw new InsertException(
				"添加用户数据出现未知错误，请联系系统管理员");
		}
	}
	
	@Override
	public User login(String username, String password) {
		// 打桩输出
		System.err.println("UserServiceImpl.login()");
		System.err.println("\t客户端提交的用户名：" + username);
		System.err.println("\t客户端提交的密码：" + password);
		// 调用userMapper的findByUsername()，根据参数username查询用户数据
		User result = userMapper.findByUsername(username);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户名错误");
		}

		// 判断查询结果中的isDelete是否为1
		if (result.getIsDelete() == 1) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户名错误");
		}

		// 从查询结果中获取盐值
		String salt = result.getSalt();
		// 调用getMd5Password()将参数password和salt结合起来加密
		String md5Password = getMd5Password(password, salt);
		
		// 打桩输出
		System.err.println("\t该用户加密使用的盐值：" + salt);
		System.err.println("\t客户端提交的密码加密：" + md5Password);
		System.err.println("\t数据库中存储的密码：" + result.getPassword());
		
		// 判断查询结果中的密码，与以上加密得到的密码，是否不一致
		if (!result.getPassword().equals(md5Password)) {
			// 是：抛出PasswordNotMatchException
			throw new PasswordNotMatchException("密码错误");
		}
		
		// 创建新的User对象
		User user = new User();
		// 将查询结果中的uid、username、avatar封装到新User对象中
		user.setUid(result.getUid());
		user.setUsername(result.getUsername());
		user.setAvatar(result.getAvatar());
		// 返回新User对象
		return user;
	}

	@Override
	public void changePassword(Integer uid, String username, String oldPassword, String newPassword) {
		// 调用userMapper的findByUid()方法，根据参数uid查询用户数据
		User result = userMapper.findByUid(uid);
		// 检查查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}

		// 检查查询结果中的isDelete是否为1
		if (result.getIsDelete().equals(1)) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}

		// 从查询结果中取出盐值
		String salt = result.getSalt();
		// 将参数oldPassword结合盐值加密，得到oldMd5Password
		String oldMd5Password = getMd5Password(oldPassword, salt);
		// 判断查询结果中的password与oldMd5Password是否不一致
		if (!result.getPassword().contentEquals(oldMd5Password)) {
			// 是：抛出PasswordNotMatchException
			throw new PasswordNotMatchException("原密码错误");
		}

		// 将参数newPassword结合盐值加密，得到newMd5Password
		String newMd5Password = getMd5Password(newPassword, salt);
		// 创建当前时间对象
		Date now = new Date();
		// 调用userMapper的updatePasswordByUid()更新密码，并获取返回值
		Integer rows = userMapper.updatePasswordByUid(
			uid, newMd5Password, username, now);
		// 判断以上返回的受影响行数是否不为1
		if (rows != 1) {
			// 是：抛出UpdateException
			throw new UpdateException("更新用户数据时出现未知错误，请联系系统管理员");
		}
	}

	@Override
	public User getByUid(Integer uid) {
		// 调用userMapper的findByUid()方法，根据参数uid查询用户数据
		User result = userMapper.findByUid(uid);
		// 检查查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}

		// 检查查询结果中的isDelete是否为1
		if (result.getIsDelete().equals(1)) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}

		// 创建新的User对象
		User user = new User();
		// 将以上查询结果中的username/phone/email/gender封装到新User对象中
		user.setUsername(result.getUsername());
		user.setPhone(result.getPhone());
		user.setEmail(result.getEmail());
		user.setGender(result.getGender());

		// 返回新User对象
		return user;
	}

	@Override
	public void changeInfo(Integer uid, String username, User user) {
		// 调用userMapper的findByUid()方法，根据参数uid查询用户数据
		User result = userMapper.findByUid(uid);
		// 检查查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}

		// 检查查询结果中的isDelete是否为1
		if (result.getIsDelete().equals(1)) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}
		
		// 向参数user中补全数据:uid
		user.setUid(uid);
		// 向参数user中补全数据:modifiedUser(username)
		user.setModifiedUser(username);
		// 向参数user中补全数据:modifiedTime(new Date())
		user.setModifiedTime(new Date());
		// 调用userMapper的updateInfoByUid(User user)方法执行修改，并获取返回值
		Integer rows = userMapper.updateInfoByUid(user);
		// 判断以上返回的受影响行数是否不为1
		if (rows != 1) {
			// 是：抛出UpdateException
			throw new UpdateException("更新用户数据时出现未知错误，请联系系统管理员");
		}
	}

	@Override
	public void changeAvatar(Integer uid, String username, String avatar) {
		// 调用userMapper的findByUid()方法，根据参数uid查询用户数据
		User result = userMapper.findByUid(uid);
		// 检查查询结果是否为null
		if (result == null) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}

		// 检查查询结果中的isDelete是否为1
		if (result.getIsDelete().equals(1)) {
			// 是：抛出UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}
		
		// 创建当前时间对象
		Date now = new Date();
		// 调用userMapper的updateAvatarByUid()方法执行更新，并获取返回值
		Integer rows = userMapper.updateAvatarByUid(uid, avatar, username, now);
		// 判断以上返回的受影响行数是否不为1
		if (rows != 1) {
			// 是：抛出UpdateException
			throw new UpdateException("更新用户数据时出现未知错误，请联系系统管理员");
		}
	}
	
	/**
	 * 执行密码加密
	 * @param password 原密码
	 * @param salt 盐值
	 * @return 加密后的密文
	 */
	private String getMd5Password(String password, String salt) {
		// 【加密规则】
		// 1. 无视原始密码的强度；
		// 2. 使用UUID作为盐值，在原始密码的左右两侧拼接；
		// 3. 循环加密3次。
		for (int i = 0; i < 3; i++) {
			password = DigestUtils.md5DigestAsHex(
				(salt + password + salt).getBytes())
					.toUpperCase();
		}
		return password;
	}

}








