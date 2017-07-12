package com.paipianwang.pat.facade.team.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.facade.team.service.biz.PmsTeamBiz;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamDao;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.entity.PmsTeamTmp;
import com.paipianwang.pat.facade.team.entity.TeamBusiness;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;
/**
 * team Dubbo服务接口实现
 * @author Jack
 * @version 1.0
 */
@Service("pmsTeamFacade")
public class PmsTeamServiceImpl implements PmsTeamFacade {
	
	private static Logger logger = LoggerFactory.getLogger("error");

	@Autowired
	private final PmsTeamBiz biz = null;
	@Autowired
	private PmsTeamDao pmsTeamDao;

	@Override
	public DataGrid<PmsTeam> listWithPagination(PageParam pageParam, Map<String, Object> paramMap) {
		return biz.listWithPagination(pageParam,paramMap);
	}

	@Override
	public PmsTeam findTeamById(final Long teamId) {
		return biz.findTeamById(teamId.longValue());
	}

	@Override
	public PmsTeam getTeamInfo(final Long teamId) {
		return biz.getTeamInfo(teamId.longValue());
	}

	@Override
	public List<String> getTags(final List<Integer> ids) {
		List<String> tags = new ArrayList<>();
		for (int i = 0; i < ids.size(); i++) {
			tags.add(TeamBusiness.get(ids.get(i)));
		}
		return tags;
	}

	@Override
	public long checkExist(final PmsTeam team) {
		
		Map<String, Object> map = new HashMap<>();
		if (team.getPhoneNumber() != null && !"".equals(team.getPhoneNumber())) {
			map.put("phoneNumber", team.getPhoneNumber());
		}
		if (team.getLoginName() != null && !"".equals(team.getLoginName())) {
			map.put("loginName", team.getLoginName());
		}
		final long count = biz.checkExist(map);
		return count;
	}

	@Override
	public PmsTeam register(final PmsTeam team) {
		final long ret = biz.register(team);
		if (ret > 0){
			// 保存成功
			team.setTeamId(team.getTeamId());
			return team;
		} else if(ret == -1) {
			PmsTeam pmsTeam = new PmsTeam();
			pmsTeam.setTeamId(-1);
			return pmsTeam;
		}
		return null;
	}

	@Override
	public PmsTeam doLogin(final String phoneNumber) {
		final List<PmsTeam> team = biz.checkTeam(phoneNumber);
		if (team != null) {
			if (team.size() == 1) {
				return team.get(0);
			} else {
				logger.error("数据库中存在多个相同手机号：" + phoneNumber);
			}
		} else {
			logger.error("数据库中不存在该供应商 手机号：" + phoneNumber);
		}
		return null;
	}

	@Override
	public PmsTeam findTeamByLoginNameAndPwd(final PmsTeam original) {
		final PmsTeam team = biz.findTeamByLoginNameAndPwd(original);
		return team;
	}

	@Override
	public long updateTeamAccount(final PmsTeam team) {
		return biz.updateTeamAccount(team);
	}

	@Override
	public long updateTeamInfomation(final PmsTeam team) {
		final long ret = biz.updateTeamInfomation(team);
		return ret;
	}

	@Override
	public long updateTeamDescription(final PmsTeam team) {
		final long ret = biz.updateTeamDescription(team);
		return ret;
	}

