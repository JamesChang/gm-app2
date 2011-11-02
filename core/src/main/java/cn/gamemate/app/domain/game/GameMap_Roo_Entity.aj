// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.game;

import cn.gamemate.app.domain.game.GameMap;
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

privileged aspect GameMap_Roo_Entity {
    
    @PersistenceContext
    transient EntityManager GameMap.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long GameMap.id;
    
    @Version
    @Column(name = "version")
    private Integer GameMap.version;
    
    public Long GameMap.getId() {
        return this.id;
    }
    
    public void GameMap.setId(Long id) {
        this.id = id;
    }
    
    public Integer GameMap.getVersion() {
        return this.version;
    }
    
    public void GameMap.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void GameMap.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void GameMap.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            GameMap attached = GameMap.findGameMap(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void GameMap.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void GameMap.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public GameMap GameMap.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        GameMap merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager GameMap.entityManager() {
        EntityManager em = new GameMap().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long GameMap.countGameMaps() {
        return entityManager().createQuery("SELECT COUNT(o) FROM GameMap o", Long.class).getSingleResult();
    }
    
    public static List<GameMap> GameMap.findAllGameMaps() {
        return entityManager().createQuery("SELECT o FROM GameMap o", GameMap.class).getResultList();
    }
    
    public static GameMap GameMap.findGameMap(Long id) {
        if (id == null) return null;
        return entityManager().find(GameMap.class, id);
    }
    
    public static List<GameMap> GameMap.findGameMapEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM GameMap o", GameMap.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
