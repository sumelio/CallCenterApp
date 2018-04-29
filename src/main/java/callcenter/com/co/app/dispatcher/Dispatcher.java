package callcenter.com.co.app.dispatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

	
	public static List<Call> calls ;
	
	public static ExecutorService executor;
	
	public static List<Agent> receivers;
	
	private static ConcurrentLinkedQueue<Sender> queueCallers;
		
	private static ReentrantLock lock;

	private static boolean running;
	
	
	public static Dispatcher getInstance(Config config) {
		calls = new ArrayList<Call>();
		receivers = new ArrayList<Agent>();
		queueCallers = new ConcurrentLinkedQueue<Sender>();
		lock = new ReentrantLock();
		running = false;
		return new Dispatcher(config);
	}
	
	private Dispatcher(Config config) {
		executor = Executors.newFixedThreadPool(config.getnThreads());

		IntStream.rangeClosed(1, config.getnDirectors()).forEach(i -> {
			receivers.add(new Director("_" + i));
		});
		
		IntStream.rangeClosed(1, config.getnOperators()).forEach(i -> {
			receivers.add(new Operator("_" + i));
		});

		IntStream.rangeClosed(1, config.getnSupervisor()).forEach(i -> {
			receivers.add(new Supervisor("_" + i));
		});
	}

	
	public void dispatchCall(Sender caller) {
 
			queueCallers.add(caller);
		
			if (!running) {
				running = true;
				loopCallCenter();
			}		
	}
	
		
	private static void loopCallCenter() {

		while (queueCallers.size() > 0) {
			lock.lock();
			try {
				Sender caller = queueCallers.poll();
				
				List<Agent> idleRevicers = 
						      Collections
						       .synchronizedList(receivers.stream()
						       .filter(Agent.isBusy.negate())
						       .collect(Collectors.toList()));

				Agent agentIdle = 
						idleRevicers.stream()
						.filter(Operator.isOneOperator)
						   .findAny()
						   .orElse(idleRevicers.stream()
								.filter(Supervisor.isOneSupervisor)
								.findAny()
					            .orElse(idleRevicers.stream()
					            	.filter(Director.isOneDirector)
					            	.findAny()
					               .orElse(new AgentQueue(queueCallers))))
					   .setBusy(true);

				executor.execute(agentIdle.answer((Caller) caller));

			} finally {
				lock.unlock();
			}
		}
	}
}
