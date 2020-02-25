package cn.tedu.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.service.ex.UsernameDuplicateException;
import cn.tedu.store.util.JsonResult;

@RestController
@Deprecated
public class TestController {
	
	@RequestMapping("test")
	public JsonResult<Void> test() {
		throw new UsernameDuplicateException("xxx");
	}

}





