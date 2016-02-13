package vendingmachine.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import vendingmachine.FontLoader;
import vendingmachine.PictureLoader;
import vendingmachine.components.ChangeMachine;
import vendingmachine.components.EventListener;
import vendingmachine.states.Idle; //Moyen !!

public class VendingMachineGUI extends JFrame implements ContextListener, TemperatureListener {

  private static final long serialVersionUID = 1L;

  private EventListener observer;
  private JLabel northLabel;
  private JLabel sugarLabel;
  private JLabel temperatureLabel;

  private JTextArea infoArea;
  private List<DrinkJButton> drinkButtonsList;
  private List<CoinJButton> coinButtonsList;
  private JButton cupButton;
  private JButton changeButton;
  private JButton lessSugar;
  private JButton moreSugar;
  private JButton okButton;
  private JButton cancelButton;
  
  private JMenuBar menuBar;
  private JMenu waterSupplyMenu;
  private JCheckBoxMenuItem waterSupplyCheckBox;
  private JMenu coinsMenu;
  private JMenu drinksMenu;
  private JMenu settings;
  private JMenuItem newVM;
  private JMenuItem quit;

  private Timer timer;

  public VendingMachineGUI(EventListener observer) {
    this.observer = observer;
    observer.setObserver(this);
    timer = new Timer(1500, e -> setNorthText(observer.getNorthText()));
    timer.setRepeats(false);
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
    
    newVM.addActionListener(e -> { dispose(); VendingMachineMain.run(); } );
    quit.addActionListener(e -> dispose());
    waterSupplyCheckBox.addActionListener(e -> observer.setWaterSupply(waterSupplyCheckBox.isSelected()));
  }

  public void init() {
    // Initializes Frame
    setTitle("Vending Machine");
    Container myContainer = getContentPane();

    // Main Panel
    JPanel myPanel = new JPanel(new BorderLayout());

    // North Panel
    JPanel northPanel = new BackgroundJPanel(PictureLoader.displayPanel);
    northLabel = new JLabel();
    northLabel.setForeground(Color.RED);
    northLabel.setFont(FontLoader.DIGITAL_FONT);
    northLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
    northPanel.add(northLabel);
    northPanel.setPreferredSize(new Dimension(100, 50));
    myPanel.add(northPanel, BorderLayout.PAGE_START);

    // Center Panel
    JPanel centerPanel = new BackgroundJPanel(PictureLoader.coffee);
    centerPanel.setLayout(new GridLayout((observer.getNbrDrinks() + 1) / 2, 2, 30, 0));
    drinkButtonsList = new ArrayList<DrinkJButton>();
    for (int i = 0; i < observer.getNbrDrinks(); i++) {
      DrinkJButton myButton = new DrinkJButton(observer.getDrinks().get(i));
      drinkButtonsList.add(myButton);
      centerPanel.add(myButton);
    }
    myPanel.add(centerPanel, BorderLayout.CENTER);

    // Left Panel
    JPanel leftPanel = new BackgroundJPanel(PictureLoader.leftPanel);
    leftPanel.setLayout(new BorderLayout());
    cupButton = new JButton();
    cupButton.setBorder(BorderFactory.createEmptyBorder());
    cupButton.setContentAreaFilled(false);
    leftPanel.setPreferredSize(new Dimension(120, 550));
    leftPanel.add(cupButton, BorderLayout.PAGE_END);
    temperatureLabel = new JLabel("90° C");
    temperatureLabel.setFont(FontLoader.DIGITAL_FONT);
    temperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);
    temperatureLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    temperatureLabel.setBackground(Color.WHITE);
    temperatureLabel.setOpaque(true);
    leftPanel.add(temperatureLabel, BorderLayout.PAGE_START);
    myPanel.add(leftPanel, BorderLayout.LINE_START);

    // Right Panel
    JPanel rightPanel = new BackgroundJPanel(PictureLoader.slot);
    rightPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH; // A expliquer
    c.weightx = 1;

    sugarLabel = new JLabel();
    sugarLabel.setFont(FontLoader.DIGITAL_FONT.deriveFont(16f));
    sugarLabel.setForeground(Color.RED);
    sugarLabel.setIcon(new ImageIcon(PictureLoader.sugarDisplay));
    sugarLabel.setHorizontalTextPosition(SwingConstants.CENTER);

