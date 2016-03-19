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
 * It is a subclass of JFrame.
 */
public class VendingMachineGUI extends JFrame implements IMachineGUI, TemperatureListener {

  private static final long serialVersionUID = 1L;
  
  /**
   * Used to format the display of the temperature.
   */
  private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

  /**
   * The DoorJPanel allowing the animation of the door.
   */
  private final DoorJPanel leftPanel;

  /**
   * The IMachine associated with the frame.
   * The frame must be updated by the IMachine after each change.
   * The frame notifies the IMachine on each button presses.
   */
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

  /*
   * The JMenu and JMenuItems that are sometimes updated.
   * The other ones are local variables of {@code createMenuBar()}.
   */
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
    final PictureLoader pictures = PictureLoader.getInstance();

    this.machine = machine;
    this.machine.setObserver(this);

    leftPanel = new DoorJPanel(); // Makes possible the animation of the door
    leftPanel.setLayout(new BorderLayout());
    leftPanel.setPreferredSize(new Dimension(120, 550));

    northLabel = new JLabel();
    northLabel.setForeground(Color.WHITE);
    northLabel.setFont(FontLoader.getInstance().DIGITAL_FONT);

    sugarLabel = new JLabel();
    if (FontLoader.getInstance().DIGITAL_FONT != null) {
      sugarLabel.setFont(FontLoader.getInstance().DIGITAL_FONT.deriveFont(16f));
    }
    sugarLabel.setForeground(Color.WHITE);
    sugarLabel.setIcon(pictures.SUGAR_DISPLAY);
    sugarLabel.setHorizontalTextPosition(SwingConstants.CENTER);

    temperatureLabel = new JLabel();
    temperatureLabel.setFont(FontLoader.getInstance().DIGITAL_FONT);
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

    changeButton = new JButton();
    changeButton.setBorder(BorderFactory.createEmptyBorder());
    changeButton.setContentAreaFilled(false);
    changeButton.setOpaque(true);
    changeButton.setBackground(Color.BLACK);
    changeButton.setPreferredSize(new Dimension(pictures.CHANGE_ICON.getIconWidth(), 100));

    lessSugar = new JButton("-");
    moreSugar = new JButton("+");
    okButton = new JButton("Confirm");
    cancelButton = new JButton("Cancel");

    menuBar = new JMenuBar();
    unstickCoins = new JMenuItem("Unstick Stuck Coins");

