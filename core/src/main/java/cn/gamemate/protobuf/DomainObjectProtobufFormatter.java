package cn.gamemate.protobuf;

import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.game.GameMap;
import proto.res.ResArena;
import proto.response.ResBase;
import proto.response.ResGame;
import proto.response.ResGame.Map;

public class DomainObjectProtobufFormatter {
	
	public static com.google.protobuf.GeneratedMessage createMessage(Object object){
		String format = object.getClass().getName();
		if (format == "cn.gamemate.app.domain.game.GameMap"){
			ResBase.Response.Builder builder = ResBase.Response.newBuilder();
			builder.setMapList(ResGame.MapList.newBuilder().addMaps(
					copyMap((GameMap)object, ResGame.Map.newBuilder())));
			builder.setCode(200000);
			return builder.build();
	
		}
		return null;
		
		
	}
	
	public static ResGame.Map.Builder copyMap(GameMap map, ResGame.Map.Builder builder){
		builder
			.setId(map.getId().intValue())
			.setName(map.getName())
			.setDigest(map.getDigest())
			.setFileSize(map.getFileSize())
			.setDownloadLink(map.getFileLink())
			.setThumbnail(map.getThumbnail());
		return builder;
		
	}
	
	

}
