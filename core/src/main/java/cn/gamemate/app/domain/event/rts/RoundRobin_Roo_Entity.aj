// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.event.rts.RoundRobin;
import java.lang.Integer;
import java.util.List;

privileged aspect RoundRobin_Roo_Entity {
    
    public static long RoundRobin.countRoundRobins() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoundRobin o", Long.class).getSingleResult();
    }
    
    public static List<RoundRobin> RoundRobin.findAllRoundRobins() {
        return entityManager().createQuery("SELECT o FROM RoundRobin o", RoundRobin.class).getResultList();
    }
    
    public static RoundRobin RoundRobin.findRoundRobin(Integer id) {
        if (id == null) return null;
        return entityManager().find(RoundRobin.class, id);
    }
    
    public static List<RoundRobin> RoundRobin.findRoundRobinEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoundRobin o", RoundRobin.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
