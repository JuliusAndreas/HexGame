package com.julius.hexgame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.julius.hexgame.R;

import java.util.Arrays;

public class HexBoard extends View {

    private final int boardColor;
    private final int playerOneColor;
    private final int playerTwoColor;
    private final int emptyCellColor;

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
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());

        setMeasuredDimension(dimension, dimension);
    }
}
