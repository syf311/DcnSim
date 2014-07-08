package sim.dcn.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sim.common.ValidationHelper;

public class Request {

	private NetworkComponent source;
	
	private NetworkComponent destination;
	
	private double bandWidthDemand;
	
	private int bookedDrationInCycles;
	
	private int startCycle;
	
	private List<Double> bandWidthAllocationsAtSource;	// storing band width allocations for all cycles starting from startCycle
	
	private List<Double> bandWidthAllocationsAtDestination;	// storing band width allocations for all cycles starting from startCycle
	
	private List<Request> requestsGroup;
	
	public Request(NetworkComponent source, NetworkComponent destination, double bandWidthDemand, int bookedDrationInCycles)
	{
		ValidationHelper.notNull(source, "source");
		ValidationHelper.notNull(destination, "destination");
		ValidationHelper.largerThanZero(bandWidthDemand, "bandWidthUsage");
		ValidationHelper.largerThanZero(bookedDrationInCycles, "bookedDrationInCycles");
		
		this.source = source;
		this.destination = destination;
		this.bandWidthDemand = bandWidthDemand;
		this.bookedDrationInCycles = bookedDrationInCycles;
		this.startCycle = -1;
		this.requestsGroup = new ArrayList<Request>(Arrays.asList(this));
	}
	
	public void AddRequestToRequestsGroup(Request request) {
		this.requestsGroup.add(request);
	}
	
	public NetworkComponent getSource() {
		return this.source;
	}
	
	public NetworkComponent getDestination() {
		return this.destination;
	}
	
	public double getBandWidthUsage() {
		return this.bandWidthDemand;
	}
	
	public int getBookedDurationInCycles() {
		return this.bookedDrationInCycles;
	}
	
	public List<Double> getBandWidthAllocationsAtSource() {
		return this.bandWidthAllocationsAtSource;
	}
	
	public List<Double> getBandWidthAllocationsAtDestination() {
		return this.bandWidthAllocationsAtDestination;
	}
	
	public int getStartCycle() {
		return this.startCycle;
	}
	
	public void assignBandWidthForCurrentCycleAtSource(double bandWidthAllocated) {
		if (this.bandWidthAllocationsAtSource == null) {
			this.bandWidthAllocationsAtSource = new ArrayList<Double>();
		}
		
		this.bandWidthAllocationsAtSource.add(bandWidthAllocated);
	}
	
	public void assignBandWidthForCurrentCycleAtDestination(double bandWidthAllocated) {
		if (this.bandWidthAllocationsAtDestination == null) {
			this.bandWidthAllocationsAtDestination = new ArrayList<Double>();
		}
		
		this.bandWidthAllocationsAtDestination.add(bandWidthAllocated);
	}
	
	public double getCurrentBandWidthAllocatedAtSource() {
		return ValidationHelper.isNullOrEmpty(this.bandWidthAllocationsAtSource) 
				? 0 : this.bandWidthAllocationsAtSource.get(this.bandWidthAllocationsAtSource.size() - 1);
	}
	
	public double getCurrentBandWidthAllocatedAtDestination() {
		return ValidationHelper.isNullOrEmpty(this.bandWidthAllocationsAtDestination) 
				? 0 : this.bandWidthAllocationsAtDestination.get(this.bandWidthAllocationsAtDestination.size() - 1);
	}
	
	public boolean isGroupOver() {
		boolean isOver = true;
		
		for (Request request : this.requestsGroup) {
			if (!request.isOver()) {
				isOver = false;
				break;
			}
		}
		
		return isOver;
	}
	
	@Override
	public String toString()
	{
		return String.format(
				"request source %s destination %s bandWidthDemand %d bookedDrationInCycles %f startCycle %f", 
				this.source,
				this.destination,
				this.bandWidthDemand,
				this.bookedDrationInCycles,
				this.startCycle);
	}
	
	private boolean isOver() {
		return this.isSourceOver() && this.isDestinationOver();
	}
	
	private boolean isSourceOver() {
		boolean isOverAtSource = false;
		if (this.bandWidthAllocationsAtSource != null) {
			double totalBandWidthAllocated = 0;
			for (Double bandWidthAllocatedPerCycle : this.bandWidthAllocationsAtSource) {
				totalBandWidthAllocated += bandWidthAllocatedPerCycle.doubleValue();
			}
			
			isOverAtSource = totalBandWidthAllocated >= (this.bandWidthDemand * this.bookedDrationInCycles);
		}
		
		return isOverAtSource;
	}
	
	private boolean isDestinationOver() {
		boolean isOverAtDestination = false;
		if (this.bandWidthAllocationsAtDestination != null) {
			double totalBandWidthAllocated = 0;
			for (Double bandWidthAllocatedPerCycle : this.bandWidthAllocationsAtDestination) {
				totalBandWidthAllocated += bandWidthAllocatedPerCycle.doubleValue();
			}
			
			isOverAtDestination = totalBandWidthAllocated >= (this.bandWidthDemand * this.bookedDrationInCycles);
		}
		
		return isOverAtDestination;
	}
}
