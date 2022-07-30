package ua.com.masterok.ilovedogs;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mainViewModel.loadDogImage();
        mainViewModel.getDogImage().observe(this, new Observer<DogImage>() {
            @Override
            public void onChanged(DogImage dogImage) {
                Log.d(TAG, dogImage.toString());
            }
        });

    }

    private void init() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }


}