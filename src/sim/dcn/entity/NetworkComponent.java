package sim.dcn.entity;

import java.util.ArrayList;
import java.util.List;

import sim.common.ValidationHelper;

public abstract class NetworkComponent extends Entity {
	protected List<Link> links;
	
	abstract public String getType();
	
	public NetworkComponent(int id, List<Link> links)
	{
		super(id);
		this.links = links;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s %s", this.getType(), this.id);
	}
	
	public List<Link> getLinks() {
		return this.links;
	}
	
	public void addLink(Link link) {
		if (this.links == null) {
			this.links = new ArrayList<Link>();
		}
		
		this.links.add(link);
	}
	
	public Link getLocalLink() {
		Link localLink = null;
		if (!ValidationHelper.isNullOrEmpty(this.links)) {
			for (Link link : this.links) {
				if (link.isLocal()) {
					localLink = link;
					break;
				}
			}
		}
		
		return localLink;
	}
}
