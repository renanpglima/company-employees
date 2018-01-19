package com.callcenter.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.callcenter.ClientTeam;
import com.callcenter.Employee;
import com.callcenter.exceptions.FileFormatException;

public class FileHelper {
	public static ArrayList<ClientTeam> loadClientTeams(String csvFile) 
			throws FileFormatException, FileNotFoundException, IOException{
		
		ArrayList<ClientTeam> team = new ArrayList<ClientTeam>();
		
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		br.readLine(); //jumping first line
		String line = "";
		
		while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            
            if (parts.length != 2) {
            	br.close();
            	throw new FileFormatException();
            }
            
            String name = parts[0].trim();
            int maturity = 0;
            
            try {
            	maturity = Integer.parseInt(parts[1].trim());
            }catch (NumberFormatException e) {
            	br.close();
            	throw new FileFormatException();
            }
            
            ClientTeam ct = new ClientTeam(name, maturity);
            team.add(ct);
         }
		
		br.close();
		
		return team;
	}

	public static void loadEmployees(ArrayList<ClientTeam> clientTeams, String csvFile) 
			throws FileFormatException, FileNotFoundException, IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		br.readLine(); //jumping first line
		String line = "";
		
		int index = 0;
		ClientTeam team = clientTeams.get(index);
		index++;
		
		while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            
            if (parts.length != 5) {
            	br.close();
            	throw new FileFormatException();
            }
            
            try {
	            String name 		= parts[0]; 
	            int pLevel 			= Integer.parseInt(parts[1]);
	            int birthYear 		= Integer.parseInt(parts[2]);
	            int admissionYear 	= Integer.parseInt(parts[3]);
	            int lastProgYear 	= Integer.parseInt(parts[4]);
	            
	            Employee emp = new Employee(name, pLevel, birthYear, admissionYear, lastProgYear);
	            
	            if (team.getCurrentTeamMaturity() >= team.getMinMaturity() && index < clientTeams.size()) {
	            	team = clientTeams.get(index);
	            	index++;
	            }
	            
	            team.addEmployee(emp);
	            
	            
            }catch (NumberFormatException e) {
            	throw new FileFormatException();
            }
		}
		
		
	}
}
