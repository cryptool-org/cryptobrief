/**
 * 
 */
package sunset.gui.dialog;

import java.awt.Point;
import java.awt.Window;

import javax.swing.JDialog;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public abstract class FFaplJDialog extends JDialog {
	
	/**
	 * 
	 * @param window
	 */
	public FFaplJDialog(Window window) {
		super(window);
		setResizable(false);
	}
	
	/**
	 * init the Location of the JDialog
	 */
	protected void initLocation(){
		
		Window frame = this.getOwner();
		int x, y;
		x = frame.getLocation().x + Math.max(0, (frame.getWidth() - getWidth())/2);
		y = frame.getLocation().y + Math.max(0, (frame.getHeight() - getHeight())/2);
		Point p = new Point();
		p.setLocation(x,y);
		setLocation(p);
		this.setIconImages(frame.getIconImages());
	}
	
	@Override
	public void setVisible(boolean arg){
		if(this.isVisible() != arg){
			if(arg){
				initLocation();
			}
			super.setVisible(arg);
		}
	}
}
