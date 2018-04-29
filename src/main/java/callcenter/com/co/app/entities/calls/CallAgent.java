package callcenter.com.co.app.entities.calls;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.abstracts.Call;
import callcenter.com.co.app.entities.senders.Caller;
import callcenter.com.co.app.interfaces.Receiver;
import callcenter.com.co.app.interfaces.Sender;

public class CallAgent extends Call {

	public Caller caller;
	public Agent agent;
	
	public int timeCall = getCallTime();

	public CallAgent(Agent agent, Caller caller) {
		this.agent = agent;
		this.caller = caller;
	}

	@Override
	protected void doCall(Sender sender, Receiver receiver) {
		
		System.out.println("Start call " + agent.getClass().getSimpleName() + agent.getName() + " -> "
				+ this.caller.getName());
		try {
			Thread.sleep(timeCall);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("   ----->  End call " + agent.getClass().getSimpleName() + agent.getName() + " -> "
				+ this.caller.getName() + " timeCall=" + timeCall);

	}

	@Override
	public void run() {
		
		
		doCall(this.caller, this.agent);
		
		caller.setAttended(true);
		agent.addSender(caller);
		agent.setBusy(false);
		
	}

	@Override
	public String toString() {
		return "Call [ agent=" + agent + ", timeCall=" + timeCall + "]";
	}
	
	

}
