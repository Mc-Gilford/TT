
package com.example.domus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
    }
    /** Called when the user taps the Send button */
    /*public void sendMessage(View view) {
        Intent intent = new Intent(this, Register.class);
        Button editText = (Button) findViewById(R.id.register);
        String message = editText.getText().toString();
        intent.putExtra("HOLA", message);
        startActivity(intent);
    }*/

}