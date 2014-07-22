package sim.dcn.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import sim.common.ValidationHelper;

public class Request {

	private static AtomicInteger globalRequestId = new AtomicInteger(0);
	
	private int requestId;
	
	private NetworkComponent source;
	
	private NetworkComponent destination;
	
	private double bandWidthDemand;
	
	private int bookedDrationInCycles;
	
	private int startCycle;
	
	// storing band width allocations for all cycles starting from startCycle
	// this could be a sum-up over all source links
	private List<Double> bandWidthAllocationsAtSource;	
	
	// storing band width allocations for all cycles starting from startCycle
	// this could be a sum-up over all destination links
	private List<Double> bandWidthAllocationsAtDestination;	
	
	private List<Request> requestsGroup;
	
	private List<Path> pathsAssigned;
	
	private List<Request> splitSubRequests;
	
	private int parentRequestId;
	
	public Request(
			NetworkComponent source, 
			NetworkComponent destination, 
			double bandWidthDemand, 
			int bookedDrationInCycles,
			int parentRequestId) {
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
		this.requestId = Request.globalRequestId.incrementAndGet();
		this.parentRequestId = parentRequestId;
	}
	
	public void assignPaths(List<Path> paths) {
		ValidationHelper.notNullOrEmpty(paths, "paths");
		
		if (paths.size() == 1) {
			this.pathsAssigned = paths;
		} else {
			// split case
			this.splitSubRequests = new ArrayList<Request>(paths.size());
			for (Path path : paths) {
				Request subRequest = new Request(
								this.source, 
								this.destination, 
								path.getBandWidthDemandOnThePath(), 
								this.bookedDrationInCycles,
								this.requestId);
				subRequest.assignPaths(Arrays.asList(path));
				
				this.splitSubRequests.add(subRequest);
			}
		}
	}
	
	public void addRequestToRequestsGroup(Request request) {
		this.requestsGroup.add(request);
	}
	
	public NetworkComponent getSource() {
		return this.source;
	}
	
	public NetworkComponent getDestination() {
		return this.destination;
	}
	
	public double getBandWidthDemand() {
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
	
	public boolean isStarted() {
		return this.startCycle != -1;
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
	public String toString() {
		return String.format(
				"parent request id %d request id %d source %s destination %s bandWidthDemand %d bookedDrationInCycles %f startCycle %f",
				this.parentRequestId,
				this.requestId,
				this.source,
				this.destination,
				this.bandWidthDemand,
				this.bookedDrationInCycles,
				this.startCycle);
	}
	
	private void assignBandWidthForCurrentCycleAtSource(double bandWidthAllocated) {
		if (this.bandWidthAllocationsAtSource == null) {
			this.bandWidthAllocationsAtSource = new ArrayList<Double>();
		}
		
		this.bandWidthAllocationsAtSource.add(bandWidthAllocated);
	}
	
	private void assignBandWidthForCurrentCycleAtDestination(double bandWidthAllocated) {
		if (this.bandWidthAllocationsAtDestination == null) {
			this.bandWidthAllocationsAtDestination = new ArrayList<Double>();
		}
		
		this.bandWidthAllocationsAtDestination.add(bandWidthAllocated);
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
