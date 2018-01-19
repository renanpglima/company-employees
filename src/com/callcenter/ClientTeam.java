package com.callcenter;
import java.util.ArrayList;
import java.util.List;

public class ClientTeam {
	
	private List<Employee> employees;
	private int minMaturity;
	private String name;
	
	public ClientTeam(String name, int maturity) {
		this.employees = new ArrayList<Employee>();
		this.minMaturity = maturity;
		this.name = name;
	}
	
	public List<Employee> getEmployees() {
		return employees;
	}
	
	public void addEmployee(Employee e) {
		this.employees.add(e);
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
	
	public int getCurrentTeamMaturity() {
		int ret = 0;
		for (Employee e : this.employees) {
			ret += e.getpLevel();
		}
		return ret;
	}
	
	public String toString() {
		String ret = this.name + " - Min. Maturity " + this.minMaturity + 
				" - Current Maturity " + this.getCurrentTeamMaturity() + "\n";
		
		for (Employee e : this.employees) {
			ret += e.toString() + "\n";
		}
		
		return ret;
	}
}
