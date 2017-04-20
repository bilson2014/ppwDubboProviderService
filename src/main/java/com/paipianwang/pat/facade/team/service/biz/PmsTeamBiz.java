package com.paipianwang.pat.facade.team.service.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamDao;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamTmpDao;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.entity.PmsTeamTmp;

/**
 * 订单--服务层接口 事物管理层
 * 
 * @author Jack
 * @version 1.0
 *
 */
@Transactional
@Service
public class PmsTeamBiz {

	@Autowired
	private PmsTeamDao pmsTeamDao;
	@Autowired
	private PmsTeamTmpDao pmsTeamTmpDao;

	public DataGrid<PmsTeam> listWithPagination(PageParam pageParam, Map<String, Object> paramMap) {
		return pmsTeamDao.listWithPagination(pageParam, paramMap);
	}

	public PmsTeam findTeamById(long teamId) {
		return pmsTeamDao.getById(teamId);
	}

	public PmsTeam getTeamInfo(long id) {
		return pmsTeamDao.getTeamInfo(id);
	}

	public long checkExist(Map<String, Object> paramMap) {
		return pmsTeamDao.checkExist(paramMap);
	}

	@Transactional
	public long register(final PmsTeam team) {
		// 注册供应商时，首先检测手机号码是否被注册
		List<PmsTeam> list = pmsTeamDao.checkTeam(team.getPhoneNumber());
		if (ValidateUtil.isValid(list))
			// 如果list不为空，说明数据库有值
			return -1;
		//	否则存入数据
		return pmsTeamDao.save(team);
	}

	public List<PmsTeam> checkTeam(final String phoneNumber) {
		return pmsTeamDao.checkTeam(phoneNumber);
	}

	public PmsTeam findTeamByLoginNameAndPwd(PmsTeam original) {
		return pmsTeamDao.findTeamByLoginNameAndPwd(original);
	}

	public long updateTeamAccount(PmsTeam team) {
		return pmsTeamDao.updateTeamAccount(team);
	}

	public long updateTeamInfomation(final PmsTeam team) {
		return pmsTeamDao.updateTeamInfomation(team);
	}

	public long updateTeamDescription(final PmsTeam team) {
		return pmsTeamDao.updateTeamDescription(team);
	}

	public void delTeamMapperByTeamId(final PmsTeamTmp tmp) {
		pmsTeamTmpDao.delTeamMapperByTeamId(tmp);
	}

	public void addTeamTmp(final PmsTeamTmp tmp) {
		pmsTeamTmpDao.addTeamTmp(tmp);
	}

	public List<PmsTeamTmp> doesHaveLatestEnableTmpByTeamId(final Long teamId) {
		return pmsTeamTmpDao.doesHaveLatestEnableTmpByTeamId(teamId);
	}

	public PmsTeam getLatestTmpByTeamId(Long teamId) {
		return pmsTeamTmpDao.getLatestTmpByTeamId(teamId);
	}

	public long updatePasswordByLoginName(final PmsTeam team) {
		return pmsTeamDao.updatePasswordByLoginName(team);
	}

	public long modifyTeamPhone(final PmsTeam team) {
		return pmsTeamDao.modifyTeamPhone(team);
	}

	public List<PmsTeam> getAll() {
		return pmsTeamDao.getAll();
	}

	public long save(final PmsTeam team) {
		return pmsTeamDao.save(team);
	}

	public long saveTeamPhotoUrl(final PmsTeam team) {
		return pmsTeamDao.saveTeamPhotoUrl(team);
	}

	public long update(PmsTeam team) {
		return pmsTeamDao.update(team);
	}

	public List<PmsTeam> delete(long[] ids) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("array", ids);
		final List<PmsTeam> lists = pmsTeamDao.findTeamByArray(paramMap);
		for (long id : ids) {
			final long ret = pmsTeamDao.delete(id);
			if (ret > -1)
				continue;
			else
				new RuntimeException("delete team error ...");
		}
		return lists;
	}

	public boolean moveUp(final long teamId) {
		PmsTeam team = pmsTeamDao.getById(teamId);
		int index = team.getRecommendSort();
		// 1.降低上一个的排序
		int flag1 = pmsTeamDao.downSortByRecommendSort(index);
		// 2.提升当前id的排序
		int flag2 = pmsTeamDao.upSortByTeamId(teamId);
		return flag1 > 0 && flag2 > 0;
	}

	public boolean moveDown(long teamId) {
		PmsTeam team = pmsTeamDao.getById(teamId);
		int index = team.getRecommendSort();
		// 1.提升上一个的排序
		int flag1 = pmsTeamDao.upSortByRecommendSort(index);
		// 2.降低当前id的排序
		int flag2 = pmsTeamDao.downSortByTeamId(teamId);
		return flag1 > 0 && flag2 > 0;
	}

	public boolean delRecommend(long teamId) {
		PmsTeam team = pmsTeamDao.getById(teamId);
		int index = team.getRecommendSort();
		// 1.删除当前供应商（不推荐而已）
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("recommend", false);
		paramMap.put("teamId", teamId);
		int flag1 = pmsTeamDao.updateRecommendByTeamId(paramMap);
		// 2.提升index之下的所有排序
		int flag2 = pmsTeamDao.upAllAboveIndex(index);
		return flag1 > 0 && flag2 >= 0;
	}

}
