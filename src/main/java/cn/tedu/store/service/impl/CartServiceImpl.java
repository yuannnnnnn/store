package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tedu.store.entity.Cart;
import cn.tedu.store.entity.Product;
import cn.tedu.store.mapper.CartMapper;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.service.IProductService;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.CartNotFoundException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.vo.CartVO;

/**
 * 处理购物车数据的业务层实现类
 */
@Service
public class CartServiceImpl implements ICartService {
	
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private IProductService productService;

	@Override
	public void addToCart(Integer pid, Integer amount, Integer uid, String username) {
		// 根据参数pid和uid查询购物车中的数据
		Cart result = findByUidAndPid(uid, pid);
		Date now = new Date();
		// 判断查询结果是否为null
		if (result == null) {
			// 是：表示该用户并未将该商品添加到购物车
			// 创建Cart对象
			Cart cart = new Cart();
			// 封装数据：uid,pid,amount
			cart.setUid(uid);
			cart.setPid(pid);
			cart.setNum(amount);
			// 调用productService.getById()查询商品数据，得到商品价格
			Product product = productService.getById(pid);
			// 封装数据：price
			cart.setPrice(product.getPrice());
			// 封装数据：4个日志
			cart.setCreatedUser(username);
			cart.setCreatedTime(now);
			cart.setModifiedUser(username);
			cart.setModifiedTime(now);
			// 调用insert(cart)执行将数据插入到数据表中
			insert(cart);
		} else {
			// 否：表示该用户的购物车中已有该商品
			// 从查询结果中取出原数量，与参数amount相加，得到新的数量
			Integer num = result.getNum() + amount;
			// 执行更新数量
			updateNumByCid(result.getCid(), num, username, now);
		}
	}

	@Override
	public List<CartVO> getVOByUid(Integer uid) {
		return findVOByUid(uid);
	}

	@Override
	public Integer addNum(Integer cid, Integer uid, String username) {
		// 调用findByCid(cid)根据参数cid查询购物车数据
		Cart result = findByCid(cid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出CartNotFoundException
			throw new CartNotFoundException(
				"尝试访问的购物车数据不存在");
		}

		// 判断查询结果中的uid与参数uid是否不一致
		if (!result.getUid().equals(uid)) {
			// 是：抛出AccessDeniedException
			throw new AccessDeniedException("非法访问");
		}

		// 可选：检查商品的数量是否大于多少(适用于增加数量)或小于多少(适用于减少数量)
		// 根据查询结果中的原数量增加1得到新的数量num
		Integer num = result.getNum() + 1;

		// 创建当前时间对象，作为modifiedTime
		Date now = new Date();
		// 调用updateNumByCid(cid, num, modifiedUser, modifiedTime)执行修改数量
		updateNumByCid(cid, num, username, now);
		
		// 返回新的数量
		return num;
	}

	@Override
	public List<CartVO> getVOByCids(Integer[] cids, Integer uid) {
		List<CartVO> list = findVOByCids(cids);
//		for (CartVO cart : list) {
//			if (!cart.getUid().equals(uid)) {
//				list.remove(cart);
//			}
//		}
		Iterator<CartVO> it = list.iterator();
		while (it.hasNext()) {
			CartVO cart = it.next();
			if (!cart.getUid().equals(uid)) {
				it.remove();
			}
		}
		return list;
	}
	
	/**
	 * 插入购物车数据
	 * @param cart
	 */
	private void insert(Cart cart) {
		Integer rows = cartMapper.insert(cart);
		if (rows != 1) {
			throw new InsertException("插入商品数据时出现未知错误，请联系系统管理员");
		}
	}
	
	/**
	 * 修改购物车数据中商品的数量
	 * @param cid 购物车数据的id
	 * @param num 新的数量
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 */
	private void updateNumByCid(Integer cid, Integer num,
			String modifiedUser, Date modifiedTime) {
		Integer rows = cartMapper.updateNumByCid(cid, num, modifiedUser, modifiedTime);
		if (rows != 1) {
			throw new InsertException("修改商品数量时出现未知错误，请联系系统管理员");
		}
	}

	/**
	 * 根据购物车数据id查询购物车数据详情
	 * @param cid 购物车数据id
	 * @return 匹配的购物车数据详情，如果没有匹配的数据，则返回null
	 */
	private Cart findByCid(Integer cid) {
		return cartMapper.findByCid(cid);
	}
	
	/**
	 * 根据用户id和商品id查询购物车中的数据
	 * @param uid 用户id
	 * @param pid 商品id
	 * @return 匹配的购物车数据，如果该用户的购物车中并没有该商品，则返回null
	 */
	private Cart findByUidAndPid(Integer uid, Integer pid) {
		return cartMapper.findByUidAndPid(uid, pid);
	}
	
	/**
	 * 查询某用户的购物车数据
	 * @param uid 用户id
	 * @return 该用户的购物车数据的列表
	 */
	private List<CartVO> findVOByUid(Integer uid) {
		return cartMapper.findVOByUid(uid);
	}

	/**
	 * 根据若干个购物车数据id查询详情的列表
	 * @param cids 若干个购物车数据id
	 * @return 匹配的购物车数据详情的列表
	 */
	private List<CartVO> findVOByCids(Integer[] cids) {
		return cartMapper.findVOByCids(cids);
	}

	
}








