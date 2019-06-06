package sunset.gui.api.spec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Parameter implements Serializable {

	private static final long serialVersionUID = -3250127886623125620L;

	protected String name;
    protected List<String> type;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public List<String> getType() {
        if (type == null) {
            type = new ArrayList<String>();
        }
        return this.type;
    }
}
