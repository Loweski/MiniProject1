package com.miniproject1.bank;

public class User {
	//simple login credentials with balance
	private String name;
	private String password;
	private double balance;
	
	//flags used to determine the status of the account
	private boolean activated;
	private boolean locked;
	private boolean admin;
	
	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
		balance = 0;
		activated = false;
		locked = false;
		admin = false;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public double getBalance() {
		return balance;
	}
	
	public void printBalance() {
		System.out.println("Balance: $" + balance);
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (activated ? 1231 : 1237);
		result = prime * result + (admin ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (locked ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (activated != other.activated)
			return false;
		if (admin != other.admin)
			return false;
		if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
			return false;
		if (locked != other.locked)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", password=" + password + ", balance=" + balance + ", activated=" + activated
				+ ", locked=" + locked + ", admin=" + admin + "]\n";
	}
	public void withdraw(double amount) {
		if(amount > balance) {
			System.out.println("Withdraw denied. Amount entered exceed current balance");
			//INSERT CODE TO PRINT TO LOG HERE
		}
		else {
			balance -= amount;
			System.out.println("Withdraw approved.");
			//INSERT CODE TO PRINT TO LOG HERE
		}
		printBalance();
	}
	
	public void deposit(double amount) {
		balance += amount;
		
		System.out.println("Deposit approved.");
		//INSERT CODE TO PRINT TO LOG HERE
		printBalance();
	}
	

}
