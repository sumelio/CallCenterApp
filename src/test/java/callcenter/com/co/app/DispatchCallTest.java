package callcenter.com.co.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

import org.junit.Test;

import callcenter.com.co.app.abstracts.Agent;
import callcenter.com.co.app.abstracts.Call;
import callcenter.com.co.app.configuration.Config;
import callcenter.com.co.app.dispatcher.Dispatcher;
import callcenter.com.co.app.entities.receivers.Director;
import callcenter.com.co.app.entities.receivers.Operator;
import callcenter.com.co.app.entities.receivers.Supervisor;
import callcenter.com.co.app.entities.senders.Caller;


/**
 * Unit test for simple CallCenterApp.
 */
public class DispatchCallTest {

 
	
	
	/**
	 * Send 10 threads a same time to dispatchCall method 
	 * and match operators, supervisor and director counters after shutdown executor
	 * 
	 */
	@Test
	public void test01() {
		Config config = new Config();
		config.setnThreads(10);
        config.setnOperators(6);
        config.setnSupervisor(3);
        config.setnDirectors(1);
		Dispatcher dispatcher = Dispatcher.getInstance(config);

		// Send 20 threads a same time
		IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
			Caller caller = new Caller("Caller_" + i);
			dispatcher.dispatchCall(caller);
		});

		dispatcher.getExecutor().shutdown();
		while (!dispatcher.getExecutor().isTerminated()) {
		}

		Integer counterOperator = dispatcher.getReceivers().stream().filter(Operator.isOneOperator)
				.map((a) -> ((Agent) a).getListSenderAttended().size()).reduce(0, (a, b) -> a + b);

		Integer counterSupervisor = dispatcher.getReceivers().stream().filter(Supervisor.isOneSupervisor)
				.map((a) -> ((Agent) a).getListSenderAttended().size()).reduce(0, (a, b) -> a + b);

		Integer counterDirector = dispatcher.getReceivers().stream().filter(Director.isOneDirector)
				.map((a) -> ((Agent) a).getListSenderAttended().size()).reduce(0, (a, b) -> a + b);

		assertEquals("10 threads should be 6 operator agents", 6, counterOperator.intValue());
		assertEquals("10 threads should be 3 supervisor agents", 3, counterSupervisor.intValue());
		assertEquals("10 threads should be 1 director agents", 1, counterDirector.intValue());

	}
	
	
	
	/**
	 * Send 10 threads a same time to dispatchCall method
	 * 1. 10 threads should be 10 calls
	 * 2. The maximum call duration is Call.MAX_TIME_CALL_MILLI_SECONDS 
	 *    seconds and one should be greater than total current time dispatcher 
	 * 
	 */
	@Test
	public void test02() {
		Config config = new Config();
		config.setnThreads(10);
		config.setnOperators(8);
		config.setnSupervisor(4);
		config.setnDirectors(2);
		Dispatcher dispatcher = Dispatcher.getInstance(config);

		// Send 20 threads a same time
		IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
			Caller caller = new Caller("Caller_" + i);
			dispatcher.dispatchCall(caller);
		});
		Instant first = Instant.now();
		

		dispatcher.getExecutor().shutdown();
		while (!dispatcher.getExecutor().isTerminated()) {
		}
		Instant second = Instant.now();
		Duration duration = Duration.between(first, second);

		Integer counter = dispatcher.getReceivers().stream()
				.map((a) -> ((Agent) a).getListSenderAttended().size()).reduce(0, (a, b) -> a + b);
		
		
		assertEquals("10 threads should be 10 calls ",10, counter.intValue()); 
		
        optionalTestTime(duration);
	}


	/**
	 * Send 22 threads a same time to dispatchCall
	 * 
	 */
	@Test
	public void test03() {
		Config config = new Config();
		config.setnThreads(10);
		config.setnOperators(8);
		config.setnSupervisor(4);
		config.setnDirectors(2);

		Dispatcher dispatcher = Dispatcher.getInstance(config);

		// Send 20 threads a same time
		IntStream.rangeClosed(1, 22).parallel().forEach(i -> {
			Caller caller = new Caller("Caller_" + i);
			dispatcher.dispatchCall(caller);
		});

		dispatcher.getExecutor().shutdown();
		while (!dispatcher.getExecutor().isTerminated()) {
		}
		
		Integer counter = dispatcher.getReceivers().stream()
				.map((a) -> ((Agent) a).getListSenderAttended().size()).reduce(0, (a, b) -> a + b);
		
		

		assertEquals("22 threads should be 22 calls ", 22, counter.intValue());
	}
	
	
	
	
	
	/**
	 *  Test total time process 
	 *  
	 * @param duration
	 */
	private void optionalTestTime(Duration duration) {
		
		int cores = Runtime.getRuntime().availableProcessors();
		
		if (cores > 1) {

			long maxTimeSecond = Call.MAX_TIME_CALL_MILLI_SECONDS / 1000;
			boolean test = maxTimeSecond > duration.getSeconds();

			System.out.println("Total time=" + duration.getSeconds() + " maxTimeSecond=" + maxTimeSecond);

			assertTrue("The maximum call duration is (" + maxTimeSecond + ") seconds and one should "
					+ " be greater than total current time distpacher (" + duration.getSeconds() + ")", test);

		}
	}
	

}
