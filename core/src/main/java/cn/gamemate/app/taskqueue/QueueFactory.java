package cn.gamemate.app.taskqueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

//TODO: is there any way reduce the complexity?
//TODO: service available check.

@Configurable
public class QueueFactory {
	protected QueueFactory() {
	}

	@Autowired
	private static QueueFactory taskQueueFactory;

	@SuppressWarnings({ "unused", "static-access" })
	private static QueueFactory innerQueueFactory() {
		if (taskQueueFactory != null)
			return taskQueueFactory;
		taskQueueFactory = new QueueFactory().taskQueueFactory;
		if (taskQueueFactory == null) {
			throw new RuntimeException(
					"QueueFaction Implementation has not been injected.");
		}
		return taskQueueFactory;
	}

	/**
	 * @return the default taskqueue.
	 */
	@SuppressWarnings({ "static-access" })
	static Queue getDefaultQueue() {
		return taskQueueFactory.getDefaultQueue();
	}

	/**
	 * Returns the Queue by name.
	 * 
	 * Queues must be configured before they may be used. Attempting to use a
	 * non-existing queue name may result in errors at the point of use of the
	 * Queue object and not when calling getQueue(String).
	 * 
	 * @return the TaskQueue by name
	 */
	@SuppressWarnings({ "static-access" })
	static Queue getQueue(String queueName) {
		return taskQueueFactory.getQueue(queueName);
	}

}
