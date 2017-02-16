package com.paipianwang.pat.facade.team.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.facade.team.entity.PmsProvince;
import com.paipianwang.pat.facade.team.service.PmsProvinceFacade;
import com.paipianwang.pat.facade.team.service.biz.PmsProvinceBiz;
/**
 * Province Dubbo服务接口实现
 * @author Jack
 * @version 1.0
 */
@Service("pmsProvinceFacade")
public class PmsProvinceServiceImpl implements PmsProvinceFacade {
	
	@Autowired
	private final PmsProvinceBiz biz = null;

	@Override
	public List<PmsProvince> getAll() {
		return biz.getAll();
	}

	@Override
	public PmsProvince findProvinceById(final String provinceId) {
		return biz.findProvinceById(provinceId);
	}

}
