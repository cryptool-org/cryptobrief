package sunset.gui.api.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import sunset.gui.api.spec.Parameter;
import sunset.gui.api.spec.ParameterList;
import sunset.gui.util.SunsetBundle;

public class FFaplTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 8674948161779073789L;
	private static String[] tableHeader = new String[]{SunsetBundle.getInstance().getProperty("thead_parameter"), SunsetBundle.getInstance().getProperty("thead_type")};
	
	private List<Parameter> parameterList;
	
	public FFaplTableModel(){
		parameterList = new ArrayList<Parameter>();
	}
	
	public void addParameter(Parameter param){
		parameterList.add(param);
	}
	
	@Override
	public void setValueAt(Object val, int i, int j){
		while(parameterList.size() < i){
			parameterList.add(new Parameter());
		}
		Parameter param = parameterList.get(i);
		if(j == 0){
			param.setName((String) val);
		}else{
			param.getType().clear();
			param.getType().add((String) val);
		}
	}
	
	@Override
	public String getColumnName(int i){
		return tableHeader[i];
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return parameterList.size();
	}

	@Override
	public Object getValueAt(int i, int j) {
		if(i < getRowCount()){
			if(j == 0){
				return parameterList.get(i).getName();
			}else{
				if(parameterList.get(i).getType().size() > 0){
					return parameterList.get(i).getType().get(0);
				}else{
					return null;
				}
			}
		}else{
			return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int i){
		return String.class;
	}
	
	@Override
	public boolean isCellEditable(int i, int j){
		return true;
	}
	
	public static void setUpTypeColumn(TableColumn column){
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("Integer");
		list.add("GF");
		list.add("String");
		list.add("Z()[]");
		list.add("Z()");
		list.add("Polynomial");
		Collections.sort(list);
		Java2sAutoTextField field = new Java2sAutoTextField(list);
		field.setStrict(false);
		field.setEditable(true);
		field.setText("");
		column.setCellEditor(new DefaultCellEditor(field));
		
	}

	public void addParameterList(ParameterList parameterList) {
		if(parameterList != null){
			this.parameterList.addAll(parameterList.getParameter());
		}
	}

	public void removeRow(int row) {
		if(row < getRowCount()){
			this.parameterList.remove(row);
		}
	}

}
