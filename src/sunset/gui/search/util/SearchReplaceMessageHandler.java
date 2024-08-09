package sunset.gui.search.util;

import java.text.MessageFormat;

import sunset.gui.util.SunsetBundle;

public class SearchReplaceMessageHandler {
	private static SearchReplaceMessageHandler _instance = new SearchReplaceMessageHandler();
	
	private SearchReplaceMessageHandler() {
	}
	
	public static SearchReplaceMessageHandler getInstance() {
		return _instance;
	}
	
	public String getMessage(String msg_key) {
		return SunsetBundle.getInstance().getProperty(msg_key);
	}

	public String getMessage(String msg_key, String par) {
		String msg = SunsetBundle.getInstance().getProperty(msg_key);
		
		if (par != null) {
			msg = MessageFormat.format(msg, par);
		}
		
		return msg;
	}
}
