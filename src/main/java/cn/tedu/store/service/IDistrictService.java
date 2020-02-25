package cn.tedu.store.service;

import java.util.List;

import cn.tedu.store.entity.District;

/**
 * 处理省/市/区数据的业务层接口
 */
public interface IDistrictService {

	/**
	 * 获取全国所有省/某省所有市/某市所有区
	 * @param parent 父级代号，当获取某市所有区时，使用市的代号；当获取省所有市时，使用省的代号；当获取全国所有省时，使用"86"作为父级代号
	 * @return 全国所有省/某省所有市/某市所有区的列表
	 */
	List<District> getByParent(String parent);
	
	/**
	 * 根据省/市/区的行政代号获取省/市/区的名称
	 * @param code 省/市/区的行政代号
	 * @return 匹配的省/市/区的名称，如果没有匹配的数据，则返回null
	 */
	String getNameByCode(String code);
	
}





