package callcenter.com.co.app.configuration;

public class Config {
	/**
	 * Number of threads by pool
	 */
	private int nThreads = 0;
	
	/**
	 * number of operators
	 */
	private int nOperators = 0;
	
	/**
	 * Number of supervisors
	 */
	private int nSupervisor = 0;
	
	/**
	 * Number of directors
	 */
	private int nDirectors = 0;
			

	public int getnThreads() {
		return nThreads;
	}


	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}


	public int getnOperators() {
		return nOperators;
	}


	public void setnOperators(int nOperators) {
		this.nOperators = nOperators;
	}


	public int getnSupervisor() {
		return nSupervisor;
	}


	public void setnSupervisor(int nSupervisor) {
		this.nSupervisor = nSupervisor;
	}


	public int getnDirectors() {
		return nDirectors;
	}


	public void setnDirectors(int nDirectors) {
		this.nDirectors = nDirectors;
	}


	
}
