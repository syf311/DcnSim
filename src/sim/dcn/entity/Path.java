package sim.dcn.entity;

import sim.common.*;

public class Path {
	private Link sourceLink;
	
	private Link destinationLink;
	
	private Request request;
	
	private double bandWidthDemandOnThePath;
	
	private boolean isMinGuaranteed; 
	
	private double buyerBid;
	
	private double sellerAsk;
	
	private double realCost;
	
	public Path(
			Request request,  
			Link sourceLink,
			Link destinationLink,
			double bandWidthDemandOnThePath,
			boolean isMinGuaranteed) {
		ValidationHelper.notNull(request, "request");
		ValidationHelper.notNull(sourceLink, "sourceLink");
		ValidationHelper.notNull(destinationLink, "destinationLink");
		
		this.request = request;
		this.sourceLink = sourceLink;
		this.destinationLink = destinationLink;
		this.bandWidthDemandOnThePath = bandWidthDemandOnThePath;
		this.isMinGuaranteed = isMinGuaranteed;
	}
	
	public NetworkComponent getSource() {
		return this.request.getSource();
	}
	
	public NetworkComponent getDestination() {
		return this.request.getDestination();
	}
	
	public Request getRequest() {
		return this.request;
	}
	
	public Link getSourceLink() {
		return this.sourceLink;
	}
	
	public Link getDestinationLink() {
		return this.destinationLink;
	}
	
	public double getBandWidthDemandOnThePath() {
		return this.bandWidthDemandOnThePath;
	}
}
