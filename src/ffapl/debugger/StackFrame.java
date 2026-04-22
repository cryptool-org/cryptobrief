package ffapl.debugger;

import ffapl.lib.interfaces.ISymbol;

public class StackFrame {
    private final ISymbol funcSymbol;
    private ISymbol scopeSymbol;
    private String id;

    public StackFrame(ISymbol funcSymbol, String id) {
        this.funcSymbol = funcSymbol;
        this.id = id;
    }

    public ISymbol getScopeSymbol() {
        return scopeSymbol;
    }

    public void setScopeSymbol(ISymbol scopeSymbol) {
        this.scopeSymbol = scopeSymbol;
    }

    public int getLine() {
        return funcSymbol.getToken().line();
    }

    public int getColumn() {
        return funcSymbol.getToken().column();
    }

    public String getName() {
        if (id != null){
            return funcSymbol.getName() + "     [" + id + "]";
        }
        return funcSymbol.getName();
    }

}
