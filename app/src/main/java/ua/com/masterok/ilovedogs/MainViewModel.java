package ua.com.masterok.ilovedogs;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/random";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS = "status";
    private static final String TAG = "MainViewModel";

    private MutableLiveData<DogImage> dogImage = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isEthernetWorking = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<DogImage> getDogImage() {
        return dogImage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsEthernetWorking() {
        return isEthernetWorking;
    }

    public void loadDogImage() {
        Disposable disposable = loadDogImageRetrofit()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // Все що написано в accept() виконається в момент підписки
                .doOnSubscribe(new Consumer<Disposable>() { // .doOnSubscribe - колбек. В колбеці
                    // ми реагуємо на певні стани
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                        isEthernetWorking.setValue(true);
                    }
                })
                // Виконається після завантаження (успішного, чи не успішного). Для виконання коду тільки
                // при успішному, чи тільки при не успішному є інші методи
                .doAfterTerminate(new Action() { // .doOnSubscribe - колбек
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        isEthernetWorking.setValue(false);
                    }
                })
                .subscribe(new Consumer<DogImage>() {
                    @Override
                    public void accept(DogImage dogImageFromRx) throws Throwable {
                        dogImage.setValue(dogImageFromRx);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "Error: " + throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    // Завантажнення даних з мережі за допомогою ретрофіта
    private Single<DogImage> loadDogImageRetrofit() {
        return ApiFactory.getApiService().loadDogImage();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
