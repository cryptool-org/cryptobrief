package sunset.gui.logic;

public class SearchReplaceLogic {
	
	private static SearchReplaceLogic instance = new SearchReplaceLogic();
	
	private SearchReplaceLogic() {
	}
	
	public static SearchReplaceLogic getIntance() {
		return instance;
	}

	public int getIndexOf(String text, String pattern, int pos) {
		return text.indexOf(pattern, pos);
	}
}
