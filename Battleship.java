package com.company ;
import java.util.Scanner ;
import java.util.Random;


class Position
{
    private int row ;
    private int col ;
    public Position(){}
    public  Position(int _row , int _col)
    {
        this.row = _row;
        this.col = _col ;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

}


class Ship
{
    private int lives ;
    private String name ;
    private Position startingPosition ;
    private Position endPosition;



    public Ship(int _lives , String _name)
    {
        this.startingPosition = new Position() ;
        this.lives = _lives;
        this.name = _name ;
        this.endPosition= new Position(0,0);
        this.startingPosition = new Position(0,0);
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void setLives(int lives) {
        this.lives = lives;
    }
    protected boolean isAlive()
    {
        return lives>0;
    }

    public String getName() {
        return name;
    }
    protected int getLives() {
        return lives;
    }
    protected void setStartingPosition(int row, int col)
    {
        this.startingPosition.setRow(row);
        this.startingPosition.setCol(col);
    }
    protected void setEndPosition(int row, int col)
    {
        this.endPosition.setRow(row);
        this.endPosition.setCol(col);
    }

    protected Position getStartingPosition() {
        return startingPosition;
    }

    protected Position getEndPosition() {
        return endPosition;
    }

    protected boolean isHit(Position playerInput)
    {
        return ((this.startingPosition.getRow() == this.endPosition.getRow() &&
                this.startingPosition.getRow() == playerInput.getRow()
                &&	playerInput.getCol() >= this.startingPosition.getCol() &&
                playerInput.getCol() <= this.endPosition.getCol())
                || (this.startingPosition.getCol() == this.endPosition.getCol() &&
                this.startingPosition.getCol() == playerInput.getCol()
                && playerInput.getRow() >= this.startingPosition.getRow() &&
                playerInput.getRow() <= this.endPosition.getRow()));
    }

}

class Board
{
    private char[][] board;
    public Board()
    {
        this.board = new char[7][7] ;
        for(int i = 0; i<7 ;i++)
            for (int j = 0 ; j < 7 ; j++)
                this.board[i][j] = '.' ;
    }
    public void printBoard(){
        for(int i = 0; i<7 ;i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(this.board[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public char[][] getBoard() {
        return board;
    }

    protected void makeChange(int row, int col, char symbol)
    {
        this.board[row][col]= symbol ;
    }

}


class Game {
    private int shipSize ;
    private Ship[] ships ;
    private Board board ;
    private Board hitBoard;


    protected boolean isValidPlace(Position start,Position end)
    {
        if(start.getRow() == end.getRow())
        {
            for(int i = start.getCol() ; i < end.getCol() ;i++)
            {
                if(board.getBoard()[start.getRow()][i] == 'o')
                {
                    return false ;
                }
            }
        }
        else  if(start.getCol() == end.getCol())
        {
            for(int i = start.getRow() ; i < end.getRow() ;i++)
            {
                if(board.getBoard()[i][start.getCol()] == 'o')
                {
                    return false ;
                }
            }
        }
        return true ;
    }



    public Game()
    {
        this.hitBoard = new Board();
        this.board= new Board();
        this.shipSize = 3 ;
        this.ships = new Ship[shipSize];
    }

    public void startGame()
    {
        for(int i = 0 ; i < shipSize ;i++)
        {
            this.ships[i] = new Ship(i+2,"Ship"+ i);

            int row = 0 ;
            int col = 0;

            Random randChoice = new Random();
            int choice = randChoice.nextInt(2);
            if(choice == 0) {

                Random randRow2 = new Random();
                Random randCol = new Random();
                do {
                    row = randRow2.nextInt(7 - this.ships[i].getLives());

                    col = randCol.nextInt(7);
                    this.ships[i].setStartingPosition(row, col);
                    this.ships[i].setEndPosition(row + this.ships[i].getLives(), col);
                }while(!this.isValidPlace(this.ships[i].getStartingPosition(),this.ships[i].getEndPosition()));
                for (int k = 0; k < this.ships[i].getLives(); k++) {
                    this.board.makeChange(row + k, col, 'o');
                }
            }
            else {

                Random randCol2 = new Random();
                Random randRow = new Random();
                do {

                    col = randCol2.nextInt(7 - this.ships[i].getLives());
                    row = randRow.nextInt(7);

                    this.ships[i].setStartingPosition(row, col);
                    this.ships[i].setEndPosition(row, col + this.ships[i].getLives());
                }while(!this.isValidPlace(this.ships[i].getStartingPosition(),this.ships[i].getEndPosition()));
                for (int k = 0; k < this.ships[i].getLives(); k++) {
                    this.board.makeChange(row, col + k, 'o');
                }
            }

        }
    }



    public void playerAttack(char row,int col)
    {
        int _row = (int) row - (int)'a';
        if(this.board.getBoard()[_row][col] == 'o')
        {
            System.out.println("Hit !");
            this.board.makeChange(_row,col,'x');
            this.hitBoard.makeChange(_row,col,'x');
            for(int i = 0 ; i < this.shipSize ;i++)
            {
                if(this.ships[i].isHit(new Position(_row,col)))
                {
                    this.ships[i].setLives(this.ships[i].getLives()-1);
                    if(!this.ships[i].isAlive()){
                        System.out.println("You sunk " + this.ships[i].getName());
                        this.shipSize-- ;
                    }
                }

            }
        }
        else
        {
            System.out.println("Miss!");
            this.board.makeChange(_row,col,'-');
            this.hitBoard.makeChange(_row,col,'-');
        }
    }
    public boolean isOver()
    {
        return this.shipSize == 0 ;
    }

    public Board getHitBoard() {
        return hitBoard;
    }
/*
    public Board getBoard() {
        return board;
    }*/
}


public class Main {
    public static void main(String[] args) {

        int moves = 0;
        Game game1 = new Game();
        game1.startGame();
        while(!game1.isOver())
        {
            //game1.getBoard().printBoard();
            game1.getHitBoard().printBoard();
            Scanner sym = new Scanner(System.in);
            Scanner num = new Scanner(System.in);
            game1.playerAttack(sym.next().charAt(0),num.nextInt());
            moves++;

        }
        System.out.println("You win!");
        System.out.println("You needed" + moves + "moves" );
    }
}
