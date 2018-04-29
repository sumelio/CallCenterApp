package callcenter.com.co.app.entities.calls;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.abstracts.Call;
import callcenter.com.co.app.entities.senders.Caller;
import callcenter.com.co.app.interfaces.Receiver;
import callcenter.com.co.app.interfaces.Sender;

public class IncomingCall extends Call {
	final static Logger logger = LoggerFactory.getLogger(IncomingCall.class);
	

	public Caller caller;
	public Agent agent;
	
	public int timeCall = getCallTime();

	public IncomingCall(Agent agent, Caller caller) {
		this.agent = agent;
		this.caller = caller;
	}

	@Override
	protected void doCall(Sender sender, Receiver receiver) {
		logger.info("Start {}{} -> {}",  agent.getClass().getSimpleName() , this.agent.getName(), caller.getName());
		try {
			Thread.sleep(timeCall);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		logger.info("--> End {}{} -> {}, totalTime={}", agent.getClass().getSimpleName() , this.agent.getName(), caller.getName(), timeCall );


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
