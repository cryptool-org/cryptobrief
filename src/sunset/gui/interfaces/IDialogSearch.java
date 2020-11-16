package sunset.gui.interfaces;

import java.awt.Color;

public interface IDialogSearch {
	public String searchPattern();
	public boolean matchCase();
	public void setStatus(String status, Color color);
	public boolean useRegEx();
	public boolean dotMatchesNewLine();
	public boolean wrapAround();
}
