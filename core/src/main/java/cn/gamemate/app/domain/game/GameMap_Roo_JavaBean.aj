// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.game;

import java.lang.Integer;
import java.lang.String;

privileged aspect GameMap_Roo_JavaBean {
    
    public String GameMap.getName() {
        return this.name;
    }
    
    public void GameMap.setName(String name) {
        this.name = name;
    }
    
    public String GameMap.getDigest() {
        return this.digest;
    }
    
    public void GameMap.setDigest(String digest) {
        this.digest = digest;
    }
    
    public Integer GameMap.getFileSize() {
        return this.fileSize;
    }
    
    public void GameMap.setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
    
    public String GameMap.getFileLink() {
        return this.fileLink;
    }
    
    public void GameMap.setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }
    
    public String GameMap.getAttrsInJson() {
        return this.attrsInJson;
    }
    
    public void GameMap.setAttrsInJson(String attrsInJson) {
        this.attrsInJson = attrsInJson;
    }
    
}
