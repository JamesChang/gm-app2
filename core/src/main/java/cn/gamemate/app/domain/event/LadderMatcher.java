package cn.gamemate.app.domain.event;

interface LadderMatcher {
	void add(Party party);
	void remove(Party party);
	void tick();
	void addHandler(MatcherHandler handler);
}
