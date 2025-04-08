package com.example.first_lab;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.widget.Toast;
import java.io.FileOutputStream;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_results);

        TextView textView = findViewById(R.id.results_text_view);

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(openFileInput("results.txt")))) {

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                textView.setText("There are no data");
            } else {
                textView.setText(builder.toString());
            }

        } catch (Exception e) {
            textView.setText("There are no data");
        }
    }
    public void clearResults(View view) {
        try {
            FileOutputStream fos = openFileOutput("results.txt", MODE_PRIVATE);
            fos.write("".getBytes());
            fos.close();

            TextView textView = findViewById(R.id.results_text_view);
            textView.setText("There are no data");

            Toast.makeText(this, "Результати очищено", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Помилка при очищенні", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View view) {
        finish();
    }
}
