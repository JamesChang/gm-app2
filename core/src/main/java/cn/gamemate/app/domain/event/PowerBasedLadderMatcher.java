package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.base.Objects;

import cn.gamemate.app.domain.event.GreedyMatcher.ArenaWrapper;
import cn.gamemate.app.domain.user.UserRepository;

/**
 * @author jameszhang
 * 
 */
public class PowerBasedLadderMatcher extends AbstractLadderMatcher{
	
	final int T0_TIME=4*1000;
	final int QUEUE_MAX_LENGTH=1000;
	final int M_MAX = 5;
	

	private final int m;
	private final UserRepository userRepository; 
	
	private final Queue<Party> toBeAdd;
	private final Collection<PartyWrapper> t0;
	private final List<PartyWrapper> parties;
	private final SpaceNode space;
	private final List<MatcherHandler> handlers ;
	private final int [][][][][]setA;
	private final List<BNode> setB;
	
	
	class BNode{
		int[] selection1;
		int[] selection2;
		
		public BNode(int m) {
			selection1 = new int[m+1];
			selection2 = new int[m+1];
		}
		@Override
		public String toString() {
			return new StringBuilder().append("(")
			.append(selection1).append(",").append(selection2)
			.append(')').toString();
		}
	}
	class PartyWrapper{
		Party party;
		long tStart;
		int playerCount;
		int powerHeart;
		int powerSpan;
		
		public PartyWrapper(Party party) {
			this.party = party;
			tStart = System.currentTimeMillis();
			playerCount = party.getPlayerCount();
			
			//TODO: calculate mean of power
		}
		@Override
		public String toString() {
			return Objects.toStringHelper(this)
					.add("party", party)
					.add("tStart", tStart)
					.add("players", playerCount)
					.add("pp", powerHeart)
					.add("ps", powerSpan)
					.toString();
		}
	}
	enum SpaceNodePosition{FRONT, POINT, BACK};
	static class SpaceNode{
		PartyWrapper party;
		SpaceNodePosition pos;
		int power;
		SpaceNode next;
		SpaceNode prev;
		
		public SpaceNode() {
			party = null;
			pos = SpaceNodePosition.POINT;
			power = 0;
			next = null;
			prev = null;
		}
		
		void add(SpaceNode node){
			// only used as head
			SpaceNode pp=this, p=next;
			while (p !=null && node.power > p.power){
				pp = p;
				p = p.next;
			}
			node.next = pp.next;
			if (pp.next !=null) pp.next.prev = node;
			node.prev = pp;
			pp.next = node;
		}
		
		void update(){
			SpaceNode pp=null;
			
			if (power < prev.power){
				prev.next = next;
				next.prev = prev;
				while(power < prev.power){
					prev = prev.prev;
				}
				prev.next.prev = this;
				next = prev.next;
				prev.next = this;
			}
			if (next !=null && power > next.power){
				next.prev = prev;
				prev.next = next;
				while(next != null && power>next.power){
					pp = next;
					next = next.next;
				}
				pp.next = this;
				prev = pp;
				if (next != null) {
					next.prev = this;
				}
			}
		}
		void remove(){
			next.prev = prev;
			prev.next = next;
			prev = null;
			next = null;
		}
		
	}
	
	public PowerBasedLadderMatcher(int m, UserRepository userRepository) {
		this.m = m;
		this.userRepository = userRepository;
		t0= new LinkedBlockingQueue<PartyWrapper>();
		parties = new ArrayList<PartyWrapper>();
		space  = new SpaceNode();
		toBeAdd = new LinkedBlockingQueue<Party>();
		handlers = new ArrayList<MatcherHandler>();
		setA = new int[11][6][4][3][3];
		setB = new ArrayList<BNode>();
		initSetA(0, new int[5]);
	}
	
	private void initSetA(int l, int[] selection){
		int mCurrent = l+1;
		int maxCount = (this.m<<1)/mCurrent;
		if (l==m){
			BNode result = getMatch(selection);
			if (result != null){
				setB.add(result);
			}
		}else{
			for (int i=0;i<=maxCount;i++){
				selection[l]=i;
				initSetA(l+1, selection);
			}
		}
	}
	
	private BNode getMatch(int [] selection){
		int [][]reach = new int[m+1][m+1];
		int [][]version = new int[m+1][m+1];
		reach[0][0]=1;
		int [][] select = new int[2][m+1];
		int v = 0;
		for (int o=m;o>0;o--){
			for (int p=0;p<selection[o-1];p++){
				v++;
				for (int i=0;i<=m;++i){
					for (int j=0;j<=m;++j){
						if (reach[i][j]!=0 && version[i][j] != v){
							if (i + o <= m && reach[i+o][j] ==0){
								reach[i+o][j] = o;
								version[i+o][j]=v;
							}
							if (j+o <=m && version[i][j+o] == 0){
								reach[i][j+o] = -o;
								version[i][j+o] = v;
							}
						}
					}
				}
			}
		}
		if (reach[m][m]!=0){

			BNode result = new BNode(m);
			int i=m, j=m;
			while(i>0 || j>0){
				int select_m = reach[i][j];
				if (select_m>0){
					result.selection1[select_m]++;
					i=i-select_m;
				}else{
					result.selection2[-select_m]++;
					j=j+select_m;
				}
			}
			return result;
		}
		return null;
	}
	
	@Override
	public void add(Party party) {
		toBeAdd.add(party);
	}

	@Override
	synchronized public void remove(Party party) {
		// TODO Auto-generated method stub
		
	}

	@Override
	synchronized public void tick() {
		//add nodes from toBeAdd to t0
		//System.out.println(toBeAdd);
		Party party;
		while(true){
			party = toBeAdd.poll();
			if (party == null) break;
			PartyWrapper wrapper = new PartyWrapper(party);
			t0.add(wrapper);
		}
		//scan t0, add nodes to space
		long tNow = System.currentTimeMillis()- T0_TIME;
		for(Iterator<PartyWrapper> iter = t0.iterator();iter.hasNext();){
			PartyWrapper node = iter.next();
			if (node.tStart<tNow){
				parties.add(node);
				iter.remove();
			}
		}
	}
	
	

	

	
}

