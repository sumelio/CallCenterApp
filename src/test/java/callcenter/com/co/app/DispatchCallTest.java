package callcenter.com.co.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

import org.junit.Before;
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

	Config config;
	
	@Before
	public void configuration() {
		config = new Config();
		config.setnThreads(10);
		config.setnOperators(8);
		config.setnSupervisor(4);
		config.setnDirectors(2);
	}
	/**
	 * Send 10 threads a same time to dispatchCall method
	 * 1. 10 threads should be 10 calls
	 * 2. The maximum call duration is Call.MAX_TIME_CALL_MILLI_SECONDS 
	 *    seconds and one should be greater than total current time dispatcher 
	 * 
	 */
	@Test
	public void testAppSend10ThreadsInSameTIme() {
		Dispatcher dispatcher = Dispatcher.getInstance(config);

		// Send 20 threads a same time
		IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
			Caller caller = new Caller("Caller_" + i);
			dispatcher.dispatchCall(caller);
		});
		Instant first = Instant.now();
		

		Dispatcher.executor.shutdown();
		while (!Dispatcher.executor.isTerminated()) {
		}
		Instant second = Instant.now();
		Duration duration = Duration.between(first, second);

		
		assertEquals("10 threads should be 10 calls ",10, Dispatcher.calls.size()); 
		
        optionalTestTime(duration);
	}


	/**
	 * Send 22 threads a same time to dispatchCall
	 * 
	 */
	@Test
	public void testAppSend22ThreadsInSameTIme() {
		Dispatcher dispatcher = Dispatcher.getInstance(config);

		// Send 20 threads a same time
		IntStream.rangeClosed(1, 22).parallel().forEach(i -> {
			Caller caller = new Caller("Caller_" + i);
			dispatcher.dispatchCall(caller);
		});

		Dispatcher.executor.shutdown();
		while (!Dispatcher.executor.isTerminated()) {
		}

		assertEquals("22 threads should be 22 calls ", 22, Dispatcher.calls.size());
	}
	
	
	/**
	 * Send 10 threads a same time to dispatchCall method 
	 * and match operators, supervisor and director counters after shutdown executor
	 * 
	 */
	@Test
	public void testAppSend10ThreadsInSameTimeAndGet_6_operatorsAnd_2_supervisorAnd_1_director() {
		
		config = new Config();
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

		Dispatcher.executor.shutdown();
		while (!Dispatcher.executor.isTerminated()) {
		}
		
		
      Integer counterOperator = Dispatcher.receivers.stream()
	            .filter(Operator.isOneOperator)
	            .map((a) -> ((Agent)a).getListSenderAttended().size() )
	            .reduce(0, (a , b) ->  a + b);
		
      Integer counterSupervisor = Dispatcher.receivers.stream()
              .filter(Supervisor.isOneSupervisor)
              .map((a) -> ((Agent)a).getListSenderAttended().size() )
              .reduce(0, (a , b) ->  a + b);

      Integer counterDirector = Dispatcher.receivers.stream()
              .filter(Director.isOneDirector)
              .map((a) -> ((Agent)a).getListSenderAttended().size() )
              .reduce(0, (a , b) ->  a + b);


		assertEquals("10 threads should be 6 opertator agents", 6, counterOperator.intValue());
		assertEquals("10 threads should be 3 supervisor agents", 3, counterSupervisor.intValue());
		assertEquals("10 threads should be 1 director agents", 1, counterDirector.intValue());
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
