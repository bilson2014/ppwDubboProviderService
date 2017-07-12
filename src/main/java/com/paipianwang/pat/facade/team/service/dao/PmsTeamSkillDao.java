package com.paipianwang.pat.facade.team.service.dao;

import java.util.List;

import com.paipianwang.pat.common.core.dao.BaseDao;
import com.paipianwang.pat.facade.team.entity.PmsTeamSkill;

public interface PmsTeamSkillDao extends BaseDao<PmsTeamSkill> {

	/**
	 * 删除供应商业务技能
	 * @param teamId
	 * @return
	 */
	public long deleteByTeamId(long teamId);
	
	/**
	 * 根据业务技能名称查询供应商id集合
	 * @param businessName
	 * @return
	 */
	public List<Long> getTeamidByBusinessSkill(String skillName);
}
