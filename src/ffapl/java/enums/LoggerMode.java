package ffapl.java.enums;

/**
 * Mode Enum für Logger
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public enum LoggerMode {
	RESULT(0),
	ERROR(1),//beinhaltet result
	WARNING(2),	//beinhaltet error
	ALL(3);//beinhaltet alles
	
	private Integer mode;
	
	private LoggerMode(Integer mode){
		this.mode = mode;
	}
	
	public Integer getMode(){
		return this.mode;
	}
	
}
