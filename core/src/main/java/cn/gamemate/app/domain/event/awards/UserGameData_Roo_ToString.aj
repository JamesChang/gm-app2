// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.event.awards;

import java.lang.String;

privileged aspect UserGameData_Roo_ToString {
    
    public String UserGameData.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameID: ").append(getGameID()).append(", ");
        sb.append("UserID: ").append(getUserID()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
    
}
