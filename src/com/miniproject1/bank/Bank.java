package com.miniproject1.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bank {
	private static List<User> users = new ArrayList<User>();
	private static List<User> actList = new ArrayList<User>();
	//private static List<Admin> admins = new ArrayList<Admin>();
	static Scanner input = new Scanner(System.in);
	static String currTask;
	
	public static void main(String[] args) {
		Bank myBank = new Bank();
		
		//Admin firstAdmin = new Admin("Bobbert");
		User firstUser = new User("Bobbert", "THEBobbert");
		firstUser.setActivated(true);
		firstUser.setAdmin(true);
		firstUser.setBalance(1000000);
		users.add(firstUser);
		//admins.add(firstAdmin);
		
		while(true) {
			
			System.out.println("Welcome, input \"login\" to log in or \"create\" to create a new account.");
			
			currTask = input.nextLine();
			
			if(currTask.toLowerCase().contentEquals("exit")) {
				System.out.println("Thank you for using our bank!");
				break;
			}
			else if(currTask.toLowerCase().contentEquals("create")) {
				myBank.createUser();
			}
			else if (currTask.toLowerCase().contentEquals("login")){
				myBank.loginUser();
			}
			else {
				myBank.unknownInput();
			}
			
			
		}

		
		input.close();
	}
	
	 public void createUser() {
		String name;
		boolean found;
		while(true) {
			System.out.println("Enter your name:");
			name = input.nextLine();
			found = false;
			for(User u: users) {
				if(u.getName().equals(name)) {
					found = true;
					break;
				}
			}
			if(found) {
				System.out.println("That username is already taken.");
				continue;
			}
			break;
		}
		
		String password;
		String password2;
		
		while(true) {
			System.out.println("Enter the password you want to use:");
			password = input.nextLine();
			System.out.println("Confirm Password:");
			password2 = input.nextLine();
			if(!password.contentEquals(password2)) {
				System.out.println("Password does not match. Please try again.");
			}
			else {
				break;
			}
		}
		users.add(new User(name, password));
		actList.add(new User(name,password));
		System.out.println("Account created. Please wait for activation.");
	}
	
	 public void loginUser() {
		 System.out.println("Enter your name:");
			String name = input.nextLine();
			System.out.println("Enter Password");
			String password = input.nextLine();
			boolean found = false;
			User currUser = new User(name, password);
			for(User u: users) {
				if((u.getName().equals(name)) && (u.getPassword().equals(password))) {
					currUser = u;
					found = true;
					break;
				}
			}
			if(found) {
				//NOTE: This is to be uncommented after admin's activate() method has been established.
				if(currUser.isActivated() == false) {
					System.out.println("Your account is not activated yet. Please wait for an admin to activate it.");
					return;
				}
				
				//NOTE: This will be tested after admind's lock() and unlock() methods has been established
				//for now, it'll always ignored this section
				if(currUser.isLocked() == true) {
					System.out.println("Your account is locked. Sorry!");
					return;
				}
				else if(currUser.isAdmin() == true) {
					System.out.println("Login as user or admin?");
					String login = input.nextLine();
					if(login.toLowerCase().equals("admin")) {
						adminMenu();
					}
					else if (login.toLowerCase().equals("user")){
						userMenu(currUser);
					}
					else {
						System.out.println("Input not found. Logging you out for safety.");
					}
				}
				else {
					userMenu(currUser);
				}
				
			}
			else {
				System.out.println("Sorry we don't have you in our database.");
				System.out.println("Please check your username and password and try again.");
			}
	 }
	 public void userMenu(User user) {
		 user.printBalance();
		 while(true) {
			 System.out.println("What would you like to do today?");
			 currTask = input.nextLine();
			 if(currTask.toLowerCase().contentEquals("withdraw")) {
				 System.out.println("How much would you like to withdraw?");
				 String amount = input.nextLine();
				 try {
					 user.withdraw(Double.parseDouble(amount));
				 } catch (NumberFormatException e) {
					 System.out.println("Invalid Input");
				 }
				 
			 }
			 else if(currTask.toLowerCase().contentEquals("deposit")) {
				 System.out.println("How much would you like to deposit?");
				 String amount = input.nextLine();
				 try {
					 user.deposit(Double.parseDouble(amount));
				 } catch (NumberFormatException e) {
					 System.out.println("Invalid Input");
				 }
			 }
			 else if(currTask.toLowerCase().contentEquals("balance")){
				 user.printBalance();
			 }
			 else if(currTask.toLowerCase().contentEquals("logout")) {
				 System.out.println("Logging out");
				 break;
			 }
			 else {
				 System.out.println("Sorry, we don't understand your input. Here's a list of possible inputs.");
				 System.out.println("\twithdraw - take you to the withdraw menu");
				 System.out.println("\tdeposit - take you to the deposit menu");
				 System.out.println("\tbalance - print out your balance");
				 System.out.println("\tlogout - logout of your account");
			 }
		 }
	 }

	 public void adminMenu() {
		 while(true) {
			 System.out.println("What do you need today?");
			 currTask = input.nextLine();
			 if(currTask.toLowerCase().equals("logout")) {
				 System.out.println("Logging out");
				 break;
			 }
			 else if(currTask.toLowerCase().equals("actlist")) {
				 for(User u: actList) {
					 System.out.println(u.getName());
				 }
				 //System.out.println(actList);
			 }
			 else if(currTask.toLowerCase().equals("activate")) {
				 System.out.println("Which account are you activating?");
				 String name = input.nextLine();
				 boolean found = false;
				 for(User u: actList) {
					 if(u.getName().equals(name)) {
						 actList.remove(u);
						 for(User u2: users) {
							 if(u2.getName().equals(name)) {
								 u2.setActivated(true);
								 break;
							 }
						 }
						 found = true;
						 System.out.println("-----------");
						 System.out.println(u);
						 break;
					 }
				 }
				 if(found) {
					 System.out.println(name + " has been activated.");
				 }
				 else {
					 System.out.println("Account not found. Type in actlist to get a list of accounts waiting for activation.");
				 }
				 
			 }
			 else if(currTask.toLowerCase().equals("lock")) {
				 System.out.println("Which account are you locking?");
				 String name = input.nextLine();
				 boolean found = false;
				 for(User u : users) {
					 if(u.getName().equals(name)) {
						 u.setLocked(true);
						 found = true;
						 break;
					 }
					 System.out.println(u + "-------------");
				 }
				 if(found) {
					 System.out.println(name + " has been locked.");
				 }
				 else {
					 System.out.println("User not found.");
				 }
				 
			 }
			 else if(currTask.toLowerCase().equals("unlock")) {
				 System.out.println("Which account are you unlocking?");
				 String name = input.nextLine();
				 boolean found = false;
				 for(User u : users) {
					 if(u.getName().equals(name)) {
						 u.setLocked(false);
						 found = true;
						 break;
					 }
					 System.out.println(u + "-------------");
				 }
				 if(found) {
					 System.out.println(name + " has been unlocked.");
				 }
				 else {
					 System.out.println("User not found.");
				 }
				 
			 }
			 else if(currTask.toLowerCase().equals("promote")) {
				 System.out.println("Which account are you promoting?");
				 String name = input.nextLine();
				 boolean found = false;
				 boolean alreadyAdmin = false;
				 for(User u : users) {
					 if(u.getName().equals(name)) {
						 if(u.isAdmin()) {
							 alreadyAdmin = true;
							 break;
						 }
						 u.setAdmin(true);
						 found = true;
						 break;
					 }
					 System.out.println(u + "-------------");
				 }
				 if(found) {
					 System.out.println(name + " has been promoted.");
				 }
				 else if(alreadyAdmin) {
					 System.out.println("That user is already an admin.");
				 }
				 else {
					 System.out.println("User not found.");
				 }
			 }
			 else {
				 System.out.println("Sorry, we don't understand your input. Here's a list of possible inputs.");
				 System.out.println("\tactlist - to get a list of people waiting to be activated");
				 System.out.println("\tactivate - to activate a user");
				 System.out.println("\tlock - to lock a user");
				 System.out.println("\tunlock - to unlock a user");
				 System.out.println("\tpromote - to promote a user to admin");
				 System.out.println("\tlogout - logout of your account");
			 }
		 }
		 
		 
	 }
	 
	 public void unknownInput() {
		 System.out.println("Sorry, we don't understand your input. Here's a list of possible inputs.");
		 System.out.println("\tlogin - will take you into the login menu");
		 System.out.println("\tcreate - will take you into creating an account");
		 System.out.println("\texit - closes the program");
	 }
}
