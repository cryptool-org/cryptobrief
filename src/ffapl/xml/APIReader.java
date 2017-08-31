/**
 * 
 */
package ffapl.xml;


import java.io.File;
import java.util.Collections;
import java.util.Vector;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import sunset.gui.api.APIFunction;
import sunset.gui.api.APIProcedure;
import sunset.gui.api.APIType;
import sunset.gui.api.Parameter;
import sunset.gui.interfaces.IProperties;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class APIReader {
	public static Vector<APIFunction> APIFunction = new Vector<APIFunction>();
	public static Vector<APIProcedure> APIProcedure = new Vector<APIProcedure>();
	public static Vector<APIFunction> APISampleFunction = new Vector<APIFunction>();
	public static Vector<APIProcedure> APISampleProcedure = new Vector<APIProcedure>();
	public static Vector<APIType> APIType = new Vector<APIType>();
	private static final String _apiDir = IProperties.getInstallPath() + "apis/";
	
	/**
	 * loads the API for the specified language
	 * @param language
	 */
	public static void loadAPI(String language){
		Builder parser = new Builder();
		Document doc = null;
		Element root;
		
		String filepath = "FFaplAPI" + language.toUpperCase() + ".xml";
		
		File file = new File(_apiDir + filepath);
		try {
			/*url = XMLReader.class.getClassLoader().getResource(_languageDir + filepath);
			if(url == null){
				url = XMLReader.class.getClassLoader().getResource(_languageDir + _defaultErrorFile);
			}
			doc = parser.build(url);
			*/
			if(!file.exists()){
				doc = parser.build(_apiDir + "FFaplAPIEN.xml");
			}else{
				doc = parser.build(_apiDir + filepath);
			}
			
			root = doc.getRootElement();
			
			APIReader.APIFunction = loadAPIFunction(root);
			APIReader.APIProcedure = loadAPIProcedure(root);
			APIReader.APIType = loadAPIType(root);
			APIReader.APISampleFunction = loadAPIFunction(root.getFirstChildElement("samples"));
			APIReader.APISampleProcedure = loadAPIProcedure(root.getFirstChildElement("samples"));
			//sort Vectors
			Collections.sort(APIReader.APISampleProcedure);
			Collections.sort(APIReader.APISampleFunction);
			Collections.sort(APIReader.APIFunction);
			Collections.sort(APIReader.APIProcedure);
			Collections.sort(APIReader.APIType);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to load language file: " + filepath);
		}

	}
	
	/**
	 * loads the API for procedures
	 * @param root
	 * @return
	 */
	private static Vector<APIProcedure> loadAPIProcedure(Element root){
		Element c1, tmp;
		Elements cat, parameters, types;
		Vector<APIProcedure> result = new Vector<APIProcedure>();
		APIProcedure pr;
		Parameter p;
			cat = root.getChildElements("procedure");
			for(int i = 0; i < cat.size(); i++){
				pr = new APIProcedure();
				c1 = cat.get(i);
				pr.setName(c1.getFirstChildElement("name").getValue());
				pr.setDescription(c1.getFirstChildElement("description").getValue());
				pr.setRegex(c1.getFirstChildElement("regex").getValue());
				parameters = c1.getFirstChildElement("parameters").getChildElements("parameter");
				tmp = c1.getFirstChildElement("code");
				if(tmp != null){
					pr.setCode(tmp.getValue());
				}
				for(int j = 0; j < parameters.size(); j++){
					p = new Parameter(parameters.get(j).getFirstChildElement("name").getValue());
					types = parameters.get(j).getChildElements("type");
					for(int k = 0; k < types.size(); k++){
						p.addType(types.get(k).getValue());
					}
					pr.addParameter(p);
				}
				result.add(pr);
			}				
		return result;
	}
	
	/**
	 * loads the API for Functions
	 * @param root
	 * @return
	 */
	private static Vector<APIFunction> loadAPIFunction(Element root){
		Element c1, tmp;
		Elements cat, parameters, types;
		Vector<APIFunction> result = new Vector<APIFunction>();
		APIFunction f;
		Parameter p;
			cat = root.getChildElements("function");
			for(int i = 0; i < cat.size(); i++){
				f = new APIFunction();
				c1 = cat.get(i);
				f.setName(c1.getFirstChildElement("name").getValue());
				f.setDescription(c1.getFirstChildElement("description").getValue());
				f.setRegex(c1.getFirstChildElement("regex").getValue());
				f.setReturnType(c1.getFirstChildElement("return").getValue());
				tmp = c1.getFirstChildElement("code");
				if(tmp != null){
					f.setCode(tmp.getValue());
				}
				parameters = c1.getFirstChildElement("parameters").getChildElements("parameter");
				
				for(int j = 0; j < parameters.size(); j++){
					p = new Parameter(parameters.get(j).getFirstChildElement("name").getValue());
					types = parameters.get(j).getChildElements("type");
					for(int k = 0; k < types.size(); k++){
						p.addType(types.get(k).getValue());
					}
					f.addParameter(p);
				}
				result.add(f);
			}			
		return result;
	}
	
	/**
	 * loads the API for types
	 * @param root
	 * @return
	 */
	private static Vector<APIType> loadAPIType(Element root){
		Element c1;
		Elements cat;
		Vector<APIType> result = new Vector<APIType>();
		APIType f;
			cat = root.getChildElements("type");
			for(int i = 0; i < cat.size(); i++){
				f = new APIType();
				c1 = cat.get(i);
				f.setName(c1.getFirstChildElement("name").getValue());
				f.setDescription(c1.getFirstChildElement("description").getValue());
				f.setRegex(c1.getFirstChildElement("regex").getValue());
				result.add(f);
			}			
		return result;
	}
	

}
