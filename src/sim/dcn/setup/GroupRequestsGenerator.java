package sim.dcn.setup;

import java.util.List;

import sim.dcn.entity.Request;
import sim.dcn.entity.VirtualMachine;

// A group requests means the request source will generate a request 
// to all other virtual machines in the group and also these requests
// will be synchronized in the way that only when all the requests are
// finished do we consider the group of requests are finished.
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
