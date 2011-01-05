package cn.gamemate.app.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.JsonFormat;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.protobuf.DomainObjectProtobufFormatter;

public class ProtobufJsonView extends AbstractView{
	
	public static final String DEFAULT_CONTENT_TYPE = "application/json";
	
	

	public ProtobufJsonView() {
		setContentType(DEFAULT_CONTENT_TYPE);
		
	}



	@Override
	protected void renderMergedOutputModel(Map<String, Object> models,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		DomainModel object = (DomainModel) models.get("object");
		if (object == null) {
			throw new RuntimeException("domain object not set");
		}
		String subpb = (String)models.get("subpb");
		if (subpb == null) {
			throw new RuntimeException("protobuf field not set");
		}
		GeneratedMessage pb = ProtobufViewUtils.createMessage(object, subpb);
		String jsonFormat = JsonFormat.printToString(pb);
		arg2.getWriter().println(jsonFormat);		
	}

}
