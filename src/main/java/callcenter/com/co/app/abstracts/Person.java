package callcenter.com.co.app.abstracts;

public abstract class Person {
	
	protected String name;	
	protected boolean busy;
		
	public String getName() {
		return this.name;
	}
		
	public boolean isBusy() {
		return busy;
	}
}
