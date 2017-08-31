package sunset.gui.panel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Locale;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import sunset.gui.FFaplJFrame;
import sunset.gui.editor.FFaplRegex;
import sunset.gui.interfaces.IFFaplComponent;
import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.util.LanguageUtil;
import sunset.gui.util.SunsetBundle;
import sunset.gui.util.TranslateGUIElements;
import ffapl.utils.FFaplProperties;


@SuppressWarnings("serial")
public class JPanelLanguage extends javax.swing.JPanel implements IFFaplComponent{
	private JLabel jLabel_Language;
	private JComboBox jComboBox_Language;
	private List<Locale>  _languages;
	private FFaplJFrame _frame;

	
	public JPanelLanguage(FFaplJFrame frame) {
		super();
		_frame = frame;
		initGUI();
		initLanguages();
		translate();
	}
	
	
	private void initGUI() {
		try {
			FlowLayout thisLayout = new FlowLayout();
			thisLayout.setAlignment(FlowLayout.LEFT);
			this.setLayout(thisLayout);
			setPreferredSize(new Dimension(400, 300));
			{
				jLabel_Language = new JLabel();
				this.add(jLabel_Language);
				jLabel_Language.setText("Choose Language:");
				//jLabel_Language.setFont(new java.awt.Font(IProperties.GUIFontFamily,0,12));
				//jLabel_Language.setPreferredSize(new java.awt.Dimension(104, 14));
				jLabel_Language.setName("label_chooselang");
			}
			{
				jComboBox_Language = new JComboBox();
				this.add(jComboBox_Language);
				//jComboBox_Language.setFont(new java.awt.Font(IProperties.GUIFontFamily,0,12));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initLanguages(){
		//_languages = XMLReader.getAvailableLanguages();
		//String[] langStr = new String[_languages.size()];
		/*Hashtable<String, String> lang;
		int i = 0;
		int selection = 0;
		for(Iterator<Hashtable<String, String>> itr = _languages.iterator(); itr.hasNext(); ){
			lang = itr.next();
			langStr[i] = lang.get(IFFaplLang.LANGUAGE);// + " V" + lang.get(IFFaplLang.VERSION);
			if(_properties.get(IProperties.LANGUAGE).equals(lang.get(IFFaplLang.ACRONYM))){
				selection = i;
			}
			i++;
		}*/
		_languages = LanguageUtil.getAvailableLanguages();
		String[] langStr = new String[_languages.size()];
		int i = 0;
		int selection = 0;
		for(Locale itr : _languages){
			langStr[i] = SunsetBundle.getInstance().getProperty("language", itr);
			if(SunsetBundle.getInstance().getProperty("acronym", itr).equals(GUIPropertiesLogic.getInstance().getProperty(IProperties.LANGUAGE))){
				selection = i;
			}
			i++;
		}
		
		ComboBoxModel jComboBox_LanguageModel = 
			new DefaultComboBoxModel(langStr);
		jComboBox_Language.setModel(jComboBox_LanguageModel);
		if(jComboBox_Language.getItemCount() > selection){
			jComboBox_Language.setSelectedIndex(selection);
		}
		
	}
	
	private void translate(){
		TranslateGUIElements.translateLabel(jLabel_Language);
		
	}


	@Override
	public void storeChanges() {
		if(jComboBox_Language.getSelectedIndex() >= 0){
			String acronym = _languages.get(jComboBox_Language.getSelectedIndex()).getLanguage();
			if(!acronym.equals(GUIPropertiesLogic.getInstance().getProperty(IProperties.LANGUAGE))){
				GUIPropertiesLogic.getInstance().setProperty(IProperties.LANGUAGE, acronym);
				FFaplProperties.getInstance().setLocale(new Locale(acronym));
				SunsetBundle.getInstance().setLocale(new Locale(acronym));
				FFaplRegex.init();
				_frame.initLanguage();
			}else{
				//nothing to store
			}
		}
	}

}
