package callcenter.com.co.app.abstracts;

import java.util.Random;

import callcenter.com.co.app.interfaces.Receiver;
import callcenter.com.co.app.interfaces.Sender;

public abstract class Call  implements Runnable {
	
	/**
	 * Minimum time call 5 Seconds
	 */
    public final static int MIN_TIME_CALL_MILLI_SECONDS = 5000;
    
    /**
	 * Maximum time call 10 Seconds
	 */
    public final static int MAX_TIME_CALL_MILLI_SECONDS = 10000;
	protected abstract void doCall(Sender sender, Receiver receiver);
	
	public int getCallTime() {
		return new Random().ints(MIN_TIME_CALL_MILLI_SECONDS, 
				           MAX_TIME_CALL_MILLI_SECONDS).findAny().getAsInt();
	}
}
