package vendingmachine.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.FontLoader;
import vendingmachine.Main;
import vendingmachine.PictureLoader;
import vendingmachine.components.IMachine;

/**
 * This class creates a GUI of a vending machine using a IMachine object.
 */
public class VendingMachineGUI extends JFrame implements IMachineGUI, TemperatureListener {

  private static final long serialVersionUID = 1L;

  private final DoorJPanel leftPanel;
  
  private final IMachine machine;
  
  /*
   * The labels and text areas of the GUI.
   */
  private final JLabel northLabel;
  private final JLabel sugarLabel;
  private final JLabel temperatureLabel;
  private final JTextArea infoArea;
  
  /*
   * The buttons of the GUI.
   */
  private final List<DrinkJButton> drinkButtonsList;
  private final List<CoinJButton> coinButtonsList;
  private final JButton cupButton;
  private final JButton changeButton;
  private final JButton lessSugar;
  private final JButton moreSugar;
  private final JButton okButton;
  private final JButton cancelButton;
  
  private final JMenuBar menuBar;
  private final JMenuItem unstickCoins;

  /**
   * Timer that restarts each time a text must be displayed temporarily.
   */
  private final Timer textTimer;
  
  /**
   * Initializes the fields according to the Machine specified.
   * Associates the Machine specified and the VendingMachineGUI itself together.
   * The method {@code init()} must be called to display the frame.
   * 
   * @param machine the Machine to link with the GUI
   */
  public VendingMachineGUI(IMachine machine) {
    super();
    this.setTitle("Vending Machine");
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    this.machine = machine;
    this.machine.setObserver(this);

    leftPanel = new DoorJPanel(); // Makes possible the animation of the door
    leftPanel.setLayout(new BorderLayout());
    leftPanel.setPreferredSize(new Dimension(120, 550));
    
    northLabel = new JLabel();
    northLabel.setForeground(Color.RED);
    northLabel.setFont(FontLoader.DIGITAL_FONT);
    
    sugarLabel = new JLabel();
    if (FontLoader.DIGITAL_FONT != null) {
      sugarLabel.setFont(FontLoader.DIGITAL_FONT.deriveFont(16f));
    }
    sugarLabel.setForeground(Color.RED);
    sugarLabel.setIcon(PictureLoader.SUGAR_DISPLAY);
    sugarLabel.setHorizontalTextPosition(SwingConstants.CENTER);   
    
    temperatureLabel = new JLabel();
    temperatureLabel.setFont(FontLoader.DIGITAL_FONT);
    temperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);
    temperatureLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    temperatureLabel.setBackground(Color.WHITE);
    temperatureLabel.setOpaque(true);  
    
    infoArea = new JTextArea();
    infoArea.setEditable(false);
    
    drinkButtonsList = new ArrayList<DrinkJButton>();
    for (Drink drink: machine.getDrinks()) {
      drinkButtonsList.add(new DrinkJButton(drink));
    }
    
    coinButtonsList = new ArrayList<CoinJButton>();
    for (Coin coin: Coin.COINS) {
      coinButtonsList.add(new CoinJButton(coin));
    }
    
    cupButton = new JButton();
    cupButton.setBorder(BorderFactory.createEmptyBorder());
    cupButton.setContentAreaFilled(false);
    cupButton.setBorderPainted(false);
    
    changeButton = new JButton();
    changeButton.setBorder(BorderFactory.createEmptyBorder());
    changeButton.setContentAreaFilled(false);
    changeButton.setOpaque(true);
    changeButton.setBackground(Color.BLACK);
    changeButton.setPreferredSize(new Dimension(PictureLoader.CHANGE_ICON.getIconWidth(), 100));
    
    lessSugar = new JButton("-");
    moreSugar = new JButton("+");
    okButton = new JButton("Confirm");
    cancelButton = new JButton("Cancel");   
    
    menuBar = new JMenuBar();
    unstickCoins = new JMenuItem("Unstick stuck coins");
    
