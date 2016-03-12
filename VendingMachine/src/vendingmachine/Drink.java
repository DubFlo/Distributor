package vendingmachine;

/**
 * Defines drinks objects consisting of a name, a price (in cents) and a 
 * boolean "sugar" to tell if the drink is likely or not to contain sugar.
 */
public class Drink {

  private final String name;
  private final int price;
  private final boolean sugar;

  public Drink(String name, boolean sugar, int price) {
    this.name = name;
    this.sugar = sugar;
    this.price = price;
  }

  /**
   * @return the name of the Drink.
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return the price of the Drink, in cents.
   */
  public int getPrice() {
    return this.price;
  }

  /**
   * @return true if the Drink can contain sugar, false otherwise.
   */
  public boolean isSugar() {
    return this.sugar;
  }

}