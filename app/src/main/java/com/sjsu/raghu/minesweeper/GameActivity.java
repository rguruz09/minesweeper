package com.sjsu.raghu.minesweeper;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    //Private Variables
    TableLayout mineField;

    // Game requirements
    int iTotalRows;
    int iTotalCols;
    int iTotalMines;
    int iTotalFlags;
    int iCorrectFlags;
    //temp
    int tileWH = 9;
    int tilePadding = 9;

    //Array of Mine Cells
    Cell[][] cells;

    //Logging purpose
    private static final String TAG = "MINE ";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);



        if(getResources().getConfiguration().orientation == 1){
            // 12*8 cells with 30 mines
            iTotalRows = 12;
            iTotalCols = 8;
            iTotalMines = 20;
            setContentView(R.layout.activity_game);
            mineField = (TableLayout)findViewById(R.id.MineField);

            if (bundle == null ) {

                Button imageButton = (Button) findViewById(R.id.btnRestart);
                imageButton.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       restartButtonClick();
                                                   }
                                               }
                );

                prepareMinesTable();
                setMines();
                showGame();

            } else {
                prepareMinesTable();
                changeOrientation(bundle);
                showGame();
            }

        }else {

            iTotalRows = 8;
            iTotalCols = 12;
            iTotalMines = 20;

            setContentView(R.layout.activity_game_landscape);
            mineField = (TableLayout)findViewById(R.id.MineField);

            if (bundle == null ) {

                prepareMinesTable();
                setMines();
                showGame();

            } else {
                // Probably initialize members with default values for a new instance
                prepareMinesTable();
                changeOrientation(bundle);
                showGame();
            }

        }

