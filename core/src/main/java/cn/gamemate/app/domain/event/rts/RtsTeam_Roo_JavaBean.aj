// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.user.User;
import java.lang.Integer;
import java.lang.String;

privileged aspect RtsTeam_Roo_JavaBean {
    
    public String RtsTeam.getName() {
        return this.name;
    }
    
    public void RtsTeam.setName(String name) {
        this.name = name;
    }
    
    public String RtsTeam.getImage() {
        return this.image;
    }
    
    public void RtsTeam.setImage(String image) {
        this.image = image;
    }
    
    public String RtsTeam.getSlogan() {
        return this.slogan;
    }
    
    public void RtsTeam.setSlogan(String slogan) {
        this.slogan = slogan;
    }
    
    public String RtsTeam.getPrefix() {
        return this.prefix;
    }
    
    public void RtsTeam.setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public String RtsTeam.getUuid() {
        return this.uuid;
    }
    
    public void RtsTeam.setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public Integer RtsTeam.getLgid() {
        return this.lgid;
    }
    
    public void RtsTeam.setLgid(Integer lgid) {
        this.lgid = lgid;
    }
    
    public User RtsTeam.getLeader() {
        return this.leader;
    }
    
    public void RtsTeam.setLeader(User leader) {
        this.leader = leader;
    }
    
}
