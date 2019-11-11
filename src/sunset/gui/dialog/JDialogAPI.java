package sunset.gui.dialog;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import sunset.gui.FFaplJFrame;
import sunset.gui.api.MutableTreeNodeApiEntry;
import sunset.gui.api.spec.Snippet;
import sunset.gui.interfaces.IAbout;
import sunset.gui.listener.ActionListenerCloseWindow;
import sunset.gui.listener.ActionListenerEditApiEntry;
import sunset.gui.util.TranslateGUIElements;




@SuppressWarnings("serial")
public class JDialogAPI extends FFaplJDialog {
	private JPanel jPanel_control;
	private JScrollPane jScrollPane_info;
	private JButton jButton_close;
	private JButton jButton_edit;
	private JEditorPane jEditorPane_info;
	private MutableTreeNodeApiEntry apiEntry;
	private ActionListenerEditApiEntry editActionListener;

	/**
	 * 
	 * @param frame
	 */
	public JDialogAPI(FFaplJFrame frame,  MutableTreeNodeApiEntry apiEntry) {
		super(frame);
		this.apiEntry = apiEntry;
		editActionListener = new ActionListenerEditApiEntry(frame, this);
		initGUI();
		initComponents();
		setResizable(true);
		
	}
	
	private void initGUI() {
		try {
			this.setTitle("FFapl API Spezifikationen");
			this.setName("dialog_api");
			{
				jPanel_control = new JPanel();
				FlowLayout jPanel_controlLayout = new FlowLayout();
				jPanel_controlLayout.setAlignment(FlowLayout.RIGHT);
				getContentPane().add(jPanel_control, BorderLayout.SOUTH);
				jPanel_control.setPreferredSize(new java.awt.Dimension(384, 32));
				jPanel_control.setLayout(jPanel_controlLayout);
				
				
				//jPanel_control.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.GRAY));
				{
					jButton_edit = new JButton();
					jButton_edit.setName("button_edit");
					jPanel_control.add(jButton_edit);
					jButton_edit.setText("Edit");
					jButton_edit.setVisible(false);
					
					jButton_close = new JButton();
					jButton_close.setName("button_close");
					jPanel_control.add(jButton_close);
					jButton_close.setText("Close");
				}
			}
			jScrollPane_info = new JScrollPane();
			getContentPane().add(jScrollPane_info, BorderLayout.CENTER);
			jScrollPane_info.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			//jScrollPane_Console.setAutoscrolls(true);
			
			{
				//jPanel_info = new JPanel();
				//BorderLayout jPanel_infoLayout = new BorderLayout();
				//jPanel_info.setLayout(jPanel_infoLayout);
				//jScrollPane_info.setViewportView(jPanel_info);
				{
					jEditorPane_info = new JEditorPane();
					jEditorPane_info.setEditable(false);
					jEditorPane_info.setContentType("text/plain");
					HTMLEditorKit editorKit = new HTMLEditorKit();
					StyleSheet styleSheet = editorKit.getStyleSheet();
				    
				    styleSheet.addRule(IAbout.STYLE_SHEET);
				    editorKit.setStyleSheet(styleSheet);
					jEditorPane_info.setEditorKit(editorKit);
					
					FlowLayout jTextPane_infoLayout = new FlowLayout();
					jEditorPane_info.setLayout(jTextPane_infoLayout);
					jScrollPane_info.setViewportView(jEditorPane_info);
					if(apiEntry != null){
						jEditorPane_info.setText(apiEntry.getHTMLInfo());
					}
				}
			}
			
			this.setSize(500, 300);
			translate();
			//pack();				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * init components
	 */
	private void initComponents() {
		jButton_close.addActionListener(new ActionListenerCloseWindow(this));
		jButton_edit.addActionListener(editActionListener);
	}
	
	/**
	 * Re-init the Dialog
	 */
	public void reInit(MutableTreeNodeApiEntry apiEntry){
		this.apiEntry = apiEntry;
		if(apiEntry != null){
			jEditorPane_info.setText(apiEntry.getHTMLInfo());
			if(apiEntry.getEntry()  instanceof Snippet){
				jButton_edit.setVisible(true);
				editActionListener.setSnippet((Snippet) apiEntry.getEntry());
			}else{
				jButton_edit.setVisible(false);
			}
		}else{
			jButton_edit.setVisible(false);
		}
		
		translate();
	}
	
	/**
	 * Translates the components
	 */
	private void translate(){
		TranslateGUIElements.translateButton(jButton_close);
		TranslateGUIElements.translateButton(jButton_edit);
		TranslateGUIElements.translateDialog(this);	
	}

}
