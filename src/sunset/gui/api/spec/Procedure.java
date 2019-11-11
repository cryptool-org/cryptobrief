package sunset.gui.api.spec;

import java.io.Serializable;

public class Procedure extends ApiEntry implements Serializable {

	private static final long serialVersionUID = 5148918213467477658L;

    protected ParameterList parameterList;
    protected String body;

    public ParameterList getParameterList() {
        return parameterList;
    }

    public void setParameterList(ParameterList value) {
        this.parameterList = value;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String value) {
        this.body = value;
    }
}
