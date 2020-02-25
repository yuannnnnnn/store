package cn.tedu.store.service;

import java.util.List;

import cn.tedu.store.entity.Address;

/**
 * 处理收货地址数据的业务接口
 */
public interface IAddressService {

	/**
	 * 创建新的收货地址
	 * @param uid 当前登录的用户的id
	 * @param username 当前登录的用户名
	 * @param address 用户提交的收货地址数据
	 */
	void addnew(Integer uid, String username, Address address);
	
	/**
	 * 查询某用户的收货地址数据列表
	 * @param uid 用户的id
	 * @return 该用户的收货地址数据列表
	 */
	List<Address> getByUid(Integer uid);
	
	/**
	 * 设置默认收货地址
	 * @param aid 收货地址数据的id
	 * @param uid 当前登录的用户的id
	 * @param username 当前登录的用户名
	 */
	void setDefault(Integer aid, Integer uid, String username);
	
	/**
	 * 删除收货地址
	 * @param aid 收货地址数据的id
	 * @param uid 当前登录的用户的id
	 * @param username 当前登录的用户名
	 */
	void delete(Integer aid, Integer uid, String username);
	
	/**
	 * 根据收货地址数据的id，查询收货地址详情
	 * @param aid 收货地址数据的id
	 * @param uid 当前登录的用户的id
	 * @return 匹配的收货地址详情
	 */
	Address getByAid(Integer aid, Integer uid);
	
}




