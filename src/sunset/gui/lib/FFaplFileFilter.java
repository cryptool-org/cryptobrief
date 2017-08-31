/**
 * 
 */
package sunset.gui.lib;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import sunset.gui.interfaces.IFFaplLang;
import sunset.gui.interfaces.IProperties;
import sunset.gui.util.SunsetBundle;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplFileFilter extends FileFilter {

	/**
	 * 
	 */
	public FFaplFileFilter() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File file) {
		
		if(! file.isDirectory()){
			if (file.getName().toLowerCase().endsWith(IProperties.FILEEXTENTION))
		    {
				return true;
		    }
		}else{
			//a directory
			return true;
		}
	    
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return SunsetBundle.getInstance().getProperty(IFFaplLang.FILEFILTER) + 
		" (*" +IProperties.FILEEXTENTION + ")";
	}

}
