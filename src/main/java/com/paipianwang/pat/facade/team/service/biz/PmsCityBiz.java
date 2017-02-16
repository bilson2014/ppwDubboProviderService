package com.paipianwang.pat.facade.team.service.biz;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.facade.team.entity.PmsCity;
import com.paipianwang.pat.facade.team.service.dao.PmsCityDao;
/**
 * 订单--服务层接口
 * 事物管理层
 * @author Jack
 * @version 1.0
 *
 */
@Service
public class PmsCityBiz {

	@Autowired
	private PmsCityDao pmsCityDao;

	public List<PmsCity> findCitysByProvinceId(String provinceId) {
		return pmsCityDao.findCitysByProvinceId(provinceId);
	}

	public List<PmsCity> getAll() {
		return pmsCityDao.getAll();
	}

	
	
}
