package com.example.first_lab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.content.Intent;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InputFragment extends Fragment {

    private EditText firstNumber, secondNumber;
    private RadioGroup radioGroup;
    private Button buttonOk;

    public interface OnCalculationListener {
        void onCalculate(double result);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        firstNumber = view.findViewById(R.id.first_number);
        secondNumber = view.findViewById(R.id.second_number);
        radioGroup = view.findViewById(R.id.radioGroup);
        buttonOk = view.findViewById(R.id.button_ok);

        Button buttonOpen = view.findViewById(R.id.button_open);

        buttonOk.setOnClickListener(v -> calculateResult());

        buttonOpen.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ResultsActivity.class);
            startActivity(intent);
        });

        return view;
    }


    private void calculateResult() {
        String firstText = firstNumber.getText().toString();
        String secondText = secondNumber.getText().toString();

        if (firstText.isEmpty() || secondText.isEmpty()) {
            Toast.makeText(getActivity(), R.string.enter_two_numbers, Toast.LENGTH_SHORT).show();
            return;
        }

        double num1 = Double.parseDouble(firstText);
        double num2 = Double.parseDouble(secondText);
        double output = 0.0;

        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(getActivity(), R.string.choose_an_operation, Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = getView().findViewById(selectedId);
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
                    Toast.makeText(getActivity(), R.string.division_by_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                output = num1 / num2;
                break;
        }

        String entry = num1 + " " + operation + " " + num2 + " = " + output + "\n";

        try (FileOutputStream fos = getActivity().openFileOutput("results.txt", getActivity().MODE_APPEND)) {
            fos.write(entry.getBytes());
            Toast.makeText(getActivity(), "Результат збережено", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Помилка запису у файл", Toast.LENGTH_SHORT).show();
        }

        if (getActivity() instanceof OnCalculationListener) {
            ((OnCalculationListener) getActivity()).onCalculate(output);
        }
    }

    public void clearFields() {
        if (firstNumber != null && secondNumber != null && radioGroup != null) {
            firstNumber.setText("");
            secondNumber.setText("");
            radioGroup.clearCheck();
        }
    }

}
