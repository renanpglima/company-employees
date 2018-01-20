package com.callcenter;

public class Employee implements Comparable<Employee>{
	
	private String name;
	private int pLevel;
	private int birthYear;
	private int admissionYear;
	private int lastProgressionYear;
	
	private int points;
	
	public Employee(String name, int pLevel, int birthYear, int admissionYear, int lastProgressionYear) {
		this.name = name;
		this.pLevel = pLevel;
		this.birthYear = birthYear;
		this.admissionYear = admissionYear;
		this.lastProgressionYear = lastProgressionYear;
	}

	public void updatePoints(int year) {
		
		if (this.pLevel == 5) {
			this.points = -1;
			return;
		}
		
		int companyTime = year - admissionYear;
		if (companyTime < 1) {
			this.points = -1;
			return;
		}
		
		int timeWhitoutProg = year - lastProgressionYear;
		if (this.pLevel == 4 && timeWhitoutProg < 2) {
			this.points = -1;
			return;
		}
		int age = year - birthYear;
		
		int companyTimePoints = companyTime * 2;
		int timeWPPoints = timeWhitoutProg*3;
		int agePoints = ((int)(age/5))*1; 
		
		this.points = companyTimePoints + 
				timeWPPoints +
				agePoints;
	}
	
	public int getPoints() {
		return this.points;
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

	public void promote() {
		if (this.pLevel < 5)
			this.pLevel++;
	}

	@Override
	public int compareTo(Employee o) {
		return o.pLevel - this.pLevel;
	}
}
