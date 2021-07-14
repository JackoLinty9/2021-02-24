package it.polito.tdp.PremierLeague.model;

public class GiocatoreMigliore {
	
	Player p;
	Double delta;
	
	public GiocatoreMigliore(Player p, Double delta) {
		super();
		this.p = p;
		this.delta = delta;
	}
	
	public String toString() {
		return p.getPlayerID()+" - "+p.getName()+" - "+delta;
	}

}