    textTimer = new Timer(2500, e -> updateNorthText());
    textTimer.setRepeats(false);
  }

  /**
   * Places all the components on the frame and makes it visible.
   */
  public void init() {
    final Container myContainer = getContentPane();

    // Main Panel
    final JPanel myPanel = new JPanel(new BorderLayout());

    // North Panel
    final JPanel northPanel = new BackgroundJPanel(PictureLoader.DISPLAY_PANEL);
    northPanel.add(northLabel);
    northPanel.setPreferredSize(new Dimension(100, 45));
    myPanel.add(northPanel, BorderLayout.PAGE_START);

    // Center Panel
    final JPanel centerPanel = new BackgroundJPanel(PictureLoader.COFFEE_IMAGE);
    centerPanel.setLayout(new GridLayout((machine.getDrinks().size() + 1) / 2, 2, 30, 0));
    for (DrinkJButton myButton: drinkButtonsList) {
      centerPanel.add(myButton);
    }
    myPanel.add(centerPanel, BorderLayout.CENTER);

    // Left Panel
    leftPanel.add(cupButton, BorderLayout.PAGE_END);
    leftPanel.add(temperatureLabel, BorderLayout.PAGE_START);
    myPanel.add(leftPanel, BorderLayout.LINE_START);

    // Right Panel
    final JPanel rightPanel = new BackgroundJPanel(PictureLoader.SLOT_IMAGE);
    rightPanel.setLayout(new GridBagLayout());
    final GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1;

    c.gridx = 0;  c.gridy = 0;  c.gridwidth = 2;
    rightPanel.add(sugarLabel, c);
    
    c.gridx = 0;  c.gridy = 1;  c.gridwidth = 1;
    rightPanel.add(lessSugar, c);
    
    c.gridx = 1;  c.gridy = 1;  c.gridwidth = 1;
    rightPanel.add(moreSugar, c);
    
    c.gridx = 0;  c.gridy = 2;  c.gridwidth = 2;
    rightPanel.add(okButton, c);
    
    c.gridx = 0;  c.gridy = 3;  c.gridwidth = 2;
    rightPanel.add(cancelButton, c);
    
    rightPanel.setPreferredSize(new Dimension(100, 550));
    myPanel.add(rightPanel, BorderLayout.LINE_END);

    // South Panel
    final JPanel southPanel = new JPanel();
    southPanel.setBackground(Color.DARK_GRAY);
    southPanel.setLayout(new BorderLayout());
    southPanel.add(changeButton, BorderLayout.LINE_END);
    myPanel.add(southPanel, BorderLayout.PAGE_END);
    southPanel.setPreferredSize(new Dimension(600, 100));

    // Coins buttons panel
    final JPanel coinsPanel = new JPanel(new GridLayout((Coin.COINS.size() + 1) / 2, 2, 5, 5));
    coinsPanel.setBackground(Color.WHITE);

    for (CoinJButton myButton: coinButtonsList) {
      coinsPanel.add(myButton);
    }
    coinsPanel.setMinimumSize(new Dimension(100, 100));

    // JMenuBar
    this.createMenuBar();

    // Information area
    final JPanel infoPanel = new JPanel();
    final JScrollPane scrInfoPanel = new JScrollPane(infoPanel);
    infoPanel.add(infoArea);
    infoPanel.setBackground(Color.WHITE);

    // Puts the three main panel side by side
    final JSplitPane leftPane = new JSplitPane();
    leftPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    leftPane.setLeftComponent(myPanel);
    leftPane.setRightComponent(coinsPanel);
    final JSplitPane rightPane = new JSplitPane();
    rightPane.setLeftComponent(leftPane);
    rightPane.setRightComponent(scrInfoPanel);
    myContainer.add(rightPane);
    
    // Ending operations
    this.setJMenuBar(menuBar);
    this.addListeners();
    this.updateUI();

    this.setMinimumSize(new Dimension(400, 400));
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }
  
  /**
   * Add the action listeners to all the buttons of the GUI
   * (the cup, the change, +, -, Confirm, Cancel, coins and drinks buttons).
   */
  private void addListeners() {
    cupButton.addActionListener(e -> {
      if (!leftPanel.animationIsRunning()) {
        machine.takeCup();
        }
      });
    changeButton.addActionListener(e -> machine.takeChange());
    lessSugar.addActionListener(e -> machine.less());
    moreSugar.addActionListener(e -> machine.more());
    okButton.addActionListener(e -> machine.confirm());
    cancelButton.addActionListener(e -> machine.cancel());

    for (DrinkJButton drinkButton: drinkButtonsList) {
      drinkButton.addActionListener(e -> machine.drinkButton(drinkButton.getDrink()));
    }
    for (CoinJButton coinButton: coinButtonsList) {
      coinButton.addActionListener(e -> machine.coinInserted(coinButton.getCoin()));
    }
  }
  
  /**
   * Creates the items of frame the menu bar and sets their action listeners.
   */
  private void createMenuBar() {
    final JMenu waterSupplyMenu = new JMenu("Water Supply");
    final JMenu coinsMenu = new JMenu("Coin Stock");
    final JMenu drinksMenu = new JMenu("Drink Stock");
    final JMenu repairMenu = new JMenu("Repair");
    final JMenu settings = new JMenu("Settings");
    menuBar.add(waterSupplyMenu);
    menuBar.add(coinsMenu);
    menuBar.add(drinksMenu);
    menuBar.add(repairMenu);
    menuBar.add(settings);
    
    final JCheckBoxMenuItem waterSupplyBox = new JCheckBoxMenuItem("Water supply enabled", true);
    final JMenuItem instantWarming = new JMenuItem("Instant Warming"); 
    final JMenuItem newVM = new JMenuItem("New Vending Machine");
    final JMenuItem quit = new JMenuItem("Quit");
    waterSupplyMenu.add(waterSupplyBox);
    waterSupplyMenu.add(instantWarming);
    repairMenu.add(unstickCoins);
    settings.add(newVM);
    settings.add(quit);
    
    waterSupplyBox.addActionListener(e -> machine.setWaterSupply(waterSupplyBox.isSelected()));
    instantWarming.addActionListener(e -> machine.setTemperature(93));
    unstickCoins.addActionListener(e -> machine.repairStuckCoins());
    unstickCoins.setEnabled(false);
    newVM.addActionListener(e -> {
      dispose();
      Main.run();
    });
    quit.addActionListener(e -> System.exit(0));
    
    for (Coin coin: Coin.COINS) {
      final JMenuItem item = new JMenuItem(coin.TEXT);
      item.addActionListener(e -> coinStockDialog(coin));
      coinsMenu.add(item);
    }
    
    for (Drink drink: machine.getDrinks()) {
      final JMenuItem item = new JMenuItem(drink.getName());
      item.addActionListener(e -> drinkStockDialog(drink));
      drinksMenu.add(item);
    }
  }

  @Override
  public void setChangeBool(boolean bool) {
    if (bool) {
      changeButton.setIcon(PictureLoader.CHANGE_ICON);
    } else {
      changeButton.setIcon(null);
    }
  }

  @Override
  public void setCupBool(boolean bool) {
    if (bool) {
      cupButton.setIcon(PictureLoader.CUP_ICON);
      leftPanel.doorAnimation();
    } else {
      cupButton.setIcon(null);
      leftPanel.closeDoor();
    }
  }
  
  @Override
  public void updateSugarText() {
    sugarLabel.setText(machine.getSugarText().toUpperCase());
  }

  @Override
  public void updateNorthText() {
    northLabel.setText(machine.getNorthText().toUpperCase());
  }
  
  @Override
  public void setTemporaryNorthText(String msg) {
    northLabel.setText(msg.toUpperCase(Locale.ENGLISH));
    textTimer.restart();
  }
  
  @Override
  public void updateChangeOutInfo() {
    changeButton.setToolTipText(machine.getChangeOutInfo());
  }
  
  @Override
  public void updateInfo() {
    infoArea.setText(machine.getInfo());
  }

  @Override
  public void updateUI() {
    updateInfo();
    updateNorthText();
    updateSugarText();
    updateChangeOutInfo();
  }

  @Override
  public void setTemperature(double temperature) {
    if (temperature < 0) {
      temperatureLabel.setText("NO WATER");
    } else {
      final DecimalFormat f = new DecimalFormat("#.#");
      temperatureLabel.setText(f.format(temperature) + " �C");
    }
  }

  /**
   * Displays a JOptionPane letting the user enter a new value for the stock of an item.
   * The value entered must be a positive integer, otherwise -1 is returned.
   * 
   * @param element the String of the name of the element to change
   * @return the new int value of the element
   */
  private int stockDialog(String element) {
    int value = -1;
    if (machine.isAvailableForMaintenance()) {
      final String inputValue = JOptionPane.showInputDialog(
          this, "Enter the new stock value for the " + element + ": ");
      try {
        value = Integer.parseInt(inputValue);
        if (value < 0) {
          throw new NumberFormatException();
        }
      } catch (NumberFormatException exc) {
        JOptionPane.showMessageDialog(
            this, "The value is not valid. Nothing has been changed.");
      }
    } else {
      JOptionPane.showMessageDialog(
          this, "Now is not the time to use that !"
              + "\nPlease end or wait for the end of the current operation.");
    }
    return value;
  }
  
  /**
   * Creates a JOptionPane to change the stock of the Coin specified.
   * 
   * @param coin the Coin whose stock value must be changed
   */
  private void coinStockDialog(Coin coin) {
    int value = stockDialog(coin.TEXT + " coin");
    if (value >= 0) {
      machine.setCoinStock(coin, value);
    }
  }
  
  /**
   * Creates a JOptionPane to change the stock of the Drink specified.
   * 
   * @param drink the Drink whose stock value must be changed
   */
  private void drinkStockDialog(Drink drink) {
    int value = stockDialog(drink.getName());
    if (value >= 0) {
      machine.setDrinkStock(drink, value);
    }
  }

  @Override
  public void enableRepair(boolean b) {
    unstickCoins.setEnabled(b);
  }
  
}