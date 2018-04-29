package callcenter.com.co.app.entities.calls;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.abstracts.Call;
import callcenter.com.co.app.entities.senders.Caller;
import callcenter.com.co.app.interfaces.Receiver;
import callcenter.com.co.app.interfaces.Sender;

public class CallAgent extends Call {
	final static Logger logger = LoggerFactory.getLogger(CallAgent.class);
	

	public Caller caller;
	public Agent agent;
	
	public int timeCall = getCallTime();

	public CallAgent(Agent agent, Caller caller) {
		this.agent = agent;
		this.caller = caller;
	}

	@Override
	protected void doCall(Sender sender, Receiver receiver) {
		
		String threadName = "["+ Thread.currentThread().getName() +"]";
		System.out.println(threadName+ "   ->  Start call " + agent.getClass().getSimpleName() + agent.getName() + " -> "
				+ this.caller.getName());
		logger.info("{}   ->  Start call {}{} -> ", threadName, this.agent.getName(),  threadName, agent.getClass().getSimpleName() , caller.getName());
		  
		
		try {
			Thread.sleep(timeCall);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("{}   ----->  End  call {}{} -> ", threadName, this.agent.getName(),  threadName, agent.getClass().getSimpleName() , caller.getName());


	}

	@Override
	public void run() {
		
		
		doCall(this.caller, this.agent);
	
		caller.setAttended(true);
		agent.setBusy(false);
		
	}

	@Override
	public String toString() {
		return "Call [ agent=" + agent + ", timeCall=" + timeCall + "]";
	}
	
	

}
