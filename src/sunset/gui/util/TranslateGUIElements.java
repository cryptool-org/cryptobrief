/**
 * 
 */
package sunset.gui.util;

import java.awt.Component;
import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import sunset.gui.tabbedpane.JTabbedPaneNamed;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class TranslateGUIElements {

	
	/**
	 * Translates JMenuBar includine Subelements
	 * @param menubar
	 */
	public static void translateMenuBar(JMenuBar menubar){
		Component comp;
		for (int i = 0; i < menubar.getComponentCount(); i++){
			comp = menubar.getComponent(i);
			if(comp instanceof JMenu){
				TranslateGUIElements.translateMenu((JMenu)comp);
			}else if(comp instanceof JMenuItem){
				TranslateGUIElements.translateMenuItem((JMenuItem)comp);
			}
		}
	}
	/**
	 * translates Menu
	 * @param menu
	 */
	public static void translateMenu(JMenu menu){
		Component comp;
		String title;
		String name;
		name = menu.getName();
		if(name != null){
			title = SunsetBundle.getInstance().getProperty(name);
			if(title != null){
				menu.setText(title);
			}
		}
	
		for (int i = 0; i < menu.getItemCount(); i++){
			comp = menu.getItem(i);
			if(comp instanceof JMenu){
				translateMenu((JMenu)comp);
			}else if(comp instanceof JMenuItem){
				translateMenuItem((JMenuItem)comp);
			}
		}
		
	}
	
	/**
	 * Translates MenuItem
	 * @param menuItem
	 */
	public static void translateMenuItem(JMenuItem menuItem){
		String title;
		String name;
		name = menuItem.getName();
		if(name != null){
			title = SunsetBundle.getInstance().getProperty(name);
			if(title != null){
				menuItem.setText(title);
			}
		}
	}
	
	/**
	 * Translates Tooltiptext of a Button
	 * @param button
	 */
	public static void translateButtonToolTipText(JButton button){
		String title;
		String name;
		name = button.getName();
		if(name != null){
			title = SunsetBundle.getInstance().getProperty(name);
			if(title != null){
				button.setToolTipText(title);
			}
		}
	}
	
	/**
	 * Translates translateTappedPane tabs
	 * @param JTabbedPaneNamed
	 */
	public static void translateTappedPane(JTabbedPaneNamed tabbedPane){
		String title = null;
		String name;
		for(int i = 0; i < tabbedPane.getTabCount(); i++){
			name = tabbedPane.getTabNameAt(i);
			if(name != null){
				title = SunsetBundle.getInstance().getProperty(name);
				if(title != null){
					tabbedPane.setTitleAt(i, title);
				}
			}
		}
	}
	
	/**
	 * Translates Label
	 * @param label
	 */
	public static void translateLabel(JLabel label) {
		String title;
		String name;
		name = label.getName();
		if(name != null){
			title = SunsetBundle.getInstance().getProperty(name);
			if(title != null){
				label.setText(title);
			}
		}	
	}
	
	/**
	 * Translates Button
	 * @param jButton_Ok
	 */
	public static void translateButton(JButton button) {
		String title;
		String name;
		name = button.getName();
		if(name != null){
			title = SunsetBundle.getInstance().getProperty(name);
			if(title != null){
				button.setText(title);
			}
		}
		
	}
	
	/**
	 * Translates Dialog
	 * @param dialog
	 */
	public static void translateDialog(Dialog dialog) {
		String title;
		String name;
		name = dialog.getName();
		if(name != null){
			title = SunsetBundle.getInstance().getProperty(name);
			if(title != null){
				dialog.setTitle(title);
			}
		}		
	}
	
	/**
	 * Translate Label
	 * @param _lineColumnTxt
	 */
	public static void translateButtonToolTipText(JLabel label) {
		String txt;
		String name;
		name = label.getName();
		if(name != null){
			txt = SunsetBundle.getInstance().getProperty(name);
			if(txt != null){
				label.setText(txt);
			}
		}		
		
	}
	public static void translateRadioButton(JRadioButton radio) {
		String txt;
		String name;
		name = radio.getName();
		if(name != null){
			txt = SunsetBundle.getInstance().getProperty(name);
			if(txt != null){
				radio.setText(txt);
			}
		}		
	}
	
	public static void translateCheckbox(JCheckBox checkbox) {
		String name = checkbox.getName();
		
		if (name != null) {
			String txt = SunsetBundle.getInstance().getProperty(name);
			
			if (txt != null) {
				checkbox.setText(txt);
			}
		}
	}

}
