package com.paipianwang.pat.facade.team.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.facade.team.entity.PmsCity;
import com.paipianwang.pat.facade.team.service.PmsCityFacade;
import com.paipianwang.pat.facade.team.service.biz.PmsCityBiz;
/**
 * city Dubbo服务接口实现
 * @author Jack
 * @version 1.0
 */
@Service("pmsCityFacade")
public class PmsCityServiceImpl implements PmsCityFacade {
	
	private static Logger logger = LoggerFactory.getLogger("error");

	@Autowired
	private final PmsCityBiz biz = null;

	@Override
	public List<PmsCity> findCitysByProvinceId(String provinceId) {
		return biz.findCitysByProvinceId(provinceId);
	}

	@Override
	public List<PmsCity> getAll() {
		return biz.getAll();
	}

	
}
