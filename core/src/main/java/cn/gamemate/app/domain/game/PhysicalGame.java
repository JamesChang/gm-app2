package cn.gamemate.app.domain.game;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import proto.response.ResGame;


import cn.gamemate.app.domain.DomainModel;

@RooJavaBean
@RooToString
public class PhysicalGame implements DomainModel{
	
	private static PhysicalGame war3;
	private static PhysicalGame sc;
	static{
		war3 = new PhysicalGame();
		war3.setName("WarCraft 3");
		war3.setMappath("");
		war3.setVersion("1.24.1.6374");
		war3.setVersionDisplay("1.24");
		war3.mainFileName = "war3.exe";
		war3.mainFileDigest = "959721a06338ae4c9e81ba94d2c1b6bd";
		war3.setId(2);
		sc = new PhysicalGame();
		sc.setVersion("1.15.1.1");
		sc.setName("StarCraft");
		sc.setMainFileName("");
		sc.setVersionDisplay("1.15");
		sc.setMainFileName("starcraft.exe");
		sc.setMainFileDigest("cd1f6ccab890dc72b95ee4d391c5a9a3");
		sc.setId(1);
	}
	
	public static PhysicalGame findPhysicalGame(int id){
		switch(id){
		case 1:
			return sc;
		case 2:
			return war3;
		default:
			throw new RuntimeException("can not reach here");
		}
	}
	
	@NotNull
	private int id;
	
	@NotNull
	@Size(max = 40)
	private String name;
	
	@NotNull
	@Size(max = 40)
	private String mappath;
	
	@NotNull
	@Size(max = 20)
	private String version;
	
	@NotNull
	@Size(max = 40)
	private String versionDisplay;
	
	@NotNull
	@Size(max = 40)
	private String mainFileName;
	
	@NotNull
	@Size(max = 32)
	private String mainFileDigest;

	@Override
	public ResGame.PhysicalGame.Builder toProtobuf() {
		proto.response.ResGame.PhysicalGame.Builder builder = ResGame.PhysicalGame.newBuilder();
		builder.setId(id).setName(name).setVersion(
				ResGame.PhysicalGameVersion.newBuilder()
				.setCode(version)
				.setDisplay(versionDisplay)
				.setDigest(mainFileDigest)
				.setFile(mainFileName)
				);
		return builder;
	}

}
