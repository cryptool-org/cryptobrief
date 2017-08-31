package ffapl.java.matcher;

import java.awt.Color;
import java.awt.Font;

public class FFaplMatcher {

	private String text;
	private int start;
	private int end;
	private int fontWeight;
	private Color color;
	
	public FFaplMatcher(){
		this.text = "";
		this.start = 0;
		this.end = 0;
		this.fontWeight = Font.PLAIN;
		this.color = Color.BLACK;
	}
	
	public boolean find(){
		return false;
	}
	
	protected void setStart(int start){
		this.start = start;
	}
	
	protected void setEnd(int end){
		this.end = end;
	}
	
	public int start(){
		return start;
	}
	
	public int end(){
		return end;
	}

	public void setFontWeight(int fontWeight) {
		this.fontWeight = fontWeight;
	}

	public int getFontWeight() {
		return fontWeight;
	}
	
	public void setText(String text) {
		this.text = text;
		this.start = 0;
		this.end = 0;
	}

	public String getText() {
		return text;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
}
