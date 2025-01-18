package com.example.hemms;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

    private static UserSession instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "UserSession"; // Tercih dosyasının adı
    private static final String KEY_USER_ID = "userId";  // Kullanıcı ID'si anahtarı

    // Constructor private yapılıyor, Singleton deseni kullanıyoruz
    private UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Singleton örneği almayı sağlayan metod
    public static synchronized UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context);
        }
        return instance;
    }

    // Kullanıcı ID'sini kaydetme metodu
    public void setUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    // Kullanıcı ID'sini alma metodu
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null); // Default olarak null döner
    }

    // Kullanıcı oturumunu temizleme (çıkış yapmak için kullanılabilir)
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
