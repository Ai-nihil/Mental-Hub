package com.example.mentalhub.cognitive;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Food implements GameObject{

    private Rect rectangle;        //food object
    private int color;             //Color used for instantiating rect object
    private int scoreVal;          //Value associated to the type of food

    private int foodType;         //Type of the food (1 - 7)

    public Food(int rectHeight, int color, int startX, int startY, int playerSize, int type){

        rectangle = new Rect(startX, startY, startX + playerSize, startY + rectHeight);

        this.color = color;

        this.foodType = type;

        //Assigns score
        switch(type){


            case 1:
                scoreVal = 10;
                break;

            case 2:
                scoreVal = 10;
                break;

            case 3:
                scoreVal = 10;
                break;

            case 4:
                scoreVal = 10;
                break;

            case 5:
                scoreVal = 10;
                break;

            case 6:
                scoreVal = 10;
                break;

            case 7:
                scoreVal = 10;
                break;
        }

    }

    public int getScoreVal(){
        return scoreVal;
    }

    public int getType(){

        return foodType;

    }

    public Rect getRectangle(){

        return rectangle;

    }

    //Represents falling food
    public void addYVal(float y){

        rectangle.top -= y;
        rectangle.bottom -= y;

    }

    //Returns true if the player swipe has crossed the food
    public boolean collisionDetection(Player player){

        return Rect.intersects(rectangle, player.getRectangle());

    }

    @Override
    public void draw(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);

    }

    @Override
    public void update(){ }

}
