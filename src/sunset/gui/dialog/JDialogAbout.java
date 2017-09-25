package sunset.gui.dialog;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import sunset.gui.interfaces.IAbout;
import sunset.gui.interfaces.IProperties;
import sunset.gui.listener.ActionListenerCloseWindow;
import sunset.gui.util.SunsetBundle;
import sunset.gui.util.TranslateGUIElements;




@SuppressWarnings("serial")
public class JDialogAbout extends FFaplJDialog {
	private JPanel jPanel_control;
	private JPanel jPanel_info;
	private JPanel jPanel_images;
	private JButton jButton_close;
	private JButton jButton_license;
	private JButton jButton_credits;
	private JEditorPane jEditorPane_info;
	private JLabel jLabel_image;

	/**
	 * 
	 * @param frame
	 */
	public JDialogAbout(Window frame) {
		super(frame);
		initGUI();
		initComponents();
	}
	
	private void initGUI() {
		try {
			this.setTitle("About Sunset");
			this.setName("dialog_about");
			{
				jPanel_control = new JPanel();
				FlowLayout jPanel_controlLayout = new FlowLayout();
				jPanel_controlLayout.setAlignment(FlowLayout.CENTER);
				getContentPane().add(jPanel_control, BorderLayout.SOUTH);
				jPanel_control.setPreferredSize(new java.awt.Dimension(384, 32));
				jPanel_control.setLayout(jPanel_controlLayout);
				
				
				//jPanel_control.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.GRAY));
				{
					jButton_credits= new JButton();
					jButton_credits.setName("button_credits");
					jPanel_control.add(jButton_credits);
					jButton_credits.setText("Credits");
				}
				{
					jButton_license = new JButton();
					jButton_license.setName("button_license");
					jPanel_control.add(jButton_license);
					jButton_license.setText("License");
				}
				{
					jButton_close = new JButton();
					jButton_close.setName("button_close");
					jPanel_control.add(jButton_close);
					jButton_close.setText("Close");
				}
			}
			{
				jPanel_info = new JPanel();
				BorderLayout jPanel_infoLayout = new BorderLayout();
				jPanel_info.setLayout(jPanel_infoLayout);
				getContentPane().add(jPanel_info, BorderLayout.CENTER);
				{
					jPanel_images = new JPanel();
					FlowLayout jPanel_imagesLayout = new FlowLayout();
					jPanel_imagesLayout.setAlignment(FlowLayout.CENTER);
					jPanel_info.add(jPanel_images, BorderLayout.NORTH);
					//jPanel_images.setPreferredSize(new java.awt.Dimension(57, 101));
					//jPanel_images.setBackground(Color.white);
					jPanel_images.setLayout(jPanel_imagesLayout);
					
					jLabel_image = new JLabel();
					jLabel_image.setIcon(new ImageIcon(getClass().getClassLoader().getResource(IProperties.IMAGEPATH + "about.png")));
					jPanel_images.add(jLabel_image);
				}
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
					jPanel_info.add(jEditorPane_info, BorderLayout.CENTER);
					jEditorPane_info.setBackground(jPanel_images.getBackground());
				}
			}
			
			//this.setSize(369, 171);
			translate();
			pack();				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * init components
	 */
	private void initComponents() {
		final JDialog owner = this;
		jButton_close.addActionListener(new ActionListenerCloseWindow(this));
		jButton_license.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog dialog = new JDialogLicense(owner);
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
		jButton_credits.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog dialog = new JDialogCredits(owner);
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
	}
	
	/**
	 * Re-init the Dialog
	 */
	public void reInit(){
		translate();
	}
	
	/**
	 * Translates the components
	 */
	private void translate(){
		TranslateGUIElements.translateButton(jButton_close);
		TranslateGUIElements.translateButton(jButton_license);
		TranslateGUIElements.translateDialog(this);	
		jEditorPane_info.setText(
			"<h1>Sunset " + IProperties.APPVERSION + "</h1>" +
		    "<div id='desc'>" + SunsetBundle.getInstance().getProperty("sunset_desc") + "<div><br/>");// +

	}

}
