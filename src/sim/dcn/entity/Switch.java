package sim.dcn.entity;

import java.util.List;

public final class Switch extends NetworkComponent {

	public Switch(int id, List<Link> links) {
		super(id, links);
	}

	@Override
	public String getType() {
		return "Switch";
	}
}
