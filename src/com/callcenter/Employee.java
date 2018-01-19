package com.callcenter;

public class Employee {
	
	private String name;
	private int pLevel;
	private int birthYear;
	private int admissionYear;
	private int lastProgressionYear;
	
	public Employee(String name, int pLevel, int birthYear, int admissionYear, int lastProgressionYear) {
		this.name = name;
		this.pLevel = pLevel;
		this.birthYear = birthYear;
		this.admissionYear = admissionYear;
		this.lastProgressionYear = lastProgressionYear;
	}

	public int getPoints() {
		return 0;
	}


	public int getpLevel() {
		return pLevel;
	}


	public void setpLevel(int pLevel) {
		this.pLevel = pLevel;
	}


	public int getBirthYear() {
		return birthYear;
	}


	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}


	public int getAdmissionYear() {
		return admissionYear;
	}


	public void setAdmissionYear(int admissionYear) {
		this.admissionYear = admissionYear;
	}


	public int getLastProgressionYear() {
		return lastProgressionYear;
	}


	public void setLastProgressionYear(int lastProgressionYear) {
		this.lastProgressionYear = lastProgressionYear;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name + " - " + this.pLevel;
	}
}
