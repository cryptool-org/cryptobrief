package sunset.gui.tabbedpane;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class JTabbedPaneNamed extends JTabbedPane {
	
	private Map<Integer, String> tabNameMapping;
	private int index = 0;
	
	public JTabbedPaneNamed() {
		super();
		tabNameMapping = new HashMap<Integer, String>();
	}
	
	/**
	 * Allows to specify a name for each tab
	 * Adds the name of each tab into a map
	 * @param title the title of the tab
	 * @param icon the icon of the tab
	 * @param component the component of the tab
	 * @param tip the tool tip of the tab
	 * @param name the name of the tab
	 */
	public void addTab(String title, Icon icon, Component component, String tip, String name) {
        super.addTab(title, icon, component, tip);
        tabNameMapping.put(index, name);
        index++;
    }
	
	/**
	 * Returns the name of the tab at position i
	 * @param i the index of the tab
	 * @return the name of the tab at index i
	 */
	public String getTabNameAt(int i) {
		if (tabNameMapping.containsKey(i))
			return tabNameMapping.get(i);
		
		return null;
	}
}
