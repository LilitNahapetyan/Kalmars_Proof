package BTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BTreePrinter {

  /**
   * Prints the nodes of the given binary tree.
   * @param root the root node of the binary tree
   */
  public static <T extends Comparable<?>> void printNode(Node root) {
    int maxLevel = BTreePrinter.maxLevel(root);

    printNodeInternal(Collections.singletonList(root), 1, maxLevel);
  }

  /**
   * Recursively prints the nodes of the binary tree.
   * @param nodes a list of nodes to print
   * @param level the level of the nodes in the binary tree
   * @param maxLevel the maximum level of the binary tree
   */
  private static <T extends Comparable<?>> void printNodeInternal(
    List<Node> nodes,
    int level,
    int maxLevel
  ) {
    if (nodes.isEmpty() || BTreePrinter.isAllElementsNull(nodes)) return;

    int floor = maxLevel - level;
    int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
    int firstSpaces = (int) Math.pow(2, (floor)) - 1;
    int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

    BTreePrinter.printWhitespaces(firstSpaces);

    List<Node> newNodes = new ArrayList<Node>();
    for (Node node : nodes) {
      if (node != null) {
        System.out.print(node.getFormula());
        newNodes.add(node.getLeft());
        newNodes.add(node.getRight());
      } else {
        newNodes.add(null);
        newNodes.add(null);
        System.out.print(" ");
      }

      BTreePrinter.printWhitespaces(betweenSpaces);
    }
    System.out.println("");

    for (int i = 1; i <= endgeLines; i++) {
      for (int j = 0; j < nodes.size(); j++) {
        BTreePrinter.printWhitespaces(firstSpaces - i);
        if (nodes.get(j) == null) {
          BTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
          continue;
        }

        if (nodes.get(j).getLeft() != null) System.out.print(
          "/"
        ); else BTreePrinter.printWhitespaces(1);

        BTreePrinter.printWhitespaces(i + i - 1);

        if (nodes.get(j).getRight() != null) System.out.print(
          "\\"
        ); else BTreePrinter.printWhitespaces(1);

        BTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
      }

      System.out.println("");
    }

    printNodeInternal(newNodes, level + 1, maxLevel);
  }

  /**
   * Prints a given number of spaces.
   * @param count the number of spaces to print
   */
  private static void printWhitespaces(int count) {
    for (int i = 0; i < count; i++) System.out.print(" ");
  }

  /**
   * Returns the maximum level of the given binary tree.
   * @param node the root node of the binary tree
   * @return the maximum level of the binary tree
   */
  private static <T extends Comparable<?>> int maxLevel(Node node) {
    if (node == null) return 0;

    return (
      Math.max(
        BTreePrinter.maxLevel(node.getLeft()),
        BTreePrinter.maxLevel(node.getRight())
      ) +
      1
    );
  }

  /**

Checks whether all elements in the given list are null.

@param list the list to check

@return true if all elements in the list are null, false otherwise
*/
  private static <T> boolean isAllElementsNull(List<T> list) {
    for (Object object : list) {
      if (object != null) return false;
    }

    return true;
  }
}
