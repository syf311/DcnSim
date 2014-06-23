package sim.dcn.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sim.common.*;

public class Link {
	
	private NetworkComponent endPoint1;
	
	private NetworkComponent endPoint2;
	
	private double capacity;
	
	private List<Request> requests1To2;
	
	private List<Request> requests2To1;
	
	public Link(
			NetworkComponent endPoint1, 
			NetworkComponent endPoint2, 
			double capacity) {
		ValidationHelper.notNull(endPoint1, "endPoint1");
		ValidationHelper.notNull(endPoint2, "endPoint2");
		ValidationHelper.largerThanZero(capacity, "capacity");
		
		this.endPoint1 = endPoint1;
		this.endPoint2 = endPoint2;
		this.capacity = capacity;
		this.requests1To2 = new LinkedList<Request>();
		this.requests2To1 = new LinkedList<Request>();
	}
	
	public NetworkComponent getEndPoint1() {
		return this.endPoint1;
	}
	
	public NetworkComponent getEndPoint2() {
		return this.endPoint2;
	}
	
	public double capacity() {
		return this.capacity;
	}
	
	public double getConsumedBandWidth() {
		return this.getConsumedBandWidth1To2() + this.getConsumedBandWidth2To1();
	}
	
	public double getConsumedBandWidth1To2()
	{
		double consumedBandWidth = 0;
		for (Request request : this.requests1To2) {
			consumedBandWidth += request.getBandWidthUsage();
		}
		
		return consumedBandWidth;
	}
	
	public double getConsumedBandWidth2To1()
	{
		double consumedBandWidth = 0;
		for (Request request : this.requests2To1) {
			consumedBandWidth += request.getBandWidthUsage();
		}
		
		return consumedBandWidth;
	}
	
	public void RunOneCycle()
	{
		Link.RunOneCycle(this.requests1To2, this);
		Link.RunOneCycle(this.requests2To1, this);
	}
	
	public void SendRequest1To2(Request request) {
		Logger.getLogger(Link.class.getName()).log(Level.INFO, String.format("Sending %s over %s", request, this));
		this.requests1To2.add(request);
	}
	
	public void SendRequest2To1(Request request) {
		Logger.getLogger(Link.class.getName()).log(Level.INFO, String.format("Sending %s over %s", request, this));
		this.requests2To1.add(request);
	}
	
	public boolean isEndPoint1(NetworkComponent endPoint) {
		return this.endPoint1 == endPoint;
	}
	
	public boolean isEndPoint2(NetworkComponent endPoint) {
		return this.endPoint2 == endPoint;
	}
	
	public NetworkComponent getTheOtherEndPoint(NetworkComponent endPoint) {
		ValidationHelper.notNull(endPoint, "endPoint");
		NetworkComponent theOtherEndPoint = null;
		if (this.endPoint1 == endPoint) {
			theOtherEndPoint = this.endPoint2;
		}
		else if (this.endPoint2 == endPoint) {
			theOtherEndPoint = this.endPoint1;
		}
		else {
			throw new IllegalStateException(String.format("%s did not match any end point on %s", endPoint, this));
		}
		
		return theOtherEndPoint;
	}
	
	@Override
	public String toString()
	{
		return String.format(
				"Link endPoint1 %s, endPoint2 %s, capacity %f, consumedBandWidth1To2 %f, consumedBandWidth2To1 %f, totalConsumedBandWidth %f", 
				this.endPoint1, 
				this.endPoint2,
				this.capacity,
				this.getConsumedBandWidth1To2(),
				this.getConsumedBandWidth2To1(),
				this.getConsumedBandWidth());
	}
	
	private static void RunOneCycle(List<Request> requests, Link link) {
		Logger.getLogger(Link.class.getName()).log(Level.INFO, String.format("Running one cycle"));
		for (Request request : requests) {
			request.elapseOneCycle();
			if (request.getRemainingDurationInCycles() == 0) {
				Logger.getLogger(Link.class.getName()).log(Level.INFO, String.format("%s was due and removed from %s", request, link));
				requests.remove(request);
			}
		}
	}
}
