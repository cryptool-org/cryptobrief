/**
 * 
 */
package sunset.gui.editor;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * FFaplViewFactory
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplViewFactory implements ViewFactory {

	private FFaplCodeTextPane _owner;
	
	/**
	 * 
	 * @param owner
	 */
	public FFaplViewFactory(FFaplCodeTextPane owner){
		_owner = owner;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.text.ViewFactory#create(javax.swing.text.Element)
	 */
	@Override
	public View create(Element element) {
		FFaplView view = new FFaplView(element, _owner);
		_owner.setActiveView(view);
		return view;
	}

}
