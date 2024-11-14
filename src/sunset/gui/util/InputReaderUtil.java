package sunset.gui.util;

import java.util.LinkedList;
import java.util.Vector;

import ffapl.exception.FFaplException;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.util.FFaplReader;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import sunset.gui.FFaplJFrame;
import sunset.gui.listener.ActionListenerInputField;

public class InputReaderUtil extends FFaplReader {

    @SuppressWarnings("deprecation")
    public String getInput(ISymbolTable symbolTable) {
        String result = "";

        LinkedList<String> input = ActionListenerInputField.getInput();
        int tabIndex = ActionListenerInputField.getTabbedPane().getSelectedIndex();

        FFaplJFrame.getInputField().setEnabled(true);
        FFaplJFrame.getInputField().grabFocus();

        try {
            Vector<ISymbol> a = symbolTable.lookupsimilar(new FFaplSymbol("", IJavaType.INTEGER));

            ActionListenerInputField.addMap(tabIndex, Thread.currentThread()); // Maps the selected tab index at the current Thread
            synchronized (input) {
                // wait for input from field
                input.wait();

                while(!ActionListenerInputField.getMap(ActionListenerInputField.getTabbedPane().getSelectedIndex()).equals(Thread.currentThread())) {
                    input.wait(); // wait until selected Tab maps to current running thread
                }

                result = input.remove(0).trim(); // take input value
                ActionListenerInputField.deleteMap(tabIndex); // delete the map of this index
            }

            Thread.sleep(10); // because if you do not wait, another lock afterwards would cause the Textfield to be disabled
        } catch (FFaplException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException ex) {
            ActionListenerInputField.deleteMap(tabIndex);
            FFaplJFrame.getInputField().setText("");
            FFaplJFrame.getInputField().setEnabled(false);

            Thread.currentThread().stop(); // deprecated, but the only way to stop the thread instantly
        }

        return result;
    }

}
