package sim.dcn.entity;

public final class Switch extends NetworkComponent {

	public Switch(int id) {
		super(id, null);
	}

	@Override
	public String getType() {
		return "Switch";
	}
}
