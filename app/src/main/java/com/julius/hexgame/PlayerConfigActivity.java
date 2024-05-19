package com.julius.hexgame;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayerConfigActivity extends AppCompatActivity {
    EditText edtTextPlayerOneName;
    EditText edtTextPlayerTwoName;
    EditText numberOfRows;
    EditText numberOfColumns;

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
        numberOfRows = findViewById(R.id.rows);
        numberOfColumns = findViewById(R.id.columns);
    }

    public void goToGamePlayAcitivity(View view) {
        int rows = 5;
        int columns = 5;
        try {
            String rowsStr = numberOfRows.getText().toString();
            String columnsStr = numberOfColumns.getText().toString();
            if (!rowsStr.trim().isEmpty()) {
                rows = Integer.parseInt(rowsStr);
            }
            if (!columnsStr.trim().isEmpty()) {
                columns = Integer.parseInt(columnsStr);
            }
        } catch (Exception e) {
            Toast.makeText(this,
                            "Enter a proper number or leave the fields empty",
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("player_one_name", edtTextPlayerOneName.getText().toString());
        intent.putExtra("player_two_name", edtTextPlayerTwoName.getText().toString());
        intent.putExtra("rows", rows);
        intent.putExtra("columns", columns);
        startActivity(intent);
    }
}