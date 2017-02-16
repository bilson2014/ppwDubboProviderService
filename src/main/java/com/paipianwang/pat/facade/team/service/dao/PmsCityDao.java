package com.paipianwang.pat.facade.team.service.dao;


import java.util.List;

import com.paipianwang.pat.common.core.dao.BaseDao;
import com.paipianwang.pat.facade.team.entity.PmsCity;

/**
 * city表数据访问接口
 * @author Jack
 * @version 1.0
 *
 */
public interface PmsCityDao extends BaseDao<PmsCity>{

	List<PmsCity> findCitysByProvinceId(String provinceId);

	List<PmsCity> getAll();

	

}
