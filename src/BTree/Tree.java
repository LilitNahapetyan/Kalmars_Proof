package BTree;

import java.util.ArrayList;
import java.util.List;

/*This code defines a class called Tree that represents a binary tree. The Tree class has a private 
root Node object that holds the root node of the tree, and a private leafs List object that holds all 
the leaf nodes of the tree. The Tree constructor takes a Node argument to initialize the root of the tree.

The collectAllLeafs method is a recursive method that traverses the tree and adds all the leaf nodes to 
the leafs list. It takes a Node argument and recursively calls itself on the left and right child nodes of t
he current node. If the current node is a leaf node (i.e. it has no left or right child), it adds the current 
node to the leafs list.

The getLeafs method is a getter method that returns the leafs list. */

public class Tree {

  //private Node root;   // Declare a private Node object called root to hold the root node of the tree
  private List<Node> leafs = new ArrayList<>(); // Declare a private List object called leafs to hold all the leaf nodes of the tree

  public Tree(Node root) {
    //this.root = root;   // Constructor that takes a Node argument to initialize the root of the tree
  }

  // Method to recursively collect all the leaf nodes of the tree
  public void collectAllLeafs(Node root) {
    if (root == null) return; // Base case: if the root is null, return
    if (root.getLeft() == null && root.getRight() == null) leafs.add(root); // If the current node is a leaf node (i.e. it has no left or right child), add it to the leafs list
    collectAllLeafs(root.getLeft()); // Recursively call collectAllLeafs on the left child of the current node
    collectAllLeafs(root.getRight()); // Recursively call collectAllLeafs on the right child of the current node
  }

  public List<Node> getLeafs() {
    return leafs; // Getter method to return the leafs list
  }
}
