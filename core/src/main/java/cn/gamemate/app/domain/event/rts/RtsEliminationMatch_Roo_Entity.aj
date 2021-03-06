// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.event.rts.RtsEliminationMatch;
import java.lang.Integer;
import java.util.List;

privileged aspect RtsEliminationMatch_Roo_Entity {
    
    public static long RtsEliminationMatch.countRtsEliminationMatches() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RtsEliminationMatch o", Long.class).getSingleResult();
    }
    
    public static List<RtsEliminationMatch> RtsEliminationMatch.findAllRtsEliminationMatches() {
        return entityManager().createQuery("SELECT o FROM RtsEliminationMatch o", RtsEliminationMatch.class).getResultList();
    }
    
    public static RtsEliminationMatch RtsEliminationMatch.findRtsEliminationMatch(Integer id) {
        if (id == null) return null;
        return entityManager().find(RtsEliminationMatch.class, id);
    }
    
    public static List<RtsEliminationMatch> RtsEliminationMatch.findRtsEliminationMatchEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RtsEliminationMatch o", RtsEliminationMatch.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
