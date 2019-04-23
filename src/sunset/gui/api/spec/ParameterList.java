package sunset.gui.api.spec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParameterList implements Serializable {

	private static final long serialVersionUID = -1240433731527254501L;

    protected List<Parameter> parameter;

    public List<Parameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<Parameter>();
        }
        return this.parameter;
    }
}
