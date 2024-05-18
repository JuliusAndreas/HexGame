package com.julius.hexgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayerConfigActivity extends AppCompatActivity {
    EditText edtTextPlayerOneName;
    EditText edtTextPlayerTwoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_config);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtTextPlayerOneName = findViewById(R.id.playerOneName);
        edtTextPlayerTwoName = findViewById(R.id.playerTwoName);
    }
    public void goToGamePlayAcitivity(View view){
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("player_one_name", edtTextPlayerOneName.getText().toString());
        intent.putExtra("player_two_name", edtTextPlayerTwoName.getText().toString());
        startActivity(intent);
    }
}