package vendingmachine.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import vendingmachine.components.ChangeMachine;
import vendingmachine.components.Coin;
import vendingmachine.components.Context;
import vendingmachine.components.Drink;
import vendingmachine.components.Stock;

public class Configuration extends JFrame {

  private static final long serialVersionUID = 1L;
  private static final String[] COLUMNS_TITLES = { "Enter here the drinks names: ", "Contains sugar ?",
    "Price (in cents)  ", "Initial stock" };
  private static final String[] COINS_TITLES = { "Coins and their values: ", "Initial stock",
    "Accepted by the machine?" };
  public static final String[] DEFAULT_DRINKS = { "Black Coffee", "Cappuccino", "Hot Chocolate", "Hot Milk",
    "Green Tea", "Earl Grey", "Tomato Soup", "Mushroom Soup" };

  private JTextField[] drinksNames;
  private JCheckBox[] drinksSugar;
  private JTextField[] drinksPrices;
  private JTextField[] drinksStocks;
  private JTextField[] coinsStockValues;
  private JCheckBox[] acceptedCoinsBoxes;

  private JLabel sugarCubesNbrLabel;
  private JLabel cupsNbrLabel;
  private JLabel spoonsNbrLabel;
  private JTextField sugarCubesNbrValue;
  private JTextField cupsNbrValue;
  private JTextField spoonsNbrValue;

  private JButton createButton;
  private JLabel problemLabel;

  private List<Drink> drinkList;
  private ChangeMachine changeMachine;
  private Stock stock;

  public void init() {
    setTitle("Vending Machine Initialization");
    JPanel myPanel = new JPanel();
    myPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    MyDocumentFilter myDocumentFilter = new MyDocumentFilter();

    final int NBR_DRINKS = VendingMachineGUI.NBR_DRINKS; //Est-ce propre ou juste l'écrire à chaque fois ?
    final int NBR_COINS = ChangeMachine.COINS.length;

    c.gridy = 0;
    for (int i = 0; i < COLUMNS_TITLES.length; i++) {
      c.gridx += 1;
      myPanel.add(new JLabel(COLUMNS_TITLES[i]), c);
    }

    drinksNames = new JTextField[NBR_DRINKS];
    drinksSugar = new JCheckBox[NBR_DRINKS];
    drinksPrices = new JTextField[NBR_DRINKS];
    drinksStocks = new JTextField[NBR_DRINKS];

    for (int i = 0; i < NBR_DRINKS; i++) {
      c.gridy += 1;
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
      c.gridx = 0;
      myPanel.add(drinksNames[i], c);
      c.gridx = 1;
      myPanel.add(drinksSugar[i], c);
      c.gridx = 2;
      myPanel.add(drinksPrices[i], c);
      c.gridx = 3;
      myPanel.add(drinksStocks[i], c);
    }

    c.gridy += 1;
    for (int i = 0; i < COINS_TITLES.length; i++) {
      c.gridx = i;
      myPanel.add(new JLabel(COINS_TITLES[i]), c);
    }

    coinsStockValues = new JTextField[NBR_COINS];
    acceptedCoinsBoxes = new JCheckBox[NBR_COINS];
    for (int i = 0; i < NBR_COINS; i++) {
      c.gridy += 1;
      coinsStockValues[i] = new JTextField("5", 4);
      ((AbstractDocument)coinsStockValues[i].getDocument()).setDocumentFilter(myDocumentFilter);
      acceptedCoinsBoxes[i] = new JCheckBox();
      if (i < NBR_COINS - 3) { // to refuse coins too small by default
        acceptedCoinsBoxes[i].setSelected(true);
      }
      c.gridx = 0;
      myPanel.add(new JLabel(ChangeMachine.COINS_TEXT[i]), c);
      c.gridx = 1;
      myPanel.add(coinsStockValues[i], c);
      c.gridx = 2;
      myPanel.add(acceptedCoinsBoxes[i], c);
    }

    sugarCubesNbrLabel = new JLabel("Number of sugar cubes available: ");
    cupsNbrLabel = new JLabel("Number of cups available: ");
    spoonsNbrLabel = new JLabel("Number of spoons availables: ");
    sugarCubesNbrValue = new JTextField("20", 4);
    ((AbstractDocument)sugarCubesNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    cupsNbrValue = new JTextField("10", 4);
    ((AbstractDocument)cupsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    spoonsNbrValue = new JTextField("8", 4);
    ((AbstractDocument)spoonsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);

    c.gridy += 1;
    c.gridx = 0;
    myPanel.add(sugarCubesNbrLabel, c);
    c.gridx = 1;
    myPanel.add(sugarCubesNbrValue, c);

    c.gridy += 1;
    c.gridx = 0;
    myPanel.add(cupsNbrLabel, c);
    c.gridx = 1;
    myPanel.add(cupsNbrValue, c);

    c.gridy += 1;
    c.gridx = 0;
    myPanel.add(spoonsNbrLabel, c);
    c.gridx = 1;
    myPanel.add(spoonsNbrValue, c);

    createButton = new JButton("Click here to begin the simulation !");
    problemLabel = new JLabel();
    problemLabel.setForeground(Color.RED);
    c.gridy += 1;
    c.gridx = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    createButton.addActionListener(e -> check());
    myPanel.add(createButton, c);
    c.gridy += 1;
    myPanel.add(problemLabel, c);

    add(myPanel);
    JScrollPane scrPane = new JScrollPane(myPanel); // makes the frame scrollable in case it is shrunk
    add(scrPane);

    myPanel.setBorder(new EmptyBorder(10, 10, 18, 10));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }

  private void check() {
    // Fetches the values for the drinks
    drinkList = new ArrayList<Drink>();
    Map<Drink, Integer> drinkQty = new Hashtable<Drink, Integer>();
    try {
      for (int i = 0; i < VendingMachineGUI.NBR_DRINKS; i++) {
        final Drink d = new Drink(drinksNames[i].getText(), drinksSugar[i].isSelected(),
            Integer.parseInt(drinksPrices[i].getText())); // final ici est suffisant ?
        drinkList.add(d);
        drinkQty.put(d, Integer.parseInt(drinksStocks[i].getText()));
      }
    } catch (NumberFormatException e) {
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
    changeMachine = new ChangeMachine(coinsStock, coinsAccepted);

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
    stock = new Stock(sugarCubeNbr, cupsNbr, spoonsNbr, drinkQty);

    Context c = new Context(drinkList, changeMachine, stock);
    VendingMachineGUI gui = new VendingMachineGUI(c);
    dispose();

    gui.init();
  }

  private String getProblemText(String part) {
    return "Error while parsing " + part + " info. Fields can't be empty. Integers can't be larger than 2^31.";
  }
}
