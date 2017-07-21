package com.paipianwang.pat.facade.team.service.biz;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paipianwang.pat.common.util.Constants;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.team.entity.DIffBean;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.entity.PmsTeamBusiness;
import com.paipianwang.pat.facade.team.entity.PmsTeamSkill;
import com.paipianwang.pat.facade.team.entity.PmsTeamTmp;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamBusinessDao;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamDao;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamSkillDao;
import com.paipianwang.pat.facade.team.service.dao.PmsTeamTmpDao;
/**
 * 订单--服务层接口
 * 事物管理层
 * @author Jack
 * @version 1.0
 *
 */
@Transactional
@Service
public class PmsTeamTmpBiz {

	@Autowired
	private PmsTeamTmpDao pmsTeamTmpDao;
	@Autowired
	private PmsTeamDao pmsTeamDao;
	@Autowired
	private PmsTeamBusinessDao pmsTeamBusinessDao;
	@Autowired
	private PmsTeamSkillDao pmsTeamSkillDao;

	public void updateTeamTmp(PmsTeamTmp teamTmp) {
		//1.修改teamTmp审核状态和审核信息
		pmsTeamTmpDao.updateTeamTmpCheck(teamTmp);
		//2.如果审核通过,修改team表
		if(teamTmp.getCheckStatus() == 1){
			PmsTeamTmp tmp = pmsTeamTmpDao.getTeamTmpById(teamTmp.getId());
			PmsTeam team = pmsTeamDao.getById(tmp.getTeamId());
			team = moveInfoToTeam(team,tmp);
			pmsTeamDao.update(team);
			//更新供应商业务
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
			//更新供应商业务技能-删除后重新添加
			pmsTeamSkillDao.deleteByTeamId(team.getTeamId());
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
	}
	private PmsTeam moveInfoToTeam(PmsTeam team, PmsTeamTmp tmp) {
		team.setAddress(tmp.getAddress());
		team.setBusiness(tmp.getBusiness());
//		team.setBusinessDesc(tmp.getBusinessDescription());
		team.setTeamCity(tmp.getTeamCity());
		team.setTeamProvince(tmp.getTeamProvince());
		team.setTeamName(tmp.getTeamName());
		team.setLinkman(tmp.getLinkMan());
		team.setWebchat(tmp.getWebchat());
		team.setQq(tmp.getQq());
		team.setEmail(tmp.getEmail());
		team.setPriceRange(tmp.getPriceRange());
		team.setInfoResource(tmp.getInfoResource());
		team.setTeamDescription(tmp.getTeamDescription());
		team.setScale(tmp.getScale());
//		team.setDemand(tmp.getDemand());
		team.setEstablishDate(tmp.getEstablishDate());
		team.setOfficialSite(tmp.getOfficialSite());
		team.setTeamPhotoUrl(tmp.getTeamPhotoUrl());
		team.setSkill(tmp.getSkill());
		return team;
	}
	public List<DIffBean> findDiffTeam(Integer teamId) {
		List<DIffBean> list = new ArrayList<DIffBean>();
		List<PmsTeamTmp> tmpList = pmsTeamTmpDao.getTeamTmpByTeamId(teamId);
		if(null!=tmpList && tmpList.size()>0){
			PmsTeamTmp tmp = tmpList.get(0);
			PmsTeam team = pmsTeamDao.getById(teamId);
			list = findDiffProperty(tmp,team);
		}
		return list;
	}
	private List<DIffBean> findDiffProperty(PmsTeamTmp tmp, PmsTeam team) {
		List<DIffBean> list = new ArrayList<DIffBean>();
		String tmp_team = null == tmp.getTeamName()?"":tmp.getTeamName();
		String _team = null == team.getTeamName()?"":team.getTeamName();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamName");
			bean.setPropertyName("公司名称");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getLinkMan()?"":tmp.getLinkMan();
		_team = null == team.getLinkman()?"":team.getLinkman();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("linkman");
			bean.setPropertyName("联系人");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		
		tmp_team = null == tmp.getWebchat()?"":tmp.getWebchat();
		_team = null == team.getWebchat()?"":team.getWebchat();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("webchat");
			bean.setPropertyName("微信");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		
		tmp_team = null == tmp.getQq()?"":tmp.getQq();
		_team = null == team.getQq()?"":team.getQq();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("qq");
			bean.setPropertyName("QQ");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getEmail()?"":tmp.getEmail();
		_team = null == team.getEmail()?"":team.getEmail();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("email");
			bean.setPropertyName("邮箱");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getAddress()?"":tmp.getAddress();
		_team = null == team.getAddress()?"":team.getAddress();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("address");
			bean.setPropertyName("地址");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getTeamProvinceName()?"":tmp.getTeamProvinceName();
		_team = null == team.getTeamProvinceName()?"":team.getTeamProvinceName();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamProvince");
			bean.setPropertyName("所在省");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getTeamCityName()?"":tmp.getTeamCityName();
		_team = null == team.getTeamCityName()?"":team.getTeamCityName();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamCity");
			bean.setPropertyName("所在市");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = tmp.getPriceRange().toString();
		_team = String.valueOf(team.getPriceRange());
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("priceRange");
			bean.setPropertyName("价格区间");
			bean.setOldValue(Constants.PRICE_RANGE_MAP.get(_team));
			bean.setNewValue(Constants.PRICE_RANGE_MAP.get(tmp_team));
			list.add(bean);
		}
		tmp_team = tmp.getInfoResource().toString();
		 _team = String.valueOf(team.getInfoResource());
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("infoResource");
			bean.setPropertyName("获知渠道");
			bean.setOldValue(Constants.INFO_RESOURCE_MAP.get(_team));
			bean.setNewValue(Constants.INFO_RESOURCE_MAP.get(tmp_team));
			list.add(bean);
		}
		tmp_team = null == tmp.getBusiness()?"":tmp.getBusiness();
		_team = null == team.getBusiness()?"":team.getBusiness();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("business");
			bean.setPropertyName("业务范围");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getTeamDescription()?"":tmp.getTeamDescription();
		_team = null == team.getTeamDescription()?"":team.getTeamDescription();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamDescription");
			bean.setPropertyName("公司简介");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getScale()?"":tmp.getScale();
		_team = null == team.getScale()?"":team.getScale();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("scale");
			bean.setPropertyName("公司规模");
			bean.setOldValue(Constants.SCALE_MAP.get(_team));
			bean.setNewValue(Constants.SCALE_MAP.get(tmp_team));
			list.add(bean);
		}
