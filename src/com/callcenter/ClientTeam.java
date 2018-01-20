package com.callcenter;
import java.util.ArrayList;
import java.util.List;

public class ClientTeam implements Comparable<ClientTeam>{
	
	private List<Employee> employees;
	private int minMaturity;
	private String name;
	
	private int extraMaturity;
	private int currentMaturity;
	
	public ClientTeam(String name, int maturity){
		this.employees = new ArrayList<Employee>();
		this.minMaturity = maturity;
		this.name = name;
	}
	
	public List<Employee> getEmployees() {
		return employees;
	}
	
	public void addEmployee(Employee e) {
		this.employees.add(e);
		this.updateCurrentMaturity();
	}
	
	public void setEmployees(ArrayList<Employee> employees) {
		this.employees = employees;
		this.updateCurrentMaturity();
	}
	
	public int getMinMaturity() {
		return minMaturity;
	}
	public void setMinMaturity(int maturity) {
		this.minMaturity = maturity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void updateCurrentMaturity() {
		int ret = 0;
		for (Employee e : this.employees) {
			ret += e.getpLevel();
		}
		this.currentMaturity = ret;
	}
	
	public int getCurrentMaturity() {
		return this.currentMaturity;
	}

	public int getExtraMaturity() {
		return this.getCurrentMaturity() - this.getMinMaturity();
	}
	
	public String toString() {
		String ret = this.name + " - Min. Maturity " + this.minMaturity + 
				" - Current Maturity " + this.getCurrentMaturity() + "\n";
		
		for (Employee e : this.employees) {
			ret += e.toString() + "\n";
		}
		
		return ret;
	}

	@Override
	public int compareTo(ClientTeam o) {
		return o.getMinMaturity() - this.getMinMaturity() ;
	}
}
