package com.example.cafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.io.*;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MakeOrderAcivity extends AppCompatActivity {
    private static final String EXTRA_USER_NAME = "userName";
//    private TextView textViewGreetings;
    private TextView textViewAddittives;
    private Button buttonMakeOrder;
    private Button buttonGetAllIcons;
    private LinearLayout iconListLayout;
    private Spinner tea;
    private Spinner coffee;
    private String drink;
    private String userName;
    // Идентификатор уведомления
    private static int NOTIFY_ID = 101;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    // Идентификатор канала
    private static final String CHANNEL_ID = "Cat channel";
    private static final int REQUEST_CODE_SEND_NOTIFICATIONS = 123;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order_acivity);
        initViews();
        userName = getIntent().getStringExtra(EXTRA_USER_NAME);
        String greetings = getString(R.string.greetings, userName);
//        textViewGreetings.setText(greetings);

        buttonGetAllIcons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    getAllIcons();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonMakeOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
    }
private void getAllIcons() {
//    List<Icon> icons = fetchIconsFromServer();
    // URL
    String url = "http://77.246.159.61:5555/icons/";  // Если запускаете локально через эмулятор, используйте 10.0.2.2

    // Создаем OkHttpClient с игнорированием проверки имени хоста
    OkHttpClient client = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> true)  // Игнорируем проверку хоста
            .build();

    // Создаем GET запрос
    Request request = new Request.Builder()
            .url(url)
            .get()  // Используем метод GET без тела запроса
            .build();

    // Выполняем запрос асинхронно
    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            // Обработка ошибки
            Log.e("MakeOrderActivity", "Ошибка запроса: " + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                // Обработка успешного ответа
                String responseData = response.body().string();
                // Парсинг JSON
                IconResponse iconResponse = parseIconResponse(responseData);

//                System.out.println(response.body().string());
                Gson gson = new Gson();

                List<Icon> icons = iconResponse.getIcons();
                // Обновление UI должно происходить в основном потоке
                runOnUiThread(() -> {
                    // Очищаем предыдущие данные перед добавлением новых
                    iconListLayout.removeAllViews();
                    for (Icon icon : icons) {
                        TextView textView = new TextView(MakeOrderAcivity.this);
                        textView.setText("Имя: " + icon.getName() + ", Цена: " + icon.getPrice());
                        textView.setTextSize(16);
                        textView.setTextColor(ContextCompat.getColor(MakeOrderAcivity.this, R.color.black));
                        textView.setPadding(16, 16, 16, 16);

                        // Добавляем TextView в LinearLayout
                        iconListLayout.addView(textView);
                    }
                });
            } else {
                System.out.println("Ошибка: " + response.code());
            }
        }
        // Метод для отображения значков на экране
        private void displayIcons(List<Icon> icons) {
            // Очищаем предыдущие данные перед добавлением новых
            iconListLayout.removeAllViews();

            // Для каждого значка создаем TextView и добавляем его в LinearLayout
            for (Icon icon : icons) {
                TextView textView = new TextView(MakeOrderAcivity.this);
                textView.setText("Имя: " + icon.getName() + ", Цена: " + icon.getPrice());
                textView.setTextSize(16);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setPadding(16, 16, 16, 16);

                // Добавляем TextView в LinearLayout
                iconListLayout.addView(textView);
            }
        }
        // Метод для парсинга JSON-ответа
        private IconResponse parseIconResponse(String jsonResponse) {
            Gson gson = new Gson();
            Type iconResponseType = new TypeToken<IconResponse>() {}.getType();
            return gson.fromJson(jsonResponse, iconResponseType);
        }
        // Модель данных для парсинга JSON

    });
}
    private static class IconResponse {
        private List<Icon> icons;

        public List<Icon> getIcons() {
            return icons;
        }
    }
    // Модель данных для значка
    private static class Icon {
        private String name;
        private double price;

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }
    public static Intent newIntent(Context context, String userName){
        Intent intent = new Intent(context, MakeOrderAcivity.class);
        intent.putExtra(EXTRA_USER_NAME, userName);
        return  intent;
    }

    private  void initViews(){
        textViewAddittives = findViewById(R.id.textViewAddittives);
        buttonMakeOrder = findViewById(R.id.buttonMakeOrder);
        buttonGetAllIcons = findViewById(R.id.buttonGetAllIcons);
        iconListLayout = findViewById(R.id.iconListLayout);
    }
}