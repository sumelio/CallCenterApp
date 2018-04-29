package callcenter.com.co.app.dispatcher;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.configuration.Config;
import callcenter.com.co.app.entities.receivers.AgentQueue;
import callcenter.com.co.app.entities.receivers.Director;
import callcenter.com.co.app.entities.receivers.Operator;
import callcenter.com.co.app.entities.receivers.Supervisor;
import callcenter.com.co.app.entities.senders.Caller;
import callcenter.com.co.app.interfaces.Sender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *  This dispatcher managers all concurrent request of the dispatchCall
 *
 *
 */
public class Dispatcher {

	private  ExecutorService executor;

	private List<Agent> receivers;

	private  ConcurrentLinkedQueue<Sender> queueCallers;
		
	private  ReentrantLock lock;

	private  boolean running;

	/**
	 *  In this constructor all variables are initialized
	 *
	 * @param config
	 */
	public Dispatcher(Config config) {
		executor = Executors.newFixedThreadPool(config.getnThreads());

		this.queueCallers = new ConcurrentLinkedQueue<Sender>();
		this.lock = new ReentrantLock();
		this.running = false;

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

	/**
	 * Get all request an on queue them.
	 *
	 * This method places a caller on the concurrent queue and if the process is not running then
	 * invokes the loopCallCenter method in order to start the process
	 * @param caller This is a Caller object
	 */
	public void dispatchCall(Sender caller) {

		queueCallers.offer(caller);

		if (!running ) {
			loopCallCenter();
		}
	}

	/**
	 *  This method has a loop that gets a caller from queue and assigns an agent in order to
	 *  attend this caller. Steps:
	 *  1. Lock code block
	 *  2. The process gets a caller from queue.
	 *  3. Get a idle receivers list, in order to get all receivers are not busy in
	 *     that instance.
	 *  4. Try to get an operator agent, if there is not an operator then try to get
	 *     a supervisor agent, if there is not a supervisor then try to get a director agent,
	 *     else put on queue through of the AgentQueue.
	 *  5. Unlock the code block
 	 */
	private  void loopCallCenter() {
		lock.lock();
		try {
        //  callCenter loop
		 while (! queueCallers.isEmpty()) {

				Sender caller = queueCallers.poll();

				//Get all receivers are not busy
				List<Agent> idleReceivers =
						      Collections
						       .synchronizedList(receivers.stream()
						       .filter(Agent.isBusy.negate())
						       .collect(Collectors.toList()));

				Agent assignedAgent =
						idleReceivers.stream()
						.filter(Operator.isOneOperator)
						   .findFirst()
						   .orElse(idleReceivers.stream()
								.filter(Supervisor.isOneSupervisor)
								.findFirst()
					            .orElse(idleReceivers.stream()
					            	.filter(Director.isOneDirector)
					            	.findFirst()
										// the AgentQueue on line a caller
					               .orElse(new AgentQueue(queueCallers))))

					   .setBusy(true); // From this moment the agent is busy

			    // Create a thread (Call) and send the executorPool
				executor.execute(assignedAgent.answer((Caller) caller));


		   }

		} finally {
			// The process has finished
			this.running = ! queueCallers.isEmpty();
			lock.unlock();
		}
	}

	public List<Agent> getReceivers() {
		return this.receivers;
	}

	public ExecutorService getExecutor() {
		return this.executor;
	}
}
