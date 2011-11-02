// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.event.rts.RtsTeam;
import java.lang.Integer;
import java.lang.String;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect RtsTeam_Roo_Entity {
    
    declare @type: RtsTeam: @Entity;
    
    @PersistenceContext
    transient EntityManager RtsTeam.entityManager;
    
    @Version
    @Column(name = "version")
    private Integer RtsTeam.version;
    
    public Integer RtsTeam.getVersion() {
        return this.version;
    }
    
    public void RtsTeam.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void RtsTeam.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void RtsTeam.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            RtsTeam attached = RtsTeam.findRtsTeam(this.uuid);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void RtsTeam.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void RtsTeam.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public RtsTeam RtsTeam.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RtsTeam merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager RtsTeam.entityManager() {
        EntityManager em = new RtsTeam().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long RtsTeam.countRtsTeams() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RtsTeam o", Long.class).getSingleResult();
    }
    
    public static List<RtsTeam> RtsTeam.findAllRtsTeams() {
        return entityManager().createQuery("SELECT o FROM RtsTeam o", RtsTeam.class).getResultList();
    }
    
    public static RtsTeam RtsTeam.findRtsTeam(String uuid) {
        if (uuid == null || uuid.length() == 0) return null;
        return entityManager().find(RtsTeam.class, uuid);
    }
    
    public static List<RtsTeam> RtsTeam.findRtsTeamEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RtsTeam o", RtsTeam.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}