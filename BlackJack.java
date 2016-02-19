// BlackJack.java

import java.util.*;

public class BlackJack {

	// instance variables
	protected Vector<Player> players;
	protected Deck gameDeck;
	protected int currentPlayer;

	// constructor

	// defaults to two players
	public BlackJack() {
		currentPlayer = 1; // currentPlayer defaults to one (the dealer is at 0
							// index)
		gameDeck = new Deck();
		players = new Vector<Player>();
		players.add(new Player(true, 1000000000));
		players.add(new Player("Player 1"));
		players.add(new Player("Player 2"));
	}

	// initiates game with given number of players
	public BlackJack(int numPlayers) {
		currentPlayer = 1; // currentPlayer defaults to one (the dealer is at 0
							// index)
		gameDeck = new Deck();
		players = new Vector<Player>();
		players.add(new Player(true, 1000000000));
		for (int i = 0; i < numPlayers; i++) {
			String name = "Player "+ (i+1);
			players.add(new Player(name));
		}
	}

	// deals out two cards to each player and dealer
	public void deal() {
		for (int i = 0; i < 2; i++) { // two cards per player
			for (int s = players.size() - 1; s > -1; s--) { // hit all players
				Player current = players.get(s);
				current.hit(gameDeck);
				players.set(s, current);
			}
		}
	}

	// retrieves winner index
	public int[] getWin() {
		int[] winner = new int[players.size() - 1];
		int numWins = 0;
		
		for (int i = 0; i < winner.length; i++) {
			int index = i+1;
			winner[i] = 0; // default loss
			if (players.get(index).lastBet > 0 // must ante to win
					&& !players.get(index).isBust()) { // must not bust to win
				// if dealer busts OR if player hand > dealer hand, player wins
				if (players.get(0).isBust() ||
					players.get(0).handValue() < players.get(index).handValue()) {
					winner[i] = 1;
					numWins += 1;
					// if tied, the hand with fewer cards wins
				} else if (players.get(0).handValue() == players.get(index).handValue()
						&& players.get(0).hand.size() > players.get(index).hand
								.size()) {
					winner[i] = 1;
					numWins += 1;
				}
			}
		}
		if (players.get(0).isBust() && numWins == 0)
			winner[0] = -1; // if all bust, define first entry as -1
							// for use in quit() function in BlackJackCanvas
							// class
		return winner;
	}

	// testing code
	public static void main(String[] args) {
		BlackJack mygame = new BlackJack();
		mygame.deal();
		for (int i = 0; i < 3; i++) {
			Player current = mygame.players.get(i);
			System.out.println(current.handValue());
		}
	}
}