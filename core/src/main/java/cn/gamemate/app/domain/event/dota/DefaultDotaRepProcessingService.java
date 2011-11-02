package cn.gamemate.app.domain.event.dota;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.safehaus.uuid.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.arena.Battle.BattleStatus;
import cn.gamemate.app.domain.event.EventCenter;
import cn.gamemate.app.domain.event.Event;
import cn.gamemate.app.domain.event.awards.AwardsPackage;
import cn.gamemate.app.domain.event.dota.cass.CassandraDotaBattleDao;
import cn.gamemate.taskqueue.Task;
import cn.gamemate.taskqueue.TaskQueue;
import cn.gamemate.taskqueue.TaskQueueFactory;

public class DefaultDotaRepProcessingService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final CassandraDotaBattleDao dao;
	private final List<DotaRepProcessor> validators;
	private ExecutorService executor;
	private EventCenter eventCenter;
	
	public DefaultDotaRepProcessingService(CassandraDotaBattleDao dao) {
		this.dao = dao;
		this.validators = new ArrayList<DotaRepProcessor>();
	}

	public EventCenter getEventCenter() {
		return eventCenter;
	}

	public void setEventCenter(EventCenter eventCenter) {
		this.eventCenter = eventCenter;
	}

	protected Iterable<DotaRepProcessor> getValidators() {
		return validators;
	}
	
	protected Iterable<AwardsPackage> getAwardsPackages(Battle battle){
		String eventId = battle.getArenaSnapshot().getEventId();
		Event event = eventCenter.getEvent(Integer.parseInt(eventId));
		return event.getAwardsPackages();

	}
	
	public void setValidators(List<DotaRepProcessor> validators){
		this.validators.clear();
		this.validators.addAll(validators);
	}
	
	void processReplay(String battleId, int submitter){
		processReplay(battleId, submitter, false);
	}
	/**
	 * 获取Battle和Dota Replay数据，验证，和后续处理
	 * 
	 * @param battleId Battle UUID
	 * @param submitter Replay提交者
	 * @param overwrite 是否强制使用此replay替换battle原有数据。
	 */
	void processReplay(String battleId, int submitter, boolean overwrite){
		
		DotaRepInfo repInfo = dao.getReplayInfo(battleId, submitter);
		
		//check valid
		try{
			
			for(DotaRepProcessor validator:validators){
				validator.postProcess(repInfo);
			}
		}catch(DotaRepProcessor.ProcessingException e){
			logger.debug(e.getMessage());
			return;
		}
		DotaBattle battle;
		try{
			battle = Battle.get(new UUID(battleId), DotaBattle.class);
		}catch(Exception e){
			logger.error(e.getMessage());
			return;
		}
		
		
		//TODO： a judgement should be involved.
		//first time run this?
		if (!overwrite && battle.getStatus() == BattleStatus.DONE){
			logger.debug("Replay Ignored, because this battle has DONE");
			return;
		}
		
		
		Integer winnerForce = repInfo.getWinnerForceId();
		
		//这里是不对的。因为winnerForce是从python代码直接提供的。
		if ( winnerForce != null){
			if (repInfo.isFinished()){
				battle.setText(Battle.FINISHED);
				logger.debug(Battle.FINISHED);
			}else if (repInfo.isQuit()){
				battle.setText(Battle.QUIT);
				logger.debug(Battle.QUIT);
			}else{
				logger.debug(Battle.UNKNOWN);
				return;
			}
			
			battle.setOutcome(repInfo.getOutcome());
			battle.setParsedReply(repInfo.getCdp_output());
			battle.setEffectiveLength(repInfo.getEffectivePlayTime()/1000);
			battle.setStatus(BattleStatus.DONE);
			battle.setAdoptedRep(Integer.toString(submitter));

			// awards
			for (AwardsPackage awardsPackage : getAwardsPackages(battle)) {
				awardsPackage.calculateAndUpdate(
						battle, 
						new DotaRepInfoCommonAwardsCalculatorContext(battle, repInfo));
			}
			battle.update();
		}
		
		
	}
	
	public synchronized void start(){
		executor = Executors.newSingleThreadExecutor();
		logger.debug("------------Service Started-------------------");
		executor.execute(new Worker());
		
	}
	
	public synchronized void stop(){
		if (executor!=null)
			executor.shutdown();
		
	}
	
	class Worker implements Runnable{

		@Override
		public void run() {
			TaskQueue queue = TaskQueueFactory.getQueue("dota_replay_parsed");
			while(true){
				Task task = queue.receive(30*1000);
				if (task == null) continue;
				String battleID = task.getParam("battle_id");
				String submitterID = task.getParam("submitter_id");
				String forceString = task.getParam("overwrite");
				boolean overwrite = Boolean.valueOf(forceString);
				
				logger.debug("Dota Replay Received: for {} submitted by {}", battleID, submitterID);
				
				processReplay(battleID, Integer.parseInt(submitterID), overwrite);
				
				queue.ack(task);
			}
			
		}
		
	}
	
}
