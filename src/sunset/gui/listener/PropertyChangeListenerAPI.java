/**
 * 
 */
package sunset.gui.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSplitPane;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class PropertyChangeListenerAPI implements PropertyChangeListener {

	private double _max;

	/**
	 * 
	 * @param properties
	 */
	public PropertyChangeListenerAPI(double max) {
		_max = max;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent ev) {
		double a, b, c;
		
		if(ev.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY) || 
				ev.getPropertyName().equals(JSplitPane.WIDTH) ||
				ev.getPropertyName().equals(JSplitPane.HEIGHT)){
			if(ev.getSource() instanceof JSplitPane){
				a = ((JSplitPane)ev.getSource()).getDividerLocation();
				b = ((JSplitPane)ev.getSource()).getWidth();
				c = a/b;
				if((c) > _max){
					((JSplitPane)ev.getSource()).setDividerLocation(_max);
				}
			}
		}
		

	}

}
