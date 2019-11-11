package sunset.gui.api.spec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProcedureList implements Serializable {

	private static final long serialVersionUID = -734886252406773163L;

    protected List<Procedure> procedure;

    public List<Procedure> getProcedure() {
        if (procedure == null) {
            procedure = new ArrayList<Procedure>();
        }
        return this.procedure;
    }
}
