// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.user.User;
import java.util.List;

privileged aspect RtsSingleUserEventForce_Roo_JavaBean {
    
    public List<User> RtsSingleUserEventForce.getUsers() {
        return this.users;
    }
    
    public void RtsSingleUserEventForce.setUsers(List<User> users) {
        this.users = users;
    }
    
    public User RtsSingleUserEventForce.getUser() {
        return this.user;
    }
    
    public void RtsSingleUserEventForce.setUser(User user) {
        this.user = user;
    }
    
}