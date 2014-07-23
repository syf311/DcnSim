package sim.vn.entity;

import java.util.Calendar;

public class Beam {
	private int vehicleId;
	
	private long theSecondInPeriod;
	
	private double longitude;
	
	private double latitude;
	
	private int velocity;
	
	private int direction;
	
	private boolean passengerOnRide;
	
	private Calendar datetime;
	
	public Beam(
			int vehicleId, 
			long theSecondInPeriod,
			double longitude,
			double latitude,
			int velocity,
			int direction,
			boolean passengerOnRide,
			Calendar datetime) {
		this.vehicleId = vehicleId;
		this.theSecondInPeriod = theSecondInPeriod;
		this.longitude = longitude;
		this.latitude = latitude;
		this.velocity = velocity;
		this.direction = direction;
		this.datetime = datetime;
	}
	
	public static Beam getBeamForSearch(long theSecondInPeriod) {
		return new Beam(-1, theSecondInPeriod, -1, -1, -1, -1, false, null);
	}
	
	public Calendar getDateTime() {
		return this.datetime;
	}
	
	public int getVehicleId() {
		return this.vehicleId;
	}
	
	public long getTheSecond() {
		return this.theSecondInPeriod;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public int getVelocity() {
		return this.velocity;
	}
	
	public int getDirection() {
		return this.direction;
	}
	
	public boolean getPassengerOnRide() {
		return this.passengerOnRide;
	}
}
