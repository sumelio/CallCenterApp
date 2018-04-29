package callcenter.com.co.app.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import callcenter.com.co.app.dispatcher.Dispatcher;
import callcenter.com.co.app.entities.calls.CallAgent;
import callcenter.com.co.app.entities.senders.Caller;
import callcenter.com.co.app.interfaces.Receiver;
import callcenter.com.co.app.interfaces.Sender;

public abstract class Agent extends Person implements Receiver {
	
	public static Predicate<Receiver> isBusy = Receiver::isBusy;
	
	protected List<Sender> senderAttended = new ArrayList<Sender>();	
	
	public Agent setBusy(boolean busy) {
		this.busy = busy;
		return this;
	}

	public void addSender(Sender sender) {
		senderAttended.add(sender);
	}
	
	public List<Sender> getListSenderAttended() {
		 return senderAttended;
	}
	
	@Override
	public boolean isBusy() {
		return busy;
	}
	
	@Override
	public Runnable answer(Caller caller) {
		Call call = new CallAgent(this, caller);
		this.addSender(caller);
		return call;
	}

	@Override
	public String toString() {
		return "Agent [name=" + this.getClass().getSimpleName() + " " + name  + ", busy ="+ busy + ", senderAttended=" + senderAttended.size() + "]";
	}	
}
