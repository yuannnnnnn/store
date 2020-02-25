package cn.tedu.store.mapper;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTests {
	
	@Autowired
	private UserMapper mapper;
	
	@Test
	public void insert() {
		User user = new User();
		user.setUsername("mybatis");
		user.setPassword("1234");
		Integer rows = mapper.insert(user);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void updateInfoByUid() {
		User user = new User();
		user.setUid(19);
		user.setPhone("13800138019");
		user.setEmail("root@tedu.cn");
		user.setGender(1);
		user.setModifiedUser("系统管理员");
		user.setModifiedTime(new Date());
		Integer rows = mapper.updateInfoByUid(user);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void updateAvatarByUid() {
		Integer uid = 19;
		String avatar = "1234";
		String modifiedUser = "超级管理员";
		Date modifiedTime = new Date();
		Integer rows = mapper.updateAvatarByUid(uid, avatar, modifiedUser, modifiedTime);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void updatePasswordByUid() {
		Integer uid = 17;
		String password = "1234";
		String modifiedUser = "超级管理员";
		Date modifiedTime = new Date();
		Integer rows = mapper.updatePasswordByUid(uid, password, modifiedUser, modifiedTime);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void findByUid() {
		Integer uid = 19;
		User result = mapper.findByUid(uid);
		System.err.println(result);
	}
	
	@Test
	public void findByUsername() {
		String username = "root";
		User result = mapper.findByUsername(username);
		System.err.println(result);
	}

}









