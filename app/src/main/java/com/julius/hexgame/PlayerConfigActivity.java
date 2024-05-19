package com.julius.hexgame;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowMetrics;
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
        int hexSize = 80;
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
        Object[] validationResults = validateDimensions(rows, columns);
        if (!(Boolean) validationResults[0]) {
            Toast.makeText(this,
                            "The dimensions are too large",
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            hexSize = (Integer) validationResults[1];
        }
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("player_one_name", edtTextPlayerOneName.getText().toString());
        intent.putExtra("player_two_name", edtTextPlayerTwoName.getText().toString());
        intent.putExtra("rows", rows);
        intent.putExtra("columns", columns);
        intent.putExtra("hex_size", hexSize);
        startActivity(intent);
    }

    private Object[] validateDimensions(int rows, int columns) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dimension = Math.min(displayMetrics.heightPixels, displayMetrics.widthPixels);
        int hexSize = 80;
        while ((((columns) * hexSize * 1.6f) > dimension)
                || (((rows) * hexSize * 2f) > dimension)) {
            hexSize -= 10;
            if (hexSize < 50) break;
        }
        if (hexSize < 50) {
            return new Object[]{false, 0};
        } else {
            return new Object[]{true, hexSize};
        }
    }
}