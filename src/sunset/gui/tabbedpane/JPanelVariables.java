package sunset.gui.tabbedpane;

import ffapl.debugger.VariableJTree;
import sunset.gui.FFaplJFrame;
import sunset.gui.debugger.DebuggerTreeCellEditor;
import sunset.gui.debugger.DebuggerTreeCellRenderer;
import sunset.gui.listener.DebuggerTreeModelListener;
import sunset.gui.listener.DebuggerTreeMouseListener;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;

public class JPanelVariables extends JPanel {

    private final JScrollPane scrollPane;
    private JTree variableTree;

    public JPanelVariables(JScrollPane scrollPane) {
        this.setLayout(new BorderLayout());
        this.scrollPane = scrollPane;
        this.add(scrollPane);
    }


    public void setVariableTree(VariableJTree newTree) {
        if (newTree != null) {
            // expand every node
            for (int i = 0; i < newTree.getRowCount(); i++) {
                newTree.expandRow(i);
            }
        }
        scrollPane.setViewportView(newTree);
        variableTree = newTree;
        repaintTree();
    }

    public void repaintTree() {
        if (variableTree != null) {
            variableTree.revalidate();
            variableTree.repaint();
        }
        scrollPane.revalidate();
        scrollPane.repaint();
    }


}
