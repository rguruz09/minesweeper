package com.sjsu.raghu.minesweeper;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

/**
 * Created by Raghu on 2/26/2016.
 */
public class Cell extends Button {

    // Each Cell properties
    private boolean bMine;
    private boolean bFlag;
    private boolean bClosed;
    private int iNumofMines;

    private static final String TAG = "MINE ";


    public Cell(Context context)
    {
        super(context);
    }

    public Cell(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public Cell(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setupDefaultCell(){
        bMine = false;
        bFlag = false;
        bClosed = true;
        iNumofMines = 0;
        int drawableId = getResources().getIdentifier("bac", "drawable", "com.sjsu.raghu.minesweeper");
        this.setBackgroundResource(drawableId);

    }

    public void setbMine(){
        bMine = true;
    }
    public boolean getbMine(){
        return bMine;
    }

    public void setbFlag(){
        if(bFlag){
            bFlag = false;
            int drawableId = getResources().getIdentifier("bac", "drawable", "com.sjsu.raghu.minesweeper");
            this.setBackgroundResource(drawableId);
        }else{
            bFlag = true;
            int drawableId = getResources().getIdentifier("flag", "drawable", "com.sjsu.raghu.minesweeper");
            this.setBackgroundResource(drawableId);
        }
    }

    public boolean getbFlag(){
        return bFlag;
    }

    public void setbClosed(){
        bClosed = false;
        int drawableId = getResources().getIdentifier("empty", "drawable", "com.sjsu.raghu.minesweeper");
        this.setBackgroundResource(drawableId);
    }

    public int getiNumMines(){
        return iNumofMines;
    }
    public boolean getbClosed(){
        return bClosed;
    }

    public void setiNumofMines(){
        iNumofMines++;
    }

    public void cellOpen()
    {
        if(!this.getbClosed())
            return;

        setbClosed();

        if(this.getbMine())
            triggerMine();
        else
            showNumber();
    }

    //show the number icon
    public void showNumber()
    {
        String img =  "m"+this.getiNumMines();
        Log.v(TAG, "Image is " + img);
        //loadCellNumber(img);
        int drawableId = getResources().getIdentifier(img, "drawable", "com.sjsu.raghu.minesweeper");
        this.setBackgroundResource(drawableId);
    }

    //show the mine icon
    public void triggerMine()
    {
        int drawableId = getResources().getIdentifier("mine", "drawable", "com.sjsu.raghu.minesweeper");
        this.setBackgroundResource(drawableId);

    }




}