	@Override
	public void dealTeamTmp(final PmsTeam team) {
		PmsTeamTmp tmp = new PmsTeamTmp();
		tmp.setCheckStatus(0);
		if(null != team.getAddress()){
			tmp.setAddress(team.getAddress());
		}
		if(null != team.getBusiness()){
			tmp.setBusiness(team.getBusiness());
		}
		if(null != team.getBusinessDesc()){
			tmp.setBusinessDescription(team.getBusinessDesc());
		}
		if(null != team.getEmail()){
			tmp.setEmail(team.getEmail());
		}
		if(null != team.getEstablishDate()){
			tmp.setEstablishDate(team.getEstablishDate());
		}
		tmp.setInfoResource(team.getInfoResource());
		if(null != team.getLinkman()){
			tmp.setLinkMan(team.getLinkman());
		}
		if(null != team.getOfficialSite()){
			tmp.setOfficialSite(team.getOfficialSite());
		}
		tmp.setPriceRange(team.getPriceRange());
		
		if(null != team.getQq()){
			tmp.setQq(team.getQq());
		}
		if(null != team.getScale()){
			tmp.setScale(team.getScale());
		}
		tmp.setStatus(true);
		if(null != team.getTeamCity()){
			tmp.setTeamCity(team.getTeamCity());
		}
		if(null != team.getTeamDescription()){
			tmp.setTeamDescription(team.getTeamDescription());
		}
		tmp.setTeamId(team.getTeamId());
		if(null != team.getTeamName()){
			tmp.setTeamName(team.getTeamName());
		}
		if(null != team.getTeamProvince()){
			tmp.setTeamProvince(team.getTeamProvince());
		}
		if(null != team.getWebchat()){
			tmp.setWebchat(team.getWebchat());
		}
		if(null != team.getDemand()){
			tmp.setDemand(team.getDemand());
		}
		if(null != team.getTeamPhotoUrl()){
			tmp.setTeamPhotoUrl(team.getTeamPhotoUrl());
		}
		if(null!=team.getSkill()){
			tmp.setSkill(team.getSkill());
		}
		//删除其他tmp
		biz.delTeamMapperByTeamId(tmp);
		//增加一条记录
		biz.addTeamTmp(tmp);
	}

	@Override
	public PmsTeam findLatestTeamById(final Long teamId) {
		//查询是否有待审核的供应商
		List<PmsTeamTmp> list = biz.doesHaveLatestEnableTmpByTeamId(teamId);
		if(null != list && list.size() > 0){
			//返回供应商最新信息
			PmsTeam team = biz.getLatestTmpByTeamId(teamId);
			return team;
		}else{
			//返回team信息
			return biz.findTeamById(teamId);
		}
	}

	@Override
	public long updatePasswordByLoginName(final PmsTeam team) {
		final long ret = biz.updatePasswordByLoginName(team);
		return ret;
	}

	@Override
	public long modifyTeamPhone(final PmsTeam team) {
		return biz.modifyTeamPhone(team);
	}

	@Override
	public List<PmsTeam> getAll() {
		return pmsTeamDao.getAll();
	}

	@Override
	public long save(final PmsTeam team) {
		biz.save(team);
		return team.getTeamId();
	}

	@Override
	public long saveTeamPhotoUrl(final PmsTeam team) {
		final long ret = biz.saveTeamPhotoUrl(team);
		return ret;
	}

	@Override
	public long update(PmsTeam team) {
		final long ret = biz.update(team);
		return ret;
	}

	@Override
	public List<PmsTeam> delete(long[] ids) {
		return biz.delete(ids);
	}

	@Override
	public List<PmsTeam> getAllNoRecommend() {
		return pmsTeamDao.getAllNoRecommend();
	}

	@Override
	public boolean addRecommend(final long teamId) {
		return pmsTeamDao.addRecommend(teamId);
	}

	@Override
	public boolean moveUp(final long teamId) {
		return biz.moveUp(teamId);
	}

	@Override
	public boolean moveDown(final long teamId) {
		return biz.moveDown(teamId);
	}

	@Override
	public boolean delRecommend(final long teamId) {
		return biz.delRecommend(teamId);
	}

	@Override
	public List<PmsTeam> teamRecommendList() {
		return pmsTeamDao.teamRecommendList();
	}

	@Override
	public List<PmsTeam> getAllTeamName() {
		return pmsTeamDao.getAllTeamName();
	}

	@Override
	public boolean teamInfoUnBind(final PmsTeam team) {
		pmsTeamDao.teamInfoUnBind(team);
		return true;
	}

	@Override
	public List<PmsTeam> listWithParam(final Map<String, Object> paramMap) {
		return biz.getTeamsByCondition(paramMap);
	}

	//保存（第一次）/更新注册第一步信息
	@Override
	public PmsTeam addOrUpdateStep1(PmsTeam team) {
		final long ret = biz.addOrUpdateStep1(team);
		if (ret > 0){
			// 保存成功
			team.setTeamId(team.getTeamId());
			return team;
		} else if(ret == -1) {
			PmsTeam pmsTeam = new PmsTeam();
			pmsTeam.setTeamId(-1);
			return pmsTeam;
		}
		return null;
	}

	//更新注册第一步信息
	@Override
	public PmsTeam updateStep2(PmsTeam team) {
		
		return null;
	}

	
}
