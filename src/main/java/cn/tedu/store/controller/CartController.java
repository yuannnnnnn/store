package cn.tedu.store.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.service.ICartService;
import cn.tedu.store.util.JsonResult;
import cn.tedu.store.vo.CartVO;

@RequestMapping("carts")
@RestController
public class CartController extends BaseController {
	
	@Autowired
	private ICartService cartService;
	
	@RequestMapping("add_to_cart")
	public JsonResult<Void> addToCart(Integer pid, Integer amount, HttpSession session) {
		// 从Session中获取uid和username
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		// 调用业务对象执行添加到购物车
		cartService.addToCart(pid, amount, uid, username);
		// 返回成功
		return new JsonResult<>(OK);
	}
	
	@GetMapping({"", "/"})
	public JsonResult<List<CartVO>> getVOByUid(HttpSession session) {
		// 从Session中获取uid
		Integer uid = getUidFromSession(session);
		// 调用业务对象执行查询数据
		List<CartVO> data = cartService.getVOByUid(uid);
		// 返回成功与数据
		return new JsonResult<>(OK, data);
	}

	@RequestMapping("{cid}/num/add")
	public JsonResult<Integer> addNum(
		@PathVariable("cid") Integer cid, 
		HttpSession session) {
		// 从Session中获取uid和username
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		// 调用业务对象执行增加数量
		Integer data = cartService.addNum(cid, uid, username);
		// 返回成功
		return new JsonResult<>(OK, data);
	}

	@GetMapping("list")
	public JsonResult<List<CartVO>> getVOByCids(Integer[] cids, HttpSession session) {
		// 从Session中获取uid
		Integer uid = getUidFromSession(session);
		// 调用业务对象执行查询数据
		List<CartVO> data = cartService.getVOByCids(cids, uid);
		// 返回成功与数据
		return new JsonResult<>(OK, data);
	}
	
}






