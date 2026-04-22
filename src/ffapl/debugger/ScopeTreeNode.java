package ffapl.debugger;

import ffapl.lib.interfaces.ISymbol;

import javax.swing.tree.DefaultMutableTreeNode;

public class ScopeTreeNode extends DefaultMutableTreeNode {
    private final boolean isRoot;
    private final String name;
    private final ISymbol scope;

    public ScopeTreeNode(String name, ISymbol scope, boolean isRoot) {
        this.isRoot = isRoot;
        this.name = name;
        this.scope = scope;
    }

    public ISymbol getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    public boolean isRoot(){
        return isRoot;
    }

    @Override
    public String toString() {
        return getName();
    }
}