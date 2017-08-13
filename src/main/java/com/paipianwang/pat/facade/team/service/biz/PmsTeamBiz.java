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
import com.paipianwang.pat.common.util.Constants;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.entity.PmsTeamBusiness;
import com.paipianwang.pat.facade.team.entity.PmsTeamSkill;
import com.paipianwang.pat.facade.team.entity.PmsTeamTmp;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamBusinessDao;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamDao;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamSkillDao;
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
	@Autowired
	private PmsTeamSkillDao pmsTeamSkillDao;

	public DataGrid<PmsTeam> listWithPagination(PageParam pageParam, Map<String, Object> paramMap) {
		//根据business查询出teamId
		List<Long> teamIds=getTeamIdByBusiness(paramMap);
		//根据skill查询出teamId
		List<Long> teamIdBySkill=getTeamIdBySkill(paramMap);
		
		
		//结果取交
		if(teamIds!=null){
			teamIds.retainAll(teamIdBySkill);
		}else{
			teamIds=teamIdBySkill;
		}
		
		if(teamIds!=null && teamIds.size()==0){
			//无结果,不再查询
			return new DataGrid<PmsTeam>(0, new ArrayList<PmsTeam>());	
		}
		
		if(teamIds!=null){
			paramMap.put("teamIds",teamIds);
		}
		
		DataGrid<PmsTeam> dataGrid= pmsTeamDao.listWithPagination(pageParam, paramMap);
		
		//业务范围和业务技能另处理（速度）
		List<PmsTeam> list= dataGrid.getRows();
		if(ValidateUtil.isValid(list)){
			List<Long> ids=new ArrayList<>();
			for(PmsTeam team:list){
				ids.add(team.getTeamId());
			}
			Map<String, Object> bparamMap=new HashMap<>();
			bparamMap.put("teamIds", ids);
			//设置业务范围值
			List<PmsTeamBusiness> businessList=pmsTeamBusinessDao.getByTeams(bparamMap);
			list.forEach(team->{
				StringBuffer businessName=new StringBuffer();
				for(PmsTeamBusiness business:businessList){
					if(team.getTeamId()==business.getTeamId()){
						businessName.append(",").append(business.getBusinessName());
					}
				}
				team.setBusiness(businessName.toString().contains(",")?businessName.toString().substring(1):"");
			});
			
			//设置业务技能值
			List<PmsTeamSkill> skillList=pmsTeamSkillDao.getByTeams(bparamMap);
			list.forEach(team->{
				StringBuffer skillName=new StringBuffer();
				for(PmsTeamSkill skill:skillList){
					if(team.getTeamId()==skill.getTeamId()){
						skillName.append(",").append(skill.getSkillName());
					}
				}
				team.setSkill(skillName.toString().contains(",")?skillName.toString().substring(1):"");
			});
		}
		
		return dataGrid;
	}
	
	
	
	/**
	 * 组装根据业务类型检索供应商
	 *     检索出包含条件中所有业务的供应商id作为检索条件
	 * @param paramMap
	 * @return business检索出供应商id集合
	 *         为null表示无需继续检索
	 */
	private List<Long> getTeamIdByBusiness(Map<String, Object> paramMap){
		String[] businessArray=(String[]) paramMap.get("business");
		paramMap.remove("business");
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
			return teamIds;	
		}
		// 无business检索条件，返回空集合
		return null;
	}
	
	/**
	 * 组装根据业务技能检索供应商
	 * @param paramMap 
	 * @return 满足条件的供应商id集合 null-无需检索
	 */
	private List<Long> getTeamIdBySkill(Map<String, Object> paramMap){
		String[] skillArray=(String[]) paramMap.get("skill");
		paramMap.remove("skill");
		List<Long> teamIds=new ArrayList<Long>();
		
		if(ValidateUtil.isValid(skillArray)){			
			for(int i=0;i<skillArray.length;i++){
				//根据business查询teamId
				List<Long> resu=pmsTeamSkillDao.getTeamidByBusinessSkill(skillArray[i]);
				//各集合取交
				if(i==0){
					teamIds=resu;
				}else{
					teamIds.retainAll(resu);
				}
			}
			return teamIds;	
		}
		//无skill检索条件，跳出返回空集合
		return null;
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
		//保存供应商业务技能
		saveTeamSkill(team);
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
		//更新team业务技能
		pmsTeamSkillDao.deleteByTeamId(team.getTeamId());
		saveTeamSkill(team);
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
		//保存供应商业务技能
		saveTeamSkill(team);
		
		return result;
	}
	
	/**
	 * 保存供应商业务技能
	 * @param team
	 */
	private void saveTeamSkill(PmsTeam team){
		String skill=team.getSkill();
		if(ValidateUtil.isValid(skill)){
			String[] skillArray=skill.trim().split(",");
			for(String skillName:skillArray){
				PmsTeamSkill teamSkill=new PmsTeamSkill();
				teamSkill.setTeamId(team.getTeamId());
				teamSkill.setSkillName(skillName);
				pmsTeamSkillDao.insert(teamSkill);
			}
		}
	}

	public long saveTeamPhotoUrl(final PmsTeam team) {
		return pmsTeamDao.saveTeamPhotoUrl(team);
	}

	public long update(PmsTeam team) {
		long result= pmsTeamDao.update(team);
		//更新供应商业务范围
		updateBusiness(team);
		
		//更新供应商业务技能--全部删除后重新插入
		pmsTeamSkillDao.deleteByTeamId(team.getTeamId());
		saveTeamSkill(team);
		
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
			//删除对应供应商业务技能
			pmsTeamSkillDao.deleteByTeamId(id);
			//删除对应Teamtmp
			pmsTeamTmpDao.delTeamByTeamId(id);
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
		// 根据business查询出teamId
		List<Long> teamIds = getTeamIdByBusiness(paramMap);
		// 根据skill查询出teamId
		List<Long> teamIdBySkill = getTeamIdBySkill(paramMap);

		// 结果取交
		if (teamIds != null) {
			teamIds.retainAll(teamIdBySkill);
		} else {
			teamIds = teamIdBySkill;
		}

		if (teamIds != null && teamIds.size() == 0) {
			// 无结果,不再查询
			return new ArrayList<PmsTeam>();
		}

		if (teamIds != null) {
			paramMap.put("teamIds", teamIds);
		}
		return pmsTeamDao.listBy(paramMap);
	}


	/**
	 * 保存（第一次）/更新注册第一步信息
	 * @param team
	 * @return
	 */
	public long addOrUpdateStep1(PmsTeam team) {
		long result=0;
		if(team.getTeamId()==0l){
			//保存
			// 注册供应商时，首先检测手机号码是否被注册
			List<PmsTeam> list = pmsTeamDao.checkTeam(team.getPhoneNumber());
			if (ValidateUtil.isValid(list))
				// 如果list不为空，说明数据库有值
				return -1;
			// 否则存入数据
			result = pmsTeamDao.save(team);
		}else{
			//更新
			result=pmsTeamDao.updateSetp1(team);
			//更新前-业务范围、业务技能
			pmsTeamBusinessDao.deleteByTeamId(team.getTeamId());
			pmsTeamSkillDao.deleteByTeamId(team.getTeamId());
		}
		
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
		// 保存供应商业务技能
		saveTeamSkill(team);
		return result;
	}
	/**
	 * 更新供应商注册第二步信息
	 * @param team
	 * @return
	 */
	public long updateStep2(PmsTeam team) {
		return pmsTeamDao.updateSetp2(team);
	}

	/**
	 * 更新供应商的审核状态
	 * @param teamId
	 * @param flag
	 * @return
	 */
	public long updateFlag(long teamId,int flag) {
		
		return pmsTeamDao.updateFlag(teamId,flag);
	}
	
	/**
	 * 供应商注册流程，点击协议同意按钮后，只保存手机号码以及密码
	 * 用于保存供应商的手机号码以及密码
	 * @param team
	 * @return
	 */
	public long saveTelephoneAndPassword(PmsTeam team){
		pmsTeamDao.insert(team);
		return team.getTeamId();
	}
	
	/**
	 * 线下方法：处理20170714供应商注册（业务范围结构）变更数据转储
	 * 注：仅用于版本升级
	 */
	public void editBusinessForVersion(){
		List<PmsTeam> teams=pmsTeamDao.getAll();//getAll 添加 t.BUSINESS AS business返回
		for(PmsTeam team:teams){
			String bus=team.getBusiness();
			System.out.println("edit:"+team.getTeamName()+"-bus:"+bus);
			
			if(ValidateUtil.isValid(bus)){
				pmsTeamBusinessDao.deleteByTeamId(team.getTeamId());//可重复执行
				String[] buss=bus.split(",");
				for(String busV:buss){
					PmsTeamBusiness pt=new PmsTeamBusiness();
					pt.setTeamId(team.getTeamId());
					pt.setBusinessName(Constants.BUSINESS_MAP.get(busV));
					pmsTeamBusinessDao.insert(pt);
				}
			}
		}
	}


	/**
	 * 根据供应商名称模糊匹配供应商信息
	 * @param teamName
	 * @return
	 */
	public List<PmsTeam> listByTeamName(String teamName) {
		return pmsTeamDao.listByTeamName(teamName);
	}

}
