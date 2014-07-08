package sim.dcn.setup;

import java.util.List;

import sim.dcn.entity.Request;
import sim.dcn.entity.VirtualMachine;

public class GroupRequestsGenerator extends RegularRequestsGenerator {
	
	public GroupRequestsGenerator(
			NumericValueGenerator requestBandWidthGenerator, 
			NumericValueGenerator requestCyclesGenerator) {
		super(requestBandWidthGenerator, requestCyclesGenerator, 1);
	}
	
	@Override
	public List<Request> generateRequests(VirtualMachine requestFrom) {
		List<Request> requestsGenerated = super.generateRequests(requestFrom);
		GroupRequestsGenerator.GroupRequesets(requestsGenerated);
		return requestsGenerated;
	}
	
	private static void GroupRequesets(List<Request> requests) {
		for (Request request1 : requests) {
			for (Request request2 : requests) {
				if (request1 != request2) {
					request1.AddRequestToRequestsGroup(request2);
				}
			}
		}
	}
}
