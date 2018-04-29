package callcenter.com.co.app.entities.receivers;

import java.util.function.Predicate;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.interfaces.Receiver;

public class Operator extends Agent {
	public static Predicate<Receiver> isOneOperator = r -> r instanceof Operator;
	public Operator(String name) {
		super.name = name;
	}

 
}
