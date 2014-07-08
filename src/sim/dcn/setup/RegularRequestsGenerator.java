package sim.dcn.setup;

import java.util.ArrayList;
import java.util.List;

import sim.common.ValidationHelper;
import sim.dcn.entity.Request;
import sim.dcn.entity.Tenant;
import sim.dcn.entity.VirtualMachine;

public class RegularRequestsGenerator implements RequestsGenerator {
	
	private NumericValueGenerator requestBandWidthGenerator;
	
	private NumericValueGenerator requestCyclesGenerator;
	
	// use this value to control how many virtual machines to be involved in the requests
	// e.g., interval = 8 with virtual machines = 16 means vi will send a request to v(i+8)%16 only
	//       interval = 1 with virtual machines = 16 means vi will send a request to all other virtual machines
	private int interval;
	
	public RegularRequestsGenerator(
			NumericValueGenerator requestBandWidthGenerator, 
			NumericValueGenerator requestCyclesGenerator, 
			int interval) {
		ValidationHelper.notNull(requestBandWidthGenerator, "requestBandWidthGenerator");
		ValidationHelper.notNull(requestCyclesGenerator, "requestCyclesGenerator");
		this.requestBandWidthGenerator = requestBandWidthGenerator;
		this.requestCyclesGenerator = requestCyclesGenerator;
		this.interval = interval;
	}
	
	@Override
	public List<Request> generateRequests(VirtualMachine requestFrom) {
		ValidationHelper.notNull(requestFrom, "requestFrom");
		
		List<Request> requestsGenerated = new ArrayList<Request>();
		Integer groupId = requestFrom.getGroupId();
		Tenant tenant = requestFrom.getTenant();
		
		int groupCount = tenant.getGroupCount(groupId);
		
		int requestFromId = requestFrom.getId(); 
		int requestToId = (requestFromId + this.interval) % groupCount;
		
		while (requestToId != requestFromId) {
			VirtualMachine requestTo = tenant.getVirtualMachine(groupId, requestToId);
			Request request = new Request(
					requestFrom, 
					requestTo, 
					this.requestBandWidthGenerator.nextDoubleValue(), 
					(int)this.requestCyclesGenerator.nextDoubleValue());
			
			requestsGenerated.add(request);
			requestToId = (requestToId + this.interval) % groupCount;
		}
		
		return requestsGenerated;
	}
}
