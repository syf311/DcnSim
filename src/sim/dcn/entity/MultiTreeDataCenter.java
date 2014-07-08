package sim.dcn.entity;

import java.util.ArrayList;

import sim.common.ValidationHelper;

public final class MultiTreeDataCenter extends DataCenter {

	public MultiTreeDataCenter(String[] arguments) {
		if (arguments.length != 3) {
			throw new IllegalArgumentException("Wrong number of arguments to build a MultiTree topology data center");
		}
		
		int leafCount = Integer.parseInt(arguments[0]);
		int foreighLinksPerLeaf = Integer.parseInt(arguments[1]);
		double defaultLinkBandWidthCapacity = Double.parseDouble(arguments[2]);
		
		this.initialize(leafCount, foreighLinksPerLeaf, defaultLinkBandWidthCapacity);
	}
	
	public MultiTreeDataCenter(int leafCount, int foreighLinksPerLeaf, double defaultLinkBandWidthCapacity) {
		this.initialize(leafCount, foreighLinksPerLeaf, defaultLinkBandWidthCapacity);
	}
	
	// Note that ideally parent link's bandwidth should be at least of sum of all the bandwidth of its children's links
	// but we are not implementing this detail here at this moment and just assume it is true for the setup.
	private void initialize(int leafCount, int foreighLinksPerLeaf, double defaultLinkBandWidthCapacity) {
		ValidationHelper.powerOfTwo(leafCount, "leafCount");
		if (foreighLinksPerLeaf > (leafCount/2 - 1)) {
			throw new IllegalArgumentException("foreighLinksPerLeaf");
		}
		
		this.servers = new ArrayList<Server>();
		this.switches = new ArrayList<Switch>();
		this.links = new ArrayList<Link>();

		// create servers, switches, and local links
		// switches are created top down and left to right per level with Id starting from 1
		this.switches.add(new Switch(1));
		for (int i = 1; i < leafCount; i++) {
			Switch theSwitch = this.getSwitch(i);
			NetworkComponent networkComponentLeft = null;
			NetworkComponent networkComponentRight = null;
			if (i < leafCount/2) {
				networkComponentLeft = new Switch(i * 2);
				networkComponentRight = new Switch(i * 2 + 1);
				this.switches.add((Switch)networkComponentLeft);
				this.switches.add((Switch)networkComponentRight);
			}
			else {
				// servers are created left to right with Id starting from 1
				networkComponentLeft = new Server(this.servers.size() + 1);
				networkComponentRight = new Server(this.servers.size() + 2);
				this.servers.add((Server)networkComponentLeft);
				this.servers.add((Server)networkComponentRight);
			}
			
			// local links
			Link linkToLeft = new Link(theSwitch, networkComponentLeft, defaultLinkBandWidthCapacity, true);
			Link linkToRight = new Link(theSwitch, networkComponentLeft, defaultLinkBandWidthCapacity, true);
			this.links.add(linkToLeft);
			this.links.add(linkToRight);
			
			theSwitch.addLink(linkToLeft);
			theSwitch.addLink(linkToRight);
			networkComponentLeft.addLink(linkToLeft);
			networkComponentRight.addLink(linkToRight);
		}
		
		// create foreign links
		for (int i = 0; i < leafCount; i++) {
			Server server = this.servers.get(i);
			for (int j = 1; j <= foreighLinksPerLeaf; j++) {
				int idOfForeignSwitch = (server.getId() + 2 * j) % leafCount;
				NetworkComponent theSwitch = this.getSwitch(idOfForeignSwitch);
				Link link = new Link(server, theSwitch, defaultLinkBandWidthCapacity, false);
				server.addLink(link);
				theSwitch.addLink(link);
				this.links.add(link);
			}
		}
	}
}
