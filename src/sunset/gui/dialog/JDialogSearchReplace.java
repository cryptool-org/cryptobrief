package sunset.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
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
	private JCheckBox chckbxWrapAround;
	private JRadioButton rdbtnStandardSearch;
	private JRadioButton rdbtnAdvancedSearch;
	private JRadioButton rdbtnRegularExpression;
	private JCheckBox chckbxDotMatchNewLine;
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
		setPreferredSize(new Dimension(500, 330));
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
				
				JPanel panelOptions = new JPanel();
				
				JPanel panelMode = new JPanel();
				panelMode.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, 
						new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, 
						TitledBorder.TOP, null, new Color(0, 0, 0)), "Search Mode", TitledBorder.LEADING, 
						TitledBorder.TOP, null, null));
				panelMode.setLayout(null);
				
				panelMode.setBounds(10, 110, 140, 100);
				
				panelOptions.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, 
						new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, 
						TitledBorder.TOP, null, new Color(0, 0, 0)), "Options", TitledBorder.LEADING, 
						TitledBorder.TOP, null, null));
				panelOptions.setLayout(null);
				
				panelOptions.setBounds(160, 110, 190, 100);
				
				jLabel_searchfor = new JLabel("Search for:");
				jLabel_searchfor.setBounds(10, 10, 74, 26);
				jLabel_searchfor.setHorizontalAlignment(SwingConstants.LEFT);
				jLabel_searchfor.setName("label_searchfor");
				panelSearchReplaceMain.add(jLabel_searchfor);
				
				jTextField_searchtext = new JTextField();
				jTextField_searchtext.setBounds(90, 10, 260, 26);
				jTextField_searchtext.setColumns(10);
				panelSearchReplaceMain.add(jTextField_searchtext);
				
				jLabel_replacewith = new JLabel("Replace with:");
				jLabel_replacewith.setBounds(10, 46, 74, 26);
				jLabel_replacewith.setHorizontalAlignment(SwingConstants.LEFT);
				jLabel_replacewith.setName("label_replacewith");
				panelSearchReplaceMain.add(jLabel_replacewith);
				replaceComp.add(jLabel_replacewith);
				
				jTextField_replacetext = new JTextField();
				jTextField_replacetext.setBounds(90, 46, 260, 26);
				jTextField_replacetext.setColumns(10);
				panelSearchReplaceMain.add(jTextField_replacetext);
				replaceComp.add(jTextField_replacetext);
				
				{
					jButton_find = new JButton("Find Next");
					jButton_find.setBounds(360, 13, 101, 21);
					jButton_find.setName("button_find");
					panelSearchReplaceMain.add(jButton_find);
					getRootPane().setDefaultButton(jButton_find);
				}
				
				{
					jButton_replace = new JButton("Replace");
					jButton_replace.setBounds(360, 49, 101, 21);
					jButton_replace.setName("button_replace");
					panelSearchReplaceMain.add(jButton_replace);
					replaceComp.add(jButton_replace);
				}
				
				{
					jButton_replaceall = new JButton("Replace All");
					jButton_replaceall.setBounds(360, 85, 101, 21);
					jButton_replaceall.setName("button_replaceall");
					panelSearchReplaceMain.add(jButton_replaceall);
					replaceComp.add(jButton_replaceall);
				}
				
				chckbxMatchCase = new JCheckBox("Match case");
				chckbxMatchCase.setBounds(10, 20, 174, 21);
				chckbxMatchCase.setName("chckbx_matchcase");
				panelOptions.add(chckbxMatchCase);
				
				chckbxWrapAround = new JCheckBox("Wrap around");
				chckbxWrapAround.setBounds(10, 42, 174, 21);
				chckbxWrapAround.setName("chckbx_wraparound");
				panelOptions.add(chckbxWrapAround);

				chckbxDotMatchNewLine = new JCheckBox(". matches newline");
				chckbxDotMatchNewLine.setBounds(10, 64, 174, 21);
				chckbxDotMatchNewLine.setName("chckbx_dotall");
				chckbxDotMatchNewLine.setEnabled(false);
				panelOptions.add(chckbxDotMatchNewLine);
				
				rdbtnStandardSearch = new JRadioButton("Standard search");
				rdbtnStandardSearch.setBounds(10, 20, 120, 21);
				rdbtnStandardSearch.setName("rdbtn_standardsearch");
				panelMode.add(rdbtnStandardSearch);
				
				rdbtnAdvancedSearch = new JRadioButton("Advanced search");
				rdbtnAdvancedSearch.setBounds(10, 42, 120, 21);
				rdbtnAdvancedSearch.setName("rdbtn_advancedsearch");
				panelMode.add(rdbtnAdvancedSearch);
				
				rdbtnRegularExpression = new JRadioButton("Regular expression");
				rdbtnRegularExpression.setBounds(10, 64, 120, 21);
				rdbtnRegularExpression.setName("rdbtn_regularexpression");
				panelMode.add(rdbtnRegularExpression);
				
				ButtonGroup bgModes = new ButtonGroup();
				bgModes.add(rdbtnStandardSearch);
				bgModes.add(rdbtnAdvancedSearch);
				bgModes.add(rdbtnRegularExpression);
				
				panelSearchReplaceMain.add(panelOptions);
				panelSearchReplaceMain.add(panelMode);
				
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
		
		rdbtnStandardSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chckbxDotMatchNewLine.setEnabled(rdbtnRegularExpression.isSelected());
			}
		});
		
		rdbtnAdvancedSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chckbxDotMatchNewLine.setEnabled(rdbtnRegularExpression.isSelected());
			}
		});
		
		rdbtnRegularExpression.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chckbxDotMatchNewLine.setEnabled(rdbtnRegularExpression.isSelected());
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
		TranslateGUIElements.translateButton(jButton_replaceall);
		TranslateGUIElements.translateButton(jButton_cancel);
		TranslateGUIElements.translateLabel(jLabel_searchfor);
		TranslateGUIElements.translateLabel(jLabel_replacewith);
		TranslateGUIElements.translateCheckbox(chckbxMatchCase);
		TranslateGUIElements.translateCheckbox(chckbxWrapAround);
		TranslateGUIElements.translateRadioButton(rdbtnStandardSearch);
		TranslateGUIElements.translateRadioButton(rdbtnAdvancedSearch);
		TranslateGUIElements.translateRadioButton(rdbtnRegularExpression);
		TranslateGUIElements.translateCheckbox(chckbxDotMatchNewLine);
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
		rdbtnStandardSearch.setSelected(true);
		chckbxDotMatchNewLine.setSelected(false);
		chckbxDotMatchNewLine.setEnabled(false);
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
		return rdbtnRegularExpression.isSelected();
	}

	@Override
	public boolean dotMatchesNewLine() {
		return chckbxDotMatchNewLine.isSelected();
	}

	@Override
	public boolean useAdvancedSearch() {
		return rdbtnAdvancedSearch.isSelected();
	}
}
