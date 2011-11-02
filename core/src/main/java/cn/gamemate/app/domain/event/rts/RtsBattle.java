package cn.gamemate.app.domain.event.rts;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.springframework.roo.addon.javabean.RooJavaBean;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import proto.response.ResEvent;
import proto.response.ResWar3Detail;
import proto.response.ResWar3Detail.War3Detail;

import cn.gamemate.app.domain.DomainModel;

@RooJavaBean
@Embeddable
public class RtsBattle implements DomainModel{
	
	//private Integer winnerForce; 
	
	private Integer homeSubmitWinner;
	
	private Integer awaySubmitWinner;
	
	private Integer mandatoryWinner;
	
	private boolean disputed;
	
	private String mapName;
	
	private String expectedTime;
	
	private byte[] war3DetailData;
	
	/**
	 * @return 0 for home win, 1 for away win, null if not valid
	 */
	public Integer getWinnerForce(){

		if (mandatoryWinner !=null){
			return mandatoryWinner;
		}
		if (homeSubmitWinner == null){
			return awaySubmitWinner;
		}else if (awaySubmitWinner == null){
			return homeSubmitWinner;
		}else if (homeSubmitWinner.equals( awaySubmitWinner)){
			return homeSubmitWinner;
		}else{
			return null;
		}
		
	}
	
	public boolean isDisputed(){
		if (mandatoryWinner == null && 
			homeSubmitWinner != null && 
			awaySubmitWinner != null && 
			!homeSubmitWinner.equals(awaySubmitWinner)){
			return true;
		}
		return false;
	}
	

	@Override
	public ResEvent.EliminationBattle.Builder toProtobuf() {
		ResEvent.EliminationBattle.Builder builder = ResEvent.EliminationBattle.newBuilder();
		Integer winnerForce = getWinnerForce();
		if (winnerForce != null){
			builder.setWinner(winnerForce);
		}
		builder.setDisputed(isDisputed());
		if (expectedTime!=null)
			builder.setExpectedTime(expectedTime);
		if (mapName!=null)
			builder.setMapName(mapName);
		if (war3DetailData != null){
			try{
				War3Detail war3 = ResWar3Detail.War3Detail.parseFrom(war3DetailData);
				builder.setWar3Detail(war3);
			}catch(InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return builder;
	}
	
	
	
	

}
