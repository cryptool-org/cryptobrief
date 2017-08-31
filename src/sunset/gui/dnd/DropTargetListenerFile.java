/**
 * 
 */
package sunset.gui.dnd;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

import sunset.gui.interfaces.IProperties;
import sunset.gui.panel.JPanelCode;
import sunset.gui.panel.JPanelTabTitle;
import sunset.gui.tabbedpane.JTabbedPaneCode;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class DropTargetListenerFile implements DropTargetListener {

	private JFrame _frame;
	private JTabbedPaneCode _tabbedPane;
	private Vector<Component> _undoComp;
	private Vector<Component> _redoComp;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;
	private JLabel _lineColumnPosition;
	
	public DropTargetListenerFile(JFrame frame, JTabbedPaneCode tabbedPane, Vector<Component> undoComp, Vector<Component> redoComp,
			Vector<Component> saveComp, Vector<Component> saveAllComp,
			JLabel lineColumnPosition) {
		_tabbedPane = tabbedPane;
		_undoComp = undoComp;
		_redoComp = redoComp;
		_saveComp = saveComp;
		_saveAllComp = saveAllComp;
		_frame = frame;
		_lineColumnPosition = lineColumnPosition;
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void drop(DropTargetDropEvent e) {
		 Transferable tr = e.getTransferable();
		 Object transferData;
		 List<?> files;
		 String title;
		 int idx;
		 JPanelCode panel;
		 boolean loaded;
		 File file;
		 
		 try
	      {

	         if ( tr.isDataFlavorSupported (DataFlavor.javaFileListFlavor) )
	         {
	            e.acceptDrop(DnDConstants.ACTION_LINK);
	            transferData = tr.getTransferData(DataFlavor.javaFileListFlavor);
	            if(transferData instanceof List<?>){
		            files = (List<?>) transferData;     
		            for(Iterator<File> itr = (Iterator<File>) files.iterator(); itr.hasNext(); ){
			            file = itr.next();
			            if(file.getName().endsWith(IProperties.FILEEXTENTION)){
			            	panel = new JPanelCode(_frame, _tabbedPane, _undoComp, _redoComp, _saveComp, _saveAllComp, _lineColumnPosition);
							loaded = panel.loadFile(file);
							if(loaded){
								title = file.getName();
								_tabbedPane.addTab(title, panel);
								idx = _tabbedPane.getTabCount() - 1;
								_tabbedPane.setTabComponentAt(idx, new JPanelTabTitle(_tabbedPane, _tabbedPane.getTitleAt(idx)));
								panel.setTabTitle((JPanelTabTitle) _tabbedPane.getTabComponentAt(idx));
								_tabbedPane.setSelectedIndex(idx);
							}
			            }
		            }
					
		            
		            e.getDropTargetContext().dropComplete(true);
	            }
	         }
	         else
	         {
	            //System.err.println ("DataFlavor.javaFileListFlavor is not supported, rejected");
	            e.rejectDrop();
	         } 
	      }catch (IOException ex)
	         {
	         }
	      catch (UnsupportedFlavorException ex)
	         {
	            System.err.println ("UnsupportedFlavorException");
	            ex.printStackTrace();
	            e.rejectDrop();
	       }


	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub

	}

}
