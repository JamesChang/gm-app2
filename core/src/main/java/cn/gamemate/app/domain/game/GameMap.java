package cn.gamemate.app.domain.game;

import java.io.BufferedInputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.serializable.RooSerializable;

import proto.response.ResGame;
import proto.response.ResGame.Map.Builder;



@Entity
@RooJavaBean
@RooToString
@RooEntity
@Table(name = "gm_map")
@RooSerializable
public class GameMap {
	private static final Logger logger = LoggerFactory.getLogger(GameMap.class);

	@NotNull
	@Size(max = 40)
	private String name;

	@NotNull
	@Size(max = 32)
	private String digest;

	@NotNull
	@Column(name = "file_size")
	private Integer fileSize;

	@Column(name = "file_link")
	private String fileLink;
	
	@Column(name = "attrs_json")
	private String attrsInJson;
	
	
	public int getMaxUserCount(){ 
		//TODO not null
		ObjectMapper  m = new ObjectMapper();
		JsonNode rootNode;
		try {
			rootNode = m.readValue(this.attrsInJson, JsonNode.class);
			return rootNode.get("max_user_count").getIntValue();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
		
	}
	

	public String getThumbnail() {
		return fileLink + ".png";
	}
	
	

	public void setFile(String filename) throws Exception {

		BufferedInputStream f = new BufferedInputStream(new FileInputStream(
				filename));
		byte[] data;
		try {
			data = new byte[f.available()];
			f.read(data);
		} finally {
			f.close();
		}
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			digest = new String(md.digest(data));
			fileSize = data.length;

		} catch (NoSuchAlgorithmException e) {
			logger.error("", e);
			throw new Exception("can not reach here");

		}

	}
	 public String toString() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Thumbnail: ").append(getThumbnail()).append(", ");
	        sb.append("Id: ").append(getId()).append(", ");
	        sb.append("Version: ").append(getVersion()).append(", ");
	        sb.append("Name: ").append(getName()).append(", ");
	        sb.append("Digest: ").append(getDigest()).append(", ");
	        sb.append("FileSize: ").append(getFileSize()).append(", ");
	        sb.append("FileLink: ").append(getFileLink()).append(", ");
	        sb.append("AttrsInJson: ").append(getAttrsInJson());
	        return sb.toString();
	    }
	 
	 //TODO: move this to one another AJ file
	 public ResGame.Map.Builder toProtobuf(){
		 ResGame.Map.Builder builder = ResGame.Map.newBuilder();
		 copyTo(builder);
		 return builder;
	 }


	private void copyTo(Builder builder) {
		builder
		.setId(this.getId().intValue())
		.setName(this.getName())
		.setDigest(this.getDigest())
		.setFileSize(this.getFileSize())
		.setDownloadLink(this.getFileLink())
		.setThumbnail(this.getThumbnail());
	
		
	}
	 
}
