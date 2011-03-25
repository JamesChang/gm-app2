package cn.gamemate.app.domain;

import java.util.UUID;

public class ObjectNotFound extends DomainModelRuntimeException{
	
	public ObjectNotFound(Class cls, String id) {
		super(cls.getName()+"{id="+id+"} Not Found.");
	}
	public ObjectNotFound(Class cls, Integer id) {
		super(cls.getName()+"{id="+id.toString()+"} Not Found.");
	}
	public ObjectNotFound(Class cls, UUID id) {
		super(cls.getName()+"{id="+id.toString()+"} Not Found.");
	}

	public ObjectNotFound(Class cls, org.safehaus.uuid.UUID id) {
		super(cls.getName()+"{id="+id.toString()+"} Not Found.");
	}
	
	public long getErrorCode(){
		return 404000;
	}

}
