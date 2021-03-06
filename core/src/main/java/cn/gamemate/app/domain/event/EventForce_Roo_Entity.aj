// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event;

import cn.gamemate.app.domain.event.EventForce;
import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EventForce_Roo_Entity {
    
    @PersistenceContext
    transient EntityManager EventForce.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long EventForce.id;
    
    @Version
    @Column(name = "version")
    private Integer EventForce.version;
    
    public Long EventForce.getId() {
        return this.id;
    }
    
    public void EventForce.setId(Long id) {
        this.id = id;
    }
    
    public Integer EventForce.getVersion() {
        return this.version;
    }
    
    public void EventForce.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void EventForce.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EventForce.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EventForce attached = EventForce.findEventForce(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EventForce.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EventForce.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EventForce EventForce.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EventForce merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager EventForce.entityManager() {
        EntityManager em = new EventForce().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EventForce.countEventForces() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EventForce o", Long.class).getSingleResult();
    }
    
    public static List<EventForce> EventForce.findAllEventForces() {
        return entityManager().createQuery("SELECT o FROM EventForce o", EventForce.class).getResultList();
    }
    
    public static EventForce EventForce.findEventForce(Long id) {
        if (id == null) return null;
        return entityManager().find(EventForce.class, id);
    }
    
    public static List<EventForce> EventForce.findEventForceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EventForce o", EventForce.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
