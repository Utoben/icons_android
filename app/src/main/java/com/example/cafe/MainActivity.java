package com.example.cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextPassword;
    private Button buttonSignin;
    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editTextName.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()){
                    Log.d("MainActivity", "Empty field/s");
                    Toast.makeText(
                            MainActivity.this,
//                            getString(R.string.fill_the_fields),
                            R.string.fill_the_fields,
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    launchNextScreen(username);
                }
            }
        });
    }
    private  void launchNextScreen(String userName){
        Log.d("MainActivity", "Username: " + userName);
        Log.d("MainActivity", "Password: " + password);

        Intent intent = MakeOrderAcivity.newIntent(this, userName);
        startActivity(intent);
//        Intent intent = new Intent(MainActivity.this, MakeOrderAcivity.class);
//        intent.putExtra("userName", userName);
    }

    private  void initViews(){
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignin = findViewById(R.id.buttonSignin);
    }

}