package vendingmachine.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import vendingmachine.components.ChangeMachine;
import vendingmachine.components.Coin;
import vendingmachine.components.Context;
import vendingmachine.components.Drink;
import vendingmachine.components.Stock;

public class Configuration extends JFrame {

  private static final long serialVersionUID = 1L;

  private static final String[] COLUMNS_TITLES = { "  Enter here the drinks names:  ",
    "  Contains sugar?  ", "  Price (in cents):  ", "  Initial stock:  " };
  private static final String[] COINS_TITLES = { "Coins and their values:  ", "  Initial stock:  ",
    "  Accepted by the machine?  " };
  private static final String[] DEFAULT_DRINKS = { "Black Coffee", "Cappuccino", "Hot Chocolate",
    "Hot Milk", "Green Tea", "Earl Grey", "Tomato Soup", "Mushroom Soup", "Hot Water", "Oolong Tea" };
  private static final Integer[] NBR_DRINKS_LIST = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

  private JPanel myPanel;
  private JPanel drinkPanel;
  private JPanel coinPanel;
  private JPanel stockPanel;
  private JScrollPane scrPane;

  private MyDocumentFilter myDocumentFilter;

  private JLabel drinkNbrLabel; //Pas d'instance ?
  private JComboBox<Integer> drinkNbrComboBox;

  private JTextField[] drinksNames;
  private JCheckBox[] drinksSugar;
  private JTextField[] drinksPrices;
  private JTextField[] drinksStocks;
  private JTextField[] coinsStockValues;
  private JCheckBox[] acceptedCoinsBoxes;

  private JLabel sugarCubesNbrLabel; //Pas d'instance ?
  private JLabel cupsNbrLabel;
  private JLabel spoonsNbrLabel;
  private JTextField sugarCubesNbrValue;
  private JTextField cupsNbrValue;
  private JTextField spoonsNbrValue;

  private JButton createButton;
  private JLabel problemLabel;

