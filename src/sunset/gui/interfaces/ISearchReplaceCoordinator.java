package sunset.gui.interfaces;

public interface ISearchReplaceCoordinator {
	public boolean findString();
	public boolean isSearchPatternSelected();
	public void replaceText();
	public void resetCaretPosition();
}
