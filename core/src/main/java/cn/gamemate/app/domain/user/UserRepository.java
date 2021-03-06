package cn.gamemate.app.domain.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import cn.gamemate.app.domain.ObjectNotFound;
import cn.gamemate.app.domain.user.User.UserStatus;
/**
 * @author jameszhang
 *
 *	Thread Safe
 */
public class UserRepository implements UserRepositoryMBean{
	
	public static class UserRepositoryStats implements Serializable{

		private static final long serialVersionUID = 1L;

		public UserRepositoryStats(UserRepository userRepository) {
			this.userRepository = userRepository;
		}
		transient private UserRepository userRepository;
		
		private int allUserCount;
		private int inArenaUserCount;
		private int idleUserCount;
		private int gamingUserCount;
		private int waitingUserCount;
		
		public Integer getAllUserCount() {
			return allUserCount;
		}
		public Integer getInArenaUserCount() {
			return inArenaUserCount;
		}
		public Integer getIdleUserCount() {
			return idleUserCount;
		}
		public Integer getGamingUserCount() {
			return gamingUserCount;
		}
		public Integer getWaitingUserCount(){
			return waitingUserCount;
		}
		public void clear(){
			
		}
		
		
		@Override
		public String toString(){
			return new StringBuilder()
			.append("users{")
			.append(" total:").append(allUserCount).append(",")
			.append(" idle:").append(idleUserCount).append(",")
			.append(" waiting:").append(waitingUserCount).append(",")
			.append(" gaming:").append(gamingUserCount).append(",")
			.append(" inArena:").append(inArenaUserCount)
			.append(" }")
			.toString();
		}
		
		
		private void doCount(){
			int total=0, idle=0, waiting=0, gaming=0, inArena=0;
			total = userRepository.users.size();
			for (Entry<Integer, User> entry: userRepository.users.entrySet()){
				User user = entry.getValue();
				if (user.getStatus() == UserStatus.ONLINE){
					if (user.getArenaId()==null){
						idle ++;
					}
					else{
						waiting++;
					}
				}
				else if (user.getStatus() == UserStatus.GAMING){
					gaming ++;
				}
				if (user.getArenaId()!=null){
					inArena ++;
				}
			}
			this.allUserCount = total;
			this.idleUserCount = idle;
			this.waitingUserCount = waiting;
			this.gamingUserCount = gaming;
			this.inArenaUserCount = inArena;
			
		}
		
	}
	
	
	protected final ConcurrentHashMap<Integer, User> users=new ConcurrentHashMap<Integer, User>();
	private final ArrayList<UserExtension> handlers = new ArrayList<UserExtension>();
	private final UserRepositoryStats stats;
	public UserRepository() {
		stats= new UserRepositoryStats(this);
	}
	
	/**
	 * add User Event Handler, aka. Extension.
	 * note: this method is NOT thread safe thus it should only be invoked at application initialization time.
	 * @param x
	 */
	public void addExtension(UserExtension x){
		handlers.add(x);
	}
	
	public User getUser(Integer id){
		return getUser(id, true);
	}
	
	public User getUser(Integer id, boolean mustExist){
		User user = users.get(id);
		if (user == null && mustExist)
			throw new ObjectNotFound(User.class, id);
		return user;
	}
	
	
	private void fireUserLoggedIn(User user){
		for(UserExtension handler:handlers){
			handler.userLoggedIn(user);
		}
	}
	private void fireUserLoggedOut(User user){
		for(UserExtension handler:handlers){
			handler.userLoggedOut(user);
		}
	}
	private void fireUserLoggedDrop(User user){
		for(UserExtension handler:handlers){
			handler.userDrop(user);
		}
	}
	public void fireUserBrowseOnly(User user){
		for(UserExtension handler:handlers){
			handler.userBrowseOnly(user);
		}
	}
	
	public void login(User user){
		if (user == null){
			throw new NullPointerException("the user trying to login is null");
		}
		if (users.get(user.getId())!=null){
			return;
			//logout(user);
		}
		user.setStatus(UserStatus.ONLINE);
		if (!users.containsKey(user.getId())){
			users.put(user.getId(), user);
		}
		fireUserLoggedIn(user);
	}
	

	public void login(Integer userId) {
		User user = User.findUser(userId);
		if (user == null){
			throw new ObjectNotFound(User.class, userId);
		}
		if (users.get(userId)!=null){
			return;
			//logout(user);
		}
		user.setStatus(UserStatus.ONLINE);
		if (!users.containsKey(userId)){
			users.put(user.getId(), user);
		}
		fireUserLoggedIn(user);
	}
	
	public void logout(User user){
		users.remove(user.getId());
		fireUserLoggedOut(user);
	}
	public void logout(Integer userId){
		User u = users.get(userId);
		if(u !=null){
			logout(u);
		}
	}
	
	public void drop(User user){
		users.remove(user.getId());
		fireUserLoggedDrop(user);
	}
	
	public void drop(Integer userId){
		User u = users.get(userId);
		if(u !=null){
			drop(u);
		}
	}
	@Override
	public String showStats(){
		stats.doCount();
		return stats.toString();
	}
	@Override 
	public String showAllUserList(){
		StringBuilder result = new StringBuilder();
		result.append("{");
		for (Entry<Integer, User> entry: users.entrySet()){
			User user = entry.getValue();
			genUserStatusString(result, user);
			result.append(",");
		}
		result.append("}");
		return result.toString();
	}

	private void genUserStatusString(StringBuilder result,
			User user) {
		
		result.append("User{")
			.append("ID:").append(user.getId()).append(", ")
			.append("Name:").append(user.getName()).append(", ")
			.append("Relay:").append(user.getRelayServiceName()).append(", ");
		
		UserStatus userStatus = user.getStatus();
		Integer arenaId = user.getArenaId();
		if (arenaId != null){
			result.append("Status:").append("InArena").append(", ");
		}else{
			result.append("Status:").append(userStatus).append(", ");
		}
		result.append("}");
	}
	
	public String showUserStatus(String key){
		User user = users.get(Integer.valueOf(key));
		if (user == null) return null;
		StringBuilder result = new StringBuilder();
		genUserStatusString(result, user);
		return result.toString();
	}
	
	public UserRepositoryStats getStat(){
		stats.doCount();
		return stats;
	}
		
	
}
