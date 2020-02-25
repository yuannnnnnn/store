package cn.tedu.store;

import java.sql.Connection;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private DataSource dataSource;

	@Test
	public void getConnection() throws Exception {
		Connection conn = dataSource.getConnection();
		System.err.println(conn);
	}
	
	@Test
	public void getMd5Password() {
		String password = "123456";
		// 【加密规则】
		// 1. 无视原始密码的强度；
		// 2. 使用UUID作为盐值，在原始密码的左右两侧拼接；
		// 3. 循环加密3次。
		String salt = UUID.randomUUID().toString();
		System.err.println(salt);
		for (int i = 0; i < 3; i++) {
			password = DigestUtils
				.md5DigestAsHex(
					(salt + password + salt).getBytes());
			System.err.println(password);
		}
		// 1234
		// 81dc9bdb52d04dc20036dbd8313ed055
		// 123456
		// e10adc3949ba59abbe56e057f20f883e
		// b42935857a8e526f523f9abfaeca6f75
		// 1
		// c4ca4238a0b923820dcc509a6f75849b
		// TeduJSD_1907
		// b940c7738697d16e91c19906ec96bc71
	}
	
	
}








