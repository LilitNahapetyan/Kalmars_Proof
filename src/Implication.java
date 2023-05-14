/*This code defines an enumeration called Implication that represents the four possible cases of 
the implication operator. Each case is defined as a constant value of the enumeration, with its 
corresponding a and b values. The getCase method takes two integer arguments (a and b) and returns 
the corresponding Implication case based on the values of a and b. The getA and getB methods are getter 
methods that allow the a and b values of each case to be accessed from outside the enumeration. */

public enum Implication {
  FIRST(0, 0),
  SECOND(1, 1),
  THIRD(1, 0),
  FOURTH(0, 1);

  private final int a;
  private final int b;

  Implication(int a, int b) {
    this.a = a;
    this.b = b;
  }

  public int getA() {
    return a;
  }

  public int getB() {
    return b;
  }

  public static Implication getCase(int a, int b) {
    switch (a * 2 + b) {
      case 0:
        return FIRST;
      case 1:
        return FOURTH;
      case 2:
        return THIRD;
      case 3:
        return SECOND;
      default:
        throw new IllegalArgumentException(
          "Invalid values for a and b: " + a + ", " + b
        );
    }
  }
}
