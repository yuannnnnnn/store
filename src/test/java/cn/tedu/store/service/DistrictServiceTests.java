package cn.tedu.store.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.District;
import cn.tedu.store.service.ex.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistrictServiceTests {

	@Autowired
	private IDistrictService service;
	
	@Test
	public void getByParent() {
		try {
			String parent = "86";
			List<District> list = service.getByParent(parent);
			System.err.println("count=" + list.size());
			for (District item : list) {
				System.err.println(item);
			}
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void getNameByCode() {
		try {
			String code = "430000";
			String result = service.getNameByCode(code);
			System.err.println(result);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
	
}









