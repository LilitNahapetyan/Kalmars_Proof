import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import javax.swing.*;

public class InputUI extends JFrame {

  private JLabel label1, label2;
  private JTextField textField1, textField2;
  private JButton calculateButton;
  public String statementImp = "";
  public String valuesImp = "";
  public boolean checkedPassed = false;

  public InputUI() {
    super("Input UI");
    // Initialize components
    label1 = new JLabel("Formula:");
    label2 = new JLabel("Values:");
    textField1 = new JTextField(30);
    textField2 = new JTextField(30);
    textField1.setPreferredSize(new Dimension(200, 40));
    textField2.setPreferredSize(new Dimension(200, 40));

    calculateButton = new JButton("Calculate");

    // Set layout and add components
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(20, 10, 10, 10);
    add(label1, gbc);
    gbc.gridx = 1;
    add(textField1, gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    add(label2, gbc);
    gbc.gridx = 1;
    add(textField2, gbc);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    add(calculateButton, gbc);

    // Style components
    Font labelFont = new Font("Arial", Font.BOLD, 16);
    label1.setFont(labelFont);
    label2.setFont(labelFont);
    calculateButton.setFont(labelFont);
    calculateButton.setBackground(new Color(59, 89, 152));
    calculateButton.setForeground(Color.WHITE);
    calculateButton.setFocusPainted(false);

    // Add action listener to calculate button

    
    calculateButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          String statement = textField1.getText();
          String values = textField2.getText();

          if (statement.length() == 0 || values.length() == 0) {
            JOptionPane.showMessageDialog(InputUI.this,"Please fill all field");
            return;
          }
          if (!values.matches("[01]+")) {
            JOptionPane.showMessageDialog(InputUI.this,"Values can only contain 0 or 1");
            return;
          }
          if (!checkStatement(statement)) {
            JOptionPane.showMessageDialog(InputUI.this, "Statement is invalid");
            return;
          }
          if (!checkParenthesesBalanced(statement)) {
            JOptionPane.showMessageDialog(InputUI.this,"Parentheses are not balanced");
            return;
          }
          if (!checkGeneralImplication(statement)) {
            JOptionPane.showMessageDialog(InputUI.this,"There is more than one general implication sign");
            return;
          }
          if (countOfVariables(statement) != values.length()) {
            JOptionPane.showMessageDialog(InputUI.this,"Values count does not match statement variables count");
            return;
          }
          statementImp = statement;
          valuesImp = values;
          checkedPassed = true;
          dispose();
        }
      }
    );
    // Set JFrame properties
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 400);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private static boolean checkParenthesesBalanced(String input) {
    Stack<Character> stack = new Stack<>();
    for (char c : input.toCharArray()) {
      if (c == '(') {
        stack.push(c);
      } else if (c == ')') {
        if (stack.isEmpty() || stack.pop() != '(') {
          return false;
        }
      }
    }
    return stack.isEmpty();
  }

  private static boolean checkStatement(String statement) {
    if (
      statement.isEmpty() ||
      statement.charAt(0) == '>' ||
      statement.charAt(statement.length() - 1) == '>' ||
      statement.charAt(statement.length() - 1) == '!'
    ) {
      return false;
    }

    if (
      !statement.matches("[A-Z>!()]+") ||
      statement.matches(".*[A-Z]!.*|.*[A-Z][A-Z].*|.*\\)[A-Z].*|.*[A-Z]\\(.*")
    ) {
      return false;
    }
    return true;
  }

  public static boolean checkGeneralImplication(String formula) {
    int depth = 0;
    ArrayList<Integer> countImpArr = new ArrayList<Integer>(
      Collections.singletonList(0)
    );

    for (int i = 0; i < formula.length() - 1; i++) {
      char c = formula.charAt(i);
      if (c == '(') {
        depth++;
        if (depth >= countImpArr.size()) {
          countImpArr.add(0);
        }
      } else if (c == ')') {
        countImpArr.set(depth, 0);
        depth--;
      } else if (c == '>') {
        if (countImpArr.get(depth) == 1) {
          return false;
        }
        countImpArr.set(depth, 1);
      }
    }
    return true;
  }

  private static int countOfVariables(String statement) {
    Set<Character> variables = new HashSet<>();

    for (int i = 0; i < statement.length(); i++) {
      char c = statement.charAt(i);
      if (c >= 'A' && c <= 'Z') {
        variables.add(c);
      }
    }

    return variables.size();
  }
}
