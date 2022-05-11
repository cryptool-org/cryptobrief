package sunset.gui.listener;

import java.awt.Adjustable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Synchronize the Vertical scroll bar of two JScrollPane
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class AdjustmentListenerScrollPaneVertical 
implements AdjustmentListener, MouseMotionListener {
	private JScrollPane _scrollPane;
	
	public AdjustmentListenerScrollPaneVertical(JScrollPane scrollPane){
		  _scrollPane = scrollPane;
	}
		
	  public void adjustmentValueChanged(AdjustmentEvent evt) {
	    Adjustable source = evt.getAdjustable();
	    
	    if (evt.getValueIsAdjusting()) {
	      return;
	    }
	    int orient = source.getOrientation();
	   
	    if (orient == Adjustable.VERTICAL) {
	    	_scrollPane.getVerticalScrollBar().setValue(source.getValue());
	    }
	  }

	@Override
	public void mouseDragged(MouseEvent arg) {
	    if (arg.getSource() instanceof JScrollBar) {
	    	if(((JScrollBar)arg.getSource()).getOrientation() == Adjustable.VERTICAL){
	    		_scrollPane.getVerticalScrollBar().setValue(((JScrollBar)arg.getSource()).getValue());
	    	}
	    }
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
