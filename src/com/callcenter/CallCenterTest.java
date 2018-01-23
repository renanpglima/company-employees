package com.callcenter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.callcenter.exceptions.FileFormatException;
import com.callcenter.exceptions.TeamAlocationException;

class CallCenterTest {	

	
	@DisplayName("Test File Load")
	@Test
	void testLoad() throws FileNotFoundException, FileFormatException, IOException {
		CallCenter cc = new CallCenter();
		cc.load("docs/teams.txt", "docs/employees.txt");
		assertEquals(3, cc.getClientTeams().size());		
		assertEquals(7, cc.getEmployees().size());
	}

	@DisplayName("Test Allocation and teams maturity")
	@Test
	void testAllocate() throws FileNotFoundException, FileFormatException, IOException, TeamAlocationException {
		CallCenter cc = new CallCenter();
		cc.load("docs/teams.txt", "docs/employees.txt");
		cc.allocate();
		
		for(ClientTeam ct : cc.getClientTeams()) {
			assertEquals(ct.getCurrentMaturity() <= ct.getCurrentMaturity(), true);
		}
	}

	@DisplayName("Test promotion and year progress")
	@Test
	void testPromote() throws FileNotFoundException, FileFormatException, IOException, TeamAlocationException {
		CallCenter cc = new CallCenter();
		cc.load("docs/teams.txt", "docs/employees.txt");
		cc.allocate();
		int yearBefore = cc.getCurrenYear();
		cc.promote(1);
		assertEquals(yearBefore + 1, cc.getCurrenYear());
		cc.promote(2);
		assertEquals(yearBefore + 2, cc.getCurrenYear());
	}

	@DisplayName("Check if all employees have team after ballance")
	@Test
	void testBalance() throws FileNotFoundException, FileFormatException, IOException, TeamAlocationException {
		CallCenter cc = new CallCenter();
		cc.load("docs/teams.txt", "docs/employees.txt");
		cc.allocate();
		cc.balance();
		cc.promote(2);
		cc.promote(4);
		cc.balance();
		
		for (Employee e : cc.getEmployees()) {
			boolean found = false;
			
			for (ClientTeam ct : cc.getClientTeams()) {
				for (Employee e2 : ct.getEmployees()) {
					if (e2 == e) {
						found = true;
						break;
					}
				}
				if (found)
					break;
			}
			
			assertEquals(found, true);
		}
	}

}
