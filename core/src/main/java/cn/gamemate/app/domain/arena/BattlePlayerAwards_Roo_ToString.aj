// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package cn.gamemate.app.domain.arena;

import java.lang.String;

privileged aspect BattlePlayerAwards_Roo_ToString {
    
    public String BattlePlayerAwards.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gold: ").append(getGold()).append(", ");
        sb.append("Activity: ").append(getActivity()).append(", ");
        sb.append("Power: ").append(getPower());
        return sb.toString();
    }
    
}
