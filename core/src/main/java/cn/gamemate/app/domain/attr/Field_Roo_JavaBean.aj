// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.attr;

import java.lang.String;

privileged aspect Field_Roo_JavaBean {
    
    public boolean Field.isRequired() {
        return this.required;
    }
    
    public void Field.setRequired(boolean required) {
        this.required = required;
    }
    
    public String Field.getLabel() {
        return this.label;
    }
    
    public void Field.setLabel(String label) {
        this.label = label;
    }
    
    public String Field.getInitial() {
        return this.initial;
    }
    
    public void Field.setInitial(String initial) {
        this.initial = initial;
    }
    
    public String Field.getHelp_text() {
        return this.help_text;
    }
    
    public void Field.setHelp_text(String help_text) {
        this.help_text = help_text;
    }
    
}
