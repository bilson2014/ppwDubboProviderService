package com.paipianwang.pat.facade.team.service.dao;


import java.util.List;

import com.paipianwang.pat.common.core.dao.BaseDao;
import com.paipianwang.pat.facade.team.entity.PmsProvince;

/**
 * city表数据访问接口
 * @author Jack
 * @version 1.0
 *
 */
public interface PmsProvinceDao extends BaseDao<PmsProvince>{

	List<PmsProvince> getAll();

	PmsProvince findProvinceById(String provinceId);

	

}
