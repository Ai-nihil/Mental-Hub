package com.example.mentalhub.cognitive;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;

import com.example.mentalhub.R;

import java.util.ArrayList;
import java.util.Random;

public class FoodManager {

    //Used to assign images to the  food rectangles
    private static BitmapFactory bf = new BitmapFactory();

    private static Bitmap FOOD_IMAGE1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.f1);
    private static Bitmap FOOD_IMAGE2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.f2);
    private static Bitmap FOOD_IMAGE3 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.f3);
    private static Bitmap FOOD_IMAGE4 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.f4);
    private static Bitmap FOOD_IMAGE5 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.f5);
    private static Bitmap FOOD_IMAGE6 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.h1);

    private static Bitmap BOMB_IMAGE = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.h1);

    //Instantiate sound clips
    //private static MediaPlayer BOMB_NOISE = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.bomb);
    private static MediaPlayer MISSED = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.missed);
    private static MediaPlayer FOOD1 = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.pop);
    private static MediaPlayer FOOD2 = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.pop);

    private ArrayList<Food> foods;  //Array of food currently on screen

    private int foodLocation;        //Location of falling food
    private int playerSize;           //Size of the user's swipe space
    private int foodHeight;          //Height of the food rectangle
    private int color;

    private int score = 0;            //Game score

    private int highScore = Constants.PREF.getInt("key", 0);

    private int misses = 0;           //Number of food not sliced

    private Random rand = new Random();

    private long start;
    private long initialization;

    public FoodManager(int playerSize, int foodLocation, int foodHeight, int color){

        foods = new ArrayList<>();

        this.foodLocation = foodLocation;
        this.playerSize = playerSize;
        this.foodHeight = foodHeight;
        this.color = color;

        start = initialization = System.currentTimeMillis();

        //Add food to the array
        populateFood();

    }

    public int getScore(){

        return score;

    }

    public boolean collisionDetection(Player player){

        //Check each food on screen for detection
        for(Food f : foods){

            if(f.collisionDetection(player)){

                //Game over, hit a bomb
                if(f.getType() == 6){

                    //BOMB_NOISE.start();
                    return true;

                }

                //Determine a noise to play for slicing the food
                int val = rand.nextInt(2 - 1) + 1;

                if(val == 1){ FOOD1.start(); }
                else{ FOOD2.start(); }

                //Increment score
                score += f.getScoreVal();

                //Remove from the arraylist
                foods.remove(f);

            }

        }

        return false;

    }

    //Assigns next falling food to a random type
    //Based on percentage rarity
    private int determineFoodType(){

        int val = rand.nextInt(100 - 1) + 1;
        int type = -1;

        if(1 <= val && val <= 30){
            type = 1;
        }else if(30 < val && val <= 60) {
            type = 2;
        }else if(60 < val && val <= 75) {
            type = 3;
        }else if(75 < val && val <= 85) {
            type = 4;
        }else if(85 < val && val <= 90) {
            type = 5;
        }else if(90 < val && val <= 98) {
            type = 6;
        }else if(val == 100 || val == 99) {
            type = 7;
        }

        return type;
    }

    private void populateFood(){

        //Starting Y position for the food
        int currentY = Constants.SCREEN_HEIGHT- 325;

        while(currentY > -foodHeight){

            //Determine where horizontally to place the food
            int xStart = (int)(Math.random() * (Constants.SCREEN_WIDTH - playerSize));

            //Determines which type of food is spawned
            int type = determineFoodType();

            //Add to the array list
            foods.add(new Food(foodHeight, color, xStart, currentY, playerSize, type));
            currentY -= foodHeight +Constants.SCREEN_HEIGHT - 325;


        }

    }
    public boolean update(){

        int timeElapsed = (int)(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();

        //Determine fall speed of the food
        //Value gets larger as the game progresses
        float speed = (float)(Math.sqrt(1 + (start - initialization)/ 1000.0)) * Constants.SCREEN_HEIGHT / 10000.0f;

        //Add y value to each food on the screen
        for(Food food : foods){

            food.addYVal(speed * timeElapsed);

        }

        //food has made it to the top of the screen, add a strike to the count
        if(foods.get(foods.size()-1).getRectangle().bottom <= 0){



            //food isn't a bomb, mark a penalty
            if(foods.get(foods.size()-1).getType() != 6) {

                MISSED.start();
                misses++;
            }

            //Game Over
            if(misses == 3){ return true; }

            //Remove from the array list
            foods.remove(foods.get(foods.size()-1));

        }

        //Add a new food to be spawned
        int type = determineFoodType();

        int xStart = (int)(Math.random() * (Constants.SCREEN_WIDTH - playerSize));

        foods.add(0, new Food(foodHeight, color, xStart,
                foods.get(0).getRectangle().bottom + foodHeight + foodLocation,
                playerSize, type));

        return false;

    }

    public void draw(Canvas canvas){

        Bitmap photo = null;

        for(Food food : foods){

            food.draw(canvas);

            //Assigns correct image to food based on type
            switch(food.getType()){

                case 1:
                    photo = FOOD_IMAGE1;
                    break;
                case 2:
                    photo =  FOOD_IMAGE2;
                    break;
                case 3:
                    photo =  FOOD_IMAGE3;
                    break;
                case 4:
                    photo =  FOOD_IMAGE4;
                    break;
                case 5:
                    photo =  FOOD_IMAGE5;
                    break;
                case 6:
                    photo = BOMB_IMAGE;
                    break;
                case 7:
                    photo =  FOOD_IMAGE6;
                    break;
            }

            canvas.drawBitmap(photo, null, food.getRectangle(), new Paint());

        }

        //Represents score
        Paint p = new Paint();
        p.setTextSize(50);
        p.setColor(Color.WHITE);
        canvas.drawText("Score: " + score, 50, 50 + p.descent() - p.ascent(), p);



        Paint pH = new Paint();
        pH.setTextSize(50);
        pH.setColor(Color.WHITE);
        canvas.drawText("High Score: " + highScore, Constants.SCREEN_WIDTH - 350, 50 + pH.descent() - pH.ascent(), pH);


        //Number of misses
        if(misses == 1) {
            Paint p1 = new Paint();
            p1.setTextSize(50);
            p1.setFakeBoldText(true);
            p1.setColor(Color.RED);
            canvas.drawText("X", 50, 200 + p1.descent() - p1.ascent(), p1);
        }else if(misses == 2){
            Paint p1 = new Paint();
            p1.setTextSize(50);
            p1.setFakeBoldText(true);
            p1.setColor(Color.RED);
            canvas.drawText("X X", 50, 200 + p1.descent() - p1.ascent(), p1);
        }else if(misses == 3){
            Paint p1 = new Paint();
            p1.setTextSize(50);
            p1.setFakeBoldText(true);
            p1.setColor(Color.RED);
            canvas.drawText("X X X", 50, 200 + p1.descent() - p1.ascent(), p1);
        }

    }
}
