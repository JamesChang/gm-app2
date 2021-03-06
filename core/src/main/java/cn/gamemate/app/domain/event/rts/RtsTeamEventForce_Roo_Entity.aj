// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.event.rts.RtsTeamEventForce;
import java.lang.Long;
import java.util.List;

privileged aspect RtsTeamEventForce_Roo_Entity {
    
    public RtsTeamEventForce.new() {
        super();
    }

    public static long RtsTeamEventForce.countRtsTeamEventForces() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RtsTeamEventForce o", Long.class).getSingleResult();
    }
    
    public static List<RtsTeamEventForce> RtsTeamEventForce.findAllRtsTeamEventForces() {
        return entityManager().createQuery("SELECT o FROM RtsTeamEventForce o", RtsTeamEventForce.class).getResultList();
    }
    
    public static RtsTeamEventForce RtsTeamEventForce.findRtsTeamEventForce(Long id) {
        if (id == null) return null;
        return entityManager().find(RtsTeamEventForce.class, id);
    }
    
    public static List<RtsTeamEventForce> RtsTeamEventForce.findRtsTeamEventForceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RtsTeamEventForce o", RtsTeamEventForce.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
