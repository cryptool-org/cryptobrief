package sunset.gui.search.advanced.exception;

@SuppressWarnings("serial")
public class MatchingPairConfigurationException extends Exception {
	public MatchingPairConfigurationException(String msg) {
		super("Bad matching pair configuration: " + msg);
	}
}
