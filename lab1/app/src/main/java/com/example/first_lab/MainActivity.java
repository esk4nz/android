package com.example.first_lab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements InputFragment.OnCalculationListener, ResultFragment.OnCancelListener {

    private InputFragment inputFragment;
    private ResultFragment resultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        inputFragment = (InputFragment) fragmentManager.findFragmentByTag("input");

        if (inputFragment == null) {
            inputFragment = new InputFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, inputFragment, "input")
                    .commit();
        }
    }

    @Override
    public void onCalculate(double result) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.hide(inputFragment);

        resultFragment = ResultFragment.newInstance(result);
        resultFragment.setOnCancelListener(this);

        transaction.add(R.id.fragment_container, resultFragment, "result");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCancel() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        inputFragment = (InputFragment) fragmentManager.findFragmentByTag("input");

        if (inputFragment != null) {
            inputFragment.clearFields();
            transaction.show(inputFragment);
        } else {
            inputFragment = new InputFragment();
            transaction.add(R.id.fragment_container, inputFragment, "input");
        }

        resultFragment = (ResultFragment) fragmentManager.findFragmentByTag("result");
        if (resultFragment != null) {
            transaction.remove(resultFragment);
        }

        transaction.commit();
    }
}
