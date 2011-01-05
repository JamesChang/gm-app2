package cn.gamemate.app.domain.attr;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
public class Field {
	private boolean required;
	private String label;
	private String initial;
	private String help_text;

	public Field(String label, boolean required, String initial,
			String help_text) {
		super();
		this.required = required;
		this.label = label;
		this.initial = initial;
		this.help_text = help_text;
	}
	
	public Field(String label){
		this(label, true, null, null);
	}
	
	public boolean validate(String value){
		if (required && (value==null || value=="")){
			return false;
		}
		return true;
	}

}
