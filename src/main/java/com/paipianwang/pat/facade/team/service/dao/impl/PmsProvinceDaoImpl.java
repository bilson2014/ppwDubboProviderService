package com.paipianwang.pat.facade.team.service.dao.impl;


import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.paipianwang.pat.common.core.dao.impl.BaseDaoImpl;
import com.paipianwang.pat.facade.team.service.dao.PmsProvinceDao;
import com.paipianwang.pat.facade.team.entity.PmsProvince;
@Repository
public class PmsProvinceDaoImpl extends BaseDaoImpl<PmsProvince> implements PmsProvinceDao {

	public static final String SQL_FINDALL= "findAll";
	public static final String SQL_FIND_PROVINCE_BY_ID= "findProvinceById";
	@Autowired
	private SqlSessionTemplate sessionTemplate = null;

	@Override
	public List<PmsProvince> getAll() {
		return sessionTemplate.selectList(getStatement(SQL_FINDALL));
	}

	@Override
	public PmsProvince findProvinceById(String provinceId) {
		return sessionTemplate.selectOne(getStatement(SQL_FIND_PROVINCE_BY_ID),provinceId);
	}
	
}
