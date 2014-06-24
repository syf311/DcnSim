package sim.common;

public class ExceptionHelper {
	private ExceptionHelper() {
		
	}
	
	public static String toString(Exception ex) {
		ValidationHelper.notNull(ex, "ex");
		StringBuffer exceptionDetails = new StringBuffer();
		exceptionDetails.append(ex.toString() + "\r\n");
		StackTraceElement[] stackTraceElements = ex.getStackTrace();
		for (StackTraceElement element : stackTraceElements) {
			exceptionDetails.append(element.toString() + "\r\n");
		}
		
		return exceptionDetails.toString();
	}
}
