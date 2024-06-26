package com.julius.hexgame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.julius.hexgame.R;

public class HexBoard extends View {

    private final int boardColor;
    private final int playerOneColor;
    private final int playerTwoColor;
    private final int emptyCellColor;
    private final Paint paint = new Paint();
    private final Paint strokePaint = new Paint();
    private final Path path = new Path();
    private short supremum = 5;
    private short hexagonRadius = (short) (getWidth() / supremum);
    private short oddAreaHexagons;
    private short evenAreaHexagons;
    private short totalColumnsAndRows;

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

        hexagonRadius = (short) (dimension / supremum);
        totalColumnsAndRows = (short) (dimension / hexagonRadius);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        strokePaint.setStrokeWidth(2);
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);

//        float radius = 50.0f;
//        CornerPathEffect corEffect = new CornerPathEffect(radius);
//        paint.setPathEffect(corEffect);
        // Center and radius of the hexagon
//        float cx = 500; // Center X coordinate
//        float cy = 500; // Center Y coordinate
//        short r = 55;  // Radius of the circumscribed circle
//        path.moveTo(400, 20);
//        path.lineTo(600, 20);
//        path.lineTo(700, 100);
//        path.lineTo(600, 200);
//        path.lineTo(400, 200);
//        path.lineTo(300, 100);
//        path.lineTo(400, 20);
        drawGameBoard(canvas);
    }

    private void drawGameBoard(Canvas canvas) {
//        for (int i = 0; i < totalColumns; i++) {
//            if (i % 2 == 0) {
//
//            } else {
//
//            }
//        }
        for (int row = 0; row < totalColumnsAndRows; row++) {
            for (int col = 0; col < totalColumnsAndRows; col++) {
                float x = col * hexagonRadius * 1.5f + hexagonRadius * 1.1f;
                float y = row * hexagonRadius * 2f + (col % 2) * hexagonRadius + hexagonRadius;
//                if (row == 0) {
//                    x
//                }

                drawHexagon(canvas, x, y);
            }
        }
    }

    private void drawHexagon(Canvas canvas, float cx, float cy) {
        // Calculate vertex positions
        for (int i = 0; i < 6; i++) {
            float angle = (float) (i * 2 * Math.PI / 6); // Angle for each vertex
            float x = cx + hexagonRadius * (float) Math.cos(angle);
            float y = cy + hexagonRadius * (float) Math.sin(angle);

            if (i == 0) {
                path.moveTo(x, y); // Move to the first vertex
            } else {
                path.lineTo(x, y); // Draw a line to the next vertex
            }
        }
        path.close();
        canvas.drawPath(path, paint);
        canvas.drawPath(path, strokePaint);
    }
}
