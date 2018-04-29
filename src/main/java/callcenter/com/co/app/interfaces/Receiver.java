package callcenter.com.co.app.interfaces;

import callcenter.com.co.app.entities.senders.Caller;

public interface Receiver {
	
	public Runnable answer(Caller caller);
	
	default boolean isBusy() {
		return false;
	}
}
