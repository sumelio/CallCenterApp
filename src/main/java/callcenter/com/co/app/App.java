package callcenter.com.co.app;

import java.util.stream.IntStream;

import callcenter.com.co.app.configuration.Config;
import callcenter.com.co.app.dispatcher.Dispatcher;
import callcenter.com.co.app.entities.senders.Caller;

/**
 * Hello world!
 *
 */
public class App { 

//	public static void main(String[] args) {
//
//		Config config = new Config();
//		config.setnThreads(10);
//		config.setnOperators(8);
//		config.setnSupervisor(4);
//		config.setnDirectors(2);
//		
//		Dispatcher dispatcher = Dispatcher.getInstance(config);
//
//		
//		// Send 20 threads a same time
//		IntStream.rangeClosed(1, 20)
//		         .parallel()
//		         .forEach(i -> {  
//		        	 Caller caller = new Caller("Caller_" + i);
//		        	 dispatcher.dispatchCall(caller); 
//		          });
//		
//		
//		dispatcher.executor.shutdown();
//		while (!dispatcher.executor.isTerminated()) {
//		}
//		
//		dispatcher.receivers.stream().forEach(c -> {
//			System.out.println(c);
//		});
//	} 
}
