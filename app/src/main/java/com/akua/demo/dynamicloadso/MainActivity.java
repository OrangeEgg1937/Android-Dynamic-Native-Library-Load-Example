package com.akua.demo.dynamicloadso;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.akua.demo.dynamicloadso.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView tv = binding.sampleText;

        try {
            DynamicSOLoader.loadLibraryFromInternalStorage(this);
            tv.setText(DynamicSOLoader.stringFromJNI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}