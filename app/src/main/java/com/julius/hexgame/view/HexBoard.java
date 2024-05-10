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
import android.widget.Toast;

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

        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());

        setMeasuredDimension(dimension, dimension);


//        float hexWidth = getWidth() / (2 * getRequiredColumns(getWidth(), getHeight()) - 1f);
//        float hexHeight = getHeight() / (1.5f * getRequiredRows(getWidth(), getHeight()));
//        hexagonSize = Math.min(hexWidth, hexHeight);
//
//        // Calculate the number of columns and rows based on the hexagon size
//        numColumns = (int) (width / (hexagonSize * 2)) + 1;
//        numRows = (int) (height / (hexagonSize * 1.5f)) + 1;

//        hexagonChunk = (short) (dimension / supremum);
//        hexagonHeight = (short) (hexagonChunk - (hexagonChunk / 10));
//        hexagonRadius = (short) ((hexagonHeight / 2) / (Math.PI / 6));
        numColumns = 5;
        numRows = 5;
        hexagonSize = 80;

        // Calculate horizontal and vertical offsets to center the grid within the square view
        xOffset = (dimension - ((numColumns-1) * hexagonSize * 1.6f)) / 2f;
        yOffset = (dimension - ((numRows-1) * hexagonSize * 2f)) / 2f;
//        totalRows = (short) (dimension / hexagonChunk);
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//        // Calculate the maximum hexagon size that fits within the square view
//        float dimension = Math.min(w, h);
//        float hexWidth = dimension / (6 * getRequiredColumns(dimension) - 1f);
//        float hexHeight = dimension / (1.5f * getRequiredRows(dimension));
//        hexagonSize = Math.min(hexWidth, hexHeight);
//
//        // Calculate the number of columns and rows based on the hexagon size
//        numColumns = (int) (dimension / (hexagonSize * 2)) + 1;
//        numRows = (int) (dimension / (hexagonSize * 1.5f)) + 1;
//    }

    private void drawGameBoard(Canvas canvas) {
//        for (int i = 0; i < totalColumns; i++) {
//            if (i % 2 == 0) {
//
//            } else {
//
//            }
//        }
//        for (int row = 0; row < 5; row++) {
//            for (int col = 0; col < 5; col++) {
//                float x = col * hexagonRadius * 1.5f + hexagonRadius;
//                float y = row * hexagonRadius * 2f + (col % 2) * hexagonRadius + ((float) hexagonHeight / 2);
//                drawHexagon(canvas, x, y);
//            }
//        }

        // Draw the hexagonal grid
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                float x = xOffset + col * hexagonSize * 1.6f;
                float y = yOffset + row * hexagonSize * 2f;

                if (col % 2 != 0) {
                    y += hexagonSize; // Offset y for odd columns to create staggered layout
                }

                drawHexagon(canvas, x, y, hexagonSize);
            }
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
    }


    //    }
//        canvas.drawPath(path, strokePaint);
//        canvas.drawPath(path, paint);
//        path.close();
//        }
//            }
//                path.lineTo(x, y); // Draw a line to the next vertex
//            } else {
//                path.moveTo(x, y); // Move to the first vertex
//            if (i == 0) {
//
//            float y = cy + hexagonSize * (float) Math.sin(angle);
//            float x = cx + hexagonSize * (float) Math.cos(angle);
//            float angle = (float) (i * 2 * Math.PI / 6); // Angle for each vertex
//        for (int i = 0; i < 6; i++) {
//        // Calculate vertex positions
//    private void drawHexagon(Canvas canvas, float cx, float cy) {
    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        strokePaint.setStrokeWidth(2);
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
//        drawHexagon(canvas, 600,600, 55);
//        hexagonSize = 55;
        drawGameBoard(canvas);
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
                Toast.makeText(getContext().getApplicationContext(), String.format("You clicked on (%s,%s)", row, col), Toast.LENGTH_SHORT).show();
                // Handle click on hexagon at row, col
                // Example: highlight the clicked hexagon
                // invalidate() to redraw the view
                invalidate();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    private int[] getClickedHexagon(float touchX, float touchY) {
//        // Calculate the hexagon grid coordinates based on the touch coordinates
//        float xOffset = (getWidth() - (numColumns * hexagonSize * 2 - hexagonSize)) / 1.75f;
//        float yOffset = (getHeight() - (numRows * hexagonSize * 1.5f - hexagonSize * 0.5f)) / 2f;
//
//        int col = (int) ((touchX - xOffset) / (hexagonSize * 1.6f));
//        int row = (int) ((touchY - yOffset) / (hexagonSize * 2f));
//
//        if (col % 2 != 0) {
//            // Adjust row for odd columns
//            row = (int) ((touchY - yOffset - hexagonSize * 0.75f) / (hexagonSize * 1.5f));
//        }
//
//        // Check if the touch coordinates are within the bounds of the hexagon grid
//        if (row >= 0 && row < numRows && col >= 0 && col < numColumns) {
//            return new int[]{row, col};
//        }
//
//        return null;



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

    public static boolean willExceedBoundary(int numRows, int numColumns, float hexagonSize, float dimension) {
        // Calculate required grid dimensions
        float gridWidth = numColumns * hexagonSize * 1.5f;
        float gridHeight = (float) (numRows * hexagonSize * Math.sqrt(3) / 2f);

        // Check if grid dimensions exceed the square view dimensions
        return gridWidth > dimension || gridHeight > dimension;
    }

    //    private int getRequiredColumns(float dimension) {
//        float sideLength = dimension / (2 * (dimension / dimension) - 1f);
//        return (int) (dimension / (sideLength * 2)) + 1;
//    }
//
//    private int getRequiredRows(float dimension) {
//        float sideLength = dimension / (1.5f * (dimension / dimension));
//        return (int) (dimension / (sideLength * 1.5f)) + 1;
//    }
}
