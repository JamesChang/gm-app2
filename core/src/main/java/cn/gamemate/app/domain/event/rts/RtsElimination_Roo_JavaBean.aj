// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.event.rts.RtsEliminationMatch;
import cn.gamemate.app.domain.event.rts.RtsEliminationMatch.Status;
import cn.gamemate.app.domain.event.rts.RtsEventForce;
import cn.gamemate.app.domain.event.rts.RtsEventForceType;
import cn.gamemate.app.domain.event.rts.RtsLocation;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.User;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import java.util.List;

privileged aspect RtsElimination_Roo_JavaBean {
    
    public Date RtsElimination.getCreationDate() {
        return this.creationDate;
    }
    
    public void RtsElimination.setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Integer RtsElimination.getGameId() {
        return this.gameId;
    }
    
    public void RtsElimination.setGameId(Integer gameId) {
        this.gameId = gameId;
    }
    
    public GameMap RtsElimination.getMap() {
        return this.map;
    }
    
    public void RtsElimination.setMap(GameMap map) {
        this.map = map;
    }
    
    public List<RtsEliminationMatch> RtsElimination.getChildren() {
        return this.children;
    }
    
    public void RtsElimination.setChildren(List<RtsEliminationMatch> children) {
        this.children = children;
    }
    
    public Status RtsElimination.getStatus() {
        return this.status;
    }
    
    public void RtsElimination.setStatus(Status status) {
        this.status = status;
    }
    
    public Integer RtsElimination.getDefaultRequiredWin() {
        return this.defaultRequiredWin;
    }
    
    public void RtsElimination.setDefaultRequiredWin(Integer defaultRequiredWin) {
        this.defaultRequiredWin = defaultRequiredWin;
    }
    
    public RtsLocation RtsElimination.getDefaultPlayGround() {
        return this.defaultPlayGround;
    }
    
    public void RtsElimination.setDefaultPlayGround(RtsLocation defaultPlayGround) {
        this.defaultPlayGround = defaultPlayGround;
    }
    
    public RtsEventForceType RtsElimination.getEventForceType() {
        return this.eventForceType;
    }
    
    public void RtsElimination.setEventForceType(RtsEventForceType eventForceType) {
        this.eventForceType = eventForceType;
    }
    
    public String RtsElimination.getExpectedTime() {
        return this.expectedTime;
    }
    
    public void RtsElimination.setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }
    
    public String RtsElimination.getRules() {
        return this.rules;
    }
    
    public void RtsElimination.setRules(String rules) {
        this.rules = rules;
    }
    
    public List<User> RtsElimination.getAdministrators() {
        return this.administrators;
    }
    
    public void RtsElimination.setAdministrators(List<User> administrators) {
        this.administrators = administrators;
    }
    
    public void RtsElimination.setExpectedRegistrationStartTime(Date expectedRegistrationStartTime) {
        this.expectedRegistrationStartTime = expectedRegistrationStartTime;
    }
    
    public Date RtsElimination.getExpectedRegistrationEndTime() {
        return this.expectedRegistrationEndTime;
    }
    
    public void RtsElimination.setExpectedRegistrationEndTime(Date expectedRegistrationEndTime) {
        this.expectedRegistrationEndTime = expectedRegistrationEndTime;
    }
    
    public Date RtsElimination.getExpectedExtraCheckStartTime() {
        return this.expectedExtraCheckStartTime;
    }
    
    public void RtsElimination.setExpectedExtraCheckStartTime(Date expectedExtraCheckStartTime) {
        this.expectedExtraCheckStartTime = expectedExtraCheckStartTime;
    }
    
    public Date RtsElimination.getExpectedExtraCheckEndTime() {
        return this.expectedExtraCheckEndTime;
    }
    
    public void RtsElimination.setExpectedExtraCheckEndTime(Date expectedExtraCheckEndTime) {
        this.expectedExtraCheckEndTime = expectedExtraCheckEndTime;
    }
    
    public boolean RtsElimination.isAutoExtraCheck() {
        return this.autoExtraCheck;
    }
    
    public void RtsElimination.setAutoExtraCheck(boolean autoExtraCheck) {
        this.autoExtraCheck = autoExtraCheck;
    }
    
    public Integer RtsElimination.getRegistrationLimit() {
        return this.registrationLimit;
    }
    
    public void RtsElimination.setRegistrationLimit(Integer registrationLimit) {
        this.registrationLimit = registrationLimit;
    }
    
    public boolean RtsElimination.isAllowPlayerRegistrationAfterPrepared() {
        return this.allowPlayerRegistrationAfterPrepared;
    }
    
    public void RtsElimination.setAllowPlayerRegistrationAfterPrepared(boolean allowPlayerRegistrationAfterPrepared) {
        this.allowPlayerRegistrationAfterPrepared = allowPlayerRegistrationAfterPrepared;
    }
    
    public boolean RtsElimination.isAllowPlayerRegister() {
        return this.allowPlayerRegister;
    }
    
    public void RtsElimination.setAllowPlayerRegister(boolean allowPlayerRegister) {
        this.allowPlayerRegister = allowPlayerRegister;
    }
    
    public void RtsElimination.setRegisteredForces(List<RtsEventForce> registeredForces) {
        this.registeredForces = registeredForces;
    }
    
}
