package sunset.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sunset.gui.FFaplJFrame;
import sunset.gui.interfaces.IDialogSearchReplace;
import sunset.gui.listener.ActionListenerCloseWindow;
import sunset.gui.listener.ActionListenerFindString;
import sunset.gui.util.TranslateGUIElements;

import javax.swing.JTabbedPane;
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
public class JDialogSearchReplace extends JDialog implements IDialogSearchReplace {

	private FFaplJFrame _frame;
	private final JPanel contentPanel = new JPanel();
	private JTabbedPane jTabbedPane_main;
	private JTextField jTextField_searchtext;
	private JTextField jTextField_replacetext;
	private JButton jButton_find;
	private JButton jButton_replace;
	private JButton jButton_cancel;
	private JLabel jLabel_searchfor;
	private JLabel jLabel_replacewith;
	private Vector<Component> replaceComp = new Vector<Component>();
	private int dialog_width = 450;
	private int dialog_height = 260;
	private JPanel panelStatus;
	private JLabel jLabel_status;
	private JCheckBox chckbxMatchCase;
	private JCheckBox chckbxRegularExpression;
	private JCheckBox chckbxDotMatchNewLine;
	private JCheckBox chckbxWrapAround;

	/**
	 * Create the dialog.
	 */
	public JDialogSearchReplace(FFaplJFrame frame) {
		super(frame);
		setResizable(false);
		this._frame = frame;
		setFont(new Font("Dialog", Font.PLAIN, 10));
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
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			jTabbedPane_main = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(jTabbedPane_main);
			{
				JPanel panelSearchReplace = new JPanel();
				jTabbedPane_main.addTab("Search", null, panelSearchReplace, "Search");
				jTabbedPane_main.setEnabledAt(0, true);
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
					jButton_find.setBounds(330, 13, 81, 21);
					panelSearchReplaceMain.add(jButton_find);
					jButton_find.setName("button_find");
					getRootPane().setDefaultButton(jButton_find);
				}
				
				jButton_replace = new JButton("Replace");
				jButton_replace.setName("button_replace");
				jButton_replace.setBounds(330, 49, 81, 21);
				panelSearchReplaceMain.add(jButton_replace);
				replaceComp.add(jButton_replace);
				
				chckbxMatchCase = new JCheckBox("Match case");
				chckbxMatchCase.setToolTipText("case sensitive search");
				chckbxMatchCase.setBounds(88, 87, 120, 21);
				panelSearchReplaceMain.add(chckbxMatchCase);
				
				chckbxRegularExpression = new JCheckBox("Regular expression");
				chckbxRegularExpression.setToolTipText("regular expression search");
				chckbxRegularExpression.setBounds(88, 108, 120, 21);
				panelSearchReplaceMain.add(chckbxRegularExpression);
				
				chckbxDotMatchNewLine = new JCheckBox(". matches newline");
				chckbxDotMatchNewLine.setToolTipText(". also matches newline characters like \\n");
				chckbxDotMatchNewLine.setBounds(210, 108, 120, 21);
				chckbxDotMatchNewLine.setVisible(false);
				panelSearchReplaceMain.add(chckbxDotMatchNewLine);
				
				chckbxWrapAround = new JCheckBox("Wrap around");
				chckbxWrapAround.setToolTipText("start from beginning if pattern was not found");
				chckbxWrapAround.setBounds(210, 87, 93, 21);
				panelSearchReplaceMain.add(chckbxWrapAround);
				
				panelStatus = new JPanel();
				panelSearchReplace.add(panelStatus, BorderLayout.SOUTH);
				panelStatus.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				
				jLabel_status = new JLabel("");
				panelStatus.add(jLabel_status);
			}
			{
				jTabbedPane_main.addTab("Replace", null, jTabbedPane_main.getTabComponentAt(0), "Replace");
				jTabbedPane_main.setEnabledAt(1, true);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				jButton_cancel = new JButton("Cancel");
				jButton_cancel.setName("button_cancel");
				buttonPane.add(jButton_cancel);
			}
		}
	}
	
	private void initListener() {
		
		jButton_cancel.addActionListener(new ActionListenerCloseWindow(this));
		jButton_find.addActionListener(new ActionListenerFindString(this));
		this.addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		    	jTextField_searchtext.requestFocusInWindow();
		    }
		});
		
		jTabbedPane_main.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean bReplaceTabSelected = jTabbedPane_main.getSelectedIndex() == 1;
				
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
		TranslateGUIElements.translateButton(jButton_cancel);
		TranslateGUIElements.translateButton(jButton_find);
		TranslateGUIElements.translateLabel(jLabel_searchfor);
		TranslateGUIElements.translateLabel(jLabel_replacewith);
		TranslateGUIElements.translateDialog(this);
	}
	
	/**
	 * Performs preparation work before the dialog is displayed. Selects the corresponding tab in the dialog.
	 * Calculates the optimal position of the dialog, clears it's status and translates all GUI elements according to the specified language.
	 * @param isReplace
	 */
	public void prepareDialog(boolean isReplace) {
		jTabbedPane_main.setSelectedIndex(isReplace ? 1 : 0);
		
		for (Component comp : replaceComp) {
			comp.setVisible(isReplace);
		}
		
		int x_pos = _frame.getWidth()/2 - dialog_width/2 + _frame.getX();
		int y_pos = _frame.getHeight()/2 - dialog_height/2 + _frame.getY();
		
		setStatus("", Color.black);
		setBounds(x_pos, y_pos, dialog_width, dialog_height);
		translate();
	}

	@Override
	public String searchPattern() {
		return jTextField_searchtext.getText();
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
