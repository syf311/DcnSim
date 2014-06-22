package sim.dcn.entity;

import sim.common.ValidationHelper;

public class Request {

	private NetworkComponent source;
	
	private NetworkComponent destination;
	
	private double bandWidthUsage;
	
	private int bookedDrationInCycles;
	
	private int remainingDurationInCycles;
	
	public Request(NetworkComponent source, NetworkComponent destination, double bandWidthUsage, int bookedDrationInCycles)
	{
		ValidationHelper.notNull(source, "source");
		ValidationHelper.notNull(destination, "destination");
		ValidationHelper.largerThanZero(bandWidthUsage, "bandWidthUsage");
		ValidationHelper.largerThanZero(bookedDrationInCycles, "bookedDrationInCycles");
		
		this.source = source;
		this.destination = destination;
		this.bandWidthUsage = bandWidthUsage;
		this.bookedDrationInCycles = bookedDrationInCycles;
		this.remainingDurationInCycles = bookedDrationInCycles;
	}
	
	public NetworkComponent getSource() {
		return this.source;
	}
	
	public NetworkComponent getDestination() {
		return this.destination;
	}
	
	public double getBandWidthUsage() {
		return this.bandWidthUsage;
	}
	
	public int getBookedDurationInCycles() {
		return this.bookedDrationInCycles;
	}
	
	public int getRemainingDurationInCycles() {
		return this.remainingDurationInCycles;
	}
	
	public void elapseOneCycle() {
		this.remainingDurationInCycles -= 1;
	}
	
	@Override
	public String toString()
	{
		return String.format(
				"request source %s destination %s bookedBandWidthUsage %d bookedDrationInCycles %f remainingDurationInCycles %d", 
				this.source,
				this.destination,
				this.bandWidthUsage,
				this.bookedDrationInCycles,
				this.remainingDurationInCycles);
	}
}
