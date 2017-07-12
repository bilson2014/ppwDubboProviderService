package com.paipianwang.pat.facade.team.service.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.paipianwang.pat.common.core.dao.impl.BaseDaoImpl;
import com.paipianwang.pat.facade.team.entity.PmsTeamSkill;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamSkillDao;

@Repository
public class PmsTeamSkillDaoImpl extends BaseDaoImpl<PmsTeamSkill> implements PmsTeamSkillDao {

	public static final String SQL_DELETE_SKILL_BYTEAMID="deleteSkillByTeamId";
	public static final String SQL_FIND_TEAMID_BY_SKILL="findTeamIdbySkill";
	
	@Autowired
	private SqlSessionTemplate sessionTemplate = null;
	
	/**
	 * 删除供应商的业务技能数据
	 */
	@Override
	public long deleteByTeamId(long teamId) {		
		return sessionTemplate.delete(SQL_DELETE_SKILL_BYTEAMID, teamId);
	}

	/**
	 * 根据业务技能名称查询所属供应商id集合
	 */
	@Override
	public List<Long> getTeamidByBusinessSkill(String skillName) {
		return sessionTemplate.selectList(SQL_FIND_TEAMID_BY_SKILL, skillName);
	}
	
}
