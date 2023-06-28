package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao ;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private List<Business> vertici;
	
	//ricorsione
	private List<Business> tour ;
	private int maxLunghezza; 
	private Business source;
	private Business target;
	
	
	public Model() {
		
		this.dao = new YelpDao();
		this.grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}
	
	
	public List<String> getCity() {
		return this.dao.getCity();
	}
	
	
	public void loadNodes(String city) {
		
		if (this.vertici.isEmpty()) {
			this.vertici = this.dao.getFiltereBusiness(city);
		}
	}
	
	public void clearGrafo() {
		this.grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.vertici = new ArrayList<>();
	}
	
	public void creaGrafo(String city) {
		
		clearGrafo();
		loadNodes(city);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		
		for (Business b1 : this.grafo.vertexSet()) {
			for (Business b2 : this.grafo.vertexSet()) {
				if (!b1.equals(b2)) {
					LatLng pos1 = new LatLng(b1.getLatitude(), b1.getLongitude());
					LatLng pos2 = new LatLng(b2.getLatitude(), b2.getLongitude());
					double peso = LatLngTool.distance(pos1, pos2, LengthUnit.KILOMETER);
					Graphs.addEdgeWithVertices(this.grafo, b1, b2, peso);
				}
			}
		}
	}
	
	public BusinessDistance getLocaleDistante(Business b) {
		
		BusinessDistance buss = null;
		double bestDistance = 0.0;
		
		for (DefaultWeightedEdge edge : this.grafo.outgoingEdgesOf(b)) {
			double currentPeso = this.grafo.getEdgeWeight(edge);
			if (currentPeso > bestDistance) {
				bestDistance = currentPeso;
				Business objective = Graphs.getOppositeVertex(this.grafo, edge, b);
				buss = new BusinessDistance(objective, bestDistance);
			}
		}
		
		return buss;
		
	}
	
	public List<Business> getVertici() {
		return this.vertici;
	}
	
	public int numArchi()  {
		return this.grafo.edgeSet().size();
	}
	
	
	//ricorsione
	public List<Business> getTour(Business b1, Business b2, double x) {
		
		this.target = b2;
		this.source = b1;
		this.tour = new ArrayList<Business>();
		this.maxLunghezza = 0;
		
		//dominio --> tutti i vertici che pero hanno arco maggiore della media 
		List<Business> dominioSource = new ArrayList<>(Graphs.neighborListOf(this.grafo, this.source));
		List<Business> parziale = new ArrayList<>();
		parziale.add(this.source);
		ricorsione(parziale, dominioSource, x);
		return this.tour;
		
	}


	private void ricorsione(List<Business> parziale, List<Business> dominioSource, double x) {
		// TODO Auto-generated method stub
		
		Business currentB = parziale.get(parziale.size()-1);
		if (currentB.equals(this.target)) {
			if (parziale.size()-1 >= this.maxLunghezza) {
				this.tour = new ArrayList<>(parziale);
				this.maxLunghezza  =parziale.size();
				return;
			}
		}
		
		if (parziale.size() > this.maxLunghezza) {
			this.tour = new ArrayList<>(parziale);
			this.maxLunghezza  =parziale.size();
		}
		
		List<Business> dom = new ArrayList<>();
		for (Business b : dominioSource) {
			double recensione = b.getStars();
			if (b.equals(this.target) && !parziale.contains(b)) {
				parziale.add(b);
				dom = Graphs.neighborListOf(this.grafo, b);
				dom.removeAll(parziale);
				ricorsione(parziale, dom, x);
				
			}
			else if (!parziale.contains(b) && recensione > x) {
				parziale.add(b);
				dom = Graphs.neighborListOf(this.grafo, b);
				dom.removeAll(parziale);
				ricorsione(parziale, dom, x);
			}
			
		}
		
		
		
	}


	public double kmPercorsi(List<Business> parziale) {
		double dTot = 0.0;
		for (int i = 1; i < parziale.size(); i++) {
			LatLng current = new LatLng(parziale.get(i).getLatitude(), parziale.get(i).getLongitude());
			LatLng precedente = new LatLng(parziale.get(i-1).getLatitude(), parziale.get(i-1).getLongitude());
			double distanza = LatLngTool.distance(current, precedente, LengthUnit.KILOMETER);
			dTot += distanza;
		}
		
		return dTot;
	}
	
	
}
