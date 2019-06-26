package it.polito.tdp.food.model;

public class Adiacenza {

	private int condiment1;
	private int condiment2;
	private int count=0;
	
	public Adiacenza(int condiment1, int condiment2, int count) {
		super();
		this.condiment1 = condiment1;
		this.condiment2 = condiment2;
		this.count = count;
	}
	public int getCondiment1() {
		return condiment1;
	}
	public void setCondiment1(int condiment1) {
		this.condiment1 = condiment1;
	}
	public int getCondiment2() {
		return condiment2;
	}
	public void setCondiment2(int condiment2) {
		this.condiment2 = condiment2;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "Adiacenza [condiment1=" + condiment1 + ", condiment2=" + condiment2 + ", count=" + count + "]";
	}
	
	
}
