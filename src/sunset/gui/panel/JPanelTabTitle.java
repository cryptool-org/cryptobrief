package sunset.gui.panel;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import sunset.gui.FFaplJFrame;

import sunset.gui.button.TabCloseButton;
import sunset.gui.interfaces.IProperties;



@SuppressWarnings("serial")
public class JPanelTabTitle extends javax.swing.JPanel {
	private JButton jButton_close;
	private JLabel jLabel_title;
	private JTabbedPane _owner;
		
	public JPanelTabTitle(JTabbedPane owner, String tabTitle) {
		super();
		_owner = owner;
		this.setFocusable(false);
		initGUI(tabTitle);
	}
	
	private void initGUI(String tabTitle) {
		try {
			FlowLayout thisLayout = new FlowLayout();
			this.setLayout(thisLayout);
			setOpaque(false);
			//this.setPreferredSize(new java.awt.Dimension(166, 131));
			{
				jLabel_title = new JLabel();
				jLabel_title.setFocusable(false);
				this.add(jLabel_title);
				jLabel_title.setText(tabTitle);
			}
			{
				jButton_close = new TabCloseButton(_owner, this);
				jButton_close.setFocusable(false);
				this.add(jButton_close);
				jButton_close.setIcon(new ImageIcon(getClass().getClassLoader().getResource(IProperties.IMAGEPATH + "close.png")));
				jButton_close.setVisible(_owner.getTabCount() == 1);
                                FFaplJFrame.getStartComp().add(jButton_close);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the button visibility to true or false
	 * @param val
	 */
	public void setButtonVisible(boolean val){
		jButton_close.setVisible(val);
		//this.updateUI();
	}
        
        public JButton getButton(){
                return jButton_close;
        }

	public void inputSaved(boolean val) {
		int idx;
		if(val){
			idx = jLabel_title.getText().indexOf("*");
			if(idx == 0){
				jLabel_title.setText(jLabel_title.getText().substring(1));
			}
		}else{
			idx = jLabel_title.getText().indexOf("*");
			if(idx != 0){
				jLabel_title.setText("*" + jLabel_title.getText());
			}
		}
	}
	
	/**
	 * Sets the title
	 * @param title
	 */
	public void setTitle(String title){
		jLabel_title.setText(title);
	}

	/**
	 * Returns the title
	 * @return
	 */
	public String getTitle() {
		return jLabel_title.getText();
	}

	

}
