package cn.gamemate.app.domain;

import com.google.protobuf.GeneratedMessage.Builder;

public class DomainModelView implements DomainModel{
	
	private Builder builder;
	
	public DomainModelView(Builder builder) {
		this.builder = builder;
	}

	@Override
	public Builder toProtobuf() {
		return builder;
	}

}
