package com.example.first_lab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText first_number, second_number;
    private TextView result;
    private Button button;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        first_number = findViewById(R.id.first_number);
        second_number = findViewById(R.id.second_number);
        result = findViewById(R.id.result);
        button = findViewById(R.id.button);
        radioGroup = findViewById(R.id.radioGroup);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });
    }

    private void calculateResult() {
        String firstText = first_number.getText().toString();
        String secondText = second_number.getText().toString();

        if (firstText.isEmpty() || secondText.isEmpty()) {
            Toast.makeText(this, R.string.enter_two_numbers, Toast.LENGTH_SHORT).show();
            return;
        }

        double num1 = Double.parseDouble(firstText);
        double num2 = Double.parseDouble(secondText);
        double output = 0.0;

        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, R.string.choose_an_operation, Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedId);
        String operation = selectedButton.getText().toString();

        switch (operation) {
            case "+":
                output = num1 + num2;
                break;
            case "-":
                output = num1 - num2;
                break;
            case "*":
                output = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    Toast.makeText(this, R.string.division_by_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                output = num1 / num2;
                break;
            default:
                Toast.makeText(this, R.string.unknown, Toast.LENGTH_SHORT).show();
                return;
        }

        result.setText("Результат: " + output);
    }
}
