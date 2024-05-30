package com.julius.hexgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AIConfigActivity extends AppCompatActivity {
    MediaPlayer configMenuSong;
    EditText edtTextPlayerOneName;
    EditText edtTextPlayerTwoName;
    EditText numberOfRows;
    EditText numberOfColumns;
    private boolean mediaIsStopped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aiconfig);
        configMenuSong = MediaPlayer.create(this, R.raw.amomentspeace);
        configMenuSong.start();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        numberOfRows = findViewById(R.id.rowsAI);
        numberOfColumns = findViewById(R.id.columnsAI);
    }
    @Override
    protected void onPause() {
        super.onPause();
        configMenuSong.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configMenuSong.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        configMenuSong.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        configMenuSong.pause();
        configMenuSong.seekTo(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        configMenuSong.release();
    }

    public void goToAIGamePlayAcitivity(View view) {
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
        intent.putExtra("player_one_name", "Human");
        intent.putExtra("player_two_name", "AI");
        intent.putExtra("rows", rows);
        intent.putExtra("columns", columns);
        intent.putExtra("hex_size", hexSize);
        intent.putExtra("AI", true);
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