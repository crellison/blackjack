// Player.java
// Cole Ellison and Will Muir

import java.util.*; // for Vector

public class Player {

	// instance variables
	protected boolean isDealer; // true=dealer, false=non-dealer
	protected Vector<Integer> hand;
	protected boolean bust;
	protected BankAccount account;
	protected int lastBet;
	protected String name;

	final int MAX_DEALER_VALUE = 17;
	final int BUST = 21;

	// dealers cannot get "hit" once the value of their hand exceeds 17

	// constructors

	// creates basic player
	public Player() {
		name = "";
		isDealer = false;
		hand = new Vector<Integer>();
		bust = false;
		account = new BankAccount();
		lastBet = 0;
	}

	public Player(String playerName) {
		name = playerName;
		isDealer = false;
		hand = new Vector<Integer>();
		bust = false;
		account = new BankAccount();
		lastBet = 0;
	}

	// creates player, dealer boolean decides if player object is dealer
	public Player(boolean dealer, int account) {
		isDealer = dealer;
		hand = new Vector<Integer>();
		bust = false;
		this.account = new BankAccount(account);
		lastBet = 0;
		name = "dealer";
	}

	// instance methods

	// defines name of player
	public void setName(String newName) {
		name = newName;
	}

	public void clearHand() {
		hand = new Vector<Integer>();
	}

	// busts if handValue() > 21
	public boolean isBust() {
		if (handValue() > BUST)
			bust = true;
		return bust;
	}

	// determines max value of hand
	public int handValue() {
		int val = 0;
		if (!hand.isEmpty()) {
			for (int i = hand.size() - 1; i > -1; i--) {
				val = val + cardValue(hand.get(i));
			}
			if (hasAce() && (val + 10) < 22)
				val = val + 10;
		}
		return val;
	}

	// returns true if hand contains an ace
	public boolean hasAce() {
		boolean ace = false;
		if (hand.contains(0))
			return !ace;
		else if (hand.contains(13))
			return !ace;
		else if (hand.contains(26))
			return !ace;
		else if (hand.contains(39))
			return !ace;
		else
			return ace;
	}

	// hits the hand by adding a card to it
	public void hit(Deck deck) {
			hand.add(deck.drawCard());
	}

	// static methods

	// determines the value of a card
	public static int cardValue(int card) {
		int[] cardVal = Deck.parseCard(card);
		card = cardVal[1];
		if (card < 10)
			return card + 1;
		else
			return 10;
	}

	public static void main(String[] args) {
		Deck deck = new Deck();
		System.out.println("Player");
		Player myplayer = new Player();
		while (myplayer.handValue() < 21) {
			System.out.print(cardValue(deck.topCard()));
			myplayer.hit(deck);
			System.out.println(" : " + myplayer.handValue());
		}
		System.out.println("Dealer");
		Player mydealer = new Player(true, 1000000000);
		while (mydealer.handValue() < 17) {
			System.out.print(cardValue(deck.topCard()));
			mydealer.hit(deck);
			System.out.println(" : " + mydealer.handValue());
		}
	}
}