package com.example.first_lab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {

    private TextView resultText;
    private Button buttonCancel;
    private OnCancelListener cancelListener;

    public interface OnCancelListener {
        void onCancel();
    }

    public static ResultFragment newInstance(double result) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putDouble("result", result);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        resultText = view.findViewById(R.id.result_text);
        buttonCancel = view.findViewById(R.id.button_cancel);

        Bundle bundle = getArguments();
        if (bundle != null) {
            double result = bundle.getDouble("result", 0);
            resultText.setText("Результат: " + result);
        }

        buttonCancel.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
        });

        return view;
    }

    public void setOnCancelListener(OnCancelListener listener) {
        this.cancelListener = listener;
    }
}
