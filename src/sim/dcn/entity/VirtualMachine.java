package sim.dcn.entity;

import sim.common.ValidationHelper;

public final class VirtualMachine extends NetworkComponent {

	private Server server;
	
	public VirtualMachine(int id) {
		super(id, null);
	}

	@Override
	public String getType() {
		return "VirtualMachine";
	}
	
	public void DeployToServer(Server server) {
		ValidationHelper.notNull(server, "server");
		this.server = server;
		this.links = server.getLinks();
	}
	
	public Server getServer() {
		return this.server;
	}
}
