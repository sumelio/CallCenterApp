package callcenter.com.co.app.entities.receivers;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.entities.senders.Caller;
import callcenter.com.co.app.interfaces.Sender;

import java.util.concurrent.ConcurrentLinkedQueue;

public class AgentQueue extends Agent {

	private ConcurrentLinkedQueue<Sender> queue;
	
	public AgentQueue(ConcurrentLinkedQueue<Sender> queue) {
		this.queue = queue;
	}

	@Override
	public Runnable answer(Caller caller) {
		queue.add(caller);
		return () -> {/* I am sorry, hold on a second, our agents are busy, */};
	}
}
