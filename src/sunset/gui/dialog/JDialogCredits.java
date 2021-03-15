package sunset.gui.dialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Window;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

import sunset.gui.interfaces.ILicense;
import sunset.gui.interfaces.IProperties;
import sunset.gui.listener.ActionListenerCloseWindow;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.util.TranslateGUIElements;



@SuppressWarnings("serial")
public class JDialogCredits extends FFaplJDialog {
	private JPanel jPanel_Control;
	private JPanel jPanel_south;
	private JPanel jPanel_images;
	//private JLabel jLabel_gnu;
	private JLabel jLabel_cc;
	private JLabel jLabel_sunset;
	private JLabel jLabel_ffapl;
	private JLabel jLabel_uni;
	private JLabel jLabel_syssec;
	private JButton jButton_close;
	//private JPanel jPanel_license;
	private JEditorPane jEditorPane_license;
	private JScrollPane jScrollPane_license;

	public JDialogCredits(Window frame) {
		super(frame);
		initGUI();
		initComponents();
	}
	
	private void initGUI() {
		try {
			this.setTitle("Credits");
			this.setName("dialog_credits");
			{
				jPanel_south = new JPanel();
				BorderLayout jPanel_southLayout = new BorderLayout();
				jPanel_south.setLayout(jPanel_southLayout);
				getContentPane().add(jPanel_south, BorderLayout.SOUTH);
				jPanel_south.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.GRAY));
				
				jPanel_images = new JPanel();
				FlowLayout jPanel_imagesLayout = new FlowLayout();
				jPanel_images.setLayout(jPanel_imagesLayout);
				jPanel_south.add(jPanel_images, BorderLayout.WEST);
				
				//jLabel_gnu = new JLabel();
				//jLabel_gnu.setIcon(new ImageIcon(getClass().getClassLoader().getResource(IProperties.IMAGEPATH + "gnu.png")));
				//jPanel_images.add(jLabel_gnu);
				
//				jLabel_cc = new JLabel();
//				jLabel_cc.setIcon(new ImageIcon(getClass().getClassLoader().getResource(IProperties.IMAGEPATH + "cc.png")));
//				jPanel_images.add(jLabel_cc);
				
				jLabel_sunset = new JLabel();
				jLabel_sunset.setIcon(new ImageIcon(getClass().getClassLoader().getResource(IProperties.IMAGEPATH + "sunset24.png")));
				jPanel_images.add(jLabel_sunset);
				
				jLabel_ffapl = new JLabel();
				jLabel_ffapl.setIcon(new ImageIcon(getClass().getClassLoader().getResource(IProperties.IMAGEPATH + "ffapl24.png")));
				jPanel_images.add(jLabel_ffapl);
				
				jLabel_uni = new JLabel();
				jLabel_uni.setIcon(new ImageIcon(getClass().getClassLoader().getResource(IProperties.IMAGEPATH + "uni24.png")));
				jPanel_images.add(jLabel_uni);
				
				jLabel_syssec = new JLabel();
				jLabel_syssec.setIcon(new ImageIcon(getClass().getClassLoader().getResource(IProperties.IMAGEPATH + "syssec24.png")));
				jPanel_images.add(jLabel_syssec);
				
				jPanel_Control = new JPanel();
				FlowLayout jPanel_ControlLayout = new FlowLayout();
				jPanel_ControlLayout.setAlignment(FlowLayout.RIGHT);
				jPanel_ControlLayout.setAlignOnBaseline(true);
				jPanel_south.add(jPanel_Control, BorderLayout.EAST);
				jPanel_Control.setLayout(jPanel_ControlLayout);
				
				{
					jButton_close = new JButton();
					jPanel_Control.add(jButton_close);
					jButton_close.setName("button_close");
					jButton_close.setText("Close");
					jButton_close.setLocation(0, 4);
				}
			}
			jScrollPane_license = new JScrollPane();
			
			jScrollPane_license.setAutoscrolls(true);
			jScrollPane_license.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			getContentPane().add(jScrollPane_license, BorderLayout.CENTER);
			
				{
					//jPanel_license = new JPanel();
					//BorderLayout jPanel_licenseLayout = new BorderLayout();
					//jPanel_license.setLayout(jPanel_licenseLayout);
					
					{
						jEditorPane_license = new JTextPane();
						jScrollPane_license.setViewportView(jEditorPane_license);
						//jPanel_license.add(jEditorPane_license, BorderLayout.CENTER);
						jEditorPane_license.setContentType("text/plain");
						jEditorPane_license.setEditorKit(new HTMLEditorKit());
						jEditorPane_license.setEditable(false);
						jEditorPane_license.setBackground(Color.white);
						jEditorPane_license.setBorder(null);
					}
				}
			this.setSize(550, 475);
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
		jEditorPane_license.addHyperlinkListener(new HyperlinkListener(){

			@Override
			public void hyperlinkUpdate(HyperlinkEvent event) {
				Desktop desktop = Desktop.getDesktop();
				if(event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) 
					if(desktop != null){
						try {
							desktop.browse(event.getURL().toURI());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
				}
			
		});
	}
	
	/**
	 * Translates the components
	 */
	private void translate(){
		String lang = GUIPropertiesLogic.getInstance().getProperty(IProperties.LANGUAGE);
		TranslateGUIElements.translateButton(jButton_close);
		TranslateGUIElements.translateDialog(this);
		
		jEditorPane_license.setText("The following persons contributed to Sunset/FFapl: <br><br> Alexander O. Ortner: initial concept, language definition, compiler and GUI"
				+ "<br>Johannes Winkler: full support of elliptic curves (in affine coordinates)<br>Volker Bugl, Markus Wiltsche: support for user input and GUI improvements"
				+ "<br>Max-Julian Jakobitsch: generation of random points on elliptic curves of degree 2<br>Manuel Langer: established upwards compatibility with Java 9 and later, implemented the normal, advanced and regular expression search and replace function in the IDE");

	}
	

}
