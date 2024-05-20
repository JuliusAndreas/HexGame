package com.julius.hexgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    TextView txtGameState;
    Button btnPlayAgain;
    Button btnHome;
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
        txtGameState = findViewById(R.id.gameStateText);
        btnPlayAgain = findViewById(R.id.playAgainBtn);
        btnHome = findViewById(R.id.homeBtn);
        hexBoard = findViewById(R.id.hexBoard);
        btnHome.setVisibility(View.GONE);
        btnPlayAgain.setVisibility(View.GONE);
        int rows = getIntent().getIntExtra("rows", 5);
        int columns = getIntent().getIntExtra("columns", 5);
        int hexSize = getIntent().getIntExtra("hex_size", 80);
        hexBoard.setUpBoard(rows, columns, hexSize);
        String playerOneNameStr = getIntent().getStringExtra("player_one_name");
        String playerTwoNameStr = getIntent().getStringExtra("player_two_name");
        playerOneNameStr = (playerOneNameStr == null || playerOneNameStr.isEmpty()) ?
                "Player 1" : playerOneNameStr;
        playerTwoNameStr = (playerTwoNameStr == null || playerTwoNameStr.isEmpty()) ?
                "Player 2" : playerTwoNameStr;
        txtPlayerOneName.setText(playerOneNameStr);
        txtPlayerTwoName.setText(playerTwoNameStr);
        hexBoard.setPlayerNames(playerOneNameStr, playerTwoNameStr);
        hexBoard.setElements(txtPlayerOneScore, txtPlayerTwoScore, txtGameState,
                btnHome, btnPlayAgain);
    }
}