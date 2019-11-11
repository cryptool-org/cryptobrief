package sunset.gui.api.spec;

import java.io.Serializable;

public class SnippetCode implements Serializable {

	private static final long serialVersionUID = -1572817980504927959L;
	
	protected SnippetList snippetList;

	public SnippetList getSnippetList() {
        return snippetList;
    }

    public void setSnippetList(SnippetList value) {
        this.snippetList = value;
    }
}
