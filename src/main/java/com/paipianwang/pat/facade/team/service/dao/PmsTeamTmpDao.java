package com.paipianwang.pat.facade.team.service.dao;


import java.util.List;

import com.paipianwang.pat.common.core.dao.BaseDao;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.entity.PmsTeamTmp;

/**
 * team 临时表数据访问接口
 * @author Jack
 * @version 1.0
 *
 */
public interface PmsTeamTmpDao extends BaseDao<PmsTeamTmp>{

	/**
	 * 删除临时数据
	 */
	public void delTeamMapperByTeamId(final PmsTeamTmp tmp);
	/**
	 * 添加一个临时team数据
	 */
	public void addTeamTmp(final PmsTeamTmp tmp);
	/**
	 * 是否含有供应商最新审核信息,包含未审核和审核不通过
	 */
	public List<PmsTeamTmp> doesHaveLatestEnableTmpByTeamId(final Long teamId);
	/**
	 * 获取最新的team数据
	 */
	public PmsTeam getLatestTmpByTeamId(Long teamId);
	
	public void updateTeamTmpCheck(PmsTeamTmp teamTmp);
	
	public PmsTeamTmp getTeamTmpById(Long teamId);
	
	public List<PmsTeamTmp> getTeamTmpByTeamId(Integer teamId);
	/**
	 * 删除供应商临时数据-物理
	 * @param teamId
	 */
	public long delTeamByTeamId(long teamId);


}
