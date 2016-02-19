import java.applet.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class BlackJackApplet extends Applet implements ActionListener {

	protected BlackJackCanvas canvas;
	protected Button surrenderButton, hitButton, quitButton, standButton,
			betButton, doubleButton, newButton, nameButton;
	// protected Vector<BlackJackPlayer> players; // array of all players still
	// in the game, including the house
	// array of CardPlayer instead?
	protected Label playerNum;
	protected Label lastWinner;
	protected Label playerInfo;
	protected TextField betField;
	protected TextField playerName;
	protected BlackJack game;
	protected Choice numPlayers;

	final int NUM_PLAYERS = 5;
	
	public void init() {
		// create game
		game = new BlackJack(NUM_PLAYERS);
		game.deal();
		// intiate labels and textfields
		playerNum = new Label("");
		playerInfo = new Label("");
		lastWinner = new Label("");
		betField = new TextField(3);
		playerName = new TextField(10);
		// make canvas;
		canvas = new BlackJackCanvas(this, game);
		// design layout
		this.setLayout(new BorderLayout());
		this.add("North", titlePanel());
		this.add("South", playPanel());
		this.add("Center", canvas);
		// start game play
		canvas.bet(); // first player ante up
		canvas.resetPlayerLabels();
		this.setSize(650, 550);
	}

	public Panel titlePanel() {
		setFont(new Font("Helvetica", Font.BOLD, 14));
		Panel tPanel = new Panel();
		tPanel.setLayout(new GridLayout(4, 1));
		Label title = new Label("BLACKJACK");
		title.setBackground(Color.green.darker());
		title.setAlignment(Label.CENTER);
		tPanel.add(title);
		tPanel.add(playerLabel());
		tPanel.add(infoLabel());
		tPanel.add(lastWinnerLabel());
		return tPanel;
	}

	public Label playerLabel() {
		playerNum.setAlignment(Label.CENTER);
		playerNum.setBackground(Color.green.darker());
		return playerNum;
	}

	public Label infoLabel() {
		playerInfo.setAlignment(Label.CENTER);
		playerInfo.setBackground(Color.green.darker());
		return playerInfo;
	}

	public Label lastWinnerLabel() {
		lastWinner.setAlignment(Label.CENTER);
		lastWinner.setBackground(Color.green.darker());
		return lastWinner;
	}

	public Panel playPanel() {
		Panel playPanel = new Panel();
		playPanel.setLayout(new GridLayout(2,1));
		setFont(new Font("Helvetica", Font.BOLD, 12));
		Panel pPanel = new Panel();
		pPanel.setLayout(new FlowLayout());
		pPanel.setBackground(Color.black);
		Panel pPanel2 = new Panel();
		pPanel2.setLayout(new FlowLayout());
		pPanel2.setBackground(Color.black);
		hitButton = playButton("HIT");
		hitButton.addActionListener(this);
		standButton = playButton("STAND");
		standButton.addActionListener(this);
		doubleButton = playButton("DOUBLE");
		doubleButton.addActionListener(this);
		surrenderButton = playButton("SURRENDER");
		surrenderButton.addActionListener(this);
		nameButton = playButton("SET NAME");
		nameButton.addActionListener(this);
		betButton = playButton("BET");
		betButton.addActionListener(this);
		quitButton = playButton("NEW GAME");
		quitButton.addActionListener(this);
		newButton = playButton("NEW HAND");
		newButton.addActionListener(this);
		// create panels
		pPanel.add(namePanel());
		pPanel.add(hitButton);
		pPanel.add(standButton);
		pPanel.add(surrenderButton);
		pPanel2.add(betPanel());
		pPanel2.add(doubleButton);
		pPanel2.add(newButton);
		pPanel2.add(playersChoice());
		// add to large panel
		playPanel.add(pPanel);
		playPanel.add(pPanel2);
		return playPanel;
	}

	public Button playButton(String s) {
		Button pButton = new Button(s);
		pButton.setBackground(Color.green.darker());
		return pButton;
	}

	public Panel betPanel() {
		Panel bets = new Panel();
		bets.setLayout(new FlowLayout());
		betField.addActionListener(this);
		bets.add(betField);
		bets.add(betButton);
		return bets;
	}
	
	public Panel namePanel() {
		Panel name = new Panel();
		name.setLayout(new FlowLayout());
		playerName.addActionListener(this);
		name.add(playerName);
		name.add(nameButton);
		return name;
	}

	public Panel playersChoice() {
		Panel newGame = new Panel();
		newGame.setLayout(new FlowLayout());
		numPlayers = new Choice();
		for (int i = 5; i>0; i--) {
			numPlayers.add(Integer.toString(i));
		}
		numPlayers.setBackground(Color.white);
		newGame.add(numPlayers);
		newGame.add(quitButton);
		return newGame;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == surrenderButton)
			canvas.surrender();
		else if (e.getSource() == hitButton)
			canvas.hit();
		else if (e.getSource() == quitButton)
			canvas.quit();
		else if (e.getSource() == standButton)
			canvas.stand();
		else if (e.getSource() == doubleButton)
			canvas.doubleDown();
		else if (e.getSource() == newButton)
			canvas.newGame();
		else if (e.getSource() == nameButton)
			canvas.namePlayer();
		else
			canvas.bet();
	}

	public void play() {

	}

}
