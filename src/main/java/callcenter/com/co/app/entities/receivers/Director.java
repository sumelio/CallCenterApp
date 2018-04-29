package callcenter.com.co.app.entities.receivers;

import java.util.function.Predicate;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.interfaces.Receiver;

public class Director extends Agent {
	public static Predicate<Receiver> isOneDirector = r -> r instanceof Director;
	public Director(String name) {
		super.name = name;
	}

	
	
}
