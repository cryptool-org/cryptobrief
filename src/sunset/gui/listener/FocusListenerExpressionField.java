package sunset.gui.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FocusListenerExpressionField implements FocusListener {
    final JTextField _owner;

    public FocusListenerExpressionField(JTextField owner) {
        _owner = owner;
        drawPlaceholder();
    }

    @Override
    public void focusGained(FocusEvent e) {
        // Clear the placeholder when clicked
        if (_owner.getText().equals("Put In Custom Statement")) {
            _owner.setText("");
            _owner.setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {

        drawPlaceholder();
    }

    private void drawPlaceholder(){
        // Restore the placeholder if left empty
        if (_owner.getText().isEmpty()) {
            _owner.setForeground(Color.GRAY);
            _owner.setText("Put In Custom Statement");
        }
    }
}
