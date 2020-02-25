package cn.tedu.store.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.service.ex.ServiceException;
import cn.tedu.store.vo.CartVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTests {

	@Autowired
	private ICartService service;
	
	@Test
	public void addToCart() {
		try {
			Integer pid = 10000007;
			Integer amount = 1;
			Integer uid = 2;
			String username = "不知道";
			service.addToCart(pid, amount, uid, username);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void getVOByUid() {
		Integer uid = 19;
		List<CartVO> list = service.getVOByUid(uid);
		System.err.println("count=" + list.size());
		for (CartVO item : list) {
			System.err.println(item);
		}
	}
	
	@Test
	public void addNum() {
		try {
			Integer cid = 12;
			Integer uid = 19;
			String username = "不知道";
			Integer num = service.addNum(cid, uid, username);
			System.err.println("OK. New num=" + num);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void getVOByCids() {
		Integer[] cids = {7,13,14,15,16,17};
		Integer uid = 19;
		List<CartVO> list = service.getVOByCids(cids, uid);
		System.err.println("count=" + list.size());
		for (CartVO item : list) {
			System.err.println(item);
		}
	}
	
}









