// BankAccount.java
// Cole Ellison and Will Muir

public class BankAccount {

	// instance variables
	protected int balance;

	// constructor (initial balance = 100)
	public BankAccount() {
		balance = 100;
	}

	// self defined initial balance
	public BankAccount(int initialBalance) {
		balance = initialBalance;
	}

	// instance methods

	public void printBal() {
		System.out.println(balance);
	}

	// withdraws and returns true if possible, else returns false
	public boolean withdraw(int amount) {
		if (balance >= amount) {
			balance -= amount;
			return true;
		} else {
			return false;
		}
	}

	// deposits given amount into account
	public void deposit(int amount) {
		balance += amount;
	}

	// tests methods for functionality
	public static void main(String[] args) {
		BankAccount mine = new BankAccount();
		mine.printBal();
		mine.withdraw(40); // try to withdraw
		mine.printBal();
		mine.withdraw(80); // try to overdraw
		mine.printBal();
		mine.deposit(10000000); // try to deposit
		mine.printBal();
	}
}