package cn.gamemate.app.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.google.protobuf.GeneratedMessage;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.protobuf.DomainObjectProtobufFormatter;

public class ProtobufBinaryView extends AbstractView{
	
	public static final String DEFAULT_CONTENT_TYPE = "application/protobuf";
	
	

	public ProtobufBinaryView() {
		setContentType(DEFAULT_CONTENT_TYPE);
		
	}



	@Override
	protected void renderMergedOutputModel(Map<String, Object> models,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		DomainModel object = (DomainModel) models.get("object");
		Exception ex = (Exception) models.get("ex");
		if (object != null) {
			String subpb = (String)models.get("subpb");
			if (subpb == null) {
				throw new RuntimeException("protobuf field not set");
			}
			GeneratedMessage pb = ProtobufViewUtils.createMessage(object, subpb);
			pb.writeTo(arg2.getOutputStream());
		}
		else if (ex != null){
			GeneratedMessage pb = ProtobufViewUtils.createMessage(ex);
			pb.writeTo(arg2.getOutputStream());
		}
		else{
			GeneratedMessage pb = ProtobufViewUtils.createMessage();
			pb.writeTo(arg2.getOutputStream());
		}
		
	}

}
