package cn.gamemate.app.domain.event.rts;

import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;

import com.google.common.collect.Lists;

import cn.gamemate.app.domain.event.EntityEvent;
import cn.gamemate.app.domain.event.Event;
import cn.gamemate.app.domain.event.awards.AwardsPackage;

@RooJavaBean
public class RtsAwardsPackageLoader {
	
	private List<AwardsPackage> rtsAwardsPackage;
	
	private List<AwardsPackage> sjtuAwardsPackage;
	
	private List<AwardsPackage> group310;
	
	public List<AwardsPackage> getAwardsPackage(RtsEliminationMatch event){
		EntityEvent parent = event.getParent();
		
		if (parent==null){return Lists.newArrayList();}
			
		if (parent instanceof RoundRobin)
			return group310;
		else if (parent.getParentId()!=null){
			
			
			return rtsAwardsPackage;
		}else{
			return sjtuAwardsPackage;
		}
		
	}

}
