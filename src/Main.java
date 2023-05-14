import BTree.BTreePrinter;
import BTree.Node;
import BTree.Tree;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Main {

  private static final char implicationOperator = '>';
  private static final char norOperator = '!';
  private static final Map<Character, Integer> vector1 = new HashMap<>();

  public static void generateTree(String formula, Node node) {
    int indexOfImplication = formula.indexOf(implicationOperator);
    int indexOfOpeningBracket = formula.indexOf('(');
    int indexOfNor = formula.indexOf(norOperator);
    if (indexOfNor == 0) { // !(....)
      if (indexOfOpeningBracket == 1) {
        int indexOfClose = findClosingParen(
          formula.toCharArray(),
          indexOfOpeningBracket
        );
        if (indexOfClose == (formula.length() - 1)) {
          indexOfImplication = -1;
        }
      }
    }
    if (
      indexOfOpeningBracket != -1 &&
      indexOfImplication != -1 &&
      indexOfOpeningBracket < indexOfImplication
    ) {
      int indexOfClosingBracket = findClosingParen(
        formula.toCharArray(),
        indexOfOpeningBracket
      );
      if (indexOfClosingBracket == formula.length() - 1) {
        if (indexOfOpeningBracket == 0) {
          formula = formula.substring(1, formula.length() - 1);
          generateTree(formula, node);
          return;
        }
      } else {
        indexOfImplication = ++indexOfClosingBracket;
      }
    }

    if (indexOfImplication != -1) {
      String firstPart = formula.substring(0, indexOfImplication);
      String secondPart = formula.substring(indexOfImplication + 1);
      int val_1 = Expression.calculate(firstPart);
      int val_2 = Expression.calculate(secondPart);
      Implication caseNumber = Implication.getCase(val_1, val_2);
      switch (caseNumber) {
        case FIRST:
          if (firstPart.charAt(0) != norOperator) 
          firstPart = norOperator + firstPart;
          node.setLeft(new Node(firstPart));
          generateTree(firstPart, node.getLeft());
          break;
        case SECOND:
          node.setRight(new Node(secondPart));
          generateTree(secondPart, node.getRight());
          break;
        case THIRD:
          if (secondPart.charAt(0) != norOperator) secondPart =
            norOperator + secondPart;
          node.setLeft(new Node(firstPart));
          node.setRight(new Node(secondPart));
          generateTree(firstPart, node.getLeft());
          generateTree(secondPart, node.getRight());
          break;
        case FOURTH:
          if (countVariables(firstPart) < countVariables(secondPart)) {
            if (firstPart.charAt(0) != norOperator) firstPart =
              norOperator + firstPart;
            node.setLeft(new Node(firstPart));
            generateTree(firstPart, node.getLeft());
          } else {
            node.setRight(new Node(secondPart));
            generateTree(secondPart, node.getRight());
          }
          break;
      }
    } else {
      if (formula.length() == 1) {
        int val = vector1.get(formula.charAt(0));
        if (val == 0) node.setFormula(norOperator + node.getFormula());
        return;
      }
      if (formula.length() == 2) {
        int val = vector1.get(formula.charAt(1));
        if (val == 1) {
          node.setLeft(new Node(formula.substring(1)));
          node.setFormula(norOperator + formula);
          generateTree(node.getLeft().getFormula(), node.getLeft());
          return;
        }
        return;
      }
      int val = Expression.calculate(formula.substring(1));
      if (val == 1) {
        node.setLeft(new Node(formula.substring(1)));
        node.setFormula(norOperator + formula);
        generateTree(node.getLeft().getFormula(), node.getLeft());
        return;
      }
      generateTree(formula.substring(1), node);
    }
  }

  public static void putValues(String statement, String values) {
    String variables = getSortedVariables(statement);
    for (int i = 0; i < variables.length(); i++) {
      char variable = variables.charAt(i);
      char value = values.charAt(i);
      vector1.put(variable, value == '0' ? 0 : 1);
    }
  }

  public static int countVariables(String str) {
    Set<Character> variables = new HashSet<>();

    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (c >= 'A' && c <= 'Z') {
        variables.add(c);
      }
    }

    return variables.size();
  }

  public static int findClosingParen(char[] text, int openPos) {
    int closePos = openPos;
    int counter = 1;
    while (counter > 0) {
      char c = text[++closePos];
      if (c == '(') {
        counter++;
      } else if (c == ')') {
        counter--;
      }
    }
    return closePos;
  }

  private static String getSortedVariables(String statement) {
    Set<Character> letters = new HashSet<>();
    for (int i = 0; i < statement.length(); i++) {
      char c = statement.charAt(i);
      if (c >= 'A' && c <= 'Z') {
        letters.add(c);
      }
    }
    StringBuilder sb = new StringBuilder();
    for (char c : letters) {
      sb.append(c);
    }
    return sb.toString();
  }
  

  public static String generateDOTCode(Node node) {
    StringBuilder sb = new StringBuilder();
    sb
      .append("\"").append(node.getFormula()).append("\"").append(" [label=\"").append(node.getFormula()).append("\"];\n");

    if (node.getLeft() != null) {
      sb.append("\"").append(node.getFormula()).append("\"").append(" -> ").append("\"").append(node.getLeft().getFormula())
        .append("\"").append(";\n");
      sb.append(generateDOTCode(node.getLeft()));
    }
    if (node.getRight() != null) {
      sb.append("\"").append(node.getFormula()).append("\"").append(" -> ").append("\"")
        .append(node.getRight().getFormula()).append("\"").append(";\n");
      sb.append(generateDOTCode(node.getRight()));
    }
    return sb.toString();
  }

  public static void generateDOTFile(Node node, String filename) {
    try {
      FileWriter fw = new FileWriter(filename);
      fw.write("digraph G {\n");
      fw.write(generateDOTCode(node));
      fw.write("}\n");
      fw.close();
      String command =
        "dot -Tpng -Gdpi=700 -Gsize=800,0 " +
        filename +
        " -o " +
        filename.replace(".dot", ".png");
      Process p = Runtime.getRuntime().exec(command);
      p.waitFor();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    InputUI inputUI = new InputUI();

    // wait until user enters the input and clicks on the calculate button
    while (inputUI.statementImp.isEmpty() || inputUI.valuesImp.isEmpty()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    if (inputUI.checkedPassed) {
      String statement = inputUI.statementImp;
      String values = inputUI.valuesImp;

      putValues(statement, values);
      Expression.values = vector1;
      int value = Expression.calculate(statement);
      String v = getSortedVariables(statement);
      System.out.println("value " + v);
      System.out.println("value " + value);
      String producedFormula = statement;
      if (value == 0) {
        producedFormula = norOperator + "(" + statement + ")";
      }
      System.out.println("Given formula: " + statement);
      System.out.println("We need to take out: " + producedFormula);
      Node root = new Node(producedFormula);
      Tree tree = new Tree(root);
      generateTree(producedFormula, root);
      System.out.println();
      BTreePrinter.printNode(root);
      System.out.println("=================");
      tree.collectAllLeafs(root);
      String sep = "";
      System.out.print("Necessary propositions: ");
      for (Node node : tree.getLeafs()) {
        System.out.print(sep + node.getFormula());
        sep = ",";
      }
      generateDOTFile(root, "tree.dot");
    }
  }
}
