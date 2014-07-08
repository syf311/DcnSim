package sim.dcn.entity;

import java.util.ArrayList;
import java.util.List;

import sim.common.ValidationHelper;
import sim.dcn.setup.RequestsGenerator;

public final class VirtualMachine extends NetworkComponent {

	private Server server;
	
	private Tenant tenant;
	
	private double minimumGauraneedBandwidthPerLink;
	
	private List<Request> requests;
	
	private int groupId;
	
	private RequestsGenerator requestGenerator;
	
	public VirtualMachine(
			int id, 
			Tenant tenant, 
			double minimumGauraneedBandwidthPerLink, 
			int groupId, 
			RequestsGenerator requestGenerator) {
		super(id, null);
		ValidationHelper.notNull(tenant, "tenant");
		ValidationHelper.notNull(requestGenerator, "requestGenerator");
		this.tenant = tenant;
		this.minimumGauraneedBandwidthPerLink = minimumGauraneedBandwidthPerLink;
		this.requests = new ArrayList<Request>();
		this.groupId = groupId;
	}

	@Override
	public String getType() {
		return "VirtualMachine";
	}
	
	public void deployToServer(Server server) {
		ValidationHelper.notNull(server, "server");
		this.server = server;
		this.links = server.getLinks();
		server.addVirtualMachine(this);
	}
	
	public Server getServer() {
		return this.server;
	}
	
	public Tenant getTenant() {
		return this.tenant;
	}
	
	public double getMinimumGauraneedBandwidthPerLink() {
		return this.minimumGauraneedBandwidthPerLink;
	}
	
	public void generateRequests() {
		List<Request> unfinishedRequests = this.getUnfinishedRequests();
		List<Request> requestsGenerated = null;
		if (ValidationHelper.isNullOrEmpty(unfinishedRequests)) {
			// all requests finished, we should simply generate new ones and add them to the processing queue
			requestsGenerated = this.requestGenerator.generateRequests(this);
			
		} else {
			// we have unfinished requests here, hence we should 
			// filter out those destinations that previous requests not done yet
			// ideally, for group requests, any unfinished member request will result in the group unfinished 
			// and hence all the members will appear in the unfinished requests list.
			// And for regular requests, the unfinished request will be filtered out.
			requestsGenerated = this.requestGenerator.generateRequests(this);
			
			List<Request> requestsToFilterOut = new ArrayList<Request>();
			for (Request newRequest : requestsGenerated) {
				for (Request unfinishedRequest : unfinishedRequests) {
					if (newRequest.getDestination() == unfinishedRequest.getDestination()) {
						requestsToFilterOut.add(newRequest);
					}
				}
			}
			
			requestsGenerated.removeAll(requestsToFilterOut);
		}
		
		if (!ValidationHelper.isNullOrEmpty(requestsGenerated)) {
			this.requests.addAll(requestsGenerated);
		}
	}
	
	public List<Request> getRequests() {
		return this.requests;
	}
	
	public int getGroupId() {
		return this.groupId;
	}
	
	public List<Request> getUnfinishedRequests() {
		List<Request> unfinishedRequests = new ArrayList<Request>();
		
		if (!ValidationHelper.isNullOrEmpty(this.requests)) {
			for (Request request : this.requests) {
				if (!request.isGroupOver()) {
					unfinishedRequests.add(request);
				}
			}
		}
		
		return unfinishedRequests;
	}
}
