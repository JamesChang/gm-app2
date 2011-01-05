package cn.gamemate.app.domain.event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.gamemate.app.domain.user.UserRepository;

import com.google.common.base.Objects;


//must be thread safe


//TODO: when someone leave or status changed, quit imm.

public class GreedyMatcher {
	
	final int QUEUE_MAX_LENGTH = 1000;
	final int T0_TIME = 4*1000;
	
	class ArenaWrapper{
		Party arena;
		long tStart;
		int playerCount;
		
		public ArenaWrapper(Party arena) {
			this.arena = arena;
			tStart = System.currentTimeMillis();
			playerCount = arena.getPlayerCount();
		}
		@Override
		public String toString() {
		
			return Objects.toStringHelper(this).add("arena", arena).add("tStart", tStart).add("players", playerCount).toString();
		}
		
	}
	
	private final int mcount;
	private final UserRepository userRepository; 
	//private final List<List<ArenaWrapper>> workspace;
	private final Queue<Party> toBeAdd;
	private final Collection<ArenaWrapper> t0;
	private final List<ArenaWrapper> space;
	private final List<MatcherHandler> handlers ;
	private long lastModify;
	private long lastWork;
	
	public GreedyMatcher(int mcount, UserRepository userRepository) {
		this.mcount = mcount;
		this.userRepository = userRepository;
		//this.workspace = new ArrayList<List<ArenaWrapper>>();
		//for (int i=0;i<mcount;i++){
		//	workspace.add(new ArrayList<ArenaWrapper>());
		//}
		t0= new LinkedBlockingQueue<ArenaWrapper>();
		space = new ArrayList<ArenaWrapper>();
		toBeAdd = new LinkedBlockingQueue<Party>();
		handlers = new ArrayList<MatcherHandler>();
	}
	
	public void add(Party arena){
		//TODO prevent from the same arena being added twice
		toBeAdd.add(arena);
	}
	
	synchronized public void remove(Party arena){
		toBeAdd.remove(arena);
		for (Iterator<ArenaWrapper> iter = t0.iterator(); iter.hasNext();){
			ArenaWrapper next = iter.next();
			if (next.arena.equals(arena)){
				iter.remove();
			}
		}
		for (Iterator<ArenaWrapper> iter = space.iterator(); iter.hasNext();){
			ArenaWrapper next = iter.next();
			if (next.arena.equals(arena)){
				iter.remove();
			}
		}
	}
	
	
	synchronized public void tick(){
		doOnce();
	}
	
	synchronized public void addHandler(MatcherHandler handler){
		handlers.add(handler);
	}
	
	protected void fireMatched(List<Party> parties1, List<Party> parties2){
		for(MatcherHandler handler: handlers){
			MatcherContext ctx = new MatcherContext();
			ctx.force1Parties= parties1;
			ctx.force2Parties= parties2;
			handler.onMatched(ctx);
		}
	}
	
	protected void doOnce(){
		//add nodes from toBeAdd to t0
		//System.out.println(toBeAdd);
		Party arena;
		while(true){
			arena = toBeAdd.poll();
			if (arena == null) break;
			ArenaWrapper wrapper = new ArenaWrapper(arena);
			t0.add(wrapper);
		}
		//System.out.println(toBeAdd);		
		//scan t0, add nodes to space
		long tNow = System.currentTimeMillis()- T0_TIME;
		//System.out.println("now" + tNow);
		//System.out.println("t0:"+ t0);
		for(Iterator<ArenaWrapper> iter = t0.iterator();iter.hasNext();){
			ArenaWrapper node = iter.next();
			if (node.tStart<tNow){
				space.add(node);
				iter.remove();
				lastModify = tNow;
			}
		}
		//System.out.println("t0:"+ t0);
		//System.out.println("space:"+ space);
		
		if (lastWork == lastModify){
			return;
		}
		lastWork = tNow;
		//scan space
		while(true){
			
			int [][]reach = new int[mcount+1][mcount+1];
			int [][]version = new int[mcount+1][mcount+1];
			reach[0][0]=1;
			int [][] select = new int[2][mcount+1];
			List<Party> result1 = new ArrayList<Party>();
			List<Party> result2 = new ArrayList<Party>();
			int v = 0;
			for(Iterator<ArenaWrapper> iter = space.iterator();iter.hasNext();){
				ArenaWrapper node = iter.next();
				v++;
				//System.out.println("round: "+ v);
				for (int i=0;i<=mcount;++i){
					for (int j=0;j<=mcount;j++){
						//System.out.println(reach[i][j]);
						//System.out.println(version[i][j]);
						
						if (reach[i][j]!=0 && version[i][j] != v){
							if (i+node.playerCount <= mcount && reach[i+node.playerCount][j] == 0){
								
								reach[i+node.playerCount][j] = node.playerCount;
								version[i+node.playerCount][j] = v;
								//System.out.println("set "+ (i+node.playerCount) + " " + j + " " + reach[i+node.playerCount][j]);
							}
							if (j+node.playerCount <= mcount && reach[i][j+node.playerCount] == 0){
								reach[i][j+node.playerCount] = -node.playerCount;
								version[i][j+node.playerCount] = v;
								//System.out.println("set "+ i + " " + (node.playerCount+j) + " " + reach[i][node.playerCount+j]);
							}
						}
								
					}
				}	
			}
			System.out.println("reach:");
			for (int i=0;i<=mcount;i++){				
				for (int j=0;j<=mcount;j++){
					System.out.print(" " + reach[i][j]);
				}
				System.out.println();
			}
			if (reach[mcount][mcount]!=0)
			System.out.println("MATCHED");
			//reach[mfull]!=0 --> get a matching  
			if (reach[mcount][mcount]!=0){
				int i = mcount, j=mcount;
				
				while(i>0 || j>0){
					int select_mcount = reach[i][j];
					if (select_mcount>0){
						select[0][select_mcount] ++;
						i=i-select_mcount;
					}	
					else{
						select[1][-select_mcount]++;
						j=j+select_mcount;
					}
				}
				/*for (int k =0;k<select[0].length;k++){
					System.out.println("select0: "+ k + " "+ select[0][k]);
				}
				for (int k =0;k<select[1].length;k++){
					System.out.println("select1: "+ k + " "+ select[1][k]);
				}*/
				
				for(Iterator<ArenaWrapper> iter = space.iterator();iter.hasNext();){
					ArenaWrapper node = iter.next();
					if (select[0][node.playerCount] >0){
						select[0][node.playerCount] --;
						result1.add(node.arena);
						iter.remove();
					}else if (select[1][node.playerCount] >0){
						select[1][node.playerCount] --;
						result2.add(node.arena);
						iter.remove();
					}
				}
				fireMatched(result1, result2);
			}else{
				break;
			}
		}
	}

}
