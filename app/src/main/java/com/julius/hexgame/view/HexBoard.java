package com.julius.hexgame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.julius.hexgame.R;
import com.julius.hexgame.util.GameLogic;

public class HexBoard extends View {

    private final int boardColor;
    private final int playerOneColor;
    private final int playerTwoColor;
    private final int emptyCellColor;
    private final Paint paint = new Paint();
    private final Paint strokePaint = new Paint();
    private final Path path = new Path();
    private short supremum = 5;
    private int numColumns;
    private int numRows;
    private short hexagonRadius;
    private short oddAreaHexagons;
    private short evenAreaHexagons;
    private short totalRows;
    private short hexagonChunk;
    private float hexagonSize;
    private short hexagonHeight;
    private float xOffset;
    private float yOffset;
    private final GameLogic gameLogic = new GameLogic();
    private int dimension;
    private TextView txtPlayerOneScore;
    private TextView txtPlayerTwoScore;
    private TextView txtGameState;
    private Button btnHome;
    private Button btnPlayAgain;
    private boolean hasShownGameOverTxt = false;
    private String playerOneName;
    private String playerTwoName;

    public HexBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributesArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.HexBoard, 0, 0);
        try {
            boardColor = attributesArray.getInteger(R.styleable.HexBoard_boardColor, 0);
            playerOneColor = attributesArray.getInteger(R.styleable.HexBoard_playerOneColor, 0);
            playerTwoColor = attributesArray.getInteger(R.styleable.HexBoard_playerTwoColor, 0);
            emptyCellColor = attributesArray.getInteger(R.styleable.HexBoard_emptyCellColor, 0);
        } finally {
            attributesArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);

        this.dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());

        setMeasuredDimension(dimension, dimension);

        gameLogic.setRowsAndCols(numRows, numColumns);

        // Calculate horizontal and vertical offsets to center the grid within the square view
        xOffset = (dimension - ((numColumns - 1) * hexagonSize * 1.6f)) / 2f;
        yOffset = (dimension - ((numRows - 1) * hexagonSize * 2f)) / 2f;
    }

    public void setUpBoard(int numRows, int numColumns, float hexagonSize) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.hexagonSize = hexagonSize;
    }

    public void setElements(TextView txtPlayerOneScore, TextView txtPlayerTwoScore,
                            TextView txtGameState, Button btnHome, Button btnPlayAgain) {
        this.txtPlayerOneScore = txtPlayerOneScore;
        this.txtPlayerTwoScore = txtPlayerTwoScore;
        this.txtGameState = txtGameState;
        this.btnHome = btnHome;
        this.btnPlayAgain = btnPlayAgain;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (gameLogic.isGameOver()) {
            String winnerName;
            if (gameLogic.getWinner() == gameLogic.getPlayerOne()) {
                winnerName = playerOneName;
            } else if (gameLogic.getWinner() == gameLogic.getPlayerTwo()) {
                winnerName = playerTwoName;
            } else {
                winnerName = "";
            }
            txtGameState.setText(String.format("%s Won", winnerName));
            if (!hasShownGameOverTxt) {
                // do some animation
                this.hasShownGameOverTxt = true;
            }
        } else {
            String playerToPlayName;
            if (gameLogic.getTurn() == gameLogic.getPlayerOne()) {
                playerToPlayName = playerOneName;
            } else {
                playerToPlayName = playerTwoName;
            }
            txtGameState.setText(String.format("%s's turn", playerToPlayName));
        }
        strokePaint.setStrokeWidth(2.7f);
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        drawGameBoard(canvas);

        txtPlayerOneScore.setText(String.valueOf(gameLogic.getPlayerOneScore()));
        txtPlayerTwoScore.setText(String.valueOf(gameLogic.getPlayerTwoScore()));
    }

    private void drawGameBoard(Canvas canvas) {
        // Draw the hexagonal grid
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                float x = xOffset + col * hexagonSize * 1.6f;
                float y = yOffset + row * hexagonSize * 2f;

                if (col % 2 != 0) {
                    y += hexagonSize; // Offset y for odd columns to create staggered layout
                }
                applyHexagonColor(row, col);
                drawHexagon(canvas, x, y, hexagonSize);
            }
        }
    }

    private void applyHexagonColor(int row, int col) {
        switch (gameLogic.getHexState(row, col)) {
            case 2:
                paint.setColor(Color.BLUE);
                return;
            case 3:
                paint.setColor(Color.RED);
                return;
            case 4:
                if (gameLogic.getTurn() == gameLogic.getPlayerOne()) {
                    paint.setColor(Color.BLUE);
                    paint.setAlpha(100);
                    return;
                }
                if (gameLogic.getTurn() == gameLogic.getPlayerTwo()) {
                    paint.setColor(Color.RED);
                    paint.setAlpha(100);
                    return;
                }
                return;
            default:
                paint.setColor(Color.GRAY);
        }
    }

    private void drawHexagon(Canvas canvas, float centerX, float centerY, float size) {
        float angle = (float) Math.toRadians(60);
        path.moveTo(centerX + size * (float) Math.cos(0), centerY + size * (float) Math.sin(0));
        for (int i = 1; i < 6; i++) {
            path.lineTo(centerX + size * (float) Math.cos(angle * i), centerY + size * (float) Math.sin(angle * i));
        }
        path.close();

        canvas.drawPath(path, paint);
        canvas.drawPath(path, strokePaint);
        path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Determine which hexagon was clicked
            int[] clickedHex = getClickedHexagon(x, y);
            if (clickedHex != null) {
                int row = clickedHex[0];
                int col = clickedHex[1];
                gameLogic.act(row, col);

                // invalidate() to redraw the view
                invalidate();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    private int[] getClickedHexagon(float touchX, float touchY) {
        // Adjust touch coordinates to account for grid offsets
        float adjustedX = touchX - xOffset;
        float adjustedY = touchY - yOffset;

        // Calculate grid coordinates based on the adjusted touch coordinates
        int col = Math.round(adjustedX / (hexagonSize * 1.6f)); // Horizontal spacing is 1.6 * hexagonSize
        int row = Math.round(adjustedY / (hexagonSize * 2f)); // Vertical spacing is 2 * hexagonSize

        if (col % 2 != 0) {
            // Adjust row for odd columns
            row = Math.round((adjustedY - hexagonSize) / (hexagonSize * 2f));
        }

        // Check if the click is within the bounds of the grid
        if (col >= 0 && col < numColumns && row >= 0 && row < numRows) {
            // Verify if the click is inside the hexagon boundaries (optional)
            // Perform further validation if needed based on hexagon geometry
            return new int[]{row, col};
        }
        return null; // No hexagon clicked
    }

    public void setPlayerNames(String playerOneNameStr, String playerTwoNameStr) {
        this.playerOneName = playerOneNameStr;
        this.playerTwoName = playerTwoNameStr;
    }
}
