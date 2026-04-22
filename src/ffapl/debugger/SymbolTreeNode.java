package ffapl.debugger;


import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.IVm;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPrime;
import sunset.gui.util.StringUtil;

import javax.swing.tree.DefaultMutableTreeNode;

public class SymbolTreeNode extends DefaultMutableTreeNode {
    private final IVm interpreter;
    private final ISymbol symbol;

    public SymbolTreeNode(ISymbol symbol, IVm interpreter) {
        this.symbol = symbol;
        this.interpreter = interpreter;
    }

    public boolean isGlobal() {
        return symbol.isGlobal();
    }

    public ISymbol getSymbol() {
        return symbol;
    }

    public String getSymbolName() {
        return symbol.getName();
    }

    public String getValue() throws FFaplAlgebraicException {
        try{
            return interpreter.getValue(symbol.getOffset(), symbol.isGlobal());
        } catch (ArrayIndexOutOfBoundsException ignored){
            // when stepping over a return statement, the UI still tries to retrieve variables from within the scope
            // so to avoid trying to load invalid errors we have a catch block here
            return "undefined";
        }
    }

    @Override
    public String toString() {
        try {
            String val = this.getValue();
            if (this.symbol.getType() instanceof FFaplPrime || this.symbol.getType() instanceof FFaplInteger) {
                val = StringUtil.toScientific(val);
            }
            return this.getSymbolName() + "= " + val;
        } catch (FFaplAlgebraicException e) {
            throw new RuntimeException(e);
        }
    }
}