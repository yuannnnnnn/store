package cn.tedu.store.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.entity.Order;
import cn.tedu.store.service.IOrderService;
import cn.tedu.store.util.JsonResult;

@RestController
@RequestMapping("orders")
public class OrderController extends BaseController {

	@Autowired
	private IOrderService orderService;
	
	@RequestMapping("create")
	public JsonResult<Order> create(
			Integer aid, Integer[] cids,
			HttpSession session) {
		// 从Session中取出uid和username
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		// 调用业务对象执行业务
		Order data = orderService.create(aid, cids, uid, username);
		// 返回成功与数据
		return new JsonResult<>(OK, data);
	}
	
}









