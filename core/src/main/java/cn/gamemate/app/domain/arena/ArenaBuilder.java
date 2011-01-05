package cn.gamemate.app.domain.arena;

public interface ArenaBuilder {
	
	boolean isReady();
	Arena newArena();
	ArenaBuilder copy();

}
