// BlackJackCanvas.java
// Cole Ellison and Will Muir

import java.awt.*;

@SuppressWarnings("serial")
public class BlackJackCanvas extends Canvas {

	// instance variables
	protected BlackJack game;
	protected BlackJackApplet parent;
	// constants
	final int CARD_HEIGHT = 60;
	final int CARD_WIDTH = 40;
	final int BORDER = 5;

	// constructor
	public BlackJackCanvas(BlackJackApplet a, BlackJack game) {
		parent = a;
		this.game = game;
	}

	public void paint(Graphics g) {
		Dimension d = getSize();
		// variables for player loop and x,y coordinates
		int numPlayers = game.players.size() - 1; // dealer is not a player
		int hand_x = CARD_WIDTH;
		int dealer_y = CARD_WIDTH / 2;
		int player_y = dealer_y + CARD_HEIGHT + CARD_WIDTH;
		// background and hand labels
		g.setColor(Color.green.darker());
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.black);
		g.setFont(new Font("Helvetica", Font.BOLD, 12));
		g.drawString("dealer", hand_x, dealer_y - BORDER);
		// print dealer hand
		printHand(g, game.players.get(0), hand_x, dealer_y,
				0 == game.currentPlayer);
		// print player hands
		currentPlayer(g); // highlight current player
		for (int i = 1; i <= numPlayers; i++) {
			// print hands and labels of players
			g.setColor(Color.black);
			boolean current = game.currentPlayer == i;
			Player thisPlayer = game.players.get(i);
			g.setFont(new Font("Helvetica", Font.BOLD, 12));
			g.drawString(thisPlayer.name, hand_x, player_y - BORDER);
			printHand(g, thisPlayer, hand_x, player_y, current);
			if (thisPlayer.isBust()) { // mark hand if bust
				g.setColor(Color.red);
				g.setFont(new Font("Helvetica", Font.BOLD, 20));
				g.drawString("BUST", hand_x + CARD_WIDTH + BORDER, player_y
						+ CARD_HEIGHT + CARD_WIDTH);
			}
			hand_x = hand_x + CARD_WIDTH * 3;
		}
		resetPlayerLabels();
	}

	// prints a player's hand
	// if they are currently playing, all are face up, else the first is turned
	// over
	// dealer hands are printed face up
	public void printHand(Graphics g, Player current, int x, int y,
			boolean theirTurn) {
		if (theirTurn) { // print first dealt cards face up if current turn
			printcard(g, current.hand.get(0), x, y, true);
			printcard(g, current.hand.get(1), x + CARD_WIDTH + BORDER, y, true);
		} else if (current.isDealer) { // flip second card of dealer
			printcard(g, current.hand.get(0), x, y, false);
			printcard(g, current.hand.get(1), x + CARD_WIDTH / 2, y + BORDER
					* 2, true);
		} else { // hide cards of other players
			printcard(g, current.hand.get(0), x, y, false);
			printcard(g, current.hand.get(1), x + CARD_WIDTH / 2, y + BORDER
					* 2, false);
		}

		if (!current.isDealer) { // dealer's hits are printed horizontally
			y = y + CARD_HEIGHT + BORDER * 3;
			if (current.hand.size() > 2) {
				for (int i = 2; i < current.hand.size(); i++) {
					printcard(g, current.hand.get(i), x, y, true);
					y = y + BORDER * 4;
				}
			}
		} else { // player hits are printed vertically
			x = x + CARD_WIDTH + BORDER;
			if (current.hand.size() > 2) {
				for (int i = 2; i < current.hand.size(); i++) {
					x = x + CARD_WIDTH + BORDER;
					printcard(g, current.hand.get(i), x, y, true);
				}
			}
		}
	}

	// prints a card with coordinates and boolean for face up or down
	public void printcard(Graphics g, int card, int x, int y, boolean faceUp) {
		g.setColor(Color.white);
		g.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT);
		if (faceUp) { // show the face
			g.setColor(Color.red);
			g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
			g.drawRect(x + BORDER / 2, y + BORDER / 2, CARD_WIDTH - BORDER,
					CARD_HEIGHT - BORDER);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Helvetica", Font.BOLD, 10));
			int[] parsedCard = Deck.parseCard(card); // extract the value
			g.drawString(Deck.cardSymbol(parsedCard), x + BORDER, y
					+ (BORDER * 3)); // draw the card value on the card
		} else { // print back of card
			g.setColor(Color.red);
			g.fillRect(x + BORDER / 2, y + BORDER / 2, CARD_WIDTH - BORDER,
					CARD_HEIGHT - BORDER);
		}
	}

	// highlights the current player's id above card
	public void currentPlayer(Graphics g) {
		int current = game.currentPlayer;
		if (current > 0) {
			int y = CARD_HEIGHT + CARD_WIDTH + BORDER;
			int x = CARD_WIDTH;
			for (int i = 1; i < current; i++) {
				x = x + CARD_WIDTH * 3;
			}
			g.setColor(Color.green);
			g.fillRect(x, y, CARD_WIDTH * 2 + BORDER, BORDER * 3);
		}
	}

	// converts betting label to int and returns value
	public int stringToInt(String s) {
		int betAmount = Integer.parseInt(s);
		return betAmount;
	}

	// starts a new game, deals cards, and antes the first player
	public void quit() {
		int numPlayers = 5 - (int) parent.numPlayers.getSelectedIndex();
		game = new BlackJack(numPlayers);
		game.deal();
		bet();
	}

	// refunds player and removes them from the game
	public void surrender() {
		Player current = game.players.get(game.currentPlayer);
		current.account.deposit(current.lastBet);
		current.lastBet = 0;
		game.players.set(game.currentPlayer, current);
		stand();
	}

	// stores account balances and deals a new game
	public void newGame() {
		// check for bankrupt players
		for (int i = (game.players.size() - 1); i >= 1; i--) {
			Player current = game.players.get(i);
			if (current.account.balance == 0)
				game.players.remove(i);
		}
		// if all players did not bet, refund everyone
		if (game.currentPlayer < game.players.size() - 1) {
			for (int i = 1; i < game.players.size(); i++) {
				Player current = game.players.get(i);
				current.account.deposit(current.lastBet);
				game.players.set(i, current);
			}
		}
		parent.lastWinnerLabel().setText(""); // clear win label
		// clear the hands of all players
		for (int i = 0; i < game.players.size(); i++) {
			Player current = game.players.get(i);
			current.clearHand();
			current.lastBet = 0;
			current.bust = false;
			game.players.set(i, current);
		}
		if (game.players.size() == 1) {
			game = new BlackJack(5); // if everyone lost, new game!
			parent.lastWinner.setText("House wins! Let's play again.");
		} else { // otherwise, deal out cards
			game.gameDeck = new Deck();
			game.currentPlayer = 1;
		}
		game.deal();
		bet(); // first player ante up at new game
		resetPlayerLabels();
	}

	// player stands and play proceeds to next player
	// if last player, hits the dealer as necessary
	public void stand() {
		if (parent.lastWinner.getText() != "")
			newGame(); // if the winners have already been printed, the game is
						// over
		// and the stand click was accidental
		else {
			parent.betField.setText("");
			if (game.currentPlayer < game.players.size() - 1) {
				game.currentPlayer++;
				bet(); // next player ante up
				resetPlayerLabels();
			} else {
				Player dealer = game.players.get(0);
				while (dealer.handValue() < 17) {
					dealer.hit(game.gameDeck);
				}
				game.players.set(0, dealer);
				printPoints();
				repaint();
			}
		}
	}

	// takes bet amount from account and stores in lastBet
	// also used for ante up function
	public void bet() {
		int betAmount;
		try {
			betAmount = stringToInt(parent.betField.getText());
		} catch (NumberFormatException e) {
			betAmount = 5; // formatted for ante, but the basic bet is $5
		}
		Player current = game.players.get(game.currentPlayer);
		if (betAmount > 0 && betAmount<=current.account.balance) {
			current.account.withdraw(betAmount);
			current.lastBet += betAmount;
		}
		resetPlayerLabels();
	}

	// double player's bet, hit them, then automatically stand
	public void doubleDown() {
		int thisPlayer = game.currentPlayer;
		Player current = game.players.get(thisPlayer);
		if (current.hand.size() == 2) { // only allow doubling when no hit
			current.account.withdraw(current.lastBet);
			current.lastBet += current.lastBet;
			game.players.set(thisPlayer, current); // double current bet
			hit(); // give player their one card
			if (thisPlayer == game.currentPlayer) // if player didn't bust,
													// stand
				stand();
		}
	}

	// give player one card, automatically stand if busted once card received
	public void hit() {
		Player current = game.players.get(game.currentPlayer);
		if (!current.isBust()) { // while not bust, hit player and reassign
			current.hit(game.gameDeck);
			game.players.set(game.currentPlayer, current);
			if (current.isBust()) {
				parent.playerLabel().setText("BUST");
				stand();
			}
			// if bust and last player, quit out and reveal winner
		} else { // if bust, make the player stand
			stand();
		}
		resetPlayerLabels();
	}

	// resets player labels of account balance, bet amount, and hand value
	public void resetPlayerLabels() {
		Player current = game.players.get(game.currentPlayer);
		parent.playerLabel().setText(
				current.name + " || Current Bet: " + current.lastBet);
		parent.infoLabel().setText(
				current.handValue() + " points || " + current.account.balance
						+ " dollars");
		repaint();
	}

	// prints points of game, only called in stand if on last player
	public void printPoints() {
		parent.playerLabel().setText("");
		parent.infoLabel().setText("");
		game.currentPlayer = 0;
		// make dealer current player so their hand is shown
		int[] winner = new int[game.players.size() - 1];
		winner = game.getWin();
		if (winner[0] == -1)
			parent.lastWinnerLabel().setText("ALL BUST");
		else {
			String winningLabel = "Winners: ";

			for (int i = 0; i < winner.length; i++) { // for len of winner array
				if (winner[i] == 1) { // if player won
					// add player id to winning Label
					int index = i + 1;
					Player current = game.players.get(index);
					winningLabel += current.name + ": " + current.handValue()
							+ "  ";
					// award with bet value
					current.account.deposit(current.lastBet * 2);
					game.players.set(index, current);
				}
			} // if no one won, but the dealer didn't bust
			int numWinners = 0;
			for (int i : winner) {
				numWinners += i;
			}
			if (numWinners == 0) {
				int points = game.players.get(0).handValue();
				winningLabel = "Winner: Dealer: " + points;
			}
			parent.lastWinnerLabel().setText(winningLabel);
		}
	}

	public void namePlayer() {
		Player current = game.players.get(game.currentPlayer);
		String newName = parent.playerName.getText();
		if (!newName.equals("")) { // if text field was not empty, set name
			current.name = newName;
			game.players.set(game.currentPlayer, current);
			parent.playerName.setText("");
		}
	}
}
