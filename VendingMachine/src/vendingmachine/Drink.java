package vendingmachine;

/**
 * Defines drinks objects consisting of a name, a price and a boolean "sugar".
 * The attributes are a String name, a price (in cents) and a
 * boolean "sugar" to tell if the drink is likely or not to contain sugar.
 */
public class Drink {

  /**
   * The name of the Drink.
   */
  private final String name;

  /**
   * The price of the Drink (in cents).
   */
  private final int price;

  /**
   * True if the Drink may contain sugar, false otherwise.
   */
  private final boolean sugar;

  /**
   * Creates a Drink instance with the specified attributes.
   * 
   * @param name the String name of the Drink
   * @param sugar true if the Drink can contain sugar, false otherwise
   * @param price the price of the Drink (in cents)
   */
  public Drink(String name, boolean sugar, int price) {
    this.name = name;
    this.sugar = sugar;
    this.price = price;
  }

  /**
   * @return the String name of the Drink
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return the price of the Drink, in cents
   */
  public int getPrice() {
    return this.price;
  }

  /**
   * @return true if the Drink may contain sugar, false otherwise
   */
  public boolean isSugar() {
    return this.sugar;
  }

}