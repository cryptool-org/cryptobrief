package sunset.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sunset.gui.FFaplJFrame;
import sunset.gui.listener.ActionListenerCloseWindow;
import sunset.gui.search.SearchReplaceCoordinator;
import sunset.gui.search.interfaces.ISearchReplaceDialog;
import sunset.gui.search.interfaces.ISearchReplaceCoordinator;
import sunset.gui.search.interfaces.ISearchReplaceShowDialog;
import sunset.gui.search.listener.ActionListenerFindString;
import sunset.gui.search.listener.ActionListenerReplaceAll;
import sunset.gui.search.listener.ActionListenerReplaceString;
import sunset.gui.tabbedpane.JTabbedPaneNamed;
import sunset.gui.util.TranslateGUIElements;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class JDialogSearchReplace extends JDialog implements ISearchReplaceDialog, ISearchReplaceShowDialog {

	private final JPanel contentPanel = new JPanel();
	private JTabbedPaneNamed jTabbedPaneNamed_main;
	private JTextField jTextField_searchtext;
	private JTextField jTextField_replacetext;
	private JButton jButton_find;
	private JButton jButton_replace;
	private JButton jButton_replaceall;
	private JButton jButton_cancel;
	private JLabel jLabel_searchfor;
	private JLabel jLabel_replacewith;
	private Vector<Component> replaceComp = new Vector<Component>();
	private JCheckBox chckbxMatchCase;
	private JCheckBox chckbxRegularExpression;
	private JCheckBox chckbxDotMatchNewLine;
	private JCheckBox chckbxWrapAround;
	private JLabel jLabel_status;

	/**
	 * Create the dialog.
	 */
	public JDialogSearchReplace(FFaplJFrame frame) {
		super(frame);
		setResizable(false);
		setFont(new Font("Dialog", Font.PLAIN, 10));
		setLocationRelativeTo(null);
		initGUI();
		initListener();
		translate();
	}
	
	private void initGUI() {
		setTitle("Search");
		setName("dialog_searchreplace");		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setPreferredSize(new Dimension(480, 280));
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			jTabbedPaneNamed_main = new JTabbedPaneNamed();
			contentPanel.add(jTabbedPaneNamed_main);
			{
				JPanel panelSearchReplace = new JPanel();				
				panelSearchReplace.setLayout(new BorderLayout(0, 0));
				
				JPanel panelSearchReplaceMain = new JPanel();
				panelSearchReplace.add(panelSearchReplaceMain, BorderLayout.CENTER);
				panelSearchReplaceMain.setLayout(null);
				
				jLabel_searchfor = new JLabel("Search for:");
				jLabel_searchfor.setBounds(10, 10, 74, 26);
				jLabel_searchfor.setHorizontalAlignment(SwingConstants.CENTER);
				jLabel_searchfor.setName("label_searchfor");
				panelSearchReplaceMain.add(jLabel_searchfor);
				
				jTextField_searchtext = new JTextField();
				jTextField_searchtext.setBounds(88, 10, 224, 26);
				jTextField_searchtext.setColumns(10);
				panelSearchReplaceMain.add(jTextField_searchtext);
				
				jLabel_replacewith = new JLabel("Replace with:");
				jLabel_replacewith.setBounds(10, 46, 74, 26);
				jLabel_replacewith.setHorizontalAlignment(SwingConstants.CENTER);
				jLabel_replacewith.setName("label_replacewith");
				panelSearchReplaceMain.add(jLabel_replacewith);
				replaceComp.add(jLabel_replacewith);
				
				jTextField_replacetext = new JTextField();
				jTextField_replacetext.setBounds(88, 46, 224, 26);
				jTextField_replacetext.setColumns(10);
				panelSearchReplaceMain.add(jTextField_replacetext);
				replaceComp.add(jTextField_replacetext);
				{
					jButton_find = new JButton("Find Next");
					jButton_find.setBounds(330, 13, 101, 21);
					jButton_find.setName("button_find");
					panelSearchReplaceMain.add(jButton_find);
					getRootPane().setDefaultButton(jButton_find);
				}
				
				{
					jButton_replace = new JButton("Replace");
					jButton_replace.setBounds(330, 49, 101, 21);
					jButton_replace.setName("button_replace");
					panelSearchReplaceMain.add(jButton_replace);
					replaceComp.add(jButton_replace);
				}
				
				{
					jButton_replaceall = new JButton("Replace All");
					jButton_replaceall.setBounds(330, 85, 101, 21);
					jButton_replaceall.setName("button_replaceall");
					panelSearchReplaceMain.add(jButton_replaceall);
					replaceComp.add(jButton_replaceall);
				}
				
				chckbxMatchCase = new JCheckBox("Match case");
				chckbxMatchCase.setBounds(88, 87, 224, 21);
				chckbxMatchCase.setName("chckbx_matchcase");
				panelSearchReplaceMain.add(chckbxMatchCase);
				
				chckbxRegularExpression = new JCheckBox("Regular expression");
				chckbxRegularExpression.setBounds(88, 133, 120, 21);
				chckbxRegularExpression.setName("chckbx_regex");
				panelSearchReplaceMain.add(chckbxRegularExpression);
				
				chckbxDotMatchNewLine = new JCheckBox(". matches newline");
				chckbxDotMatchNewLine.setBounds(210, 133, 151, 21);
				chckbxDotMatchNewLine.setVisible(false);
				chckbxDotMatchNewLine.setName("chckbx_dotall");
				panelSearchReplaceMain.add(chckbxDotMatchNewLine);
				
				chckbxWrapAround = new JCheckBox("Wrap around");
				chckbxWrapAround.setBounds(88, 110, 224, 21);
				chckbxWrapAround.setName("chckbx_wraparound");
				panelSearchReplaceMain.add(chckbxWrapAround);
				
				jTabbedPaneNamed_main.addTab("Search", null, 
						panelSearchReplace, "Search", 
						"tabbedPane_tabsearch");
				jTabbedPaneNamed_main.setEnabledAt(0, true);
				
				jTabbedPaneNamed_main.addTab("Replace", null, 
						jTabbedPaneNamed_main.getTabComponentAt(0), "Replace", 
						"tabbedPane_tabreplace");
				jTabbedPaneNamed_main.setEnabledAt(1, true);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BorderLayout(0, 0));
			
			JPanel panelControl = new JPanel();
			buttonPane.add(panelControl, BorderLayout.EAST);
			
			jButton_cancel = new JButton("Cancel");
			jButton_cancel.setName("button_cancel");
			panelControl.add(jButton_cancel);
			
			JPanel panelStatus = new JPanel();
			buttonPane.add(panelStatus, BorderLayout.WEST);
			
			jLabel_status = new JLabel("Status");
			jLabel_status.setHorizontalAlignment(SwingConstants.CENTER);
			panelStatus.add(jLabel_status);
		}
	}
	
	private void initListener() {
		jButton_cancel.addActionListener(new ActionListenerCloseWindow(this));
		
		ISearchReplaceCoordinator coordinator = new SearchReplaceCoordinator(this);
		jButton_find.addActionListener(new ActionListenerFindString(coordinator));
		jButton_replace.addActionListener(new ActionListenerReplaceString(coordinator));
		jButton_replaceall.addActionListener(new ActionListenerReplaceAll(coordinator));
		
		this.addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		    	jTextField_searchtext.requestFocusInWindow();
		    }
		});
		
		jTabbedPaneNamed_main.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = jTabbedPaneNamed_main.getSelectedIndex();
				boolean bReplaceTabSelected = jTabbedPaneNamed_main
						.getTabNameAt(selectedIndex).equals("tabbedPane_tabreplace");
				
				for (Component comp : replaceComp) {
					comp.setVisible(bReplaceTabSelected);
				}
			}
	    });
		
		chckbxRegularExpression.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chckbxDotMatchNewLine.setVisible(chckbxRegularExpression.isSelected());
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
		TranslateGUIElements.translateButton(jButton_find);
		TranslateGUIElements.translateButton(jButton_replace);
		TranslateGUIElements.translateButton(jButton_cancel);
		TranslateGUIElements.translateLabel(jLabel_searchfor);
		TranslateGUIElements.translateLabel(jLabel_replacewith);
		TranslateGUIElements.translateCheckbox(chckbxMatchCase);
		TranslateGUIElements.translateCheckbox(chckbxRegularExpression);
		TranslateGUIElements.translateCheckbox(chckbxDotMatchNewLine);
		TranslateGUIElements.translateCheckbox(chckbxWrapAround);
		TranslateGUIElements.translateTappedPane(jTabbedPaneNamed_main);
		TranslateGUIElements.translateDialog(this);
	}
	
	/**
	 * Performs preparation work before the dialog is displayed. 
	 * @param isReplace
	 */
	@Override
	public void prepareDialog(boolean isReplace) {
		jTabbedPaneNamed_main.setSelectedIndex(isReplace ? 1 : 0);
		
		for (Component comp : replaceComp) {
			comp.setVisible(isReplace);
		}
		
		pack();
		resetFields();
		translate();
	}
	
	private void resetFields() {
		jTextField_searchtext.setText("");
		jTextField_replacetext.setText("");
		chckbxMatchCase.setSelected(false);
		chckbxRegularExpression.setSelected(false);
		chckbxDotMatchNewLine.setSelected(false);
		chckbxWrapAround.setSelected(false);
		jLabel_status.setText("");
	}

	@Override
	public String searchPattern() {
		return jTextField_searchtext.getText();
	}

	@Override
	public String replaceText() {
		return jTextField_replacetext.getText();
	}

	@Override
	public void setStatus(String status, Color color) {
		jLabel_status.setText(status);
		jLabel_status.setForeground(color);
	}

	@Override
	public boolean matchCase() {
		return chckbxMatchCase.isSelected();
	}

	@Override
	public boolean wrapAround() {
		return chckbxWrapAround.isSelected();
	}

	@Override
	public boolean useRegEx() {
		return chckbxRegularExpression.isSelected();
	}

	@Override
	public boolean dotMatchesNewLine() {
		return chckbxDotMatchNewLine.isSelected();
	}
}
