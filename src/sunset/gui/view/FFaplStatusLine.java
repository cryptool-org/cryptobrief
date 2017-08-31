package sunset.gui.view;

import sunset.gui.enums.LineState;

public class FFaplStatusLine {

	private Integer line;
	private Integer column;
	private LineState state;
	
	public FFaplStatusLine(Integer line, Integer column, LineState state){
		this.setLine(line);
		this.setColumn(column);
		this.setState(state);		
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(Integer line) {
		this.line = line;
	}

	/**
	 * @return the line
	 */
	public Integer getLine() {
		return line;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(Integer column) {
		this.column = column;
	}

	/**
	 * @return the column
	 */
	public Integer getColumn() {
		return column;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(LineState state) {
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public LineState getState() {
		return state;
	}
}
