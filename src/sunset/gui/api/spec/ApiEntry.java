package sunset.gui.api.spec;

import java.io.Serializable;

public class ApiEntry implements Serializable {

	private static final long serialVersionUID = -6192142655771784250L;

	protected String name;
    protected String description;
    protected String regex;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String value) {
        this.regex = value;
    }
}
