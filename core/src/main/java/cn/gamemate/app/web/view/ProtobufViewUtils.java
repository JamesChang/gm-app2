package cn.gamemate.app.web.view;

import java.util.HashMap;
import java.util.Map;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelRuntimeException;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;

import proto.response.ResBase;
import proto.response.ResBase.ResponseError;
import proto.response.ResBase.ResponseError.Builder;


public class ProtobufViewUtils {
	private static Map<String,  FieldDescriptor> subFields = new HashMap<String,  FieldDescriptor>();
	private static ResBase.Response success;
	private static ResBase.Response serverError;
	static{
		for(FieldDescriptor field:ResBase.Response.getDescriptor().getFields()){
			subFields.put(field.getName(), field);
		}
		success = ResBase.Response.newBuilder().setCode(200000).build();
		serverError = ResBase.Response.newBuilder().setCode(500000).build();
	}
	
	
	public static GeneratedMessage createMessage(DomainModel domainModel, String fieldname){
		ResBase.Response.Builder builder = ResBase.Response.newBuilder();
		FieldDescriptor field = subFields.get(fieldname);
		if (field ==null){
			throw new RuntimeException("field not found");
		}
		builder.setField(field, domainModel.toProtobuf().build());
		builder.setCode(200000);
		return builder.build();
	}


	public static GeneratedMessage createMessage() {
		return success;
	}


	public static GeneratedMessage createMessage(Exception ex) {
		
		if (ex instanceof DomainModelRuntimeException){
			DomainModelRuntimeException dmex = (DomainModelRuntimeException)ex;
			ResBase.Response.Builder builder = ResBase.Response.newBuilder();
			builder.setCode((int)dmex.getErrorCode());
			Builder errBuilder = ResponseError.newBuilder()
			.setCode((int)dmex.getErrorCode())
			.setDesc(ex.getMessage());
			if (dmex.getData()!=null){
				errBuilder.setErrorData(dmex.getData());
			}
			builder.addErrors(errBuilder);
			return builder.build();
		}else{
			return serverError;
			
		}
		
	}
}
