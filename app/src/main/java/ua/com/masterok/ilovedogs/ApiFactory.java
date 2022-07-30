package ua.com.masterok.ilovedogs;

// Цей клас створює реалізацію інтерфейсу ApiService. Ця реалізацію на всю аппку має бути одна

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    // Сінглтон
    private static ApiService apiService;
    // Базова частина посилання повинна закінчуватись /. ЕндПоінт (закінчення посилання)
    // вказується в інтерфейсі ApiService, в антоції @Get
    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/";

    public static ApiService getApiService() {
        if (apiService == null) {
            // Створювати екземпляр apiService буде ретрофіт. Для цього спочатку потрібно отримати
            // екземпляр Retrofit скориставшись білдером, до якого додати декілька параметрів:
            // 1. базовий ЮРЛ
            // 2. адаптер який буде перетворювати джейсони в екземпляри нашого класу ДогІмдж
            // 3. .addCallAdapterFactory() - для того щоб можна було працювати з класами з РХ
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
            // Створення ексземпляра інтерфейсу ApiService
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

}
