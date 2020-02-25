package cn.tedu.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.entity.Product;
import cn.tedu.store.service.IProductService;
import cn.tedu.store.util.JsonResult;

@RestController
@RequestMapping("products")
public class ProductController extends BaseController {

	@Autowired
	private IProductService productService;
	
	@RequestMapping("hot_list")
	public JsonResult<List<Product>> getHotList() {
		List<Product> data = productService.getHotList();
		return new JsonResult<>(OK, data);
	}
	
	@GetMapping("{id}/details")
	public JsonResult<Product> getById(@PathVariable("id") Integer id) {
		// 调用业务对象执行获取数据
		Product data = productService.getById(id);
		// 返回成功和数据
		return new JsonResult<>(OK, data);
	}
	
}








