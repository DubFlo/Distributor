package vendingmachine.components;

/**
 * Defines drinks objects consisting of a name, a price and a 
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

  public String getName() {
    return this.name;
  }

  public int getPrice() {
    return this.price;
  }

  public boolean isSugar() {
    return this.sugar;
  }

}