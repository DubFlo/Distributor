package vendingmachine.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import vendingmachine.components.*;

public class VendingMachineGUI implements ContextListener {

	private EventListener observer;
	
	private JLabel northLabel;
	private JLabel sugarLabel;
	JTextArea textArea;
	
	private List<DrinkJButton> drinkButtonsList;
	private List<CoinJButton> coinButtonsList;
	private JButton cupButton;
	private JButton changeButton;
	JButton lessSugar;
	JButton moreSugar;
	JButton okButton;
	JButton cancelButton;
	
	private static final String PATH = "src"+File.separator+"resources"+File.separator;
	private static final BufferedImage cupImage;
	private static final BufferedImage changeImage;
	static {
		BufferedImage cup;
		BufferedImage change;
		try {
			cup = ImageIO.read(new File(PATH + "change.png"));
			change = ImageIO.read(new File(PATH + "change.png"));
		}
		catch (IOException e) {
			cup = null;
			change = null;
			//log
		}
		cupImage = cup;
		changeImage = change;
	}
	
	public VendingMachineGUI(EventListener observer) throws IOException {
		this.observer = observer;
		observer.setContextListener(this);
		this.init();
	}

	private void init() throws IOException {
		//Initializes Frame
		JFrame myFrame = new JFrame("Vending Machine");
		Container myContainer = myFrame.getContentPane();
		
		//Main Panel
		JPanel myPanel = new JPanel(new BorderLayout());
		
		//North Panel
		JPanel northPanel = new BackgroundJPanel(PATH + "displayPanel.jpg");;
		northLabel = new JLabel();
		northLabel.setForeground(Color.RED);
		northLabel.setFont(new Font("courier new", Font.BOLD, 20));
		northLabel.setHorizontalAlignment(SwingConstants.LEFT);
		northPanel.add(northLabel);
		northPanel.setPreferredSize(new Dimension(100, 50));
		myPanel.add(northPanel, BorderLayout.PAGE_START);
		
		//Center Panel
		JPanel centerPanel = new BackgroundJPanel(PATH + "coffee3.png");
		centerPanel.setLayout(new GridLayout(4, 2, 30, 0));
		drinkButtonsList = new ArrayList<DrinkJButton>();
		for (int i = 0; i < 8; i++) {
			DrinkJButton myButton = new DrinkJButton(observer.getDrinks().get(i));
			drinkButtonsList.add(myButton);
			centerPanel.add(myButton);
		}
		myPanel.add(centerPanel, BorderLayout.CENTER);
		
		//Left Panel
		JPanel leftPanel = new BackgroundJPanel(PATH + "leftPanel.png");
		leftPanel.setLayout(new BorderLayout());
		cupButton = new JButton();
		cupButton.setBorder(BorderFactory.createEmptyBorder());
		cupButton.setContentAreaFilled(false);
		leftPanel.setPreferredSize(new Dimension(100, 550));
		leftPanel.add(cupButton, BorderLayout.PAGE_END);
		myPanel.add(leftPanel, BorderLayout.LINE_START);
		
		//Right Panel
		JPanel rightPanel = new BackgroundJPanel(PATH + "fente-de-luxe.jpg");
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH; //A expliquer
		
		sugarLabel = new JLabel();
		sugarLabel.setFont(new Font("courier new", Font.BOLD, 14));
		sugarLabel.setForeground(Color.RED);
		BufferedImage screenImage = ImageIO.read(new File(PATH + "displaySugar.jpg"));
		sugarLabel.setIcon(new ImageIcon(screenImage));
		sugarLabel.setHorizontalTextPosition(JLabel.CENTER);
		
		lessSugar = new JButton("-");
		moreSugar = new JButton("+");
		okButton = new JButton("Confirm");
		cancelButton = new JButton("Cancel");
		
		c.gridx = 0;	c.gridy = 0;	c.gridwidth = 2;
		rightPanel.add(sugarLabel, c);
		c.gridx = 0;	c.gridy = 1;	c.gridwidth = 1;
		rightPanel.add(lessSugar, c);
		c.gridx = 1;	c.gridy = 1;	c.gridwidth = 1;
		rightPanel.add(moreSugar, c);
		c.gridx = 0;	c.gridy = 2;	c.gridwidth = 2;
		rightPanel.add(okButton, c);
		c.gridx = 0;	c.gridy = 3;	c.gridwidth = 2;
		rightPanel.add(cancelButton, c);
		rightPanel.setPreferredSize(new Dimension(100, 550));
		myPanel.add(rightPanel, BorderLayout.LINE_END);
		
		//South Panel
		JPanel southPanel = new BackgroundJPanel(PATH + "southPanel.png");
		southPanel.setLayout(new BorderLayout());
		changeButton = new JButton();
		changeButton.setBorder(BorderFactory.createEmptyBorder());
		changeButton.setContentAreaFilled(false);
		southPanel.add(changeButton, BorderLayout.LINE_END);
		myPanel.add(southPanel, BorderLayout.PAGE_END);
		southPanel.setPreferredSize(new Dimension(600, 100));
		
		//coins buttons
		JPanel coinsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
		coinsPanel.setBackground(Color.WHITE);
		coinButtonsList = new ArrayList<CoinJButton>();
		final String[] coinsFiles = {PATH + "2euro.png", PATH + "1euro.png", PATH + "50cent.png",
				PATH + "20cent.png", PATH + "10cent.png", PATH + "5cent.png", PATH + "2cent.png",
				PATH + "1cent.png"};
		for (int i = 0; i < 8; i++) {
			CoinJButton myButton = new CoinJButton(ChangeMachine.COINS[i], new ImageIcon(coinsFiles[i]));
			coinButtonsList.add(myButton);
			coinsPanel.add(myButton);
		}
		
		//JMenuBar
		JMenuBar menuBar = new JMenuBar();
		JMenu waterSupplyMenu = new JMenu("Water Supply");
		JMenu coinsMenu = new JMenu("Coin Stock");
		JMenu drinksMenu = new JMenu("Drink Stock");
		JMenu settings = new JMenu("Settings");
		menuBar.add(waterSupplyMenu);
		menuBar.add(coinsMenu);
		menuBar.add(drinksMenu);
		menuBar.add(settings);
		
		//Information area
		JPanel infoPanel = new JPanel();
		textArea = new JTextArea();
		textArea.setEditable(false);
		infoPanel.add(textArea);
		infoPanel.setBackground(Color.WHITE);
		
		//
		addListeners();
		observer.changeState(vendingmachine.states.Idle.getInstance());
		
		//Ending operations
		JSplitPane leftPane= new JSplitPane();
		leftPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		leftPane.setLeftComponent(myPanel);
		leftPane.setRightComponent(coinsPanel);
        JSplitPane rightPane = new JSplitPane();
        rightPane.setLeftComponent(leftPane);
        rightPane.setRightComponent(infoPanel);
		myFrame.setJMenuBar(menuBar);
		myContainer.add(rightPane);
		
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);
	}
	
	private void addListeners() {
		cupButton.addActionListener(e -> observer.takeCup());
		changeButton.addActionListener(e -> observer.takeChange());
		lessSugar.addActionListener(e -> observer.less());
		moreSugar.addActionListener(e -> observer.more());
		okButton.addActionListener(e -> observer.confirm());
		cancelButton.addActionListener(e -> observer.cancel());
		
		for (int i = 0; i < drinkButtonsList.size(); i++) {
			DrinkJButton d = drinkButtonsList.get(i);
			d.addActionListener(e -> observer.drinkButton(d.getDrink()));
		}
		for (int i = 0; i < coinButtonsList.size(); i++) {
			CoinJButton b = coinButtonsList.get(i);
			b.addActionListener(e -> observer.coinInserted(b.getCoin()));
		}
		
	}

	public void updateGUI() {
		// TODO - implement VendingMachineGUI.updateGUI
		
	}

	@Override
	public void setNorthText(String msg) {
		northLabel.setText(msg);
	}

	@Override
	public void setTempNorthText(String msg) {
		northLabel.setText(msg);
		//+++
		//
		//
		//
	}

	@Override
	public void setInfo() {
		textArea.setText(observer.getInfo());	
	}

	@Override
	public void setSugarText(String msg) {
		sugarLabel.setText(msg);
	}

	@Override
	public void setCupBool(boolean b) {
		if (b) cupButton.setIcon(new ImageIcon(cupImage));
		else cupButton.setIcon(null);
	}

	@Override
	public void setChangeBool(boolean b) {
		if (b) changeButton.setIcon(new ImageIcon(changeImage));
		else changeButton.setIcon(null);
	}
	
}