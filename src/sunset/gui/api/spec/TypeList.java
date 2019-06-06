package sunset.gui.api.spec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TypeList implements Serializable {

	private static final long serialVersionUID = 7776359732048641268L;

    protected List<Type> type;

    public List<Type> getType() {
        if (type == null) {
            type = new ArrayList<Type>();
        }
        return this.type;
    }
}
