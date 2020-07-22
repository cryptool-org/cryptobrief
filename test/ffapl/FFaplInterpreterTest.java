package ffapl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import ffapl.java.logging.FFaplLogMessage;
import ffapl.java.logging.FFaplLogger;
import ffapl.utils.Observable;
import ffapl.utils.Observer;

public class FFaplInterpreterTest {

	//@DynamicTest
	public void InterpreterTest() throws InterruptedException, ExecutionException {
		FFaplLogger logger = new FFaplLogger("test");
		logger.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				FFaplLogMessage logMessage;
				logMessage = ((FFaplLogMessage) arg);
				System.out.println(logMessage.message());

			}
		});

		FFaplInterpreter interpreter = new FFaplInterpreter(logger, getClass().getResourceAsStream("test.ffapl"));

		Executors.newCachedThreadPool().submit(interpreter).get();

	}

}
