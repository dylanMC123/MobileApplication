package com.example.project.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project.R;

public class Game2 extends AppCompatActivity implements View.OnTouchListener
{
    private final static int START_DRAGGING = 0;
    private final static int STOP_DRAGGING = 1;
    Button testButton;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        testButton = (Button) findViewById(R.id.test_drag_drop);
        testButton.setDrawingCacheEnabled(true);
        testButton.setOnTouchListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent me)
    {

        if (me.getAction() == MotionEvent.ACTION_DOWN)
        {
            status = START_DRAGGING;
        }
        if (me.getAction() == MotionEvent.ACTION_UP)
        {
            status = STOP_DRAGGING;
            Log.i("Drag", "Stopped Dragging");
        }
        else if (me.getAction() == MotionEvent.ACTION_MOVE)
        {
            if (status == START_DRAGGING)
            {
                System.out.println("Dragging");
                if (testButton.getLayoutParams() instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) testButton.getLayoutParams();


                    // Get new button position
                     int newX = (int) me.getRawX() - (testButton.getWidth() / 2);
                    int newY = (int) me.getRawY() - (testButton.getHeight() / 2);

                    // Update position
                    layoutParams.setMargins(newX, newY, 0, 0);
                    testButton.setLayoutParams(layoutParams);


                    if (newX > 100) {
                        testButton.setBackgroundColor(Color.GREEN);
                        testButton.setText("Hello");
                    }

                }
            }
        }
        return true;
    }
}