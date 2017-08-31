/**
 * 
 */
package sunset.gui.editor;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplEditorKit extends StyledEditorKit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 251349811071032628L;

	 private ViewFactory _ffaplViewFactory;

	    public FFaplEditorKit(FFaplCodeTextPane owner) {
	        _ffaplViewFactory = new FFaplViewFactory(owner);
	    }
	   
	    @Override
	    public ViewFactory getViewFactory() {
	        return _ffaplViewFactory;
	    }

	    @Override
	    public String getContentType() {
	        return "text/ffapl";
	    }


}
