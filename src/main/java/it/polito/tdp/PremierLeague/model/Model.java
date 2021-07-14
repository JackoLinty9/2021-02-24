package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private SimpleDirectedWeightedGraph<Player,DefaultWeightedEdge> grafo;
	private Map<Integer, Player>idMap;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap=new TreeMap<Integer,Player>();
		this.dao.listAllPlayers(idMap);
	}
	
	public List<Match>listAllMatches() {
		return dao.listAllMatches();
	}
	
	public void creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo vertici "filtrati"
		Graphs.addAllVertices(this.grafo, dao.listAllVerteces(m,idMap));
		
		//aggiungo gli archi
		for(Arco a : dao.listAllEdge(m, idMap)) {
			if(a.getPeso() >= 0) {
				//p1 meglio di p2
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), 
							a.getP2(), a.getPeso());
				}
			} else {
				//p2 meglio di p1
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), 
							a.getP1(), (-1) * a.getPeso());
				}
			}
		}
	}

	public Integer getNVertici() {
		return grafo.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public GiocatoreMigliore giocatoreMigliore() {
		Player migliore=null;
		Double maxDelta = (double) Integer.MIN_VALUE;
		for(Player p: this.grafo.vertexSet()) {
			Double usc=0.0;
			Double ent=0.0;
			for(DefaultWeightedEdge d: this.grafo.outgoingEdgesOf(p))
				usc+=this.grafo.getEdgeWeight(d);
			for(DefaultWeightedEdge d: this.grafo.incomingEdgesOf(p))
				ent+=this.grafo.getEdgeWeight(d);
			if((usc-ent)>maxDelta) {
				maxDelta=(usc-ent);
				migliore=p;
			}
		}
	    return new GiocatoreMigliore(migliore,maxDelta);
	}
	
}
