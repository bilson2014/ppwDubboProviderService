package com.paipianwang.pat.facade.team.service.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.paipianwang.pat.common.core.dao.impl.BaseDaoImpl;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamTmpDao;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.entity.PmsTeamTmp;
@Repository
public class PmsTeamTmpDaoImpl extends BaseDaoImpl<PmsTeamTmp> implements PmsTeamTmpDao {

	public static final String SQL_DEL_TEAMTMP_BY_TEAMID= "delTeamTmpByTeamId";
	public static final String SQL_ADD_TEAMTMP= "addTeamTmp";
	public static final String SQL_DOES_HAVELATESTENABLETMP_BYTEAMID= "doesHaveLatestEnableTmpByTeamId";
	public static final String SQL_GET_LATESTTMP_BYTEAMID= "getLatestTmpByTeamId";
	public static final String SQL_UPDATE_TEAMTMP_CHECK= "updateTeamTmpCheck";
	public static final String SQL_GET_TEAMTMP_BYID= "getTeamTmpById";
	public static final String SQL_GET_TEAMTMP_BYTEAMID= "getTeamTmpByTeamId";
	@Autowired
	private SqlSessionTemplate sessionTemplate = null;

	@Override
	public void delTeamMapperByTeamId(PmsTeamTmp tmp) {
		sessionTemplate.update(getStatement(SQL_DEL_TEAMTMP_BY_TEAMID), tmp);
	}

	@Override
	public void addTeamTmp(PmsTeamTmp tmp) {
		sessionTemplate.insert(getStatement(SQL_ADD_TEAMTMP), tmp);
	}

	@Override
	public List<PmsTeamTmp> doesHaveLatestEnableTmpByTeamId(Long teamId) {
		return sessionTemplate.selectList(getStatement(SQL_DOES_HAVELATESTENABLETMP_BYTEAMID), teamId);
	}

	@Override
	public PmsTeam getLatestTmpByTeamId(Long teamId) {
		return sessionTemplate.selectOne(getStatement(SQL_GET_LATESTTMP_BYTEAMID), teamId);
	}

	@Override
	public void updateTeamTmpCheck(PmsTeamTmp teamTmp) {
		sessionTemplate.update(getStatement(SQL_UPDATE_TEAMTMP_CHECK), teamTmp);
	}

	@Override
	public PmsTeamTmp getTeamTmpById(Long id) {
		return sessionTemplate.selectOne(getStatement(SQL_GET_TEAMTMP_BYID), id);
	}

	@Override
	public List<PmsTeamTmp> getTeamTmpByTeamId(Integer teamId) {
		return sessionTemplate.selectList(getStatement(SQL_GET_TEAMTMP_BYTEAMID), teamId);
	}

}
