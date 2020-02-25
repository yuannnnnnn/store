package cn.tedu.store.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.Product;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductMapperTests {

	@Autowired
	private ProductMapper mapper;
	
	@Test
	public void findById() {
		Integer id = 10000017;
		Product result = mapper.findById(id);
		System.err.println(result);
	}
	
	@Test
	public void findHotList() {
		List<Product> list = mapper.findHotList();
		System.err.println("count=" + list.size());
		for (Product item : list) {
			System.err.println(item);
		}
	}
	
}








