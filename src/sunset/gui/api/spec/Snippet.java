package sunset.gui.api.spec;

import java.io.Serializable;

public class Snippet extends ApiEntry implements Serializable {
	
	private static final long serialVersionUID = -4544274362488451669L;
	
	protected String body;
	
	public Snippet() {
	}
	
	public Snippet(String name, String desc, String body) {
		super.setName(name);
		super.setDescription(desc);
		this.body = body;
	}

    public String getBody() {
        return body;
    }

    public void setBody(String value) {
        this.body = value;
    }
}
