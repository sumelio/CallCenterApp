package callcenter.com.co.app.entities.receivers;

import java.util.function.Predicate;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.interfaces.Receiver;

public class Supervisor extends Agent {
	public static Predicate<Receiver> isOneSupervisor = r -> r instanceof Supervisor;
	public Supervisor(String name) {
		super.name = name;
	}


}
