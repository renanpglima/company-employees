package com.callcenter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import com.callcenter.exceptions.FileFormatException;
import com.callcenter.exceptions.TeamAlocationException;
import com.callcenter.util.FileHelper;

public class CallCenter {

	private ArrayList<ClientTeam> clientTeams;
	public ArrayList<ClientTeam> getClientTeams() {
		return clientTeams;
	}

	public ArrayList<Employee> getEmployees() {
		return employees;
	}

	public int getCurrenYear() {
		return currenYear;
	}

	private ArrayList<Employee> employees;

	private int currenYear;

	public CallCenter() {
		this.currenYear = Calendar.getInstance().get(Calendar.YEAR);
	}

	public String load(String team, String employees) 
			throws FileFormatException, FileNotFoundException, IOException {
		this.clientTeams = FileHelper.loadClientTeams(team);
		this.employees = FileHelper.loadEmployees(employees);
		return "loaded";
	}

	public String allocate() throws TeamAlocationException{
		
		if (this.clientTeams == null || this.employees == null || 
				this.clientTeams.size() == 0 ||
				this.employees.size() == 0) {
			throw new TeamAlocationException();
		}

		for (ClientTeam ct : this.clientTeams) {
			ct.setEmployees(new ArrayList<Employee>());
		}
		
		int index = 0;
		ClientTeam team = this.clientTeams.get(index);
		index++;

		for (Employee e : this.employees) {
			
			if (team.getCurrentMaturity() >= team.getMinMaturity() && 
					index < this.clientTeams.size()) {
				team = clientTeams.get(index);
				index++;
			}

			team.addEmployee(e);
		}

		return this.toString();
	}

	public String promote(int promote) throws TeamAlocationException {
		
		if (this.clientTeams == null || this.employees == null || 
				this.clientTeams.size() == 0 ||
				this.employees.size() == 0) {
			throw new TeamAlocationException();
		}
		
		Employee[] promotedEmployees = new Employee[promote];
		
		for (ClientTeam team : this.clientTeams) {
			
			for (Employee e : team.getEmployees()) {
		
				e.updatePoints(this.currenYear);
				if (e.getPoints() == -1)
					continue;
				
				this.checkAndApplyPromotion(e, promotedEmployees);
			}
			
		}
		
		this.currenYear++;
		
		String ret = "";
		
		for (Employee e : promotedEmployees) {
			if (e == null)
				break;
			e.promote();
			ret += e.getName() + " - From: " + (e.getpLevel()-1) + " - To: " + e.getpLevel() + "\n"; 
		}
		
		return ret;
	}

	private void checkAndApplyPromotion(Employee e, Employee[] promotedEmployees) {
		int len = promotedEmployees.length;
		
		for (int i = 0; i < len; i++) {
			
			if (promotedEmployees[i] == null) {
				promotedEmployees[i] = e;
				break;
			}else if (promotedEmployees[i].getPoints() < e.getPoints()) {
				
				for (int j = len-1; j > i; j--) {
					promotedEmployees[j] = promotedEmployees[j-1];
				}
				promotedEmployees[i] = e;
				
				break;
			}
		}
		
	}

	public String balance() throws TeamAlocationException {
		
		if (this.clientTeams == null || this.employees == null || 
				this.clientTeams.size() == 0 ||
				this.employees.size() == 0) {
			throw new TeamAlocationException();
		}
		
		Employee[] employeesArray = new Employee[this.employees.size()];
		employeesArray = this.employees.toArray(employeesArray);
		Arrays.sort(employeesArray);
		
		ClientTeam[] teamArray = new ClientTeam[this.clientTeams.size()];
		teamArray = this.clientTeams.toArray(teamArray);
		Arrays.sort(teamArray);
		
		
		int pSum = 0;
		for (Employee e : this.employees) {
			pSum += e.getpLevel();
		}
		
		int minSum = 0;
		for (ClientTeam ct : this.clientTeams) {
			minSum += ct.getMinMaturity();
			ct.setEmployees(new ArrayList<Employee>());
			ct.updateCurrentMaturity();
		}
		
		int target = (pSum - minSum)/this.clientTeams.size();
		
		ArrayList<Employee> notAllocated = new ArrayList<Employee>();
		
		for (int i = 0 ; i <employeesArray.length; i++) {
			Employee e = employeesArray[i];
			boolean added = false;
			for (int j = 0 ; j < teamArray.length; j++) {
				int newMaturity = teamArray[j].getCurrentMaturity() + e.getpLevel();
				int maxMaturity = teamArray[j].getMinMaturity() + target;
				if (newMaturity <= maxMaturity) {
					teamArray[j].addEmployee(e);
					added = true;
					break;
				}
			}
			
			if (!added) {
				notAllocated.add(e);
			}
		}
		
		
		for (Employee e : notAllocated) {
			
			ClientTeam t = teamArray[0];
			
			for (int i = 1; i < teamArray.length; i++) {
				if (t.getExtraMaturity() > teamArray[i].getExtraMaturity()) {
					t = teamArray[i];
				}
			}
			t.addEmployee(e);
		}
		
		
		return this.toString();
	}

	public String toString() {
		String ret = "";

		for (ClientTeam ct : this.clientTeams) {
			ret += ct.toString() + "\n";
		}

		return ret;
	}

	public void run() {
		String loadUse = "*** LOAD\t->\tload team_file_name.txt employees_file_name.txt\n";
		String allocateUse = "*** ALLOCATE\t->\tallocate\n";
		String promoteUse = "*** PROMOTE\t->\tpromote quantity(int)\n";
		String balanceUse = "*** BALANCE\t->\tbalance\n";

		String usage = "Check how to use the commands:\n"
				+ loadUse + allocateUse + promoteUse + balanceUse;

		System.out.println("*****************************************\n"
				+  "****WELLCOME TO THE CALLCENTER SYSTEM****\n"
				+  "*****************************************\n"
				+ usage);

		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(System.in));

		String output = usage;
		String cmd;
		String input;
		String [] args;

		while (true) {
			try {
				input = br.readLine();
				args = input.split("\\s+");

				if (args.length < 0) {
					System.out.println(usage);
				}

				cmd = args[0];
				output = usage;

				switch (cmd) {
				case "load":
					if (args.length == 3)
						output = this.load(args[1], args[2]);

					break;

				case "allocate":
					if (args.length == 1) 
						output = this.allocate();

					break;

				case "promote":
					if (args.length == 2) {
						try{
							int promote = Integer.parseInt(args[1]);
							output = this.promote(promote);
						}catch (NumberFormatException e) {
							output = usage;
						}
					}

					break;

				case "balance":
					if (args.length == 1)
						output = this.balance();

					break;
				}

			} catch (FileFormatException e) {
				output = "ERROR: There is some problem in the input files\n" + output; 
			} catch (FileNotFoundException e1) {
				output = "ERROR: The file was not found\n" + output; 
			} catch (IOException e) {
				output = "ERROR: We have some problem reading the input\n" + output; 
			} catch (TeamAlocationException e1) {
				output = "ERROR: You need to load files before call allocate\n" + output; 
			}

			System.out.println(output);
		}
	}

	public static void main(String[] args) {

		CallCenter callCenter = new CallCenter();
		callCenter.run();
	}

}
