package com.julius.hexgame;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.julius.hexgame.view.HexBoard;

public class GamePlayActivity extends AppCompatActivity {

    TextView txtPlayerOneName;
    TextView txtPlayerTwoName;
    TextView txtPlayerOneScore;
    TextView txtPlayerTwoScore;
    HexBoard hexBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtPlayerOneName = findViewById(R.id.namePlayerOne);
        txtPlayerTwoName = findViewById(R.id.namePlayerTwo);
        txtPlayerOneScore = findViewById(R.id.scorePlayerOne);
        txtPlayerTwoScore = findViewById(R.id.scorePlayerTwo);
        hexBoard = findViewById(R.id.hexBoard);
        int rows = getIntent().getIntExtra("rows", 5);
        int columns = getIntent().getIntExtra("columns", 5);
        int hexSize = getIntent().getIntExtra("hex_size", 80);
        hexBoard.setUpBoard(rows, columns, hexSize);
        String playerOneNameStr = getIntent().getStringExtra("player_one_name");
        String playerTwoNameStr = getIntent().getStringExtra("player_two_name");
        txtPlayerOneName.setText((playerOneNameStr == null || playerOneNameStr.isEmpty()) ?
                "Player 1" : playerOneNameStr);
        txtPlayerTwoName.setText((playerTwoNameStr == null || playerTwoNameStr.isEmpty()) ?
                "Player 2" : playerTwoNameStr);
    }
}