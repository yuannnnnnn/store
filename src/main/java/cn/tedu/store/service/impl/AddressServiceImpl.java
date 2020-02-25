package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.store.entity.Address;
import cn.tedu.store.mapper.AddressMapper;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.service.IDistrictService;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.AddressCountLimitException;
import cn.tedu.store.service.ex.AddressNotFoundException;
import cn.tedu.store.service.ex.DeleteException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.UpdateException;

@Service("addressService")
public class AddressServiceImpl implements IAddressService {
	
	@Autowired
	private AddressMapper addressMapper;
	@Autowired
	private IDistrictService districtService;
	
	@Value("${project.max-count}")
	public int maxCount;
	
	@Override
	public void addnew(Integer uid, String username, Address address) {
		// 根据参数uid调用countByUid()方法，统计当前用户的收货地址数据的数量
		Integer count = countByUid(uid);
		// 判断数量是否达到上限值
		if (count >= maxCount) {
			// 是：抛出AddressCountLimitException
			throw new AddressCountLimitException(
				"收货地址数量已经达到上限(" + maxCount + ")！");
		}

		// 补全数据：省、市、区的名称
		String provinceName = districtService.getNameByCode(address.getProvinceCode());
		String cityName = districtService.getNameByCode(address.getCityCode());
		String areaName = districtService.getNameByCode(address.getAreaCode());
		address.setProvinceName(provinceName);
		address.setCityName(cityName);
		address.setAreaName(areaName);
		// 补全数据：将参数uid封装到参数address中
		address.setUid(uid);
		// 补全数据：根据以上统计的数量，得到正确的isDefault值，并封装
		Integer isDefault = count == 0 ? 1 : 0;
		address.setIsDefault(isDefault);
		// 补全数据：4项日志
		Date now = new Date();
		address.setCreatedUser(username);
		address.setCreatedTime(now);
		address.setModifiedUser(username);
		address.setModifiedTime(now);

		// 执行插入收货地址数据
		insert(address);
	}

	@Override
	public List<Address> getByUid(Integer uid) {
		List<Address> list = findByUid(uid);
		for (Address address : list) {
			address.setUid(null);
			address.setProvinceCode(null);
			address.setCityCode(null);
			address.setAreaCode(null);
			address.setCreatedUser(null);
			address.setCreatedTime(null);
			address.setModifiedUser(null);
			address.setModifiedTime(null);
		}
		return list;
	}

	@Override
	@Transactional
	public void setDefault(Integer aid, Integer uid, String username) {
		// 根据参数aid，调用findByAid()查询收货地址数据
		Address result = findByAid(aid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出AddressNotFoundException
			throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
		}

		// 判断查询结果中的uid与参数uid是否不一致(使用equals()判断)
		if (!result.getUid().equals(uid)) {
			// 是：抛出AccessDeniedException：非法访问
			throw new AccessDeniedException("非常访问");
		}

		// 将该用户的所有收货地址全部设置为非默认
		updateNonDefaultByUid(uid);

		// 执行设置默认收货地址
		updateDefaultByAid(aid, username, new Date());
	}

	@Override
	@Transactional
	public void delete(Integer aid, Integer uid, String username) {
		// 根据参数aid，调用findByAid()查询收货地址数据
		Address result = findByAid(aid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出AddressNotFoundException
			throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
		}

		// 判断查询结果中的uid与参数uid是否不一致(使用equals()判断)
		if (!result.getUid().equals(uid)) {
			// 是：抛出AccessDeniedException：非法访问
			throw new AccessDeniedException("非常访问");
		}

		// 根据参数aid，调用deleteByAid()执行删除
		deleteByAid(aid);

		// 判断查询结果中的isDefault是否为0
		if (result.getIsDefault() == 0) {
			return;
		}

		// 调用持久层的countByUid()统计目前还有多少收货地址
		Integer count = countByUid(uid);
		// 判断目前的收货地址的数量是否为0
		if (count == 0) {
			return;
		}

		// 调用findLastModified()找出最近修改的收货地址数据
		Address lastModified = findLastModified(uid);
		// 从以上查询结果中找出aid属性值
		Integer lastModifiedAid = lastModified.getAid();
		// 执行设置默认收货地址
		updateDefaultByAid(lastModifiedAid, username, new Date());
	}

	@Override
	public Address getByAid(Integer aid, Integer uid) {
		Address address = findByAid(aid);
		if (address == null) {
			throw new AddressNotFoundException("尝试访问的收货地址数据不存在");
		}
		
		if (!address.getUid().equals(uid)) {
			throw new AccessDeniedException("非法访问");
		}
		
		address.setProvinceCode(null);
		address.setCityCode(null);
		address.setAreaCode(null);
		address.setCreatedUser(null);
		address.setCreatedTime(null);
		address.setModifiedUser(null);
		address.setModifiedTime(null);
		
		return address;
	}
	
	/**
	 * 插入收货地址数据
	 * @param address 收货地址数据
	 */
	private void insert(Address address) {
		Integer rows = addressMapper.insert(address);
		if (rows != 1) {
			throw new InsertException("增加收货地址数据时出现未知错误，请联系系统管理员");
		}
	}
	
	/**
	 * 根据收货地址id删除数据
	 * @param aid 收货地址id
	 */
	private void deleteByAid(Integer aid) {
		Integer rows = addressMapper.deleteByAid(aid);
		if (rows != 1) {
			throw new DeleteException("删除收货地址数据时出现未知错误，请联系系统管理员");
		}
	}
	
	/**
	 * 将某用户的所有收货地址设置为非默认
	 * @param uid 用户的id
	 */
	private void updateNonDefaultByUid(Integer uid) {
		Integer rows = addressMapper.updateNonDefaultByUid(uid);
		if (rows < 1) {
			throw new UpdateException("更新收货地址数据时出现未知错误，请联系系统管理员");
		}
	}

	/**
	 * 将指定的收货地址设置为默认
	 * @param aid 收货地址数据的id
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 */
	private void updateDefaultByAid(
		Integer aid, String modifiedUser, Date modifiedTime) {
		// 根据以上aid，调用持久层的updateDefaultByAid()把这条收货地址设置为默认，并获取返回的受影响的行数
		Integer rows = addressMapper.updateDefaultByAid(aid, modifiedUser, modifiedTime);
		// 判断受影响的行数是否不为1
		if (rows != 1) {
			// 是：抛出UpdateException
			throw new UpdateException("更新收货地址数据时出现未知错误，请联系系统管理员");
		}
	}
	
	/**
	 * 统计某用户的收货地址数据的数量
	 * @param uid 用户的id
	 * @return 该用户的收货地址数据的数量
	 */
	private Integer countByUid(Integer uid) {
		return addressMapper.countByUid(uid);
	}
	
	/**
	 * 根据收货地址数据id，查询收货地址详情
	 * @param aid 收货地址数据id
	 * @return 匹配的收货地址详情，如果没有匹配的数据，则返回null
	 */
	private Address findByAid(Integer aid) {
		return addressMapper.findByAid(aid);
	}
	
	/**
	 * 查询某用户最后修改的收货地址
	 * @param uid 用户的id
	 * @return 该用户最后修改的收货地址，如果该用户没有收货地址数据，则返回null
	 */
	private Address findLastModified(Integer uid) {
		return addressMapper.findLastModified(uid);
	}
	
	/**
	 * 查询某用户的收货地址数据列表
	 * @param uid 用户的id
	 * @return 该用户的收货地址数据列表
	 */
	private List<Address> findByUid(Integer uid) {
		return addressMapper.findByUid(uid);
	}

}





