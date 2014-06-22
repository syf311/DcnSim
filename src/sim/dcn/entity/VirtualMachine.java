package sim.dcn.entity;

import java.util.List;

public final class VirtualMachine extends NetworkComponent {

	public VirtualMachine(int id, List<Link> links) {
		super(id, links);
	}

	@Override
	public String getType() {
		return "VirtualMachine";
	}
}
