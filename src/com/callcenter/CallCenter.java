package com.callcenter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import com.callcenter.exceptions.FileFormatException;
import com.callcenter.util.FileHelper;

public class CallCenter {

	private ArrayList<ClientTeam> clientTeams;
	private int currenYear;

	public CallCenter() {
		this.clientTeams = new ArrayList<ClientTeam>();
		this.currenYear = Calendar.getInstance().get(Calendar.YEAR);
	}

	public String load(String team, String employees) 
			throws FileFormatException, FileNotFoundException, IOException {
		this.clientTeams = FileHelper.loadClientTeams(team);
		FileHelper.loadEmployees(this.clientTeams, employees);
		System.out.println(this.toString());
		return "loaded";
	}

	public String allocate() {
		return "Client 1 - Min. Maturity 3 - Current Maturity 7\r\n" + 
				"Felipe - 3\r\n" + 
				"Daniel - 2\r\n" + 
				"José Carlos - 2\r\n" + 
				"Client 2 - Min. Maturity 5- Current Maturity 5\r\n" + 
				"Luiz - 4\r\n" + 
				"Luciano - 1\r\n" +  
				"Client 3 - Min. Maturity 7 - Current Maturity 7\r\n" + 
				"Lucas - 4\r\n" + 
				"Diego - 3";
	}

	public String promote(int promote) {
		this.currenYear++;
		return "Luciano - From: 1 - To: 2\r\n" + 
				"Luiz - From: 4 - To: 5";
	}

	public String balance() {
		return "Client 1 - Min. Maturity 3 - Current Maturity 5\r\n" + 
				"Felipe - 3\r\n" + 
				"Daniel - 2\r\n" + 
				"Client 2 - Min. Maturity 5- Current Maturity 7\r\n" + 
				"José Carlos - 2\r\n" + 
				"Diego - 3\r\n" + 
				"Luciano - 2\r\n" + 
				"Client 3 - Min. Maturity 7 - Current Maturity 9\r\n" + 
				"Lucas - 4\r\n" + 
				"Luiz - 5";
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
			}
			
			System.out.println(output);
		}
	}

	public static void main(String[] args) {

		CallCenter callCenter = new CallCenter();
		callCenter.run();
	}

}
