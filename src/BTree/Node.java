package BTree;

public class Node {

  private Node left; // reference to the left child node
  private Node right; // reference to the right child node
  private String formula; // the logical formula stored in this node

  public Node(String formula) {
    this.formula = formula;
    this.left = null;
    this.right = null;
  }
  public Node getLeft() {
    return left;
  }
  public void setLeft(Node left) {
    this.left = left;
  }
  public Node getRight() {
    return right;
  }
  public void setRight(Node right) {
    this.right = right;
  }
  public String getFormula() {
    return formula;
  }
  public void setFormula(String formula) {
    this.formula = formula;
  }
}
