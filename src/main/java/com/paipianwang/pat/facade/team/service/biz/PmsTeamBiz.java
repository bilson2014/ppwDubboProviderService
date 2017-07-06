package com.paipianwang.pat.facade.team.service.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.entity.PmsTeamBusiness;
import com.paipianwang.pat.facade.team.entity.PmsTeamTmp;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamBusinessDao;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamDao;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamTmpDao;

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
	@Autowired
	private PmsTeamBusinessDao pmsTeamBusinessDao;

	public DataGrid<PmsTeam> listWithPagination(PageParam pageParam, Map<String, Object> paramMap) {
		//根据business查询出teamId
		List<Long> teamIds=getTeamIdByBusiness(paramMap);
		
		if(teamIds==null){
			//无结果,不再查询
			return new DataGrid<PmsTeam>(0, new ArrayList<PmsTeam>());		
		}
		
		
		return pmsTeamDao.listWithPagination(pageParam, paramMap);
	}
	
	/**
	 * 组装根据业务类型检索供应商
	 *     检索出包含条件中所有业务的供应商id作为检索条件
	 * @param paramMap
	 * @return business检索出供应商id集合
	 *         为null表示无对应供应商，无需继续检索
	 */
	private List<Long> getTeamIdByBusiness(Map<String, Object> paramMap){
		String[] businessArray=(String[]) paramMap.get("business");
		List<Long> teamIds=new ArrayList<Long>();
		
		if(ValidateUtil.isValid(businessArray)){			
			for(int i=0;i<businessArray.length;i++){
				//根据business查询teamId
				List<Long> resu=pmsTeamBusinessDao.getTeamidByBusiness(businessArray[i]);
				//各集合取交
				if(i==0){
					teamIds=resu;
				}else{
					teamIds.retainAll(resu);
				}
			}
			if(teamIds.size()==0){
				//无结果，不继续
				return null;
			}else{
				paramMap.put("teamIds",teamIds);
			}		
		}
		//无business检索条件，跳出
		return teamIds;
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
		// 否则存入数据
		long result = pmsTeamDao.save(team);
		// 保存供应商业务范围
		String business = team.getBusiness();
		if (ValidateUtil.isValid(business)) {
			String[] businessArray = business.trim().split(",");
			for (String businessName : businessArray) {
				PmsTeamBusiness teamBusiness = new PmsTeamBusiness();
				teamBusiness.setBusinessName(businessName.trim());
				teamBusiness.setTeamId(team.getTeamId());
				pmsTeamBusinessDao.insert(teamBusiness);
			}
		}
		return result;
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
		long result= pmsTeamDao.updateTeamInfomation(team);
		//更新team 业务
		updateBusiness(team);
		return result;
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

	@Transactional
	public long save(final PmsTeam team) {
		long result= pmsTeamDao.save(team);
		//保存供应商业务范围
		String business=team.getBusiness();
		if(ValidateUtil.isValid(business)){
			String[] businessArray=business.trim().split(",");
			for(String businessName:businessArray){
				PmsTeamBusiness teamBusiness=new PmsTeamBusiness();
				teamBusiness.setBusinessName(businessName.trim());
				teamBusiness.setTeamId(team.getTeamId());
				pmsTeamBusinessDao.insert(teamBusiness);
			}
		}
		return result;
	}

	public long saveTeamPhotoUrl(final PmsTeam team) {
		return pmsTeamDao.saveTeamPhotoUrl(team);
	}

	public long update(PmsTeam team) {
		long result= pmsTeamDao.update(team);
		//更新供应商业务范围
		updateBusiness(team);
		return result;
	}
	
	/**
	 * 更新供应商业务公共方法：
	 *    删除旧数据
	 *    插入新数据
	 * @param team
	 */
	private void updateBusiness(PmsTeam team) {
		// 删除旧数据
		pmsTeamBusinessDao.deleteByTeamId(team.getTeamId());
		// 添加新数据
		String business = team.getBusiness();
		if (ValidateUtil.isValid(business)) {
			String[] businessArray = business.trim().split(",");
			for (String businessName : businessArray) {
				PmsTeamBusiness teamBusiness = new PmsTeamBusiness();
				teamBusiness.setBusinessName(businessName.trim());
				teamBusiness.setTeamId(team.getTeamId());
				pmsTeamBusinessDao.insert(teamBusiness);
			}
		}
	}

	public List<PmsTeam> delete(long[] ids) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("array", ids);
		final List<PmsTeam> lists = pmsTeamDao.findTeamByArray(paramMap);
		for (long id : ids) {
			final long ret = pmsTeamDao.delete(id);
			//删除对应供应商业务
			pmsTeamBusinessDao.deleteByTeamId(id);
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

	@Transactional
	public List<PmsTeam> getTeamsByCondition(Map<String, Object> paramMap) {
		//根据business查询出teamId
				List<Long> teamIds=getTeamIdByBusiness(paramMap);
				
				if(teamIds==null){
					//无结果,不再查询
					return new  ArrayList<PmsTeam>();		
				}
		return pmsTeamDao.listBy(paramMap);
	}

}
