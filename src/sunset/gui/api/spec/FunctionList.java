package sunset.gui.api.spec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FunctionList implements Serializable {

	private static final long serialVersionUID = -8854472568141460100L;

    protected List<Function> function;

    public List<Function> getFunction() {
        if (function == null) {
            function = new ArrayList<Function>();
        }
        return this.function;
    }
}
