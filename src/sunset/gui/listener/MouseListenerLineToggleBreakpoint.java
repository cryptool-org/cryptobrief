package sunset.gui.listener;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.tabbedpane.JTabbedPaneCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseListenerLineToggleBreakpoint implements MouseListener {

    private final JTabbedPaneCode _tabbedPane_code;
    private final JTextPane jTextPane_LineNumber;

    public MouseListenerLineToggleBreakpoint(JTextPane jTextPane_LineNumber, JTabbedPaneCode _tabbedPane_code) {
        this.jTextPane_LineNumber = jTextPane_LineNumber;
        this._tabbedPane_code = _tabbedPane_code;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final int fontHeight = jTextPane_LineNumber.getFontMetrics(jTextPane_LineNumber.getFont()).getHeight();
        final Insets margin = jTextPane_LineNumber.getMargin();
        final int line = Math.max(1, (int) Math.floor((e.getY() - margin.top) / (double) fontHeight) + 1);
        FFaplCodeTextPane textPane = ((FFaplCodeTextPane) _tabbedPane_code.currentCodePane());
        textPane.getDebugger().toggleBreakpoint(line);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
