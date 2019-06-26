package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private Graph<Condiment, DefaultWeightedEdge> grafo;
	private List<Condiment> listaCondimenti;
	private Map<Integer, Condiment> condimentIdMap;
	private FoodDao dao;
	
	// FOOD code può essere lo stesso per porzioni diverse
	// alcuni food possono essere anche condiment
	
	
	private List<Condiment> dietaBest;
	private Double totCalorieMax=0.0;
	
	public Model() {
		dao = new FoodDao();
		condimentIdMap = new HashMap<Integer, Condiment>();
	}

	public void creaGrafo(Integer calorie) {
		listaCondimenti = dao.listCondimentByCal(this.condimentIdMap, calorie);
		
//		for(Condiment c: this.condimentIdMap.values()) {
//			System.out.println(c.toString());
//		}
//		
		this.grafo = new SimpleWeightedGraph<Condiment, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, listaCondimenti);
		
//		for(Condiment c: this.grafo.vertexSet()) {
//			System.out.println(c.toString());
//		}
		
		List<Adiacenza> archi = dao.getAdiacenze(calorie);
		
		for(Adiacenza a: archi) {
//			System.out.println(a.getCondiment1()+" - "+a.getCondiment2());
			if(this.condimentIdMap.containsKey(a.getCondiment1()) &&
					this.condimentIdMap.containsKey(a.getCondiment2())) {
				// Attenzione! Controllo su ripetizione arco già fatto nella query dao
				// con fc1.condiment_food_code<fc2.condiment_food_code
//				System.out.println("enta");
				Graphs.addEdge(this.grafo, this.condimentIdMap.get(a.getCondiment1()),
						 this.condimentIdMap.get(a.getCondiment2()), a.getCount());
				
			}
		}
		
//		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
//			System.out.println(e);
//		}
	}
	
	public Double getCalorieIngrediente(Integer condimentFoodCode) {
		return this.condimentIdMap.get(condimentFoodCode).getCondiment_calories();
	}
	
	public int getNumeroCibiIngrediente(Integer condimentFoodCode) {
		// troppo codice su java, chiedo direttamente al db
//		int count=0;
//		Map<Integer, Food> foods = new HashMap<Integer, Food>();
//		for(DefaultWeightedEdge e : this.grafo.edgesOf(this.condimentIdMap.get(condimentFoodCode))) {
//			if(foods.containsKey(this.grafo.getEdgeSource(e))count = (int) (count+this.grafo.getEdgeWeight(e));
//		}
//		return count;
		return dao.getNumeroCibiIngrediente(condimentFoodCode);
	}
	
	public List<Condiment> listIngredientiOrdinatiPerCalorieDecr(){
		Collections.sort(this.listaCondimenti);
		return this.listaCondimenti;
	}
	
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}

	public int numVertici() {
		return this.grafo.vertexSet().size();
	}

	public List<Condiment> getCondimentTraSelezionati() {
		return this.listaCondimenti;
	}
	
	/*
	 * RICORSIONE
	 * 
	 * Soluzione parziale: Lista di Condiment indipendenti
	 * 	(lista di vertici non adiacenti a due a due)
	 * Livello della ricorsione: lunghezza della lista
	 * Casi terminali: non trova altri vertici da aggiungere -> Verifica se il cammino
	 * ha max calorie totali tra quelli visto fino ad ora
	 * Generazione delle soluzioni: dato un vertice, aggiungo un vertice
	 * non adiacente e non ancora parte del percorso
	 * 
	 */
	
	public List<Condiment> dietaEquilibrata(Condiment ingrediente){
		
		// VARIABILI PER RICORSION
		List<Condiment> parziale = new ArrayList<Condiment>();
		this.dietaBest = new ArrayList<Condiment>();
		// il primo elemento della lista è sempre l'ingrediente selezionato
		// l'ordine non conta
		parziale.add(ingrediente);
		Double totCalorie = ingrediente.getCondiment_calories();
		cerca(1, parziale, totCalorie);
		
		return this.dietaBest;
	}

	
	// GLI INGREDIENTI DEVONO ESSERE A DUE A DUE INDIPENDENTI O TRA TUTTI?
	// NON ADIACENTI SOLO COL PRECEDENTE (1) O CON TUTTI GLI ALTRI(2)?
	// NEL SECONDO CASO DEVO OGNI VOLTA CICLARE SU PARZIALE E VERIFICARE
	// CHE NON SIANO ADIACENTI
	
//	 CASO 1
	private void cerca(int livello, List<Condiment> parziale, Double totCalorie) {
		
		boolean trovato = false;
		// genera nuove soluzioni
		Condiment ultimo = parziale.get(livello-1);
//		System.out.println(ultimo.toString());
		for(Condiment c: this.grafo.vertexSet()) {
			if(!parziale.contains(c) && // non è già presente nella soluzione
				!Graphs.neighborListOf(this.grafo, ultimo).contains(c)) { // non è adiacente
				
				trovato = true;
//				System.out.println(c.toString());
				parziale.add(c);
//				totCalorie = totCalorie + c.getCondiment_calories();
				cerca(livello+1, parziale, totCalorie+ c.getCondiment_calories());
				parziale.remove(c);
//				totCalorie = totCalorie - c.getCondiment_calories();
			}
		}
		
		if(!trovato) {
			if(totCalorie>this.totCalorieMax) {
				this.dietaBest = new ArrayList<Condiment>(parziale);
				this.totCalorieMax = totCalorie;
			}
		}
		
	}
	
	
//	// CASO 2 (corretto?)
//	private void cerca(int livello, List<Condiment> parziale, Double totCalorie) {
//		
//		boolean trovato = false;
//		// genera nuove soluzioni
//		Condiment ultimo = parziale.get(livello-1);
////		System.out.println(ultimo.toString());
//		for(Condiment c: this.grafo.vertexSet()) {
//			boolean adiacente = false;
//			if(!parziale.contains(c)) {
//				for(Condiment condim : parziale) {
//					if(Graphs.neighborListOf(this.grafo, condim).contains(c)) {
//						adiacente = true;
//						break;
//					}
//				}
//				if(!adiacente) {					
//					trovato = true;
////					System.out.println(c.toString());
//					parziale.add(c);
////					totCalorie = totCalorie + c.getCondiment_calories();
//					cerca(livello+1, parziale, totCalorie+ c.getCondiment_calories());
//					parziale.remove(c);
////					totCalorie = totCalorie - c.getCondiment_calories();
//				}
//				
//			}
//		}
//		
//		if(!trovato) {
//			if(totCalorie>this.totCalorieMax) {
//				this.dietaBest = new ArrayList<Condiment>(parziale);
//				this.totCalorieMax = totCalorie;
//			}
//		}
//	}
		

	public Double calorieDietaBest() {
		return this.totCalorieMax;
	}
}
