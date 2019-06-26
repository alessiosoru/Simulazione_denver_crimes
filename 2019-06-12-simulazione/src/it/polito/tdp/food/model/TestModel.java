package it.polito.tdp.food.model;

public class TestModel {
	public static void main(String[] args) {
		Model model =  new Model();
		model.creaGrafo(30);
		System.out.println("GRAFO CREATO!\nVERTCI: "+model.numVertici()+
				"\nARCHI: "+model.numArchi());
		for(Condiment c : model.listIngredientiOrdinatiPerCalorieDecr()) {
			System.out.println("INGREDIENTE: "+c.getDisplay_name()+
					"\nCalorie: "+model.getCalorieIngrediente(c.getFood_code())+
					"\n# cibi che lo contengono: "+model.getNumeroCibiIngrediente(c.getFood_code())+
					"\n");
		}
		
		Condiment c = model.listIngredientiOrdinatiPerCalorieDecr().get(5);
		System.out.println(" *** DIETA EQUILIBRATA che comprende "+c+" ***\n");
		for(Condiment cond : model.dietaEquilibrata(c)) {
			System.out.println(cond.toString()+"\n");
		}
		
		System.out.println("Calorie dieta: "+model.calorieDietaBest());
	}
}
