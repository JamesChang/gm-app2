// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.event.rts.GroupElimination;
import java.lang.Integer;
import java.util.List;

privileged aspect GroupElimination_Roo_Entity {
    
    public static long GroupElimination.countGroupEliminations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM GroupElimination o", Long.class).getSingleResult();
    }
    
    public static List<GroupElimination> GroupElimination.findAllGroupEliminations() {
        return entityManager().createQuery("SELECT o FROM GroupElimination o", GroupElimination.class).getResultList();
    }
    
    public static GroupElimination GroupElimination.findGroupElimination(Integer id) {
        if (id == null) return null;
        return entityManager().find(GroupElimination.class, id);
    }
    
    public static List<GroupElimination> GroupElimination.findGroupEliminationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM GroupElimination o", GroupElimination.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
