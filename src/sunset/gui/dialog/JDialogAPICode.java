package sunset.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;

import sunset.gui.FFaplJFrame;
import sunset.gui.api.MutableTreeNodeApiEntry;
import sunset.gui.api.jaxb.Snippet;
import sunset.gui.api.table.Java2sAutoTextField;
import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.lib.FFaplFileFilter;
import sunset.gui.listener.ActionListenerApplyApiEntry;
import sunset.gui.listener.ActionListenerCloseWindow;
import sunset.gui.listener.ActionListenerSaveApiEntry;
import sunset.gui.logic.ApiLogic;
import sunset.gui.util.TranslateGUIElements;

@SuppressWarnings("serial")
public class JDialogAPICode extends FFaplJDialog {
	private JPanel jPanel_control;
	private JPanel jPanel_head;
	private JButton jButton_cancel;
	private JButton jButton_ok;
	private JButton jButton_apply;
	private JLabel jLabel_name;
	private JLabel jLabel_description;
	private JLabel jLabel_snippet;
	// private JLabel jLabel_proc;
	// private JTable jTable_Parameter;
	private JTextField jTextbox_name;
	private JTextArea jTextArea_description;
	Snippet snippet;
	private JScrollPane jScrollPane_Code;
	private JPanel jPanel_Code;
	private FFaplCodeTextPane jTextPane_Code;
	private JScrollPane jScrollPane_textArea;
	private JPanel jPanel_textArea;
	private FFaplJFrame frame;

	/**
	 * 
	 * @param frame
	 */
	public JDialogAPICode(FFaplJFrame frame, Snippet snippet) {
		super(frame);
		this.frame = frame;
		initGUI();
		initComponents();
		setResizable(true);
		reInit(snippet);
	}

