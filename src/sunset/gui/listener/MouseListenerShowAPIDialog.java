/**
 * 
 */
package sunset.gui.listener;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import sunset.gui.FFaplJFrame;
import sunset.gui.api.IMutableTreeNodeAPI;
import sunset.gui.api.MutableTreeNodeApiEntry;
import sunset.gui.api.MutableTreeNodeHead;
import sunset.gui.api.jaxb.Snippet;
import sunset.gui.dialog.JDialogAPI;
import sunset.gui.dialog.JDialogAPICode;
import sunset.gui.interfaces.IProperties;
import sunset.gui.util.SunsetBundle;

/**
 * Opens a Dialog
 * 
 * @author Alexander Ortner
 * @version 1.0
 * 
 */
public class MouseListenerShowAPIDialog implements MouseListener {

	private FFaplJFrame _frame;
	private JDialogAPI _dialog;
	private JDialogAPICode _code;

	/**
	 * 
	 * @param frame
	 */
	public MouseListenerShowAPIDialog(FFaplJFrame frame) {
		_frame = frame;
		_dialog = new JDialogAPI(_frame, null);
		_code = new JDialogAPICode(_frame, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JTree tree;
		Object node;
		if (e.getSource() instanceof JTree) {
			tree = (JTree) e.getSource();
			if (tree != null && tree.getSelectionPath() != null) {
				node = tree.getClosestPathForLocation(e.getX(), e.getY()).getLastPathComponent();
				if (node instanceof MutableTreeNodeApiEntry) {
					MutableTreeNodeApiEntry entry = (MutableTreeNodeApiEntry) node;
					if (e.getClickCount() > 1  && !entry.isCustom()) {
						_dialog.reInit(entry);
						// _dialog.setModal(true);
						_dialog.setVisible(true);
					}else if (e.getClickCount() > 1  && entry.isCustom()) {
						_code.reInit((Snippet) entry.getEntry());
						_code.setVisible(true);						
					}else if((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0 && entry.isCustom()) {
						JPopupMenu popup = new JPopupMenu();
						JMenuItem mItem = new JMenuItem(SunsetBundle.getInstance().getProperty("menuitem_delete"));
						mItem.setIcon(new ImageIcon(getClass()
										.getClassLoader().getResource(
												IProperties.IMAGEPATH + "delete16.png")));
						mItem.addActionListener(new ActionListenerDeleteApiEntry(_frame, (Snippet) entry.getEntry()));
						
						JMenuItem editItem = new JMenuItem(SunsetBundle.getInstance().getProperty("button_edit"));
						editItem.setIcon(new ImageIcon(getClass()
										.getClassLoader().getResource(
												IProperties.IMAGEPATH + "edit16.png")));
						ActionListenerEditApiEntry al = new ActionListenerEditApiEntry(_frame, null);
						al.setSnippet((Snippet) entry.getEntry());
						editItem.addActionListener(al);
						
						popup.add(editItem);
						popup.add(mItem);					
						popup.show(tree, e.getX(), e.getY());
						
					}
				}else if(node instanceof MutableTreeNodeHead){
					if((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) {
						JPopupMenu popup = new JPopupMenu();
						JMenuItem mItem = new JMenuItem(SunsetBundle.getInstance().getProperty("menuitem_add"));
						mItem.setIcon(new ImageIcon(getClass()
										.getClassLoader().getResource(
												IProperties.IMAGEPATH + "new16.png")));
						mItem.addActionListener(new ActionListenerEditApiEntry(_frame, null));
						popup.add(mItem);
						popup.show(tree, e.getX(), e.getY());
						
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
