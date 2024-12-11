package com.julius.hexgame.util;

import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.julius.hexgame.PlayerConfigActivity;

public class ValidationUtil {
    public static Object[] validateDimensions(AppCompatActivity configActivity, int rows,
                                              int columns, int dimension) {
        int hexSize = 80;
        while ((((columns) * hexSize * 1.6f) > dimension)
                || (((rows) * hexSize * 2f) > dimension)) {
            hexSize -= 10;
            if (hexSize < 50) break;
        }
        if (hexSize < 50) {
            Toast.makeText(configActivity,
                            "The dimensions are too large",
                            Toast.LENGTH_SHORT)
                    .show();
            return new Object[]{false, 0};
        } else if (smallDimensions(rows, columns)) {
            Toast.makeText(configActivity,
                            "Dimensions are not large enough to play the game",
                            Toast.LENGTH_SHORT)
                    .show();
            return new Object[]{false, 0};
        } else {
            return new Object[]{true, hexSize};
        }
    }

    public static boolean smallDimensions(int rows, int columns) {
        return rows * columns <= 2;
    }
}
