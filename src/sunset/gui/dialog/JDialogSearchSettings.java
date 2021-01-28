package sunset.gui.dialog;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import sunset.gui.interfaces.IProperties;
import sunset.gui.listener.ActionListenerCloseWindow;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.search.advanced.AdvancedSearchReplace;
import sunset.gui.search.advanced.exception.MatchingPairConfigurationException;
import sunset.gui.util.TranslateGUIElements;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class JDialogSearchSettings extends JDialog {
	private final static String DEFAULT_CONFIG =
			"(...), [...], {...}, \\(...\\), \\[...\\], \\{...\\}, \\begin{%1}...\\end{%1}";
	private final JPanel contentPanel = new JPanel();
	private JLabel jLabel_matchingpairs;
	private JTextField jTextField_matchingpairs;
	private JButton jButton_save;
	private JButton jButton_cancel;
	private JLabel jLabel_status;
	
	public JDialogSearchSettings(JDialog owner) {
		super(owner);
		setResizable(false);
		setFont(new Font("Dialog", Font.PLAIN, 10));
		initGUI();
		initListener();
	}
	
	private void initGUI() {
		setTitle("Search Settings");
		setName("dialog_searchsettings");		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setPreferredSize(new Dimension(600, 110));
		contentPanel.setLayout(new BorderLayout(0, 0));
		{			
			JPanel panelMain = new JPanel();
			contentPanel.add(panelMain, BorderLayout.CENTER);
			panelMain.setLayout(null);
			
			jTextField_matchingpairs = new JTextField();
			jTextField_matchingpairs.setBounds(94, 10, 473, 19);
			panelMain.add(jTextField_matchingpairs);
			jTextField_matchingpairs.setColumns(10);
			
			jButton_save = new JButton("Save");
			jButton_save.setBounds(387, 39, 85, 21);
			jButton_save.setName("button_save");
			panelMain.add(jButton_save);
			
			jButton_cancel = new JButton("Cancel");
			jButton_cancel.setBounds(482, 39, 85, 21);
			jButton_cancel.setName("button_cancel");
			panelMain.add(jButton_cancel);
			
			jLabel_matchingpairs = new JLabel("Matching pairs:");
			jLabel_matchingpairs.setBounds(10, 13, 85, 13);
			jLabel_matchingpairs.setName("label_matchingpairs");
			panelMain.add(jLabel_matchingpairs);
			
			jLabel_status = new JLabel("");
			jLabel_status.setBounds(10, 43, 367, 13);
			panelMain.add(jLabel_status);
		}
	}
	
	private void initListener() {
		jButton_cancel.addActionListener(new ActionListenerCloseWindow(this));
		jButton_save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String matchingPairs = jTextField_matchingpairs.getText();
				try {
					new AdvancedSearchReplace(matchingPairs);
					GUIPropertiesLogic.getInstance().setProperty(IProperties.GUI_SEARCH_PAIRS, matchingPairs);
					updateStatus("Settings saved successfully.", Color.blue);
				} catch (MatchingPairConfigurationException e1) {
					updateStatus(e1.getMessage(), Color.red);
				}
			}
			
		});
		
		this.addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		    	jTextField_matchingpairs.requestFocusInWindow();
		    }
		});
		
		this.addEscapeListener(this);
	}
	
	private void addEscapeListener(final JDialog dialog) {
	    ActionListener escListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
	    };

	    dialog.getRootPane().registerKeyboardAction(escListener,
	            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	            JComponent.WHEN_IN_FOCUSED_WINDOW);

	}
	
	private void translate() {
		TranslateGUIElements.translateButton(jButton_cancel);
		TranslateGUIElements.translateButton(jButton_save);
		TranslateGUIElements.translateLabel(jLabel_matchingpairs);
		TranslateGUIElements.translateDialog(this);
	}
	
	public void prepareAndSwowDialog(JDialog owner) {
		pack();
		setLocationRelativeTo(owner);
		translate();
		String pairs = GUIPropertiesLogic.getInstance().getProperty(IProperties.GUI_SEARCH_PAIRS);
		if (pairs == null) {
			// use default matching pairs
			pairs = DEFAULT_CONFIG;
		}
		jTextField_matchingpairs.setText(pairs);
		updateStatus("", Color.black);
		setVisible(true);
	}
	
	public void updateStatus(String status, Color color) {
		jLabel_status.setForeground(color);
		jLabel_status.setText(status);
	}
}