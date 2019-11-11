package sunset.gui.api.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import sunset.gui.api.spec.Parameter;
import sunset.gui.util.StringUtil;

public class FFaplTableRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 365379101721925543L;

	public FFaplTableRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component c =  super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
		if(row == (table.getModel().getRowCount() - 1) && column == 0){
			String cellVal = (String) table.getModel().getValueAt(row, column);
			if(!StringUtil.getInstance().isBlank(cellVal)){
				((FFaplTableModel) table.getModel()).addParameter(new Parameter());
				table.repaint();
			}
		}else if(row < (table.getModel().getRowCount() - 1)){
			String cellVal1 = (String) table.getModel().getValueAt(row, 0);
			//String cellVal2 = (String) table.getModel().getValueAt(row, 1);
			if(StringUtil.getInstance().isBlank(cellVal1)){
				((FFaplTableModel) table.getModel()).removeRow(row);
				table.repaint();
			}
		}
		return c;
	}

}