//        if (savedInstanceState == null ) {
//            // Restore value of members from saved state
//            setContentView(R.layout.activity_game);
//        } else {
//            // Probably initialize members with default values for a new instance
//            setContentView(R.layout.activity_game_landscape);
//        }


    }


    public void changeOrientation(Bundle bundle){
        //prepareMinesTable();
        ArrayList<String> al;


        for(int i = 0; i < iTotalCols; i++){
            for (int j = 0; j < iTotalRows; j++){
                al = bundle.getStringArrayList(i+","+j);
                if(Boolean.parseBoolean(al.get(0)))
                  cells[j][i].setbClosed();
                if(Boolean.parseBoolean(al.get(1)))
                    cells[j][i].setbFlag();
                if(Boolean.parseBoolean(al.get(2)))
                    cells[j][i].setbMine();

                cells[j][i].setiNumofMines(Integer.parseInt(al.get(3)));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        for (int i = 0; i < iTotalRows; i++ ){
            for(int j = 0; j < iTotalCols; j++){
                ArrayList<String> a = new ArrayList<>();
                int inum = cells[i][j].getiNumMines();
                a.add( (cells[i][j].getbClosed()) ? "true" : "false");
                a.add( (cells[i][j].getbFlag()) ? "true" : "false");
                a.add( (cells[i][j].getbMine()) ? "true" : "false");
                a.add(inum+"");
                bundle.putStringArrayList(i+","+j, a);
            }
        }


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(bundle);

    }

    public void restartButtonClick(){
        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    public void prepareMinesTable(){

        //get the array of cells
        cells = new Cell[iTotalRows][iTotalCols];

        for(int row = 0; row < iTotalRows; row++){
            for(int col = 0; col < iTotalCols; col++){

                cells[row][col] = new Cell(this);
                cells[row][col].setupDefaultCell();

                final int curRow = row;
                final int curCol = col;

                //add a click listener
                cells[row][col].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Log.v(TAG, "Cell is clicked" + curRow +" " + curCol);
                        cellClicked(curRow, curCol, true);
                    }
                });

                //add a long click listener
                cells[row][col].setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View view)
                    {
                        if(cells[curRow][curCol].getbClosed()){
                            cells[curRow][curCol].setbFlag();
                            if(cells[curRow][curCol].getbFlag()){
                                if(cells[curRow][curCol].getbMine())
                                    iCorrectFlags++;
                                iTotalFlags++;
                            }else {
                                if(cells[curRow][curCol].getbMine())
                                    iCorrectFlags--;
                                iTotalFlags--;
                            }
                            TextView textView = (TextView) findViewById(R.id.ctrMines);
                            textView.setText(iTotalFlags+"");
                            if(iTotalFlags == iTotalMines){
                                if(isGameWon()){
                                    winGame();
                                }else {
                                    loseGame();
                                }
                            }
                        }
                        return true;
                    }
                });
            }
        }
    }

    public void setMines(){

        Random random = new Random();
        int curMineRow;
        int curMineCol;

        for(int i = 0; i < iTotalMines; i++){

            curMineCol = random.nextInt(iTotalCols);
            curMineRow = random.nextInt(iTotalRows);

            if(curMineCol == iTotalCols && curMineRow == iTotalRows){
                    i--;
            }else if(cells[curMineRow][curMineCol].getbMine()){
                i--;
            }else{

                cells[curMineRow][curMineCol].setbMine();

                int startRow = curMineRow-1;
                int startCol = curMineCol-1;

                int checkRows = 3;
                int checkCols = 3;

                //for Rows
                if(startRow < 0){
                    startRow = 0;
                    checkRows = 2;
                }else if(startRow+3 > iTotalRows)
                    checkRows = 2;

                //for Columns
                if(startCol < 0){
                    startCol = 0;
                    checkCols = 2;
                }
                else if(startCol+3 > iTotalCols)
                    checkCols = 2;

                for(int j=startRow;j<startRow+checkRows;j++)
                {
                    for(int k=startCol;k<startCol+checkCols;k++)
                    {
                        if(!cells[j][k].getbMine()){
                            Log.v(TAG, "mine Index"+j+" "+k );
                            cells[j][k].setiNumofMines();
                        }

                    }
                }
            }
        }
    }

    public void showGame()
    {
        for(int row=0; row<iTotalRows; row++)
        {
            //create a new table row
            TableRow tableRow = new TableRow(this);
            //set the height and width of the row
            tableRow.setLayoutParams(new TableRow.LayoutParams((tileWH * tilePadding) * iTotalCols, tileWH * tilePadding));

            //for every column
            for(int col=0; col<iTotalCols; col++)
            {
                //set the width and height of the tile
                cells[row][col].setLayoutParams(new TableRow.LayoutParams(tileWH * tilePadding, tileWH * tilePadding));
                //add some padding to the tile
                cells[row][col].setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
                //add the tile to the table row
                tableRow.addView(cells[row][col]);
            }
            //add the row to the minefield layout
            mineField.addView(tableRow,new TableLayout.LayoutParams((tileWH * tilePadding) * iTotalCols, tileWH * tilePadding));
        }
    }

    public void cellClicked(int row, int col, boolean frmUsr)
    {
        if(frmUsr && !cells[row][col].getbFlag())
        {
            if(cells[row][col].getbMine())
            {
                cells[row][col].cellOpen();
                loseGame();
            }
            else if (isGameWon())
            {
                winGame();
            }
            else
            {
                cellClicked(row, col, false);
            }
        }

        if(!frmUsr && (cells[row][col].getbMine() || cells[row][col].getbFlag())) {
            return;
        }

        if(cells[row][col].getbFlag()){
            return;
        }

        cells[row][col].cellOpen();

        if(cells[row][col].getiNumMines() > 0)
            return;

        //go one row and col back
        int startRow = row-1;
        int startCol = col-1;
        //check 3 rows across and 3 down
        int checkRows = 3;
        int checkCols = 3;
        if(startRow < 0) //if it is on the first row
        {
            startRow = 0;
            checkRows = 2;
        }
        else if(startRow+3 > iTotalRows) //if it is on the last row
            checkRows = 2;

        if(startCol < 0)
        {
            startCol = 0;
            checkCols = 2;
        }
        else if(startCol+3 > iTotalCols) //if it is on the last row
            checkCols = 2;

        for(int i=startRow;i<startRow+checkRows;i++) //3 or 2 rows across
        {
            for(int j=startCol;j<startCol+checkCols;j++) //3 or 2 rows down
            {
                if(cells[i][j].getbClosed())
                    cellClicked(i,j, false);
            }
        }
    }

    public boolean isGameWon()
    {
        if( iCorrectFlags == iTotalMines )
            return true;
        else
            return false;
    }

    public void loseGame()
    {
        //imageButton.setBackgroundResource(R.drawable.lose);
        TextView textView = (TextView) findViewById(R.id.gameMessage);
        textView.setText("Sorry!!! you lose.. Please try again");
        endGame();
    }

    public void winGame()
    {
       // imageButton.setBackgroundResource(R.drawable.win);
        TextView textView = (TextView) findViewById(R.id.gameMessage);
        textView.setText("Congrats !!! you won");
        endGame();
    }

    public void endGame()
    {

        for(int i=0;i<iTotalRows;i++)
        {
            for(int j=0;j<iTotalCols;j++)
            {
                //if the tile is covered
                if(cells[i][j].getbClosed())
                {
                    //if there is no flag or mine
                    if(!cells[i][j].getbFlag() && !cells[i][j].getbMine())
                    {
                        cells[i][j].cellOpen();
                    }
                    //if there is a mine but no flag
                    else if(cells[i][j].getbMine() && !cells[i][j].getbFlag())
                    {
                        cells[i][j].cellOpen();
                    }
                }
            }
        }

    }

}
