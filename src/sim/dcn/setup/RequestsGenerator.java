package sim.dcn.setup;

import java.util.List;

import sim.dcn.entity.Request;
import sim.dcn.entity.VirtualMachine;

public interface RequestsGenerator {
	List<Request> generateRequests(VirtualMachine requestFrom);
}
