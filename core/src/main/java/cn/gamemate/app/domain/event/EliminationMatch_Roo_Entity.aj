// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event;

import cn.gamemate.app.domain.event.EliminationMatch;
import java.lang.Integer;
import java.util.List;

privileged aspect EliminationMatch_Roo_Entity {
    
    public static long EliminationMatch.countEliminationMatches() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EliminationMatch o", Long.class).getSingleResult();
    }
    
    public static List<EliminationMatch> EliminationMatch.findAllEliminationMatches() {
        return entityManager().createQuery("SELECT o FROM EliminationMatch o", EliminationMatch.class).getResultList();
    }
    
    public static EliminationMatch EliminationMatch.findEliminationMatch(Integer id) {
        if (id == null) return null;
        return entityManager().find(EliminationMatch.class, id);
    }
    
    public static List<EliminationMatch> EliminationMatch.findEliminationMatchEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EliminationMatch o", EliminationMatch.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}