package com.paipianwang.pat.facade.team.service.biz;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.facade.team.entity.PmsProvince;
import com.paipianwang.pat.facade.team.service.dao.PmsCityDao;
import com.paipianwang.pat.facade.team.service.dao.PmsProvinceDao;
/**
 * 订单--服务层接口
 * 事物管理层
 * @author Jack
 * @version 1.0
 *
 */
@Service
public class PmsProvinceBiz {

	@Autowired
	private PmsProvinceDao pmsProvinceDao;

	public List<PmsProvince> getAll() {
		return pmsProvinceDao.getAll();
	}

	public PmsProvince findProvinceById(final String provinceId) {
		return pmsProvinceDao.findProvinceById(provinceId);
	}

	
	
}
