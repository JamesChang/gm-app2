package cn.gamemate.app.domain.event.rts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResCampusArena;
import proto.response.ResEvent;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.event.EntityEvent;
import cn.gamemate.app.domain.event.awards.UserEventModeData;
import cn.gamemate.app.domain.user.User;

import com.google.common.collect.Lists;

@RooJavaBean
@Entity
@RooEntity
@DiscriminatorValue("rts_home")
public class RtsHome extends EntityEvent implements RtsChildrenSupport{
	
	
	@OneToMany
	@JoinColumn(name="parent_id")
	@OrderBy("creationDate DESC")
	List<RtsElimination> children=Lists.newArrayList();
	
	@Basic
	java.util.Date creationDate;
	
	//- Properties -------------------------------------------
	
	@Override
	public List<? extends RtsElimination> getChildren() {
		return children;
	}
	
	//- Methods ---------------------------------------------
	

	@Override
	public ResEvent.EventGet.Builder toProtobuf(int verbose) {
		ResEvent.EventGet.Builder builder = super.toProtobuf(verbose);
		
		List<RtsElimination> copiedChildren=Lists.newArrayList(children);
		
		Collections.sort(copiedChildren, new Comparator<RtsElimination>(){

			@Override
			public int compare(RtsElimination arg0, RtsElimination arg1) {
				return arg0.getId() - arg1.getId();
			}
			
		});
		
		for(RtsElimination child:copiedChildren){
			builder.addChildren(child.toProtobuf(1));
		}
		
		
		
		return builder;
	}
	
	
	public LeaderBoard getLeaderboad(String mode){
		if (mode==null){return null;}
		List<UserEventModeData> list = UserEventModeData.findForLeaderBoard(0, 20, getId(), mode);
		LeaderBoard leaderBoard = new LeaderBoard(list);
		return leaderBoard;
	}
	
    //- Subclasses ---------------------------------------------
	
	public class LeaderBoard implements DomainModel{
		
		private List<LeaderBoardItem> list;
		
		public LeaderBoard(List<UserEventModeData> dataList) {
			list = Lists.newArrayList();
			for (UserEventModeData data:dataList){
				LeaderBoardItem item = new LeaderBoardItem();
				User user = User.findUser(data.getUserID());
				item.setUsername(user.getName());
				item.setPortrait(user.getPortrait());
				item.setId(Integer.toString(user.getId()));
				item.setData(data);
				list.add(item);
			}
		}

		@Override
		public ResCampusArena.CA078_Leader_Board.Builder toProtobuf() {
			ResCampusArena.CA078_Leader_Board.Builder builder = 
				ResCampusArena.CA078_Leader_Board.newBuilder();
			for(LeaderBoardItem item: list){
				builder.addItems(item.toProtobuf());
			}
			return builder;
		}
		
	}
	
	public class LeaderBoardItem implements DomainModel{
		
		private String username;
		private String portrait;
		private String id;
		private UserEventModeData data;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPortrait() {
			return portrait;
		}
		public void setPortrait(String portrait) {
			this.portrait = portrait;
		}
		public UserEventModeData getData() {
			return data;
		}
		public void setData(UserEventModeData data) {
			this.data = data;
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		@Override
		public ResCampusArena.CA078_Leader_Board_Item.Builder toProtobuf() {
			ResCampusArena.CA078_Leader_Board_Item.Builder builder = 
				ResCampusArena.CA078_Leader_Board_Item.newBuilder();
			builder.setName(username)
			.setImage(portrait)
			.setRtsScore(data.rtsScore)
			.setFirst(data.first)
			.setSecond(data.second)
			.setThird(data.third)
			.setForceType("USER")
			.setId(id)
			;
			return builder;
		}
		
		
	}
	
}




