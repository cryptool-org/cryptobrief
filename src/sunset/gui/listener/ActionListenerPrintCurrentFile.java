package sunset.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.panel.JPanelCode;
import sunset.gui.tabbedpane.JTabbedPaneCode;
import sunset.gui.util.PrintUtil;

public class ActionListenerPrintCurrentFile implements ActionListener{

	private JTabbedPaneCode _tabbedPane;
	
	public ActionListenerPrintCurrentFile(JTabbedPaneCode tabbedPane) {
			_tabbedPane = tabbedPane;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JPanelCode codePane = ((JPanelCode)_tabbedPane.currentCodePanel());
		FFaplCodeTextPane codeTextPane = (FFaplCodeTextPane) codePane.getCodePane();
		try {
			//codeTextPane.print(null, null, true, null, null, true);
			PrintUtil.printToPrinter( codePane.getTitle(), codeTextPane.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
