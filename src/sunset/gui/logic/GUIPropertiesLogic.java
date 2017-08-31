package sunset.gui.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import sunset.gui.interfaces.IProperties;

public class GUIPropertiesLogic {

	private static GUIPropertiesLogic instance = new GUIPropertiesLogic();
	private static Properties properties;
	private static Properties systemProperties;
	
	public static GUIPropertiesLogic getInstance(){
		return instance;
	}
	
	private GUIPropertiesLogic(){
		try {
			checkProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getProperty(String key){
		return properties.getProperty(key);
	}
	
	public String getSystemProperty(String key){
		return systemProperties.getProperty(key);
	}
	
	public void setProperty(String key, String value){
		properties.setProperty(key, value);
	}
	
	public Boolean getBooleanProperty(String key){
		return Boolean.parseBoolean(getProperty(key));
	}
	
	public void setBooleanProperty(String key, Boolean value){
		setProperty(key, value.toString());
	}
	
	public Integer getIntegerProperty(String key){
		return Integer.parseInt(getProperty(key));
	}
	
	public void setIntegerProperty(String key, Integer value){
		setProperty(key, value.toString());
	}
	
	/**
	 * stores the properties in User directory
	 */
	public void storePropertyFile(){
		File dir = new File(IProperties.PROPERTYFILE_DIRPATH_USER);
		if(!dir.exists()){
			dir.mkdir();
		}
		File file = new File(IProperties.PROPERTYFILE_PATH_USER);
	
		try {
			properties.store(new FileOutputStream(file), "Sunset GUI properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkProperties() throws IOException{
		if(properties == null){
			initGUIProperties();
		}
	}
	
	/**
	 * init GUI properties
	 * @throws IOException 
	 */
	private void initGUIProperties() throws IOException{	
		InputStream myInputStream;
		try {
			File file = new File(IProperties.PROPERTYFILE_PATH_USER);
			loadDefaultGUIProperties();
			if (file.exists()) {
				myInputStream = new FileInputStream(file);
				properties.load(myInputStream);
				myInputStream.close();
			}else{
				//load initial install Data
				file = new File(IProperties.PROPERTYFILEPATHINSTALL);
				if (file.exists()) {
					myInputStream = new FileInputStream(file);
					properties.load(myInputStream);
					myInputStream.close();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException. Load default properties.");
		} catch (IOException e) {
			System.out.println("IO Exception in load properties. Load default properties.");
		}
	}
	

	
	/**
	 * load default properties
	 * @throws IOException 
	 */
	private void loadDefaultGUIProperties() throws IOException{
		 properties = new Properties();
		 BufferedInputStream stream = new BufferedInputStream(ClassLoader.getSystemResource(IProperties.PROPERTYFILEPATH).openStream()); 
	     properties.load(stream);
	    
	}
}
