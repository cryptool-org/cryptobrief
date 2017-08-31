package sunset.gui.api;

import java.util.Iterator;
import java.util.Vector;


/**
 * 
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class APIProcedure implements Comparable<APIProcedure>{

	/* the Attributes of the Procedure */
	protected Vector<Parameter> _parameters;
	
	protected String _name;
	
	protected String _description;
	
	protected String _regex;
	
	protected String _code;
	
	public APIProcedure(){
		_parameters = new Vector<Parameter>(0,1);
		_code = "";
	}
	
	/**
	 * Adds parameter to the procedure
	 * @param parameter
	 */
	public void addParameter(Parameter parameter){
		_parameters.add(parameter);
	}
	
	/**
	 * sets the description
	 * @param description
	 */
	public void setDescription(String description){
		_description = description;
	}
	
	/**
	 * sets the regex
	 * @param regex
	 */
	public void setRegex(String regex){
		_regex = regex;
	}
	
	/**
	 * sets the name
	 * @param name
	 */
	public void setName(String name){
		_name = name;
	}
	
	/**
	 * sets the code 
	 * @param code
	 */
	public void setCode(String code){
		_code = code;
	}
	
	/**
	 * @return the code
	 */
	public String getCode(){
		return _code;
	}
	
	/**
	 * 
	 * @return the representation
	 */
	public String getRepresentation(){
		String result = _name + "(";
		int i = 0;
		int k = 1;
		Parameter p;
		for (Iterator<Parameter> itr = _parameters.iterator(); itr.hasNext(); ){
			p = itr.next();
			if(i > 0){
				result = result + ", ";
			}
			
			//j = 0;
			//types = "";
			if(p.getTypes().size() == 1){
				result = result + p.getTypes().firstElement();
			}else if(p.getTypes().size() > 1){
				result = result + "Type" + k;
				k++;
			}
			/*for(Iterator<String> itrType = p.getTypes().iterator(); itrType.hasNext(); ){
				if(j == 1){
					types = "[" + types;
				}
				if(j > 0){
					types = types + " | ";
				}
				types = types + itrType.next();
				j++;
			}
			if(j > 1){
				types = types + "]";
			}
			result = result + types;*/
			i++;
		}
		if(k == 2){
			result = result.replace("Type1", "Type");
		}
		return result + ")";
	}
	
	/**
	 * 
	 * @return the name
	 */
	public String getName(){
		return _name;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription(){
		return _description;
	}
	
	/**
	 * 
	 * @return the regex
	 */
	public String getRegex(){
		return _regex;
	}
	
	/**
	 * 
	 * @return the usage code
	 */
	public String getUsageCode(){
		String result;
		int i = 0;
		Parameter p;
		if(_code != null && _code.trim().equals("")){
			result = _name + "(";
			for (Iterator<Parameter> itr = _parameters.iterator(); itr.hasNext(); ){
				p = itr.next();
				if(i > 0){
					result = result + ", ";
				}
				result = result + p.getName();
				
				i++;
			}
			return result + ")";
		}else{
			return _code;
		}
	}
	
	/**
	 * 
	 * @param txt will be added to end of procedure
	 * @return the usage formatted in HTML
	 */
	public String getHTMLUsage(String txt){
		String result, types;
		int i = 0 , j;
		int k = 1;
		Parameter p;
		Vector<String> typeAcronym = new Vector<String>(0,1);
		result = "<i>" + _name + "</i>(";
		for (Iterator<Parameter> itr = _parameters.iterator(); itr.hasNext(); ){
			p = itr.next();
			if(i > 0){
				result = result + ", ";
			}
			j = 0;
			types = "";
			for(Iterator<String> itrType = p.getTypes().iterator(); itrType.hasNext(); ){
				if(j == 1){
					types = "[" + types;
				}
				if(j > 0){
					types = types + " | ";
				}
				types = types + "<i>" + itrType.next() + "</i>";
				j++;
			}
			if(j > 1){
				types = types + "]";
			}
			
			result = result + "<i>" + p.getName() + "</i> : ";
			
			if(p.getTypes().size() == 1){
				result = result + types;
			}else if(p.getTypes().size() > 1){
				result = result + "<i>Type" + k + "</i>";
				typeAcronym.add("<i>Type" + k + "</i> := " + types);
				k++;
			}
			
			i++;
		}
		result = result + ")" + txt;
		if(k > 1){
			result = result + "<br/>";
		}
		for(Iterator<String> itrType = typeAcronym.iterator(); itrType.hasNext(); ){
			result = result + "<br/>" +  itrType.next();
		}
		if(k == 2){
			result = result.replace("Type1", "Type");
		}
		return result;
	}
	
	/**
	 * 
	 * @return the parameters
	 */
	public Vector<Parameter> getParameters(){
		return _parameters;
	}
	
	@Override
	public String toString(){
		return _name;
	}

	/**
	 * @return the info of the Procedure in html format
	 */
	public String htmlInfo() {
		String info = "<html>" + getHTMLUsage("") + "<br/><br/>" +
		   _description + "</html>";
		return this.replaceHTMLTags(info);
	}
	
	/**
	 * replace the tags with html tags
	 * @param val
	 * @return html formated string
	 */
	protected String replaceHTMLTags(String val){
		val = val.replace("{/b}", "</b>");
		val = val.replace("{b}", "<b>");
		val = val.replace("{/i}", "</i>");
		val = val.replace("{i}", "<i>");
		val = val.replace("{/u}", "</u>");
		val = val.replace("{u}", "<u>");
		val = val.replace("{br}", "<br/>");
		return val;
	}
	
	@Override
	public int compareTo(APIProcedure arg) {
		return _name.compareTo(arg.getName());
	}
	
}
