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
import sunset.gui.listener.ActionListenerCloseWindow;
import sunset.gui.listener.ActionListenerFindString;
import sunset.gui.util.TranslateGUIElements;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.JLayeredPane;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class JDialogSearchReplace extends JDialog {

	private FFaplJFrame _frame;
	private final JPanel contentPanel = new JPanel();
	private JTabbedPane jTabbedPane_main;
	private JTextField jTextField_searchtext;
	private JTextField jTextField_replacetext;
	private JButton jButton_find;
	private JButton jButton_cancel;
	private JLabel jLabel_searchfor;
	private JLabel jLabel_replacewith;
	private Vector<Component> replaceComp = new Vector<Component>();
	private int dialog_width = 450;
	private int dialog_height = 240;
	private JPanel panelStatus;
	private JLabel jLabel_status;

	/**
	 * Create the dialog.
	 */
	public JDialogSearchReplace(FFaplJFrame frame) {
		super(frame);
		this._frame = frame;
		setFont(new Font("Dialog", Font.PLAIN, 10));
		initGUI();
		initComponents();
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
				jLabel_searchfor.setBounds(35, 10, 113, 26);
				jLabel_searchfor.setHorizontalAlignment(SwingConstants.CENTER);
				jLabel_searchfor.setName("label_searchfor");
				panelSearchReplaceMain.add(jLabel_searchfor);
				
				jTextField_searchtext = new JTextField();
				jTextField_searchtext.setBounds(158, 11, 253, 26);
				jTextField_searchtext.setColumns(10);
				panelSearchReplaceMain.add(jTextField_searchtext);
				
				jLabel_replacewith = new JLabel("Replace with:");
				jLabel_replacewith.setBounds(35, 46, 113, 26);
				jLabel_replacewith.setHorizontalAlignment(SwingConstants.CENTER);
				jLabel_replacewith.setName("label_replacewith");
				panelSearchReplaceMain.add(jLabel_replacewith);
				replaceComp.add(jLabel_replacewith);
				
				jTextField_replacetext = new JTextField();
				jTextField_replacetext.setBounds(158, 47, 253, 26);
				jTextField_replacetext.setColumns(10);
				panelSearchReplaceMain.add(jTextField_replacetext);
				replaceComp.add(jTextField_replacetext);
				
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
				jButton_find = new JButton("Search");
				jButton_find.setName("button_find");
				buttonPane.add(jButton_find);
				getRootPane().setDefaultButton(jButton_find);
			}
			{
				jButton_cancel = new JButton("Cancel");
				jButton_cancel.setName("button_cancel");
				buttonPane.add(jButton_cancel);
			}
		}
		
		jTabbedPane_main.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean bReplaceTabSelected = jTabbedPane_main.getSelectedIndex() == 1;
				
				for (Component comp : replaceComp) {
					comp.setVisible(bReplaceTabSelected);
				}
			}
	    });
	}
	
	private void initComponents() {
		
		jButton_cancel.addActionListener(new ActionListenerCloseWindow(this));
		jButton_find.addActionListener(new ActionListenerFindString(this));
		this.addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		    	jTextField_searchtext.requestFocusInWindow();
		    }
		});
		this.addEscapeListener(this);
	}
	
	public void addEscapeListener(final JDialog dialog) {
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
	
	public FFaplJFrame getFrame() {
		return this._frame;
	}
	
	public void prepareDialog(boolean isReplace) {
		jTabbedPane_main.setSelectedIndex(isReplace?1:0);
		
		for (Component comp : replaceComp) {
			comp.setVisible(isReplace);
		}
		
		int x_pos = _frame.getWidth()/2 - dialog_width/2 + _frame.getX();
		int y_pos = _frame.getHeight()/2 - dialog_height/2 + _frame.getY();
		
		setBounds(x_pos, y_pos, dialog_width, dialog_height);
		translate();
	}
	
	public String getSearchString() {
		return jTextField_searchtext.getText();
	}
	
	public void setStatus(String status, Color color) {
		jLabel_status.setText(status);
		jLabel_status.setForeground(color);
	}
}
