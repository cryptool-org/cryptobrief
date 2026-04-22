package sunset.gui.tabbedpane;

import ffapl.debugger.StackFrame;

import javax.swing.*;
import java.awt.*;

public class JPanelStackFrames extends JPanel {
    private final JScrollPane scrollPane;

    public JPanelStackFrames(JScrollPane scrollPane) {
        this.setLayout(new BorderLayout());
        this.scrollPane = scrollPane;
        this.add(scrollPane);
    }

    public void setStackFrameList(JList<StackFrame> stackFrameStack){
        scrollPane.setViewportView(stackFrameStack);
    }
}
