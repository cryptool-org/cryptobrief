package sunset.gui.panel;

import sunset.gui.enums.LineState;
import sunset.gui.view.LineHandler;

import javax.swing.*;
import java.awt.*;

public class JTextPaneLineNumber extends JTextPane {
    private final LineHandler lineHandler;

    public JTextPaneLineNumber(LineHandler lineHandler) {
        this.lineHandler = lineHandler;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //TODO: maybe think about replacing the line number itself with the breakpoint to avoid overlap
        // or just paint the background with the line number red

        var lines = lineHandler.getLinesofState(LineState.BREAKPOINT);
        final int textHeight = getFontMetrics(getFont()).getHeight();
        final int size = (int) (textHeight * 0.8);
        final Insets margin = getMargin();
        g.setColor(Color.RED);
        for (Integer line : lines) {
            g.fillOval(this.getWidth() - size, margin.top + textHeight - size + textHeight * line -2, size, size);
        }
    }
}
