package cn.tedu.store.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.User;
import cn.tedu.store.service.ex.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

	@Autowired
	private IUserService service;
	
	@Test
	public void reg() {
		try {
			User user = new User();
			user.setUsername("error");
			user.setPassword("1234");
			user.setGender(1);
			user.setPhone("13800138066");
			user.setEmail("error@tedu.cn");
			user.setAvatar("xxxxx");
			service.reg(user);
			System.err.println("注册成功！");
		} catch (ServiceException e) {
			System.err.println("注册失败！" + e.getClass().getSimpleName());
		}
	}
	
	@Test
	public void login() {
		try {
			String username = "error";
			String password = "1234";
			User user = service.login(username, password);
			System.err.println("登录成功！" + user);
		} catch (ServiceException e) {
			System.err.println("登录失败！" + e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void changePassword() {
		try {
			Integer uid = 18;
			String username = "密码管理员";
			String oldPassword = "1234";
			String newPassword = "8888";
			service.changePassword(uid, username, oldPassword, newPassword);
			System.err.println("OK");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void getByUid() {
		try {
			Integer uid = 20;
			User user = service.getByUid(uid);
			System.err.println(user);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void changeInfo() {
		try {
			Integer uid = 20;
			String username = "数据管理员";
			User user = new User();
			user.setGender(1);
			user.setPhone("13900139020");
			user.setEmail("user20@tedu.cn");
			service.changeInfo(uid, username, user);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
	

	@Test
	public void changeAvatar() {
		try {
			Integer uid = 19;
			String username = "头像管理员";
			String avatar = "1234";
			service.changeAvatar(uid, username, avatar);
			System.err.println("OK");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
}









