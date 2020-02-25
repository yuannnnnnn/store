package cn.tedu.store.mapper;

import java.util.List;

import cn.tedu.store.entity.Product;

/**
 * 处理商品数据的持久层接口
 */
public interface ProductMapper {
	
	/**
	 * 根据商品id查询商品详情
	 * @param id 商品id
	 * @return 匹配的商品详情，如果没有匹配的数据，则返回null
	 */
	Product findById(Integer id);

	/**
	 * 查询热销商品的前4名
	 * @return 热销商品的前4名的集合
	 */
	List<Product> findHotList();

}






