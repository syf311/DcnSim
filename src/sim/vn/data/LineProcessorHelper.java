package sim.vn.data;

public class LineProcessorHelper {
	private static final int VehicleIdCol = 0;
	private static final int DateTimeCol = 1;
	private static final int LongitudeCol = 2;
	private static final int LatitutdeCol = 3;
	private static final int VelocityCol = 4;
	private static final int DirectionCol = 5;
	private static final int PassengerCol = 6;
	
	private LineProcessorHelper() {		
	}
	
	public static int getVehicleId(String[] columns) {
		return Integer.parseInt(columns[LineProcessorHelper.VehicleIdCol].trim().substring(1));
	}
	
	public static String getDateTimeString(String[] columns) {
		return columns[LineProcessorHelper.DateTimeCol].trim();
	}
	
	public static double getLongitude(String[] columns) {
		return Double.parseDouble(columns[LineProcessorHelper.LongitudeCol].trim());
	}
	
	public static double getLatitude(String[] columns) {
		return Double.parseDouble(columns[LineProcessorHelper.LatitutdeCol].trim());
	}
	
	public static int getVelocity(String[] columns) {
		return Integer.parseInt(columns[LineProcessorHelper.VelocityCol].trim());
	}
	
	public static int getDirection(String[] columns) {
		return Integer.parseInt(columns[LineProcessorHelper.DirectionCol].trim());
	}
	
	public static boolean getPassengerOnRide(String[] columns) {
		return Integer.parseInt(columns[LineProcessorHelper.PassengerCol].trim()) == 1;
	}
}
