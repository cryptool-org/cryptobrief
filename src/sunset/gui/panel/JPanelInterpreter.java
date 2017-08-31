package sunset.gui.panel;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import sunset.gui.FFaplJFrame;
import sunset.gui.interfaces.IFFaplComponent;
import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.util.TranslateGUIElements;
import ffapl.java.enums.LoggerMode;
import ffapl.java.util.LoggerUtil;


@SuppressWarnings("serial")
public class JPanelInterpreter extends javax.swing.JPanel implements IFFaplComponent{
	private JLabel jLabel_Warning;
	private JCheckBox jcheckBoxWarning;
	private FFaplJFrame _frame;

	
	public JPanelInterpreter(FFaplJFrame frame) {
		super();
		_frame = frame;
		initGUI();
		translate();
	}
	
	
	private void initGUI() {
		try {
			FlowLayout thisLayout = new FlowLayout();
			thisLayout.setAlignment(FlowLayout.LEFT);
			this.setLayout(thisLayout);
			setPreferredSize(new Dimension(400, 300));
			{
				jcheckBoxWarning = new JCheckBox();
				jcheckBoxWarning.setSelected(LoggerUtil.getLoggerMode().getMode() >= LoggerMode.WARNING.getMode());
				this.add(jcheckBoxWarning);
				//jComboBox_Language.setFont(new java.awt.Font(IProperties.GUIFontFamily,0,12));
			}
			{
				jLabel_Warning = new JLabel();
				this.add(jLabel_Warning);
				jLabel_Warning.setText("Show warnings?");
				//jLabel_Language.setFont(new java.awt.Font(IProperties.GUIFontFamily,0,12));
				//jLabel_Language.setPreferredSize(new java.awt.Dimension(104, 14));
				jLabel_Warning.setName("label_show_warning");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void translate(){
		TranslateGUIElements.translateLabel(jLabel_Warning);	
	}


	@Override
	public void storeChanges() {
		if(jcheckBoxWarning.isSelected()){
			if(LoggerUtil.getLoggerMode().getMode() < LoggerMode.WARNING.getMode()){
				GUIPropertiesLogic.getInstance().setIntegerProperty(IProperties.LOGGER_MODE, LoggerMode.WARNING.getMode());
			}
		}else{
			GUIPropertiesLogic.getInstance().setIntegerProperty(IProperties.LOGGER_MODE, LoggerMode.ERROR.getMode());
		}
	}

}
