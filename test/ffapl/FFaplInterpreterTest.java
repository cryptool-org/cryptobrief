package ffapl;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import ffapl.java.logging.FFaplLogMessage;
import ffapl.java.logging.FFaplLogger;
import ffapl.utils.Observable;
import ffapl.utils.Observer;
import sunset.gui.lib.ExecuteThread;

public class FFaplInterpreterTest {

	@Test
	public void InterpreterTest() throws InterruptedException {
		FFaplLogger logger = new FFaplLogger("test");
		logger.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				FFaplLogMessage logMessage;
				logMessage = ((FFaplLogMessage) arg);
				System.out.println(logMessage.message());

			}
		});
		FFaplInterpreter interpreter = new FFaplInterpreter(logger, new StringReader("program calculate{\n"
				+ "X: RandomGenerator(1:10);\n" + "a: Z(23);\n" + "a := 2^X;\n" + "println(a);\n" + "}\n" + ");"));
		
		ExecuteThread executeThread = new ExecuteThread(interpreter, null, null);
		executeThread.start();
		Thread.sleep(10000);

	}

}
