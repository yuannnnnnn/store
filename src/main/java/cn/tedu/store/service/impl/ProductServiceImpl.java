package cn.tedu.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tedu.store.entity.Product;
import cn.tedu.store.mapper.ProductMapper;
import cn.tedu.store.service.IProductService;
import cn.tedu.store.service.ex.ProductNotFoundException;

/**
 * 处理商品数据的业务层实现类
 */
@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductMapper productMapper;

	@Override
	public Product getById(Integer id) {
		Product product = findById(id);
		
		if (product == null) {
			throw new ProductNotFoundException("尝试访问的商品数据不存在");
		}
		
		product.setPriority(null);
		product.setCreatedUser(null);
		product.setCreatedTime(null);
		product.setModifiedUser(null);
		product.setModifiedTime(null);
		return product;
	}
	
	@Override
	public List<Product> getHotList() {
		List<Product> list = findHotList();
		for (Product product : list) {
			product.setPriority(null);
			product.setCreatedUser(null);
			product.setCreatedTime(null);
			product.setModifiedUser(null);
			product.setModifiedTime(null);
		}
		return list;
	}

	/**
	 * 根据商品id查询商品详情
	 * @param id 商品id
	 * @return 匹配的商品详情，如果没有匹配的数据，则返回null
	 */
	private Product findById(Integer id) {
		return productMapper.findById(id);
	}
	
	/**
	 * 查询热销商品的前4名
	 * @return 热销商品的前4名的集合
	 */
	private List<Product> findHotList() {
		return productMapper.findHotList();
	}

}
