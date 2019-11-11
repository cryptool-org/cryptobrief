package sunset.gui.api.spec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SnippetList implements Serializable {

	private static final long serialVersionUID = -8553241187044755196L;

	protected List<Snippet> snippet;

    public List<Snippet> getSnippet() {
        if (snippet == null) {
            snippet = new ArrayList<Snippet>();
        }
        return this.snippet;
    }
    
    public void addSnippet(Snippet newSnippet) {
    	if (snippet == null) {
            snippet = new ArrayList<Snippet>();
        }
    	snippet.add(newSnippet);
    }
}
