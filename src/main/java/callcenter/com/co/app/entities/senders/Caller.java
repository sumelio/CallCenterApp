package callcenter.com.co.app.entities.senders;

import callcenter.com.co.app.abstracts.Person;
import callcenter.com.co.app.interfaces.Sender;

public class Caller extends Person implements Sender {
	
	public boolean attended = false;

	
	public Caller(String name) {
		super.name = name;
	}

	@Override
	public boolean isAttended() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setAttended(boolean attended) {
		// TODO Auto-generated method stub
		this.attended = attended;
	}

	@Override
	public String toString() {
		return "Caller [ name "+name+", attended=" + attended +"]";
	}
	
	

}
