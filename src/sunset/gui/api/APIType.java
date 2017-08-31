/**
 * 
 */
package sunset.gui.api;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class APIType implements Comparable<APIType>{

	
	private String _name;
	
	private String _description;
	
	private String _regex;

	
	/**
	 * 
	 */
	public APIType() {
		// TODO Auto-generated constructor stub
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
	
	@Override
	public String toString(){
		return _name;
	}
	
	/**
	 * @return returns type info formatted in html
	 */
	public String htmlInfo(){
		String result = "<html>" + _name + "<br/><br/>" +
			   _description + "</html>";
		return replaceHTMLTags(result);
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
	public int compareTo(APIType arg) {
		return _name.compareTo(arg.getName());
	}
	
}
