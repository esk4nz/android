package com.example.first_lab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements InputFragment.OnCalculationListener, ResultFragment.OnCancelListener {

    private InputFragment inputFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputFragment = new InputFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, inputFragment)
                .commit();
    }

    @Override
    public void onCalculate(double result) {
        ResultFragment resultFragment = ResultFragment.newInstance(result);
        resultFragment.setOnCancelListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, resultFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCancel() {
        inputFragment = new InputFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("clear_fields", true);
        inputFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, inputFragment);
        transaction.commit();
    }
}
