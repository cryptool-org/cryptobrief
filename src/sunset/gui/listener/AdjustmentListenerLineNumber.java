/**
 * 
 */
package sunset.gui.listener;

import java.awt.Adjustable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollPane;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class AdjustmentListenerLineNumber implements AdjustmentListener{

	private JScrollPane _jScrollPane_Code;

	
	public AdjustmentListenerLineNumber(JScrollPane jScrollPane_Code){
		_jScrollPane_Code = jScrollPane_Code;
	}
	
	

	@Override
	public void adjustmentValueChanged(AdjustmentEvent evt) {
		Adjustable source = evt.getAdjustable();
	    if (evt.getValueIsAdjusting()) {
	      return;
	    }
	    int orient = source.getOrientation();
	    
	   
	    if (orient == Adjustable.VERTICAL && source.getValue() != _jScrollPane_Code.getVerticalScrollBar().getValue()) {
	    	source.setValue(_jScrollPane_Code.getVerticalScrollBar().getValue()); 
	    }
		
	}	

}
