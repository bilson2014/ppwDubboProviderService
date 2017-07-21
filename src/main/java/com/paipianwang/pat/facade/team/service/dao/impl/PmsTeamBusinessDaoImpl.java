package com.paipianwang.pat.facade.team.service.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.paipianwang.pat.common.core.dao.impl.BaseDaoImpl;
import com.paipianwang.pat.facade.team.entity.PmsTeamBusiness;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamBusinessDao;

@Repository
public class PmsTeamBusinessDaoImpl extends BaseDaoImpl<PmsTeamBusiness> implements PmsTeamBusinessDao {

	public static final String SQL_DELETE_BYTEAMID="deleteByTeamId";
	public static final String SQL_FIND_TEAMID_BY_BUSINESS="findTeamIdbyBusiness";
	public static final String SQL_FIND_BY_TEAMLIST="sqlFindByTeamList";
	
	@Autowired
	private SqlSessionTemplate sessionTemplate = null;
	
	/**
	 * 删除供应商业务范围
	 */
	@Override
	public long deleteByTeamId(long teamId) {
		return sessionTemplate.delete(getStatement(SQL_DELETE_BYTEAMID), teamId);
	}

	/**
	 * 根据业务名称查询对应供应商id集合
	 */
	@Override
	public List<Long> getTeamidByBusiness(String businessName) {
		return sessionTemplate.selectList(getStatement(SQL_FIND_TEAMID_BY_BUSINESS),businessName);
	}

	/**
	 * 根据供应商集合获取业务集合
	 */
	@Override
	public List<PmsTeamBusiness> getByTeams(Map<String, Object> paramMap) {
		return sessionTemplate.selectList(getStatement(SQL_FIND_BY_TEAMLIST),paramMap);
	}
	
}
