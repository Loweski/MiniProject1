package com.miniproject1.bank;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class Bank {
	private static List<User> users = new ArrayList<User>();
	private static List<User> actList = new ArrayList<User>();
	static Scanner input = new Scanner(System.in);
	static String currTask;

	final static Logger logger = Logger.getLogger(Bank.class);

	static ObjectOutputStream oos;
	static ObjectInputStream ois;

	public static void main(String[] args) throws IOException {

		Bank myBank = new Bank();

		User firstUser = new User("Bobbert", "THEBobbert");
		firstUser.setActivated(true);
		firstUser.setAdmin(true);
		firstUser.setBalance(1000000);

		/*
		 * This section will try to read from the Users.ser file. If it doesn't exist,
		 * the users will be set have just one user and admin named Bobbert Afterward,
		 * the finally block will close the file.
		 */
		try {
			ois = new ObjectInputStream(new FileInputStream("Users.ser"));
			users = (List<User>) ois.readObject();
			actList = (List<User>) ois.readObject();
		} catch (IOException | ClassNotFoundException e1) {
			users.add(firstUser);

		} finally {
			if (ois != null) {
				ois.close();
			}
		}

		/*
		 * This is print the first menu where the option to login or create an account
		 * comes.
		 */
		while (true) {
			System.out.println("Welcome, input \"login\" to log in or \"create\" to create a new account.");

			currTask = input.nextLine();

			/*
			 * Here, the menus comes up in which, each they can just exit the program or be
			 * taken to the method that match their input.
			 */
			if (currTask.toLowerCase().contentEquals("exit")) {
				System.out.println("Thank you for using our bank!");
				break;
			} else if (currTask.toLowerCase().contentEquals("create")) {
				myBank.createUser();
			} else if (currTask.toLowerCase().contentEquals("login")) {
				myBank.loginUser();
			} else {
				System.out.println("Sorry, we don't understand your input. Here's a list of possible inputs.");
				System.out.println("\tlogin - will take you into the login menu");
				System.out.println("\tcreate - will take you into creating an account");
				System.out.println("\texit - closes the program");
			}

		}
		/*
		 * Will save the data to the Users.ser file.
		 */
		try {
			oos = new ObjectOutputStream(new FileOutputStream("Users.ser"));
			oos.writeObject(users);
			oos.writeObject(actList);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		input.close();
	}

	/**
	 * This takes the user into the steps of setting up an account.
	 */
	public void createUser() {
		String name;
		boolean found;
		while (true) {
			System.out.println("Enter the username you want to use:");
			name = input.nextLine();
			found = false;
			/*
			 * Look to see it a user already has the username.
			 */
			for (User u : users) {
				if (u.getName().equals(name)) {
					found = true;
					break;
				}
			}
			/*
			 * If taken, tell them it's taken and have the user repeat the process.
			 */
			if (found) {
				System.out.println("That username is already taken.");
				continue;
			}
			/*
			 * Otherwise, continue onward to the password section.
			 */
			break;
		}

		String password;
		String password2;

		/*
		 * This loop containes the usual "pick a password" process. If the password
		 * entered both times are different, it'll them the user it doesn't match and
		 * repeat the process again. Otherwise, it moves on.
		 */
		while (true) {
			System.out.println("Enter the password you want to use:");
			password = input.nextLine();
			System.out.println("Confirm Password:");
			password2 = input.nextLine();
			if (!password.contentEquals(password2)) {
				System.out.println("Password does not match. Please try again.");
			} else {
				break;
			}
		}
		/*
		 * Account gets created and the new user is added to the activation list and
		 * will wait for an admin to activate it.
		 */
		users.add(new User(name, password));
		actList.add(new User(name, password));
		System.out.println("Account created. Please wait for activation.");
		logger.info("A new account for " + name + " has been created.");
	}

	/**
	 * This takes the user into the steps of logging in. It will also prompt up the
	 * selection to log in as a user or admin if the user is also an admin. If the
	 * user is just a regular user, it will take them directly into the user's menu
	 * screen.
	 */
	public void loginUser() {
		/*
		 * Takes in the username and password
		 */
		System.out.println("Enter username:");
		String name = input.nextLine();
		System.out.println("Enter Password");
		String password = input.nextLine();
		boolean found = false;
		User currUser = new User(name, password);
		/*
		 * Checks to see if the information entered is in our database.
		 */
		for (User u : users) {
			if ((u.getName().equals(name)) && (u.getPassword().equals(password))) {
				currUser = u;
				found = true;
				break;
			}
		}
		/*
		 * If found, checks the status of the account to see if they're activated or an
		 * admin.
		 */
		if (found) {
			if (currUser.isActivated() == false) {
				System.out.println("Your account is not activated yet. Please wait for an admin to activate it.");
				return;
			} else if (currUser.isAdmin() == true) {
				/*
				 * If the user is also an admin, allows them the option to either login as a
				 * user or an admin, giving two different menu and options.
				 */
				System.out.println("Login as user or admin?");
				String login = input.nextLine();
				if (login.toLowerCase().equals("admin")) {
					adminMenu(currUser);
				} else if (login.toLowerCase().equals("user")) {
					userMenu(currUser);
				} else {
					/*
					 * In case there was a mistake, the user will be logged out just in case.
					 */
					System.out.println("Input not found. Logging you out for safety.");
				}
			} else {
				/*
				 * If just a regular user, will take them straight into the user's menu.
				 */
				userMenu(currUser);
			}

		} else {
			/*
			 * If user's information isn't found, tell them they're not found and to recheck
			 * their information just in case.
			 */
			System.out.println("Sorry we don't have you in our database.");
			System.out.println("Please check your username and password and try again.");
		}
	}

	/**
	 * Opens up the user menu for depositing and withdrawing. More options will be
	 * available late. Note: It will check if the user is locked. If locked, it'll
	 * tell them that their account is locked and log them out. No balance is shown
	 * to them.
	 * 
	 * @param user
	 *            - The user that's currently viewing their account.
	 */
	public void userMenu(User user) {
		/*
		 * Will check to see if the user is locked first. If locked, they are
		 * immediately told and then logged out.
		 */
		if (user.isLocked() == true) {
			System.out.println("Your account is locked. Sorry!");
			return;
		}
		/*
		 * Balance is not shown to locked accounts.
		 */
		user.printBalance();
		while (true) {
			System.out.println("What would you like to do today?");
			currTask = input.nextLine();
			/*
			 * Program will ask user for input to see what they user need to do today.
			 */
			if (currTask.toLowerCase().contentEquals("withdraw")) {
				System.out.println("How much would you like to withdraw?");
				String amount = input.nextLine();
				/*
				 * A try block to see if the user enter a valid input.
				 */
				if (Double.parseDouble(amount) < 0) {
					System.out.println("You cannot withdraw negative amount.");
					logger.info(user.getName() + " tried to withdraw a negative value.");
				}
				else {
					try {
						user.withdraw(Double.parseDouble(amount));
						logger.info(user.getName() + "withdraw $" + amount);
					} catch (NumberFormatException e) {
						/*
						 * Invalid Input is printed and the current task will end.
						 */
						System.out.println("Invalid Input");
						logger.info(user.getName() + " entered an invalid input for withdrawal.");
					}
				}
				

			} else if (currTask.toLowerCase().contentEquals("deposit")) {
				/*
				 * Similar to withdraw, it will check for a valid input.
				 */
				System.out.println("How much would you like to deposit?");
				String amount = input.nextLine();
				if (Double.parseDouble(amount) < 0) {
					System.out.println("You cannot deposit negative amount.");
					logger.info(user.getName() + " tried to deposit a negative value.");
				} else {
					try {
						user.deposit(Double.parseDouble(amount));
						logger.info(user.getName() + "deposited $" + amount);
					} catch (NumberFormatException e) {
						System.out.println("Invalid Input");
						logger.info(user.getName() + " entered an invalid input for deposit.");
					}
				}

			} else if (currTask.toLowerCase().contentEquals("balance")) {
				// simply print the balance of the current user
				user.printBalance();
			} else if (currTask.toLowerCase().contentEquals("logout")) {
				// log the user out
				System.out.println("Logging out");
				break;
			} else {
				/*
				 * if any other input is put in, the user is notified that the program doesn't
				 * understand the input. It will then print all possible inputs.
				 */
				System.out.println("Sorry, we don't understand your input. Here's a list of possible inputs.");
				System.out.println("\twithdraw - take you to the withdraw menu");
				System.out.println("\tdeposit - take you to the deposit menu");
				System.out.println("\tbalance - print out your balance");
				System.out.println("\tlogout - logout of your account");
			}
		}
	}

	/**
	 * Opens up the admin menu for activating, locking, and promoting users.
	 * 
	 * @param user
	 *            - The user that's currently logged in as an admin.
	 */
	public void adminMenu(User user) {
		/*
		 * Similar to the user menu, only with new tasks for the admin.
		 */
		while (true) {
			System.out.println("What do you need today?");
			currTask = input.nextLine();
			if (currTask.toLowerCase().equals("logout")) {
				System.out.println("Logging out");
				break;
			} else if (currTask.toLowerCase().equals("actlist")) {
				/*
				 * Gives out a list of people waiting for activation If there's no one, it will
				 * say there's none
				 */
				if (actList.size() == 0) {
					System.out.println("There's no account waiting for activation.");
				} else {
					System.out.println("Activation List:");
					int i = 1;
					for (User u : actList) {
						System.out.println("\t" + i + ". " + u.getName());
					}
				}

			} else if (currTask.toLowerCase().equals("activate")) {
				System.out.println("Which account are you activating?");
				String name = input.nextLine();
				boolean found = false;
				/*
				 * Looks for the user in the list
				 */
				for (User u : actList) {
					if (u.getName().equals(name)) {
						/*
						 * When found, removes them from the list
						 */
						actList.remove(u);
						/*
						 * Then set their activation value
						 */
						for (User u2 : users) {
							if (u2.getName().equals(name)) {
								u2.setActivated(true);
								break;
							}
						}
						found = true;
						break;
					}
				}
				/*
				 * Then prints out that they've been activated if found, otherwise prints out
				 * the account was not found.
				 */
				if (found) {
					System.out.println(name + " has been activated.");
					logger.info(name + " has been activated by admin " + user.getName());
				} else {
					System.out.println(
							"Account not found. Type in actlist to get a list of accounts waiting for activation.");
				}

			} else if (currTask.toLowerCase().equals("lock")) {
				/*
				 * Similar process to activating an account
				 */
				System.out.println("Which account are you locking?");
				String name = input.nextLine();
				boolean found = false;
				for (User u : users) {
					if (u.getName().equals(name)) {
						u.setLocked(true);
						found = true;
						break;
					}
				}
				if (found) {
					/*
					 * Prevents an admin from locking their own account.
					 */
					if (user.getName().equals(name)) {
						System.out.println("You cannot locked your own account.");
					} else {
						System.out.println(name + " has been locked.");
						logger.info(name + " has been locked by admin " + user.getName());
					}
				} else {
					System.out.println("User not found.");
				}

			} else if (currTask.toLowerCase().equals("unlock")) {
				/*
				 * Same as the locking an account
				 */
				System.out.println("Which account are you unlocking?");
				String name = input.nextLine();
				boolean found = false;
				for (User u : users) {
					if (u.getName().equals(name)) {
						u.setLocked(false);
						found = true;
						break;
					}
				}
				if (found) {
					/*
					 * Same as before, an admin cannot unlock his own account. This is to avoid
					 * abuse from an admin that can unlock themselves.
					 */
					if (user.getName().equals(name)) {
						System.out.println("You cannot unlocked your own account.");
						logger.info("Admin: " + user.getName() + " tried to unlock his own account.");
					} else {
						System.out.println(name + " has been unlocked.");
						logger.info(name + " has been unlocked by admin " + user.getName());
					}
				} else {
					System.out.println("User not found.");
				}

			} else if (currTask.toLowerCase().equals("promote")) {
				/*
				 * Looks for the user the admin want to promote.
				 */
				System.out.println("Which account are you promoting?");
				String name = input.nextLine();
				boolean found = false;
				boolean alreadyAdmin = false;
				for (User u : users) {
					/*
					 * Will check if this user is already an admin
					 */
					if (u.getName().equals(name)) {
						if (u.isAdmin()) {
							alreadyAdmin = true;
							break;
						}
						u.setAdmin(true);
						found = true;
						break;
					}
				}
				/*
				 * Prints out that the chose user is promoted, already an admin, or if the user
				 * couldn't be found.
				 */
				if (found) {
					System.out.println(name + " has been promoted.");
					logger.info(name + " has been promoted to admin by admin " + user.getName());
				} else if (alreadyAdmin) {
					System.out.println("That user is already an admin.");
				} else {
					System.out.println("User not found.");
				}
			} else {
				/*
				 * In case there was invalid input, the program will write the possible commands
				 * for an admin.
				 */
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

}
