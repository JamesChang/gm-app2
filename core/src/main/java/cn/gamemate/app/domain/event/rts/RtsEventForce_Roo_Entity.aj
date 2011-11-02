// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.event.rts.RtsEventForce;
import java.lang.Long;
import java.util.List;

privileged aspect RtsEventForce_Roo_Entity {
    
    public static long RtsEventForce.countRtsEventForces() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RtsEventForce o", Long.class).getSingleResult();
    }
    
    public static List<RtsEventForce> RtsEventForce.findAllRtsEventForces() {
        return entityManager().createQuery("SELECT o FROM RtsEventForce o", RtsEventForce.class).getResultList();
    }
    
    public static RtsEventForce RtsEventForce.findRtsEventForce(Long id) {
        if (id == null) return null;
        return entityManager().find(RtsEventForce.class, id);
    }
    
    public static List<RtsEventForce> RtsEventForce.findRtsEventForceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RtsEventForce o", RtsEventForce.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
