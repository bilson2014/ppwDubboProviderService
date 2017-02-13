package com.paipianwang.pat.facade.team.service.dao;

import java.util.List;
import java.util.Map;

import com.paipianwang.pat.common.core.dao.BaseDao;
import com.paipianwang.pat.facade.team.entity.PmsTeam;

/**
 * team表数据访问接口
 * @author Jack
 * @version 1.0
 *
 */
public interface PmsTeamDao extends BaseDao<PmsTeam>{

	public PmsTeam getTeamInfo(final long id);

	/**
	 * 检查手机号或者用户名唯一性 如果没有,则返回0,有则返回存在的条目数
	 * @param phoneNumber
	 *            电话号码
	 * @param loginName
	 *            登录名
	 * @return 数据个数
	 */
	public long checkExist(Map<String, Object> paramMap);

	/**
	 * 保存team
	 */
	public long save(final PmsTeam team);
	/**
	 * 供应商登录
	 * 
	 * @param original
	 *            包含(登录名和密码(已加密))
	 * @return
	 */
	public List<PmsTeam> checkTeam(final String phoneNumber);

	/**
	 * 根据用户名和密码登录供应商
	 * 
	 * @param original
	 * @return
	 */
	public PmsTeam findTeamByLoginNameAndPwd(final PmsTeam original);
	/**
	 * 根据 ID 获取 team 信息
	 * 
	 * @param id
	 *            team ID
	 * @return team
	 */
	public long updateTeamAccount(final PmsTeam team);
	/**
	 * 供应商 基础信息更新(供应商名称、简介、地址、邮箱等)
	 * 
	 * @return 数据受影响个数
	 */
	public long updateTeamInfomation(final PmsTeam team);

	/**
	 * 更新供应商备注信息
	 */
	public long updateTeamDescription(final PmsTeam team);
	/**
	 * 根据登录名修改密码
	 * 
	 * @param team
	 *            (包含 登录名和密码)
	 * @return数据受影响个数
	 */
	public long updatePasswordByLoginName(final PmsTeam team);

	/**
	 * 修改手机号
	 */
	public long modifyTeamPhone(final PmsTeam team);
	/**
	 * 获取所有 team 信息
	 * 
	 * @return
	 */
	public List<PmsTeam> getAll();
	/**
	 * 保存 图片路径
	 */
	public long saveTeamPhotoUrl(final PmsTeam team);
	/**
	 * 根据 ID数组 获取 team列表
	 * 
	 * @param ids
	 *            ID数组
	 * @return team列表
	 */
	public List<PmsTeam> findTeamByArray(Map<String, Object> paramMap);

	/**
	 * 根据ID删除team
	 */
	public long delete(final long teamId);

	/**
	 * 查询未被推荐的供应商
	 */
	public List<PmsTeam> getAllNoRecommend();

	/**
	 * 添加首页供应商
	 */
	public boolean addRecommend(final long teamId);

	public int downSortByRecommendSort(final int index);

	public int upSortByTeamId(final long teamId);

	public int upSortByRecommendSort(final int index);

	public int downSortByTeamId(final long teamId);

	public int updateRecommendByTeamId(final Map<String, Object> paramMap);

	public int upAllAboveIndex(final int index);

	public List<PmsTeam> teamRecommendList();

	public List<PmsTeam> getAllTeamName();

}
