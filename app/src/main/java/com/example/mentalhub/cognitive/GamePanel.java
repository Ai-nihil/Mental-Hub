package com.example.mentalhub.cognitive;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;
import android.widget.Toast;
import com.example.mentalhub.R;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Player user;
    private Point userPoint;
    private FoodManager foodManager;

    private int highScore = Constants.PREF.getInt("key", 0);

    private boolean gameOver = false;
    private boolean gameStarted = false;
    private Handler handler;

    private Bitmap backgroundImage;
    public GamePanel(Context context){

        super(context);

        getHolder().addCallback(this);

        Constants.CURRENT_CONTEXT = context;

        //Instantiate player
        user = new Player(new Rect(100,100,200,200), Color.argb(0, 0, 0,0));

        //Instantiate location of the player
        userPoint = new Point(150,150);
        user.update(userPoint);

        //Instantiate the fruit-managing class
        foodManager = new FoodManager(200, Constants.SCREEN_HEIGHT - 325, 325, Color.argb(0,255,255,255));

        setFocusable(true);

    }





    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){



    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.balloon_bg);

        handler = new Handler();

        // Start the game after a 5-second delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameStarted = true;
                // Toast.makeText(getContext(), "Game started!", Toast.LENGTH_SHORT).show();
            }
        }, 5000); // 5000 milliseconds = 5 seconds

        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

        boolean retry = true;

        while(retry){

            try{

                thread.setRunning(false);
                thread.join();

            }catch(Exception e){

                e.printStackTrace();

            }

            retry = false;

        }

    }

    //Creates a new fruitManager and resets the location of the user
    public void resetGame(){

        userPoint = new Point(150,150);
        user.update(userPoint);

        //foodManager = new FoodManager(200, 200, 325, Color.argb(0,255,255,255));
        foodManager = new FoodManager(200, Constants.SCREEN_HEIGHT - 325, 325, Color.argb(0, 255, 255, 255));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){

            //User has tapped
            case MotionEvent.ACTION_DOWN:

                //Reset the game if ended
                if(gameOver){

                    resetGame();
                    gameOver = false;

                }

                break;

            //User has moved their finger, update the location of the rect
            case MotionEvent.ACTION_MOVE:

                userPoint.set((int)event.getX(), (int)event.getY());

        }

        return true;

    }

    public void update(){

        //Game is continuing


        if(!gameOver ){

            //Move the user to the new point
            user.update(userPoint);

            //Update the foodManager
            boolean x = foodManager.update();

            //Check if three foods have been missed
            if (x) {

                gameOver = true;

            }

            boolean y = foodManager.collisionDetection(user);

            //Check if bomb has been hit
            if (y) {

                gameOver = true;

            }
        }



    }

    @Override
    public void draw(Canvas canvas){

        super.draw(canvas);

        if (backgroundImage != null) {

            Rect srcRect = new Rect(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
            Rect destRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
            canvas.drawBitmap(backgroundImage, srcRect, destRect, null);
        }
        //canvas.drawColor(Color.rgb(255, 255,255));

        user.draw(canvas);

        foodManager.draw(canvas);

        if (!gameStarted) {
            //
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(50);
            String text = "Pop the Unhealthy Foods";
            float textWidth = textPaint.measureText(text);
            canvas.drawText(text, (getWidth() - textWidth) / 2, getHeight() / 2, textPaint);
        }

        //Set gameover screen
        if(gameOver){

            BitmapFactory bf = new BitmapFactory();

            Bitmap gOverImg = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.gameover);

            if(highScore < foodManager.getScore()){

                SharedPreferences.Editor editor = Constants.PREF.edit();
                editor.putInt("key", foodManager.getScore());
                editor.commit();

                highScore = foodManager.getScore();

            }

            canvas.drawColor(Color.BLACK);

            Paint p = new Paint();
            p.setColor(Color.GREEN);
            p.setTextSize(100);


            Rect bounds = new Rect();

            String text1 = "Game Over! Tap to Restart";
            p.getTextBounds(text1, 0, text1.length(), bounds);
            int x1 = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int y1 = (canvas.getHeight() / 2) - (bounds.height() / 2);

            String text2 = "Score: " + foodManager.getScore();
            p.getTextBounds(text2, 0, text2.length(), bounds);
            int x2 = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int y2 = (canvas.getHeight() / 2) - (bounds.height() / 2);

            String text3 = "High Score: " + highScore;
            p.getTextBounds(text3, 0, text3.length(), bounds);
            int x3 = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int y3 = (canvas.getHeight() / 2) - (bounds.height() / 2);


            //Game Over! Tap to Restart"
            canvas.drawText(text1, x1, y1, p);
            //score
            canvas.drawText(text2, x2, y2 + 200, p);
            //highScore
            canvas.drawText(text3, x3, y3 + 400, p);

        }

    }

}