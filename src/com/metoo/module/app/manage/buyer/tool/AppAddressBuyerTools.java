package com.metoo.module.app.manage.buyer.tool;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.core.domain.virtual.SysMap;
import com.metoo.core.query.support.IPageList;
import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.Address;
import com.metoo.foundation.domain.Area;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.domain.query.AddressQueryObject;
import com.metoo.foundation.service.IAddressService;
import com.metoo.foundation.service.IAreaService;

@Component
public class AppAddressBuyerTools {

	@Autowired
	private IAddressService addressService;
	@Autowired
	private IAreaService areaService;

	public Object verifyAdr(User user, Long area_id, String area_info, String mobile) {
		Address address = null;
		Map params = new HashMap();
		params.put("area_id", area_id);
		params.put("area_info", area_info);
		params.put("user_id", user.getId());
		params.put("userName", user.getUsername());
		params.put("mobile",user.getMobile());
		List<Address> addressList = this.addressService.query(
				"SELECT obj FROM Address obj WHERE obj.area.id=:area_id AND obj.area_info=:area_info AND obj.mobile=:mobile AND obj.user.id=:user_id AND obj.user.userName=:userName",
				params, -1, -1);
		if(addressList.size() != 0){
			address = addressList.get(0);
		}
		try {
			address = new Address();
			address.setAddTime(new Date());
			address.setTrueName(user.getUsername());
			address.setArea_info(CommUtil.null2String(area_info));
			address.setMobile(mobile);
			address.setTelephone(mobile);
			address.setDefault_val(user.getAddrs().size() > 0 ? 0 : 1);
			Area area = this.areaService.getObjById(CommUtil.null2Long(area_id));
			address.setArea(area);
			address.setUser(user);
			address.setEmail(user.getEmail());
			this.addressService.save(address);
			return address;
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
