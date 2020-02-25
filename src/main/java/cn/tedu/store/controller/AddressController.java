package cn.tedu.store.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.entity.Address;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.util.JsonResult;

@RequestMapping("addresses")
@RestController
public class AddressController extends BaseController {
	
	@Autowired
	private IAddressService addressService;
	
	@RequestMapping("addnew")
	public JsonResult<Void>	addnew(Address address, HttpSession session) {
		// 从Session中获取uid和username
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		// 调用业务对象的方法执行业务
		addressService.addnew(uid, username, address);
		// 响应成功
		return new JsonResult<>(OK);
	}
	
	@GetMapping({"", "/"})
	public JsonResult<List<Address>> getByUid(HttpSession session) {
		Integer uid = getUidFromSession(session);
		List<Address> data = addressService.getByUid(uid);
		return new JsonResult<>(OK, data);
	}

	@RequestMapping("{aid}/set_default")
	public JsonResult<Void> setDefault(
			@PathVariable("aid") Integer aid, 
			HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		addressService.setDefault(aid, uid, username);
		return new JsonResult<>(OK);
	}
	
	@RequestMapping("{aid}/delete")
	public JsonResult<Void> delete(
			@PathVariable("aid") Integer aid, 
			HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		addressService.delete(aid, uid, username);
		return new JsonResult<>(OK);
	}
	
}









