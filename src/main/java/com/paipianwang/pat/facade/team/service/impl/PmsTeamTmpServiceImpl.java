package com.paipianwang.pat.facade.team.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.facade.team.service.dao.PmsTeamTmpDao;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.facade.team.entity.DIffBean;
import com.paipianwang.pat.facade.team.entity.PmsTeamTmp;
import com.paipianwang.pat.facade.team.service.PmsTeamTmpFacade;
import com.paipianwang.pat.facade.team.service.biz.PmsTeamTmpBiz;
/**
 * teamTmp Dubbo服务接口实现
 * @author Jack
 * @version 1.0
 */
@Service("pmsTeamTmpFacade")
public class PmsTeamTmpServiceImpl implements PmsTeamTmpFacade {
	
	//private static Logger logger = LoggerFactory.getLogger("error");

	@Autowired
	private final PmsTeamTmpBiz biz = null;
	@Autowired
	private PmsTeamTmpDao pmsTeamTmpDao;
	@Override
	public DataGrid<PmsTeamTmp> listWithPagination(PageParam pageParam) {
		return pmsTeamTmpDao.listWithPagination(pageParam, null);
	}
	@Override
	public void updateTeamTmp(PmsTeamTmp teamTmp) {
		biz.updateTeamTmp(teamTmp);
	}
	@Override
	public List<DIffBean> findDiffTeam(Integer teamId) {
		return biz.findDiffTeam(teamId);
	}

	
}
