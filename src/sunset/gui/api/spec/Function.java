package sunset.gui.api.spec;

import java.io.Serializable;

public class Function extends Procedure implements Serializable {
	
	private static final long serialVersionUID = 724392166287102920L;

    protected String returnType;

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String value) {
        this.returnType = value;
    }
}
