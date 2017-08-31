/**
 * 
 */
package sunset.gui.api;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class APIFunction extends APIProcedure{

	protected String _returnType;
	

	/**
	 * sets the returnType
	 * @param returnType
	 */
	public void setReturnType(String returnType){
		_returnType = returnType;
	}
	
	/**
	 * @return the return type
	 */
	public String getReturnType(){
		return _returnType;
	}
	
	@Override
	public String toString(){
		return _name;
	}

	/**
	 * the info of the Procedure in html format
	 */
	public String htmlInfo() {
		String info = "<html><b>function</b> " + getHTMLUsage(" : <b>" + _returnType + "</b>") + "<br/><br/>" +
		   _description + "</html>";
		return this.replaceHTMLTags(info);
	}

	
	

}