//		tmp_team = null == tmp.getDemand()?"":tmp.getDemand();
//		_team = null == team.getDemand()?"":team.getDemand();
//		if(tmp_team.compareTo(_team) != 0){
//			DIffBean bean = new DIffBean();
//			bean.setProperty("demand");
//			bean.setPropertyName("客户要求");
//			bean.setOldValue(_team);
//			bean.setNewValue(tmp_team);
//			list.add(bean);
//		}
		tmp_team = null == tmp.getEstablishDate()?"":tmp.getEstablishDate();
		_team = null == team.getEstablishDate()?"":team.getEstablishDate();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("establishDate");
			bean.setPropertyName("成立时间");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getOfficialSite()?"":tmp.getOfficialSite();
		_team = null == team.getOfficialSite()?"":team.getOfficialSite();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("officialSite");
			bean.setPropertyName("公司官网");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
//		 tmp_team = null == tmp.getBusinessDescription()?"":tmp.getBusinessDescription();
//		 _team = null == team.getBusinessDesc()?"":team.getBusinessDesc();
//		if(tmp_team.compareTo(_team) != 0){
//			DIffBean bean = new DIffBean();
//			bean.setProperty("businessDescription");
//			bean.setPropertyName("主要客户");
//			bean.setOldValue(_team);
//			bean.setNewValue(tmp_team);
//			list.add(bean);
//		}
		
		tmp_team = null == tmp.getTeamPhotoUrl()?"":tmp.getTeamPhotoUrl();
		 _team = null == team.getTeamPhotoUrl()?"":team.getTeamPhotoUrl();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamPhotoUrl");
			bean.setPropertyName("公司Logo");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getSkill()?"":tmp.getSkill();
		_team = null == team.getSkill()?"":team.getSkill();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("skill");
			bean.setPropertyName("业务技能");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		return list;
	}

}
