package com.paipianwang.pat.facade.team.service.dao.impl;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.paipianwang.pat.common.core.dao.impl.BaseDaoImpl;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamDao;
@Repository
public class PmsTeamDaoImpl extends BaseDaoImpl<PmsTeam> implements PmsTeamDao {

	public static final String SQL_GET_TEAM_INFO= "getTeamInfo";
	public static final String SQL_CHECK_EXIST= "checkExist";
	public static final String SQL_SAVE= "save";
	public static final String SQL_CHECK_TEAM= "checkTeam";
	public static final String SQL_FIND_TEAM_BY_LOGINNAME_PWD= "findTeamByLoginNameAndPwd";
	public static final String SQL_UPDATE_TEAM_ACCOUNT= "updateTeamAccount";
	public static final String SQL_UPDATE_TEAM_INFOMATION= "updateTeamInfomation";
	public static final String SQL_UPDATE_TEAM_DESCRIPTION= "updateTeamDescription";
	public static final String SQL_UPDATE_PASSWORD_BYLOGINNAME= "updatePasswordByLoginName";
	public static final String SQL_MODIFY_TEAM_PHONE= "modifyTeamPhone";
	public static final String SQL_GET_ALL= "getAll";
	public static final String SQL_GET_ALL_TEAMNAME= "getAllTeamName";
	public static final String SQL_UPDATE_TEAM_PHOTOURL= "saveTeamPhotoUrl";
	public static final String SQL_FIND_TEAM_BYARRAY= "findTeamByArray";
	public static final String SQL_DELETE= "delete";
	public static final String SQL_GET_ALL_NORECOMMEND= "getAllNoRecommend";
	public static final String SQL_ADD_RECOMMEND= "addRecommend";
	public static final String SQL_DOWN_SORT_BY_RECOMMENDSORT= "downSortByRecommendSort";
	public static final String SQL_UP_SORT_BY_TEAMID= "upSortByTeamId";
	public static final String SQL_UP_SORT_BY_RECOMMENDSORT= "upSortByRecommendSort";
	public static final String SQL_down_SORT_BY_TEAMID= "downSortByTeamId";
	public static final String SQL_UPDATE_RECOMMEND_BY_TEAMID= "updateRecommendByTeamId";
	public static final String SQL_UP_ALL_ABOVE_INDEX = "upAllAboveIndex";
	public static final String SQL_TEAM_RECOMMEND_LIST = "teamRecommendList";
	public static final String SQL_UNBIND_THIRD = "unBindThird";
	public static final String SQL_UPDATE_STEP1 = "updateStep1";
	
	
	@Autowired
	private SqlSessionTemplate sessionTemplate = null;

	@Override
	public PmsTeam getTeamInfo(final long id) {
		return sessionTemplate.selectOne(getStatement(SQL_GET_TEAM_INFO), id);
	}

	@Override
	public long checkExist(Map<String, Object> paramMap) {
		return sessionTemplate.selectOne(getStatement(SQL_CHECK_EXIST), paramMap);
	}

	@Override
	public long save(final PmsTeam team) {
		return sessionTemplate.insert(getStatement(SQL_SAVE), team);
	}

	@Override
	public List<PmsTeam> checkTeam(final String phoneNumber) {
		return sessionTemplate.selectList(getStatement(SQL_CHECK_TEAM), phoneNumber);
	}

	@Override
	public PmsTeam findTeamByLoginNameAndPwd(final  PmsTeam original) {
		return sessionTemplate.selectOne(getStatement(SQL_FIND_TEAM_BY_LOGINNAME_PWD), original);
	}

	@Override
	public long updateTeamAccount(final  PmsTeam team) {
		return sessionTemplate.update(getStatement(SQL_UPDATE_TEAM_ACCOUNT), team);
	}

	@Override
	public long updateTeamInfomation(final PmsTeam team) {
		return sessionTemplate.update(getStatement(SQL_UPDATE_TEAM_INFOMATION),team);
	}

	@Override
	public long updateTeamDescription(final PmsTeam team) {
		return sessionTemplate.update(getStatement(SQL_UPDATE_TEAM_DESCRIPTION),team);
	}

	@Override
	public long updatePasswordByLoginName(final PmsTeam team) {
		return sessionTemplate.update(getStatement(SQL_UPDATE_PASSWORD_BYLOGINNAME),team);
	}

	@Override
	public long modifyTeamPhone(final PmsTeam team) {
		return sessionTemplate.update(getStatement(SQL_MODIFY_TEAM_PHONE),team);
	}

	@Override
	public List<PmsTeam> getAll() {
		return sessionTemplate.selectList(getStatement(SQL_GET_ALL));
	}
	@Override
	public List<PmsTeam> getAllTeamName() {
		return sessionTemplate.selectList(getStatement(SQL_GET_ALL_TEAMNAME));
	}
	@Override
	public long saveTeamPhotoUrl(final PmsTeam team) {
		return sessionTemplate.update(getStatement(SQL_UPDATE_TEAM_PHOTOURL),team);
	}

	@Override
	public List<PmsTeam> findTeamByArray(Map<String, Object> paramMap) {
		return sessionTemplate.selectList(getStatement(SQL_FIND_TEAM_BYARRAY), paramMap);
	}

	@Override
	public long delete(long id) {
		return sessionTemplate.delete(getStatement(SQL_DELETE), id);
	}

	@Override
	public List<PmsTeam> getAllNoRecommend() {
		return sessionTemplate.selectList(getStatement(SQL_GET_ALL_NORECOMMEND));
	}

	@Override
	public boolean addRecommend(long teamId) {
		return sessionTemplate.update(getStatement(SQL_ADD_RECOMMEND),teamId)>0;
	}

	@Override
	public int downSortByRecommendSort(int index) {
		return sessionTemplate.update(getStatement(SQL_DOWN_SORT_BY_RECOMMENDSORT),index);
	}

	@Override
	public int upSortByTeamId(long teamId) {
		return sessionTemplate.update(getStatement(SQL_UP_SORT_BY_TEAMID),teamId);
	}

	@Override
	public int upSortByRecommendSort(int index) {
		return sessionTemplate.update(getStatement(SQL_UP_SORT_BY_RECOMMENDSORT),index);
	}

	@Override
	public int downSortByTeamId(long teamId) {
		return sessionTemplate.update(getStatement(SQL_down_SORT_BY_TEAMID),teamId);
	}

	@Override
	public int updateRecommendByTeamId(Map<String, Object> paramMap) {
		return sessionTemplate.update(getStatement(SQL_UPDATE_RECOMMEND_BY_TEAMID),paramMap);
	}

	@Override
	public int upAllAboveIndex(int index) {
		return sessionTemplate.update(getStatement(SQL_UP_ALL_ABOVE_INDEX),index);
	}

	@Override
	public List<PmsTeam> teamRecommendList() {
		return sessionTemplate.selectList(SQL_TEAM_RECOMMEND_LIST);
	}

	@Override
	public long teamInfoUnBind(PmsTeam team) {
		return sessionTemplate.update(getStatement(SQL_UNBIND_THIRD),team);
	}

	/**
	 * 更新注册第一步信息
	 */
	@Override
	public long updateSetp1(PmsTeam team) {
		return sessionTemplate.update(SQL_UPDATE_STEP1, team);
	}
}
