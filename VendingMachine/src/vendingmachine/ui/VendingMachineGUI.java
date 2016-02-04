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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import vendingmachine.components.*;

public class VendingMachineGUI implements ContextListener {

	private EventListener observer;
	
	private JLabel northLabel;
	private JLabel sugarLabel;
	
	private List<DrinkJButton> drinkButtonsList;
	private List<CoinJButton> coinButtonsList;
	private JButton cupButton;
	private JButton changeButton;
	
	private final static String PATH = "src"+File.separator+"resources"+File.separator;
	private final static BufferedImage cupImage = ImageIO.read(new File(PATH + "cup.jpg"));
	private final static BufferedImage changeImage = ImageIO.read(new File(PATH + "change.png"));

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
		
		JButton lessSugar = new JButton("-");
		JButton moreSugar = new JButton("+");
		JButton okButton = new JButton("Confirm");
		JButton cancelButton = new JButton("Cancel");
		
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
		
		
		
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);
	}
	
	public void updateGUI() {
		// TODO - implement VendingMachineGUI.updateGUI
		
	}

	public void updateInfo() {
		// TODO - implement VendingMachineGUI.updateInfo
		
	}

	@Override
	public void setNorthText(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTempNorthText(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInfo(String txt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSugarText(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCupIcon() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChangeIcon() {
		// TODO Auto-generated method stub
		
	}

}