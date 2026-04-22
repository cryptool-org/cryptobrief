package sunset.gui.debugger;

import javax.swing.Icon;
import java.awt.*;

public class DebugIcon implements Icon {
    private final Color color;
    private final String text;

    public DebugIcon(Color color, String text) {
        this.color = color;
        this.text = text;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        g2.fillOval(x + 2, y + 2, 12, 12);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + 3 + (12 - fm.stringWidth(text)) / 2;
        int textY = y + 2 + ((12 - fm.getHeight()) / 2) + fm.getAscent();

        g2.drawString(text, textX, textY);
        g2.dispose();
    }

    @Override
    public int getIconWidth() { return 16; }

    @Override
    public int getIconHeight() { return 16; }
}