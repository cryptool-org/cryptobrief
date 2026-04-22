package sunset.gui.listener;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class KeyListenerExpressionField extends KeyAdapter {
    private final JTextField textField;
    private final List<String> history = new ArrayList<>();

    private int curIndex = 0;
    private String draft = "";

    public KeyListenerExpressionField(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (history.isEmpty()) return;

            if (curIndex == history.size()) {
                draft = textField.getText();
            }

            // Move up in history
            if (curIndex > 0) {
                curIndex--;
                textField.setText(history.get(curIndex));
            }

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (history.isEmpty()) return;

            // Move down in history
            if (curIndex < history.size() - 1) {
                curIndex++;
                textField.setText(history.get(curIndex));
            }

            else if (curIndex == history.size() - 1) {
                curIndex++;
                textField.setText(draft);
            }

        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = textField.getText().trim();

            if (!text.isEmpty() && (history.isEmpty() || !history.get(history.size() - 1).equals(text))) {
                history.add(text);
            }

            curIndex = history.size();
            draft = "";

        }
    }
}