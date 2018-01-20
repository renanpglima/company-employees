package com.callcenter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import com.callcenter.exceptions.FileFormatException;
import com.callcenter.util.FileHelper;

public class CallCenter {

	private ArrayList<ClientTeam> clientTeams;
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

	public String allocate() {

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

	public String promote(int promote) {
		
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

	public String balance() {
		
		int extraSum = 0;
		for (ClientTeam ct : this.clientTeams) {
			extraSum += ct.getExtraMaturity();
		}
		
		int target = extraSum/this.clientTeams.size();
		
		Employee[] employeesArray = new Employee[this.employees.size()];
		employeesArray = this.employees.toArray(employeesArray);
		Arrays.sort(employeesArray);
		
		ClientTeam[] teamArray = new ClientTeam[this.clientTeams.size()];
		teamArray = this.clientTeams.toArray(teamArray);
		Arrays.sort(teamArray);
		
		//clear teams
		for (ClientTeam ct : this.clientTeams) {
			ct.setEmployees(new ArrayList<Employee>());
			ct.updateCurrentMaturity();
		}
		
		int tIndex = 0;
		ClientTeam team = teamArray[tIndex++];
		
		for (int i = 0 ; i <employeesArray.length; i++) {
			Employee e = employeesArray[i];

			if ((tIndex < teamArray.length)  &&
					((team.getCurrentMaturity() + e.getpLevel()) > (team.getMinMaturity() + target))) {
				team = teamArray[tIndex++];
			}
			
			team.addEmployee(e);
		}
		
		
		return this.toString();
	}

	private void balance(ClientTeam[] pair, int diff) {
		int p0Extra = pair[0].getExtraMaturity();
		int p1Extra = pair[1].getExtraMaturity();
		
		int p0Target = pair[0].getMinMaturity() + (p0Extra - (diff/2));
		int p1Target = pair[1].getMinMaturity() + (p1Extra + (diff/2));
		
		int size = pair[0].getEmployees().size() + pair[1].getEmployees().size();
		
		Employee[] employees = new Employee[size];
		int index = 0;
		for (Employee e : pair[0].getEmployees()) {
			employees[index++] = e;
		}
		for (Employee e : pair[1].getEmployees()) {
			employees[index++] = e;
		}
		
		Arrays.sort(employees);
		
		ArrayList<Employee> p0Emp = new ArrayList<Employee>();
		ArrayList<Employee> p1Emp = new ArrayList<Employee>();
		
		for (int i = 0; i < employees.length; i++) {
			Employee e = employees[i];
			
			if (i%2 == 0) {
				if (p0Target > 0) {
					p0Emp.add(e);
					p0Target -= e.getpLevel();
				}else {
					p1Emp.add(e);
					p1Target -= e.getpLevel();
				}
				
			}else {
				if (p1Target > 0) {
					p1Emp.add(e);
					p1Target -= e.getpLevel();
				}else {
					p0Emp.add(e);
					p0Target -= e.getpLevel();
				}
			}
		}
		
		pair[0].setEmployees(p0Emp);
		pair[1].setEmployees(p1Emp);
	}

	private ClientTeam[] getMaxDiffExtraMaturity() {
		ClientTeam[] pair = new ClientTeam[2];
		pair[0] = this.clientTeams.get(0);
		pair[1] = this.clientTeams.get(1);
		
		//ordering
		if (pair[0].getExtraMaturity() < pair[1].getExtraMaturity()) {
			ClientTeam t = pair[0];
			pair[0] = pair[1];
			pair[1] = t;
		}
		
		for (int i = 2; i < this.clientTeams.size(); i++) {
			
			ClientTeam ct = this.clientTeams.get(i);
			int ctMat = ct.getExtraMaturity();
			
			if (ctMat > pair[0].getExtraMaturity()) {
				pair[0] = ct;
			}else if (ctMat < pair[1].getExtraMaturity()) {
				pair[1] = ct;
			}
		}
		
		return pair;
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
		
		//test
		try {
			this.load("docs/teams.txt", "docs/employees.txt");
			this.allocate();
			this.promote(2);
			this.balance();
		}catch(Exception e) {
			
		}

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
			}

			System.out.println(output);
		}
	}

	public static void main(String[] args) {

		CallCenter callCenter = new CallCenter();
		callCenter.run();
	}

}