  public Configuration() {
    super();
    setTitle("Vending Machine Initialization");

    drinkNbrComboBox = new JComboBox<Integer>(NBR_DRINKS_LIST);
    drinkNbrComboBox.setSelectedIndex(7); //to have 8 drinks as a default value
    drinkNbrComboBox.addActionListener(e -> again());

    myDocumentFilter = new MyDocumentFilter();

    drinkNbrLabel = new JLabel("Number of drinks (between 1 and 10): ");
    sugarCubesNbrLabel = new JLabel("Number of sugar cubes available: ");
    cupsNbrLabel = new JLabel("Number of cups available: ");
    spoonsNbrLabel = new JLabel("Number of spoons availables: ");
    
    sugarCubesNbrValue = new JTextField("20", 4);
    ((AbstractDocument)sugarCubesNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    cupsNbrValue = new JTextField("10", 4);
    ((AbstractDocument)cupsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    spoonsNbrValue = new JTextField("8", 4);
    ((AbstractDocument)spoonsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    
    problemLabel = new JLabel();
    problemLabel.setForeground(Color.RED);
    
    createButton = new JButton("Click here to begin the simulation!");
    createButton.addActionListener(e -> check());
    
    coinsStockValues = new JTextField[ChangeMachine.COINS.length];
    acceptedCoinsBoxes = new JCheckBox[ChangeMachine.COINS.length];
    
    drinksNames = new JTextField[10]; //A factoriser pour pas le refaire à chaque fois ???
    drinksSugar = new JCheckBox[10];
    drinksPrices = new JTextField[10];
    drinksStocks = new JTextField[10];
    
    for (int i = 0; i < 10; i++) {
      drinksNames[i] = new JTextField(18);
      if (i < DEFAULT_DRINKS.length) {
        drinksNames[i].setText(DEFAULT_DRINKS[i]);
      }
      drinksSugar[i] = new JCheckBox();
      if (i < 6) {
        drinksSugar[i].setSelected(true);
      }
      drinksPrices[i] = new JTextField("100", 5);
      ((AbstractDocument)drinksPrices[i].getDocument()).setDocumentFilter(myDocumentFilter);
      drinksStocks[i] = new JTextField("5", 5);
      ((AbstractDocument)drinksStocks[i].getDocument()).setDocumentFilter(myDocumentFilter);
    }
    
    for (int i = 0; i < ChangeMachine.COINS.length; i++) {
      coinsStockValues[i] = new JTextField("5", 4);
      ((AbstractDocument)coinsStockValues[i].getDocument()).setDocumentFilter(myDocumentFilter);
      acceptedCoinsBoxes[i] = new JCheckBox();
      if (i < ChangeMachine.COINS.length - 3) { // to refuse coins too small by default
        acceptedCoinsBoxes[i].setSelected(true);
      }
    }
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private void again() {
    getContentPane().removeAll();
    init(); //mettre à jour uniquement le drinkPanel
  }

  public void init() {
    myPanel = new JPanel();
    myPanel.setLayout(new GridBagLayout());
    GridBagConstraints c1 = new GridBagConstraints();
    c1.insets = new Insets(5, 5, 5, 5);

    final Integer NBR_DRINKS = (Integer)drinkNbrComboBox.getSelectedItem();
    final int NBR_COINS = ChangeMachine.COINS.length;

    c1.gridy = 0;
    myPanel.add(drinkNbrLabel, c1);
    c1.gridy = 1;
    myPanel.add(drinkNbrComboBox, c1);
    
    drinkPanel = new JPanel(new GridBagLayout());
    Border grayLine = BorderFactory.createLineBorder(Color.GRAY);
    drinkPanel.setBorder(BorderFactory.createTitledBorder(grayLine, "Drink Information"));
    GridBagConstraints c2 = new GridBagConstraints();
    c2.gridy = 0; c2.gridx = 0;
    for (int i = 0; i < COLUMNS_TITLES.length; i++) {
      drinkPanel.add(new JLabel(COLUMNS_TITLES[i]), c2);
      c2.gridx += 1;
    }

    for (int i = 0; i < NBR_DRINKS; i++) {
      c2.gridy += 1; c2.gridx = 0;
      drinkPanel.add(drinksNames[i], c2);
      c2.gridx = 1;
      drinkPanel.add(drinksSugar[i], c2);
      c2.gridx = 2;
      drinkPanel.add(drinksPrices[i], c2);
      c2.gridx = 3;
      drinkPanel.add(drinksStocks[i], c2);
    }
    
    c1.gridy = 2;
    myPanel.add(drinkPanel, c1);
    
    coinPanel = new JPanel(new GridBagLayout());
    coinPanel.setBorder(BorderFactory.createTitledBorder(grayLine, "Coin Information"));
    GridBagConstraints c3 = new GridBagConstraints();
    c3.gridy += 1; c3.gridx = 0;
    for (String s: COINS_TITLES) {
      coinPanel.add(new JLabel(s), c3);
      c3.gridx += 1;
    }

    for (int i = 0; i < NBR_COINS; i++) {
      c3.gridy += 1; c3.gridx = 0;
      coinPanel.add(new JLabel(ChangeMachine.COINS_TEXT[i]), c3);
      c3.gridx = 1;
      coinPanel.add(coinsStockValues[i], c3);
      c3.gridx = 2;
      coinPanel.add(acceptedCoinsBoxes[i], c3);
    }

    c1.gridy = 3;
    myPanel.add(coinPanel, c1);
    
    stockPanel = new JPanel(new GridBagLayout());
    stockPanel.setBorder(BorderFactory.createTitledBorder(grayLine, "Stock Information"));
    GridBagConstraints c4 = new GridBagConstraints();
    c4.gridy = 0;
    c4.gridx = 0;
    stockPanel.add(sugarCubesNbrLabel, c4);
    c4.gridx = 1;
    stockPanel.add(sugarCubesNbrValue, c4);

    c4.gridy += 1;
    c4.gridx = 0;
    stockPanel.add(cupsNbrLabel, c4);
    c4.gridx = 1;
    stockPanel.add(cupsNbrValue, c4);

    c4.gridy += 1;
    c4.gridx = 0;
    stockPanel.add(spoonsNbrLabel, c4);
    c4.gridx = 1;
    stockPanel.add(spoonsNbrValue, c4);

    c1.gridy = 4;
    myPanel.add(stockPanel, c1);
    
    c1.gridy = 5; c1.gridx = 0;
    c1.gridwidth = GridBagConstraints.REMAINDER;
    myPanel.add(createButton, c1);
    c1.gridy = 6;
    myPanel.add(problemLabel, c1);

    add(myPanel);
    scrPane = new JScrollPane(myPanel); // makes the frame scrollable in case it is shrunk
    add(scrPane);

    myPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void check() {
    // Fetches the values for the drinks
    List<Drink> drinkList = new ArrayList<Drink>();
    Map<Drink, Integer> drinkQty = new Hashtable<Drink, Integer>();
    try {
      for (int i = 0; i < (Integer)drinkNbrComboBox.getSelectedItem(); i++) {
        if (drinksNames[i].getText().equals("") || drinksNames[i].getText().length() > 18) {
          throw new IllegalArgumentException(); //Pas propre, pas la bonne exception ???
        }
        final Drink d = new Drink(drinksNames[i].getText(), drinksSugar[i].isSelected(),
            Integer.parseInt(drinksPrices[i].getText())); // final ici est suffisant ?
        drinkList.add(d);
        drinkQty.put(d, Integer.parseInt(drinksStocks[i].getText()));
      }
    } catch (IllegalArgumentException e) {
      problemLabel.setText(getProblemText("drink"));
      pack(); //propre ??
      return;
    }

    // Fetches the values for the change machine
    Map<Coin, Integer> coinsStock = new Hashtable<Coin, Integer>();
    Map<Coin, Boolean> coinsAccepted = new Hashtable<Coin, Boolean>();
    try {
      for (int i = 0; i < ChangeMachine.COINS.length; i++) {
        coinsStock.put(ChangeMachine.COINS[i], Integer.parseInt(coinsStockValues[i].getText()));
        coinsAccepted.put(ChangeMachine.COINS[i], acceptedCoinsBoxes[i].isSelected());
      }
    } catch (NumberFormatException e) {
      problemLabel.setText(getProblemText("change machine"));
      pack();
      return;
    }

    // Fetches the values for the stock
    int sugarCubeNbr = 0;
    int cupsNbr = 0;
    int spoonsNbr = 0;
    try {
      sugarCubeNbr = Integer.parseInt(sugarCubesNbrValue.getText());
      cupsNbr = Integer.parseInt(cupsNbrValue.getText());
      spoonsNbr = Integer.parseInt(spoonsNbrValue.getText());
    } catch (NumberFormatException e) {
      problemLabel.setText(getProblemText("stock"));
      pack();
      return;
    }
    Stock stock = new Stock(sugarCubeNbr, cupsNbr, spoonsNbr, drinkQty);

    Context c = new Context(
        (Integer)drinkNbrComboBox.getSelectedItem(), drinkList, coinsStock, coinsAccepted, stock);
    VendingMachineGUI gui = new VendingMachineGUI(c);
    dispose();

    gui.init();
  }

  private String getProblemText(String part) {
    return "<html>Error while parsing " + part + " info. Fields can't be empty.<br>"
        + "Names can't be longer than 18 characters. Integers can't be larger than 2^31.</html>";
  }

}
