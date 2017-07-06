package com.paipianwang.pat.facade.team.service.dao;

import java.util.List;

import com.paipianwang.pat.common.core.dao.BaseDao;
import com.paipianwang.pat.facade.team.entity.PmsTeamBusiness;

public interface PmsTeamBusinessDao extends BaseDao<PmsTeamBusiness> {

	/**
	 * 删除供应商业务范围
	 * @param teamId
	 * @return
	 */
	public long deleteByTeamId(long teamId);
	
	/**
	 * 根据业务名称查询供应商id集合
	 * @param businessName
	 * @return
	 */
	public List<Long> getTeamidByBusiness(String businessName);
}
