package sunset.gui.enums;

public enum LineState {
	ERROR(1),
	WARNING(2),
	BREAKPOINT(3),
	ACTIVE_DEBUG(4);
	
	private Integer code;

	private LineState(Integer code){
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}
}
