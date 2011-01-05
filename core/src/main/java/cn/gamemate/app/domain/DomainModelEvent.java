package cn.gamemate.app.domain;

public class DomainModelEvent {
	
	protected DomainModel model;
	
	public DomainModelEvent(DomainModel model){
		this.model = model;
	}
	
	public DomainModel getModel(){
		return model;
	}

}