    lessSugar = new JButton("-");
    moreSugar = new JButton("+");
    okButton = new JButton("Confirm");
    cancelButton = new JButton("Cancel");

    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 2;
    rightPanel.add(sugarLabel, c);
    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth = 1;
    rightPanel.add(lessSugar, c);
    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    rightPanel.add(moreSugar, c);
    c.gridx = 0;
    c.gridy = 2;
    c.gridwidth = 2;
    rightPanel.add(okButton, c);
    c.gridx = 0;
    c.gridy = 3;
    c.gridwidth = 2;
    rightPanel.add(cancelButton, c);
    rightPanel.setPreferredSize(new Dimension(100, 550));
    myPanel.add(rightPanel, BorderLayout.LINE_END);

    // South Panel
    JPanel southPanel = new BackgroundJPanel(PictureLoader.southPanel);
    southPanel.setLayout(new BorderLayout());
    changeButton = new JButton();
    changeButton.setBorder(BorderFactory.createEmptyBorder());
    changeButton.setContentAreaFilled(false);
    southPanel.add(changeButton, BorderLayout.LINE_END);
    myPanel.add(southPanel, BorderLayout.PAGE_END);
    southPanel.setPreferredSize(new Dimension(600, 100));

    // coins buttons
    JPanel coinsPanel = new JPanel(new GridLayout((ChangeMachine.COINS.length + 1) / 2, 2, 5, 5));
    coinsPanel.setBackground(Color.WHITE);
    coinButtonsList = new ArrayList<CoinJButton>();
    BufferedImage[] coinsImages = { PictureLoader.euro2, PictureLoader.euro1, PictureLoader.cent50,
        PictureLoader.cent20, PictureLoader.cent10, PictureLoader.cent5, PictureLoader.cent2,
        PictureLoader.cent1 };
    for (int i = 0; i < coinsImages.length; i++) {
      CoinJButton myButton = new CoinJButton(ChangeMachine.COINS[i], new ImageIcon(coinsImages[i]));
      coinButtonsList.add(myButton);
      coinsPanel.add(myButton);
    }

    // JMenuBar
    menuBar = new JMenuBar();
    waterSupplyMenu = new JMenu("Water Supply");
    waterSupplyCheckBox = new JCheckBoxMenuItem("Water supply enabled", true);
    coinsMenu = new JMenu("Coin Stock");
    drinksMenu = new JMenu("Drink Stock");
    settings = new JMenu("Settings");
    newVM = new JMenuItem("New Vending Machine");
    quit = new JMenuItem("Quit");
    menuBar.add(waterSupplyMenu);
    waterSupplyMenu.add(waterSupplyCheckBox);
    menuBar.add(coinsMenu);
    menuBar.add(drinksMenu);
    menuBar.add(settings);
    settings.add(newVM);
    settings.add(quit);

    // Information area
    JPanel infoPanel = new JPanel();
    JScrollPane scrInfoPanel = new JScrollPane(infoPanel);
    infoArea = new JTextArea();
    infoArea.setEditable(false);
    infoPanel.add(infoArea);
    
    infoPanel.setBackground(Color.WHITE);

    // Ending operations
    JSplitPane leftPane = new JSplitPane();
    leftPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    leftPane.setLeftComponent(myPanel);
    leftPane.setRightComponent(coinsPanel);
    JSplitPane rightPane = new JSplitPane();
    rightPane.setLeftComponent(leftPane);
    rightPane.setRightComponent(scrInfoPanel);
    myContainer.add(rightPane);
    setJMenuBar(menuBar);

    addListeners();
    observer.changeState(Idle.instance()); // moche mais où d'autre ??

    setMinimumSize(new Dimension(400, 400));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  @Override
  public void setChangeBool(boolean b) {
    if (b) {
      changeButton.setIcon(new ImageIcon(PictureLoader.changeImage));
    } else {
      changeButton.setIcon(null); // idem ??
    }
  }

  @Override
  public void setCupBool(boolean b) {
    if (b) {
      cupButton.setIcon(new ImageIcon(PictureLoader.cupImage));
    } else {
      cupButton.setIcon(null); // replace with a black picture ??
    }
  }

  @Override
  public void setInfo(String msg) {
    infoArea.setText(msg);
  }

  @Override
  public void setNorthText(String msg) {
    northLabel.setText(msg.toUpperCase());
  }

  @Override
  public void setSugarText(String msg) {
    sugarLabel.setText(msg.toUpperCase());
  }

  @Override
  public void setTemperature(double temperature) {
    if (temperature == -1) {
      temperatureLabel.setText("NO WATER");
    } else {
    DecimalFormat f = new DecimalFormat("#.#");
    temperatureLabel.setText(f.format(temperature) + " °C");
    }
  }

  @Override
  public void setTemporaryNorthText(String msg) {
    northLabel.setText(msg.toUpperCase());
    timer.restart();
  }

}