    textTimer = new Timer(2500, e -> updateNorthText());
    textTimer.setRepeats(false); // stops after one iteration
  }

  /**
   * Places all the components on the frame and makes it visible.
   */
  public void init() {
    this.setTitle("Vending Machine");
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    final Container myContainer = getContentPane();
    final PictureLoader pictures = PictureLoader.getInstance();

    // Main machine panel
    final JPanel machinePanel = new JPanel(new BorderLayout());

    // North machine panel
    final JPanel northPanel = new BackgroundJPanel(pictures.DISPLAY_PANEL);
    northPanel.add(northLabel);
    northPanel.setPreferredSize(new Dimension(100, 45));
    machinePanel.add(northPanel, BorderLayout.PAGE_START);

    // Center machine panel
    final JPanel centerPanel = new BackgroundJPanel(pictures.COFFEE_IMAGE);
    centerPanel.setLayout(new GridLayout((machine.getDrinks().size() + 1) / 2, 2, 30, 0));
    for (DrinkJButton myButton: drinkButtonsList) {
      centerPanel.add(myButton);
    }
    machinePanel.add(centerPanel, BorderLayout.CENTER);

    // Left machine panel
    leftPanel.add(cupButton, BorderLayout.PAGE_END);
    leftPanel.add(temperatureLabel, BorderLayout.PAGE_START);
    machinePanel.add(leftPanel, BorderLayout.LINE_START);

    // Right machine panel
    final JPanel rightPanel = new BackgroundJPanel(pictures.SLOT_IMAGE);
    rightPanel.setLayout(new GridBagLayout());
    final GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1;

    c.gridx = 0;  c.gridy = 0;  c.gridwidth = 2;
    rightPanel.add(sugarLabel, c);

    c.gridy = 1;  c.gridwidth = 1;
    rightPanel.add(lessSugar, c);

    c.gridx = 1;  c.gridy = 1;
    rightPanel.add(moreSugar, c);

    c.gridx = 0;  c.gridy = 2;  c.gridwidth = 2;
    rightPanel.add(okButton, c);

    c.gridx = 0;  c.gridy = 3;  c.gridwidth = 2;
    rightPanel.add(cancelButton, c);

    rightPanel.setPreferredSize(new Dimension(100, 550));
    machinePanel.add(rightPanel, BorderLayout.LINE_END);

    // South Panel
    final JPanel southPanel = new JPanel(new BorderLayout());
    southPanel.setBackground(Color.DARK_GRAY);
    southPanel.add(changeButton, BorderLayout.LINE_END);
    machinePanel.add(southPanel, BorderLayout.PAGE_END);
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

    // Information area (on the right)
    final JPanel infoPanel = new JPanel();
    final JScrollPane scrInfoPanel = new JScrollPane(infoPanel);
    infoPanel.add(infoArea);
    infoPanel.setBackground(Color.WHITE);

    // Puts the three main panel side by side
    final JSplitPane leftPane = new JSplitPane();
    leftPane.setLeftComponent(machinePanel);
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
    final JMenu otherMenu = new JMenu("Other");
    final JMenu exit = new JMenu("Exit");
    menuBar.add(waterSupplyMenu);
    menuBar.add(coinsMenu);
    menuBar.add(drinksMenu);
    menuBar.add(otherMenu);
    menuBar.add(exit);

    final JCheckBoxMenuItem waterSupplyBox = new JCheckBoxMenuItem("Water supply enabled", true);
    final JMenuItem instantWarming = new JMenuItem("Instant Warming");
    final JMenuItem newVM = new JMenuItem("New Vending Machine");
    final JMenuItem quit = new JMenuItem("Quit");
    waterSupplyMenu.add(waterSupplyBox);
    waterSupplyMenu.add(instantWarming);
    otherMenu.add(unstickCoins);
    exit.add(newVM);
    exit.add(quit);

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

    final JMenuItem cupNbrItem = new JMenuItem("Cups Stock");
    cupNbrItem.addActionListener(e -> cupStockDialog());
    otherMenu.add(cupNbrItem);
    
    final JMenuItem sugarNbrItem = new JMenuItem("Sugar Stock");
    sugarNbrItem.addActionListener(e -> sugarStockDialog());
    otherMenu.add(sugarNbrItem);
    
    final JMenuItem spoonsNbrItem = new JMenuItem("Spoons Stock");
    spoonsNbrItem.addActionListener(e -> spoonsStockDialog());
    otherMenu.add(spoonsNbrItem);
  }

  @Override
  public void setChangeBool(boolean bool) {
    if (bool) {
      PictureLoader pictures = PictureLoader.getInstance();
      changeButton.setIcon(pictures.CHANGE_ICON);
    } else {
      changeButton.setIcon(null);
    }
  }

  @Override
  public void setCupBool(boolean cup, boolean spoon) {
    PictureLoader pictures = PictureLoader.getInstance();
    if (cup) {
      if (spoon) {
        cupButton.setIcon(pictures.CUP_ICON);
      } else {
        cupButton.setIcon(pictures.CUP_ICON);
      }
      leftPanel.openDoor();
    } else {
      cupButton.setIcon(pictures.GRAY_RECTANGLE);
      leftPanel.closeDoor();
    }
  }

  @Override
  public void updateSugarText() {
    sugarLabel.setText(machine.getSugarText().toUpperCase(Locale.ENGLISH));
  }

  @Override
  public void updateNorthText() {
    northLabel.setText(machine.getNorthText().toUpperCase(Locale.ENGLISH));
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
  public void setTemporaryNorthText(String msg) {
    northLabel.setText(msg.toUpperCase(Locale.ENGLISH));
    textTimer.restart();
  }

  @Override
  public void enableRepair(boolean bool) {
    unstickCoins.setEnabled(bool); 
  }

  @Override
  public void setTemperature(double temperature) {
    if (temperature < 0) {
      temperatureLabel.setText("NO WATER");
    } else {
      temperatureLabel.setText(FORMAT.format(temperature) + " \u00B0C");
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
          this, "Enter the new value for the " + element + " stock: ");
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
          this, "Now is not the time to use that!"
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
    final int value = stockDialog(coin.TEXT + " coin");
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
    final int value = stockDialog(drink.getName());
    if (value >= 0) {
      machine.setDrinkStock(drink, value);
    }
  }

  /**
   * Creates a JOptionPane to change the number of cups in Stock.
   */
  private void cupStockDialog() {
    final int value = stockDialog("cups");
    if (value >= 0) {
      machine.setCupStock(value);
    }
  }

  /**
   * Creates a JOptionPane to change the number of sugar cubes in Stock.
   */
  private void sugarStockDialog() {
    final int value = stockDialog("sugar");
    if (value >= 0) {
      machine.setSugarStock(value);
    }
  }
  
  /**
   * Creates a JOptionPane to change the number of spoons in Stock.
   */
  private void spoonsStockDialog() {
    final int value = stockDialog("spoon");
    if (value >= 0) {
      machine.setSpoonsStock(value);
    }
  }
}