package sunset.gui.editor;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import sunset.gui.api.jaxb.ApiSpecification;
import sunset.gui.api.jaxb.Function;
import sunset.gui.api.jaxb.Procedure;
import sunset.gui.logic.ApiLogic;
import ffapl.java.matcher.CommentMatcher;
import ffapl.java.matcher.FFaplMatcher;
import ffapl.java.matcher.FuncProcMatcher;
import ffapl.java.matcher.RegexMatcher;
import ffapl.java.matcher.StringMatcher;

public class FFaplRegex {

	public static List<FFaplMatcher> additionalMatcher;
	
	public static Color GREEN = new Color(63, 127, 95);
	public static Color BLUE  = new Color(42, 0, 255);
	public static Color LILA = new Color(127, 0, 85);
	public static Color BLACK = new Color(0, 0, 0);
	
	private static String TAG_PROGRAM = "\\bprogram\\b";
	private static String TAG_PRIMITIVETYPE = "(\\bInteger\\b)|(\\bBoolean\\b)|(\\bString\\b)";
	private static String TAG_ALGEBRAICTYPE = "(\\bPrime\\b)|(\\bPolynomial\\b)|(\\bEC\\b)";
	private static String TAG_CONTAINERTYPE = "(\\bRecord\\b)|(\\bEndRecord\\b)|(\\bSameAs\\b)|(\\bBaseGF\\b)|(\\bBaseZ\\b)";
	private static String TAG_COMPLEXTYPE = "(\\bGF\\b)|(\\bPseudoRandomGenerator\\b)|(\\bRandomGenerator\\b)|(\\bZ\\b)";
	private static String TAG_LITERAL = "(\\bRandom\\b)|(\\bfalse\\b)|(\\btrue\\b)|(\\bPAI\\b)|(\\bRandomPoint\\b)|(\\bRandomPointSubfield\\b)";
	private static String TAG_CONST = "\\bconst\\b";
	private static String TAG_PROCFUNC = "(\\bprocedure\\b)|(\\bfunction\\b)";
	private static String TAG_OPERATION = "(\\bAND\\b)|(\\bOR\\b)|(\\bXOR\\b)|(\\bMOD\\b)|(\\bAND\\b)";
	private static String TAG_FOR = "(\\bfor\\b)|(\\bto\\b)|(\\bstep\\b)";
	private static String TAG_WHILE = "(\\bwhile\\b)";
	private static String TAG_IF = "(\\bif\\b)|(\\belse\\b)";
	private static String TAG_CREATION = "(\\bnew\\b)";
	private static String TAG_ARRAYLEN = "(#)";
	private static String TAG_RETURN = "(\\breturn\\b)|(\\bbreak\\b)";
	//private static String TAG_STRING = "\"([^\"])*\"";
	
     
    /**
     * inits the regex
     */
    public static void init(){
    	// NOTE: the order is important!
    	String regex = "";
    	int i = 0;
    	//load regex for Functions
    	additionalMatcher = new ArrayList<FFaplMatcher>();
    	ApiSpecification api = ApiLogic.getInstance().getApiSpecification();
    	for (Function func : api.getFunctionList().getFunction()){
    		if(i > 0){
    			regex = regex + "|";
    		}
    		regex = regex + "(\\b" +func.getRegex() + "\\s{0,}\\()";
    		i++;
    	}
    	// load regex for Procedures
    	for (Procedure proc : api.getProcedureList().getProcedure()){
    		if(i > 0){
    			regex = regex + "|";
    		}
    		regex = regex + "(\\b" + proc.getRegex() + "\\s{0,}\\()";
    		i++;
    	}
    	FuncProcMatcher fpMatcher = new FuncProcMatcher(regex);
    	additionalMatcher.add(fpMatcher);
    	     
        additionalMatcher.add(new RegexMatcher(TAG_PROGRAM + "|" +
        									   TAG_LITERAL + "|" +
        									   TAG_PRIMITIVETYPE + "|" +
        									   TAG_ALGEBRAICTYPE + "|" +
        									   TAG_CONTAINERTYPE + "|" +
        									   TAG_COMPLEXTYPE + "|" +
        									   TAG_CONST + "|" +
        									   TAG_PROCFUNC + "|" +
        									   TAG_OPERATION + "|" +
        									   TAG_FOR + "|" +
        									   TAG_WHILE + "|" +
        									   TAG_IF + "|" +
        									   TAG_CREATION + "|" +
        									   TAG_RETURN
        										,Font.BOLD, LILA));
             
        additionalMatcher.add(new RegexMatcher(TAG_ARRAYLEN,Font.PLAIN, LILA));
      
        additionalMatcher.add(new StringMatcher(Font.PLAIN, BLUE));
             
        additionalMatcher.add(new CommentMatcher("//", null));
        
        additionalMatcher.add(new CommentMatcher("/*", "*/"));
        
        additionalMatcher.add(new CommentMatcher("/**", "*/", Font.PLAIN, BLUE));
       
    }    
	
}
