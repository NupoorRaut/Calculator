package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Defining buttons and texts
    Button b_1, b_2, b_3, b_4, b_5, b_6, b_7, b_8, b_9, b_0, b_point, b_equal, b_div, b_mult, b_sub, b_add, b_clear, b_history, b_delete;

    TextView answer, input;

    private StringBuilder currentInput = new StringBuilder();
    String currentOperand = "";
    String currentOperator = "";
    double result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Linking the variables to resources
        b_history = findViewById(R.id.buttonHistory);
        b_delete = findViewById(R.id.buttonDelete);
        b_1 = findViewById(R.id.button1);
        b_2 = findViewById(R.id.button2);
        b_3 = findViewById(R.id.button3);
        b_4 = findViewById(R.id.button4);
        b_5 = findViewById(R.id.button5);
        b_6 = findViewById(R.id.button6);
        b_7 = findViewById(R.id.button7);
        b_8 = findViewById(R.id.button8);
        b_9 = findViewById(R.id.button9);
        b_0 = findViewById(R.id.button0);
        b_equal = findViewById(R.id.buttonEqual);
        b_sub = findViewById(R.id.buttonMinus);
        b_add = findViewById(R.id.buttonAdd);
        b_mult = findViewById(R.id.buttonMult);
        b_div = findViewById(R.id.buttonDiv);
        answer = findViewById(R.id.answer);
        input = findViewById(R.id.input);
        b_point = findViewById(R.id.buttondot);
        b_clear = findViewById(R.id.buttonAC);

        // Set onClick listeners for buttons
        b_0.setOnClickListener(v -> appendToCurrentInput("0"));
        b_1.setOnClickListener(v -> appendToCurrentInput("1"));
        b_2.setOnClickListener(v -> appendToCurrentInput("2"));
        b_3.setOnClickListener(v -> appendToCurrentInput("3"));
        b_4.setOnClickListener(v -> appendToCurrentInput("4"));
        b_5.setOnClickListener(v -> appendToCurrentInput("5"));
        b_6.setOnClickListener(v -> appendToCurrentInput("6"));
        b_7.setOnClickListener(v -> appendToCurrentInput("7"));
        b_8.setOnClickListener(v -> appendToCurrentInput("8"));
        b_9.setOnClickListener(v -> appendToCurrentInput("9"));
        b_div.setOnClickListener(v -> handleOperatorInput("/"));
        b_mult.setOnClickListener(v -> handleOperatorInput("x"));
        b_sub.setOnClickListener(v -> handleOperatorInput("-"));
        b_add.setOnClickListener(v -> handleOperatorInput("+"));
        b_point.setOnClickListener(v -> appendToCurrentInput("."));
        b_delete.setOnClickListener(v -> deleteInput());
        b_clear.setOnClickListener(v -> allClear());
        b_equal.setOnClickListener(v -> calculateResult());

        b_history.setOnClickListener(v -> {
            String expression = currentInput.toString();
            String answerText = answer.getText().toString();
            addToHistory(expression, answerText);
        });
    }

    private void  addToHistory(String exp,String answerText)
    {

        Intent intent=new Intent(this,Recycler_History.class       );
        startActivity(intent);
    }
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == 'x' || c == '/';
    }

    private boolean hasDecimalPoint(String number) {
        return number.contains(".");
    }

    private void onNumberInput(String value) {
        currentOperand += value;
    }

    private void appendToCurrentInput(String value) {
        // Handle decimal points
        if (value.equals(".")) {
            if (hasDecimalPoint(currentOperand)) {
                return; // If there's already a decimal point, don't add another one
            } else if (currentOperand.isEmpty()) {
                // If the operand is empty and the user presses '.', prepend with '0'
                currentOperand = "0";
            }
        }

        // Append the clicked number or decimal point to the current input
        onNumberInput(value);
        currentInput.append(value);
        input.setText(currentInput.toString());
    }

    private void allClear() {
        currentInput.setLength(0); // Clear current input
        currentOperand = ""; // Clear current operand
        input.setText("");
        answer.setText("");
        result = 0; // Reset result
        currentOperator = ""; // Clear current operator
    }

    private void deleteInput() {
        if (currentInput.length() > 0) {
            currentInput = currentInput.deleteCharAt(currentInput.length() - 1); // Remove the last character
            input.setText(currentInput.toString()); // Pass the deleted string to display
        }

        if (!currentOperand.isEmpty()) {
            currentOperand = currentOperand.substring(0, currentOperand.length() - 1); // Update currentOperand
        }
    }

    private void handleOperatorInput(String operator) {
        if (!currentOperand.isEmpty()) {
            double operand = Double.parseDouble(currentOperand); // Correctly parse the current operand

            if (currentOperator.isEmpty()) {
                // First operator input, initialize result with currentOperand
                result = operand;
            } else {
                // Perform calculation with the existing operator and operand
                result = calculate(result, operand, currentOperator);
            }

            currentOperand = ""; // Reset current operand after calculation
        }

        currentOperator = operator; // Store new operator

        if (currentInput.length() > 0) {
            char lastChar = currentInput.charAt(currentInput.length() - 1);
            if (isOperator(lastChar)) {
                // If the last character is an operator, replace it
                currentInput.setCharAt(currentInput.length() - 1, operator.charAt(0));
            } else {
                // Otherwise, append the operator
                currentInput.append(operator);
            }
            input.setText(currentInput.toString());
        }
    }

    private void calculateResult() {
        if (!currentOperand.isEmpty()) {
            // If no operator was pressed, display the operand as result
            if (currentOperator.isEmpty()) {
                result = Double.parseDouble(currentOperand);
            } else {
                // Perform the final calculation
                result = calculate(result, Double.parseDouble(currentOperand), currentOperator);
            }
        }

        // Display result
        answer.setVisibility(View.VISIBLE);
        answer.setText(String.valueOf(result));
        currentOperator = ""; // Reset operator
        currentOperand = ""; // Reset operand
    }

    private double calculate(double operand1, double operand2, String operator) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "x":
                return operand1 * operand2;
            case "/": {
                if (operand2 != 0) {
                    return operand1 / operand2;
                } else {
                    Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                    return operand1;
                }
            }
            default:
                return operand1;
        }
    }
}
