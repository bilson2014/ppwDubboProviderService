package com.paipianwang.pat.facade.team.service.dao.impl;


import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.paipianwang.pat.common.core.dao.impl.BaseDaoImpl;
import com.paipianwang.pat.facade.team.service.dao.PmsCityDao;
import com.paipianwang.pat.facade.team.entity.PmsCity;
@Repository
public class PmsCityDaoImpl extends BaseDaoImpl<PmsCity> implements PmsCityDao {

	public static final String SQL_FIND_CITY_BY_PROVINCEID= "findCitysByProvinceId";
	public static final String SQL_GETALL= "getAll";
	@Autowired
	private SqlSessionTemplate sessionTemplate = null;

	@Override
	public List<PmsCity> findCitysByProvinceId(String provinceId) {
		return sessionTemplate.selectList(getStatement(SQL_FIND_CITY_BY_PROVINCEID), provinceId);
	}

	@Override
	public List<PmsCity> getAll() {
		return sessionTemplate.selectList(getStatement(SQL_GETALL));
	}

}
