package ua.com.masterok.ilovedogs;

// Ретрофіт
// Інтерфейс в якому оголошуються всі методи

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ApiService {

    // @GET - цей тип запита дозволяє отримувати дані з мережі
    // ("") - всерендині потрібно вказати ендПоінт
    @GET("random")
    Single<DogImage> loadDogImage();

}
