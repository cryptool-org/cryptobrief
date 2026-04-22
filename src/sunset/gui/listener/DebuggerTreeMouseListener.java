package sunset.gui.listener;

import ffapl.debugger.*;
import sunset.gui.FFaplJFrame;
import sunset.gui.debugger.IFFaplDebugger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DebuggerTreeMouseListener extends MouseAdapter {
    private final VariableJTree tree;
    private final IFFaplDebugger debugger;
    private final JPopupMenu jCustomExpressionPopupMenu;
    private final JMenuItem deleteItem;

    public DebuggerTreeMouseListener(VariableJTree tree) {
        this.tree = tree;
        this.jCustomExpressionPopupMenu = new JPopupMenu();
        this.deleteItem = new JMenuItem("Remove Expression");
        this.debugger = tree.get_debugger();

        deleteItem.addActionListener((e) -> deleteSelectedNode());
        jCustomExpressionPopupMenu.add(deleteItem);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        showPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        showPopup(e);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1 && e.getClickCount() == 2) { // double-click
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) selPath.getLastPathComponent();
            if (node instanceof SymbolTreeNode) {
                tree.startEditingAtPath(selPath);
            } else if (node instanceof CustomExpressionTreeNode) {
                tree.startEditingAtPath(selPath);

            } else if (node instanceof CustomExpressionScopeTreeNode) {
                CustomExpressionScopeTreeNode scopeTreeNode = (CustomExpressionScopeTreeNode) node;
                CustomExpression customExpression = new CustomExpression("");
                debugger.addCustomExpression(customExpression);
                DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
                CustomExpressionTreeNode expressionTreeNode = new CustomExpressionTreeNode(customExpression, debugger);

                model.insertNodeInto(expressionTreeNode, scopeTreeNode, scopeTreeNode.getChildCount());
                tree.expandPath(selPath);
                FFaplJFrame.getDebuggerPanel().repaintTree();

            }

        }
    }

    private void showPopup(MouseEvent e){
        if (e.isPopupTrigger()){
            int row = tree.getClosestRowForLocation(e.getX(), e.getY());
            Object node = tree.getPathForLocation(e.getX(), e.getY()).getLastPathComponent();
            if (node instanceof CustomExpressionTreeNode){
                tree.setSelectionRow(row);
                jCustomExpressionPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private void deleteSelectedNode(){
        TreePath currentSelection = tree.getSelectionPath();

        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                    (currentSelection.getLastPathComponent());
            if (currentNode instanceof CustomExpressionTreeNode){
                CustomExpressionTreeNode customExpressionNode = ((CustomExpressionTreeNode) currentNode);
                debugger.removeCustomExpression(customExpressionNode.getExpression());
            }

            DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
            model.removeNodeFromParent(currentNode);
        }
    }
}
