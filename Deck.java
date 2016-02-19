// Deck.java
// Cole Ellison and Will Muir

import java.util.*; // for Stack and Random

public class Deck {

	// instance variables
	protected Stack<Integer> deck;

	// for cards, i/13 yeilds the suit s.t. 0=spades, 1=hearts,
	// 2=clubs, 3=diamonds
	// i%13 yeilds the value where 0=ace, 1=2, ... , 11=queen, 12=king

	// constructor
	public Deck() {
		deck = new Stack<Integer>();
		Random rand = new Random();
		int index;
		int[] sortedCards = sortedDeck();

		for (int i = 0; i < 52; i++) {
			index = Math.abs(rand.nextInt()) % 52;
			while (sortedCards[index] == -1) {
				index = Math.abs(rand.nextInt()) % 52;
			}
			deck.push(sortedCards[index]);
			sortedCards[index] = -1;
		}
	}

	// removes and returns the top card
	public int drawCard() {
		return deck.pop();
	}

	// peeks at the top card and returns its value
	public int topCard() {
		return deck.peek();
	}

	// static methods

	public static int[] parseCard(int card) {
		int suit = card / 13;
		int val = card % 13;
		int[] parsed = { suit, val };
		return parsed;
	}

	// returns a string identifying the card
	public static String cardSymbol(int[] card) {
		String cardString = "";
		// card type
		if (card[1] == 0)
			cardString = cardString + "A ";
		else if (card[1] < 10)
			cardString = cardString + (card[1] + 1) + " ";
		else if (card[1] == 10)
			cardString = cardString + "J ";
		else if (card[1] == 11)
			cardString = cardString + "Q ";
		else
			cardString = cardString + "K ";

		// suit
		if (card[0] == 0)
			cardString = cardString + "s";
		else if (card[0] == 1)
			cardString = cardString + "h";
		else if (card[0] == 2)
			cardString = cardString + "c";
		else
			cardString = cardString + "d";

		return cardString;
	}

	// creates deck of sorted "cards"
	public static int[] sortedDeck() {
		int[] sortedCards = new int[52];
		for (int i = 0; i < 52; i++) {
			sortedCards[i] = i;
		}
		return sortedCards;
	}

	// tests code
	public static void main(String[] args) {
		Deck mydeck = new Deck();
		int[] card = parseCard(mydeck.drawCard());
		System.out.println(Integer.toString(card[0]) + " "
				+ Integer.toString(card[1]));
		System.out.print(cardSymbol(card));
	}
}