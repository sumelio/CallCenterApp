package callcenter.com.co.app.configuration;

import java.util.ArrayList;
import java.util.List;

import callcenter.com.co.app.abstracts.Agent;

public class Config {
	private int nThreads = 0;
	private int nOperators = 0;
	private int nSupervisor = 0;
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
