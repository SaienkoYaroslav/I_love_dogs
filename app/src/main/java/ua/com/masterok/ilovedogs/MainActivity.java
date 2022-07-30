package ua.com.masterok.ilovedogs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private Button buttonNextImage;
    private ImageView imageViewDogImage;
    private ProgressBar progressBar;

    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        viewModel();
        onClickButtonNextImage();

    }

    private void init() {
        buttonNextImage = findViewById(R.id.button_load_next_image);
        imageViewDogImage = findViewById(R.id.image_view_dog_image);
        progressBar = findViewById(R.id.progress_bar);


    }

    private void viewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.loadDogImage();
        mainViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                if (loading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        mainViewModel.getIsEthernetWorking().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isEthernetWorking) {
                if (!isEthernetWorking) {
                    Toast.makeText(MainActivity.this, R.string.ethernet_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainViewModel.getDogImage().observe(this, new Observer<DogImage>() {
            @Override
            public void onChanged(DogImage dogImage) {
                // Завантаження картинки з інтернету і вставка в ІВ за допомогою бібліотеки Глайд
                Glide.with(MainActivity.this)
                        .load(dogImage.getMessage())
                        .into(imageViewDogImage);
            }
        });
    }

    private void onClickButtonNextImage() {
        buttonNextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.loadDogImage();
            }
        });
    }


}