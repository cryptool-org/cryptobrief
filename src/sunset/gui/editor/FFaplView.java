/**
 * 
 */
package sunset.gui.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;

import sunset.gui.enums.LineState;
import sunset.gui.view.FFaplStatusLine;

import ffapl.java.matcher.FFaplMatcher;

/**
 * View for the FFaplEditor
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplView extends PlainView {

	private SortedMap<Integer, Integer> startMap;
	private SortedMap<Integer, Color> colorMap;
	private SortedMap<Integer, Integer> fontMap;
	private int _currentLine;
	private JTextPane _owner;
	
	/**
	 * 
	 * @param element
	 * @param owner
	 */
	public FFaplView(Element element, JTextPane owner) {
		super(element);
		_owner = owner;
		//System.out.println("newview");
		startMap = new TreeMap<Integer, Integer>();
		colorMap = new TreeMap<Integer, Color>();
	    fontMap = new TreeMap<Integer, Integer>();
	}
	
	@Override
	protected void drawLine(int lineIndex, Graphics g, int x, int y) {
		_currentLine = lineIndex;
		super.drawLine(lineIndex, g, x, y);
	}
	
	
	@Override
    protected int drawUnselectedText(Graphics graphics, int x, int y, int p0,
            int p1) throws BadLocationException {

        Document doc = getDocument();
      
        String text = doc.getText(p0, p1 - p0);
       
        Segment segment = getLineBuffer();
        int i = p0;
        int xStart = x;
        
        try{
        // Colour the parts
        for (Map.Entry<Integer, Integer> entry : startMap.entrySet()) {
            int key = entry.getKey();
        	int start = Math.min(Math.max(key, p0), p1);
            int end = Math.max(Math.min(entry.getValue(), p1), p0);
            
            
            if (i < start) {           	
                graphics.setColor(Color.black);
                graphics.setFont(new Font(
                		graphics.getFont().getName(), 
                		Font.PLAIN, 
                		graphics.getFont().getSize()));
                doc.getText(i, start - i, segment);
                x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
            }

            graphics.setColor(colorMap.get(key));
            graphics.setFont(new Font(
            		graphics.getFont().getName(), 
            		fontMap.get(key), 
            		graphics.getFont().getSize()));
            i = end;
           
            doc.getText(start, i - start, segment);
            
            x = Utilities.drawTabbedText(segment, x, y, graphics, this, start);
            
        }

        // Paint possible remaining text black
        if (i < p0 + text.length()) {
            graphics.setColor(Color.black);
            graphics.setFont(new Font(
            		graphics.getFont().getName(), 
            		Font.PLAIN, 
            		graphics.getFont().getSize()));
            doc.getText(i, (p0 + text.length()) - i, segment);
            x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
        }
        }catch(Exception e){
        	e.printStackTrace();
        }
        //System.out.println(_errorLine + " == " + _currentLine);
        if(_owner instanceof FFaplCodeTextPane){
	        /*if(((FFaplCodeTextPane)_owner).getErrorLine() == _currentLine){
	        	drawErrorLine(graphics, xStart, x, y, );
	        }*/
        	FFaplStatusLine sLine = ((FFaplCodeTextPane)_owner).getLineHandler().getStateOfLine(_currentLine);
        	if(sLine != null){
        		drawErrorLine(graphics, xStart, x, y, sLine);
        	}
        	
        }
        return x;
    }

	/**
	 * draws the error line
	 * @param graphics
	 * @param start
	 * @param end
	 * @param y
	 * @param sLine 
	 */
	private void drawErrorLine(Graphics graphics, int start, int end, int y, FFaplStatusLine sLine){
		int y1, y2, tmp;
		if(LineState.ERROR.equals(sLine.getState())){
			graphics.setColor(Color.red);
		}else if(LineState.WARNING.equals(sLine.getState())){
			graphics.setColor(new Color(255, 170, 0));
		}
		if(start/2 == 0){
			y1 = y + 1;
			y2 = y + 3;
		}else{
			y1 = y + 3;
			y2 = y + 1;
		}
		for (int i = start; i < end; i = i + 2){	    
		        graphics.drawLine(i, y1, i+2, y2);
		        tmp = y2;
		        y2 = y1;
		        y1 = tmp;
		}
	}
	
	public void updateSyntaxHighlighting(){
		
		String text;
	    Vector<Integer> toremove = new Vector<Integer>();
        int s1, s2, e1;
        
        startMap = new TreeMap<Integer, Integer>();
		colorMap = new TreeMap<Integer, Color>();
	    fontMap = new TreeMap<Integer, Integer>();
		//p0 = element.getStartOffset();
		//p1 = Math.min(getDocument().getLength(), element.getEndOffset());
		
		
			//System.out.println("p0:" + p0 + " p1:" + p1);
			try {
				//text = getDocument().getText(p0, p1 - p0);
				text = getDocument().getText(0, getDocument().getLength());
				// Match all regexes on this snippet, store positions
		        /*for (Map.Entry<Pattern, Color> entry : FFaplRegex.patternColors.entrySet()) {
		     
		            Matcher matcher = entry.getKey().matcher(text);

		            while (matcher.find()) {
		                //startMap.put(p0 + matcher.start(), p0 + matcher.end());
		                //colorMap.put(p0 + matcher.start(), entry.getValue());
		                //fontMap.put(p0 + matcher.start(),  FFaplRegex.patternFontWeight.get(entry.getKey().pattern()));
		            	    startMap.put(matcher.start(), matcher.end());
			                colorMap.put(matcher.start(), entry.getValue());
			                fontMap.put(matcher.start(),  FFaplRegex.patternFontWeight.get(entry.getKey().pattern()));
		            }
		        }*/
		        
		        for (FFaplMatcher matcher : FFaplRegex.additionalMatcher) {
		        	matcher.setText(text); 
		            while (matcher.find()) {
		            	    startMap.put(matcher.start(), matcher.end());
			                colorMap.put(matcher.start(), matcher.getColor());
			                fontMap.put(matcher.start(),  matcher.getFontWeight());
		            }
		        }
		        
		        
		        /*if(FFaplRegex.multiLinestartMap != null){
			    	startMap.putAll(FFaplRegex.multiLinestartMap);
			    	colorMap.putAll(FFaplRegex.multiLinecolorMap);
			    	fontMap.putAll(FFaplRegex.multiLinefontMap);
			    }*/ 
		        
		     // check the map for overlapping parts
		       for(Iterator<Integer> itr = startMap.keySet().iterator(); itr.hasNext(); ){
		        	s1 = itr.next();
		        	e1 = startMap.get(s1);
		        	if(!toremove.contains(s1)){
			        	for(Iterator<Integer> itrinner = startMap.keySet().iterator(); itrinner.hasNext(); ){
			        		s2 = itrinner.next();
			           // 	e2 = startMap.get(s2);
			            	//control full overlapping
			            	if(s1 < s2 && e1 > s2){
			            		toremove.add(s2);
			            	}//else if(s1 < s2 && e1 < e2 && e1 > s2){
			            	//	toremove.add(s2);
			            		//startMap.put(s1, s2);
			            		//e1 = s2; 
			            	//}
			            }
		        	}
		        } //remove overlapping parts
		        for(Iterator<Integer> itr = toremove.iterator(); itr.hasNext() ;){
		        	s1 = itr.next();
		        	startMap.remove(s1);
		        	colorMap.remove(s1);
		        	fontMap.remove(s1);
		        }
				
		        
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