	private void initGUI() {
		try {
			this.setTitle("FFapl API Spezifikationen");
			this.setName("dialog_api");
			{

				jPanel_control = new JPanel();
				FlowLayout jPanel_controlLayout = new FlowLayout();
				jPanel_controlLayout.setAlignment(FlowLayout.RIGHT);
				getContentPane().setLayout(new BorderLayout());
				getContentPane().add(jPanel_control, BorderLayout.SOUTH);
				jPanel_control
						.setPreferredSize(new java.awt.Dimension(384, 32));
				jPanel_control.setLayout(jPanel_controlLayout);

				{
					jButton_ok = new JButton();
					jButton_ok.setName("button_ok");
					jPanel_control.add(jButton_ok);
					jButton_ok.setText("Ok");

					jButton_cancel = new JButton();
					jButton_cancel.setName("button_cancel");
					jPanel_control.add(jButton_cancel);
					jButton_cancel.setText("Cancel");
					
					jButton_apply = new JButton();
					jButton_apply.setName("button_apply");
					jPanel_control.add(jButton_apply);
					jButton_apply.setText("Apply");

				}

				jPanel_head = new JPanel();
				jPanel_head.setPreferredSize(new java.awt.Dimension(384, 170));
				getContentPane().add(jPanel_head, BorderLayout.CENTER);
				SpringLayout layout = new SpringLayout();
				jPanel_head.setLayout(layout);

				jLabel_name = new JLabel();
				jLabel_name.setName("label_name");
				jPanel_head.add(jLabel_name);

				jTextbox_name = new JTextField(30);
				jPanel_head.add(jTextbox_name);

				jLabel_description = new JLabel();
				jLabel_description.setName("label_description");
				jPanel_head.add(jLabel_description);

				jTextArea_description = new JTextArea("", 5, 30);
				jPanel_textArea = new JPanel();
				jPanel_textArea.setLayout(new BorderLayout());

				jScrollPane_textArea = new JScrollPane();
				jScrollPane_textArea.setViewportView(jTextArea_description);

				jPanel_textArea.add(jScrollPane_textArea, BorderLayout.CENTER);
				jPanel_head.add(jPanel_textArea);

				jLabel_snippet = new JLabel();
				jLabel_snippet.setName("label_snippet");
				jPanel_head.add(jLabel_snippet);

				jScrollPane_Code = new JScrollPane();
				jPanel_head.add(jScrollPane_Code);
				{
					jPanel_Code = new JPanel();
					BorderLayout jPanel_CodeLayout = new BorderLayout();
					jPanel_Code.setLayout(jPanel_CodeLayout);
					jScrollPane_Code.setViewportView(jPanel_Code);
					{
						jTextPane_Code = new FFaplCodeTextPane();
						jPanel_Code.add(jTextPane_Code, BorderLayout.CENTER);
						jTextPane_Code.setFont(new Font("Monospaced",
								Font.PLAIN, 12));
						jTextArea_description.setFont(new Font("Monospaced",
								Font.PLAIN, 12));
						jTextbox_name.setFont(new Font("Monospaced",
								Font.PLAIN, 12));
					}
				}

				layout.putConstraint(SpringLayout.WEST, jLabel_name, 10,
						SpringLayout.WEST, jPanel_head);
				layout.putConstraint(SpringLayout.NORTH, jLabel_name, 10,
						SpringLayout.NORTH, jPanel_head);

				layout.putConstraint(SpringLayout.NORTH, jTextbox_name, 3,
						SpringLayout.SOUTH, jLabel_name);
				layout.putConstraint(SpringLayout.WEST, jTextbox_name, 0,
						SpringLayout.WEST, jLabel_name);

				layout.putConstraint(SpringLayout.NORTH, jLabel_description,
						30, SpringLayout.SOUTH, jLabel_name);
				layout.putConstraint(SpringLayout.WEST, jLabel_description, 0,
						SpringLayout.WEST, jLabel_name);

				layout.putConstraint(SpringLayout.NORTH, jPanel_textArea, 3,
						SpringLayout.SOUTH, jLabel_description);
				layout.putConstraint(SpringLayout.WEST, jPanel_textArea, 0,
						SpringLayout.WEST, jLabel_description);
				layout.putConstraint(SpringLayout.EAST, jPanel_head, 5,
						SpringLayout.EAST, jPanel_textArea);
				layout.putConstraint(SpringLayout.EAST, jTextbox_name, 0,
						SpringLayout.EAST, jPanel_textArea);
				layout.putConstraint(SpringLayout.EAST, jScrollPane_Code, 0,
						SpringLayout.EAST, jPanel_textArea);

				layout.putConstraint(SpringLayout.NORTH, jLabel_snippet, 100,
						SpringLayout.SOUTH, jLabel_description);
				layout.putConstraint(SpringLayout.WEST, jLabel_snippet, 0,
						SpringLayout.WEST, jPanel_textArea);

				layout.putConstraint(SpringLayout.NORTH, jScrollPane_Code, 3,
						SpringLayout.SOUTH, jLabel_snippet);
				layout.putConstraint(SpringLayout.WEST, jScrollPane_Code, 0,
						SpringLayout.WEST, jLabel_snippet);

				layout.putConstraint(SpringLayout.SOUTH, jPanel_head, 5,
						SpringLayout.SOUTH, jScrollPane_Code);
			}

			this.setSize(500, 500);
			translate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * init components
	 */
	private void initComponents() {
		jButton_cancel.addActionListener(new ActionListenerCloseWindow(this));
		jButton_ok.addActionListener(new ActionListenerSaveApiEntry(this));
		jButton_apply.addActionListener(new ActionListenerApplyApiEntry(this));
	}

	/**
	 * Re-init the Dialog
	 */
	public void reInit(Snippet snippet) {
		this.snippet = snippet;
		if (this.snippet == null) {
			this.snippet = new Snippet();
		}
		jTextbox_name.setText(this.snippet.getName());
		jTextArea_description.setText(this.snippet.getDescription());
		jTextPane_Code.setText(this.snippet.getBody());

		translate();
		this.repaint();
	}

	/**
	 * Translates the components
	 */
	private void translate() {
		TranslateGUIElements.translateButton(jButton_cancel);
		TranslateGUIElements.translateButton(jButton_ok);
		TranslateGUIElements.translateButton(jButton_apply);
		TranslateGUIElements.translateLabel(jLabel_name);
		TranslateGUIElements.translateLabel(jLabel_description);
		TranslateGUIElements.translateLabel(jLabel_snippet);
		TranslateGUIElements.translateDialog(this);
	}

	public void saveApiEntry() throws MalformedURLException, JAXBException {
		snippet.setBody(jTextPane_Code.getText());
		snippet.setName(jTextbox_name.getText());
		snippet.setDescription(jTextArea_description.getText());
		if(StringUtils.isNotBlank(snippet.getName())){
			ApiLogic.getInstance().persistSnippetCode(snippet);
			this.frame.initLanguage();
		}
	}

}
