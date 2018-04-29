package callcenter.com.co.app.dispatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.abstracts.Call;
import callcenter.com.co.app.configuration.Config;
import callcenter.com.co.app.entities.receivers.AgentQueue;
import callcenter.com.co.app.entities.receivers.Director;
import callcenter.com.co.app.entities.receivers.Operator;
import callcenter.com.co.app.entities.receivers.Supervisor;
import callcenter.com.co.app.entities.senders.Caller;
import callcenter.com.co.app.interfaces.Sender;

public class Dispatcher {

	
	private  ExecutorService executor;
	
	public ExecutorService getExecutor() {
		return this.executor;
	}
	
	private List<Agent> receivers;
	
	public List<Agent> getReceivers() {
		return this.receivers;
	}
	
	private  ConcurrentLinkedQueue<Sender> queueCallers;
		
	private  ReentrantLock lock;

	private  boolean running;
	
	
	public static Dispatcher getInstance(Config config) {
		Dispatcher dispatcher = new Dispatcher(config);

		dispatcher.queueCallers = new ConcurrentLinkedQueue<Sender>();
		dispatcher.lock = new ReentrantLock();
		dispatcher.running = false;
		return dispatcher;
	}
	
	private Dispatcher(Config config) {
		executor = Executors.newFixedThreadPool(config.getnThreads());

		List<Agent> receiverList = new ArrayList<Agent>();
		
		IntStream.rangeClosed(1, config.getnOperators()).forEach(i -> {
			receiverList.add(new Operator("_" + i));
        });
		
        IntStream.rangeClosed(1, config.getnSupervisor()).forEach(i -> {
        	receiverList.add(new Supervisor("_" + i));
        });

        IntStream.rangeClosed(1, config.getnDirectors()).forEach(i -> {
        	receiverList.add(new Director("_" + i));
		});
                
        receivers = Collections.synchronizedList(receiverList);
	}

	
	public void dispatchCall(Sender caller) {
		queueCallers.offer(caller);

		if (!running) {
			running = true;
			loopCallCenter();
		}
	}
	
		
	private   void loopCallCenter() {
		lock.lock();
		try {
		 while (queueCallers.size() > 0) {

				Sender caller = queueCallers.poll();
				
				List<Agent> idleRevicers = 
						      Collections
						       .synchronizedList(receivers.stream()
						       .filter(Agent.isBusy.negate())
						       .collect(Collectors.toList()));

				Agent agentIdle = 
						idleRevicers.stream()
						.filter(Operator.isOneOperator)
						   .findFirst()
						   .orElse(idleRevicers.stream()
								.filter(Supervisor.isOneSupervisor)
								.findFirst()
					            .orElse(idleRevicers.stream()
					            	.filter(Director.isOneDirector)
					            	.findFirst()
					               .orElse(new AgentQueue(queueCallers))))
					   .setBusy(true);

				executor.execute(agentIdle.answer((Caller) caller));
		   }

		} finally {
			lock.unlock();
		}
	}
}
