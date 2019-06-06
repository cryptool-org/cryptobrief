package sunset.gui.api.spec;

import java.io.Serializable;

public class ApiSpecification implements Serializable {

	private static final long serialVersionUID = 5207366277587602916L;
	
	protected TypeList typeList;
    protected ProcedureList procedureList;
    protected FunctionList functionList;

    public TypeList getTypeList() {
        return typeList;
    }

    public void setTypeList(TypeList value) {
        this.typeList = value;
    }

    public ProcedureList getProcedureList() {
        return procedureList;
    }

    public void setProcedureList(ProcedureList value) {
        this.procedureList = value;
    }

    public FunctionList getFunctionList() {
        return functionList;
    }

    public void setFunctionList(FunctionList value) {
        this.functionList = value;
    }
}
