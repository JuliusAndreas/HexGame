package com.julius.hexgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToPVPConfig(View view) {
        Intent intent = new Intent(this, PlayerConfigActivity.class);
        startActivity(intent);
    }

    public void goToAIConfig(View view) {
        Intent intent = new Intent(this, AIConfigActivity.class);
        startActivity(intent);
    }

    public void goToMapEditor(View view) {
        Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();
    }
}