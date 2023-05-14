import java.util.Map;
import java.util.Stack;

public class Expression {

  private static Stack<Character> stack = new Stack<>(); // Create a stack to store operators
  private static Stack<Integer> intStack = new Stack<>(); //Create a stack to store the operands of the logical formula as integers
  public static Map<Character, Integer> values; // Declare a map to store the values of variables

  // This method calculates the value of a logical formula in postfix notation
  public static int calculate(String formula) {
    String postfix = convert(formula); // Convert the formula to postfix notation
    for (int i = 0; i < postfix.length(); ++i) {
      char ch = postfix.charAt(i); // Get the i-th symbol in the postfix notation
      if (ch >= 'A' && ch <= 'Z') {
        // If the symbol is a variable, push its value onto the integer stack
        intStack.push(values.get(ch));
      } else {
        // If the symbol is an operator, apply the operator to the operands on the stack
        switch (ch) {
          case '!': // If the operator is NOT, pop one operand off the stack and push its negation
            int operand = intStack.pop();
            intStack.push(nor(operand));
            break;
          case '>': // If the operator is IMPLICATION, pop two operands off the stack and push their implication
            int operand2 = intStack.pop();
            int operand1 = intStack.pop();
            intStack.push(implication(operand1, operand2));
            break;
        }
      }
    }
    return intStack.peek(); // Return the result of the calculation
  }

  // This method converts an infix logical formula to postfix notation
  private static String convert(String formula) {
    String postfix = ""; // Initialize an empty string to store the postfix notation
    for (int i = 0; i < formula.length(); ++i) {
      char symbol = formula.charAt(i); // Get the i-th symbol in the formula
      if (symbol >= 'A' && symbol <= 'Z') {
        // If the symbol is an operand (a capital letter), add it to the postfix notationզզ
        postfix += symbol;
      } else if (
        symbol == '!' &&
        (formula.charAt(i + 1) >= 'A' && formula.charAt(i + 1) <= 'Z')
      ) {
        postfix += formula.charAt(i + 1);
        postfix += symbol;
        i++;
      } else {
        if (symbol == ')') {
          // If the symbol is a closing parenthesis, pop operators from the stack
          // and add them to the postfix notation until we reach the matching
          // opening parenthesis
          while (stack.peek() != '(') {
            postfix += stack.pop(); // Pop the opening parenthesis off the stack
          }
          stack.pop(); // Pop the opening parenthesis off the stack
          if (!stack.empty() && stack.peek() == '!') {
            postfix += stack.pop();
          }
        } else {
          // If the symbol is an operator or an opening parenthesis, push it onto the stack
          stack.push(symbol);
        }
      }
    }
    // After processing all the symbols in the formula, pop any remaining operators
    // off the stack and add them to the postfix notation
    while (!stack.isEmpty()) {
      postfix += stack.pop();
    }
    return postfix; // Return the postfix notation of the formula
  }

  // This method computes the NOR (not OR) function of a boolean value
  private static int nor(int a) {
    if (a == 1) return 0;
    return 1;
  }

  // This method computes the implication (->) function of two boolean values
  private static int implication(int a, int b) {
    if (a == 0 || b == 1) return 1;
    return 0;
  }
}
