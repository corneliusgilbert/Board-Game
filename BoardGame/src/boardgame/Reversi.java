/**
 * CSCI1130 Java Assignment 6 BoardGame Reversi
 * Aim: Practise subclassing, method overriding
 *      Learn from other subclass examples
 * 
 * I declare that the assignment here submitted is original
 * except for source material explicitly acknowledged,
 * and that the same or closely related material has not been
 * previously submitted for another course.
 * I also acknowledge that I am aware of University policy and
 * regulations on honesty in academic work, and of the disciplinary
 * guidelines and procedures applicable to breaches of such
 * policy and regulations, as contained in the website.
 *
 * University Guideline on Academic Honesty:
 *   http://www.cuhk.edu.hk/policy/academichonesty
 * Faculty of Engineering Guidelines to Academic Honesty:
 *   https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Student Name: Cornelius Gilbert
 * Student ID  : 1155147665
 * Date        : 27/11/2021
 */

package boardgame;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Reversi is a TurnBasedGame
 */
public class Reversi extends TurnBasedGame {
    
    public static final String BLANK = " ";
    String winner;


    /*** TO-DO: STUDENT'S WORK HERE ***/
    //Add several fields that is important for this program.
    protected boolean endOfProgram = false;
    protected int black = 0;
    protected int white = 0;
    
    //constructor reversi to create 8x8 boardgame with the first player "BLACK" and second player "WHITE"
    public Reversi(){
        super(8,8,"BLACK","WHITE");
        this.setTitle("Reversi");
    }
    
    /*override the inherited initGame method from super class. In this case, we will prepare 2 black boxes
    and 2 white boxes at the centre of the board.
    */
    @Override
    protected void initGame(){
        for (int y = 0; y < yCount; y++)
            for (int x = 0; x < xCount; x++)
                pieces[x][y].setText(BLANK);
        pieces[3][3].setBackground(Color.WHITE);
        pieces[4][4].setBackground(Color.WHITE);
        pieces[3][4].setBackground(Color.BLACK);
        pieces[4][3].setBackground(Color.BLACK);
        pieces[3][3].setText("WHITE");
        pieces[4][4].setText("WHITE");
        pieces[3][4].setText("BLACK");
        pieces[4][3].setText("BLACK");
    }
    
    //Override the gameAction
    @Override
    protected void gameAction(JButton triggeredButton, int x, int y){
        
        //Now we call the isValidMove method. If the move is invalid, then "Invalid Move!" will be printed and the program will allow the user to input another box again.
        if (!isValidMove(x,y)){
            addLineToOutput("Invalid move!");
            return;
        }
        //if theh move is valid, then the following string will be printed.
        addLineToOutput(currentPlayer + " piece at (" + x + ", " + y + ")");
        
        //After each click is done, we check whether the game has reached its end or not.
        checkEndGame(x, y);
        

        if (gameEnded)
        {
            //If the game has reached it end, the following code will be executed
            //First, we count the pieces on the board and print the score of black and white respectively.
            countPieces();
            addLineToOutput("BLACK score: " + black);
            addLineToOutput("WHITE score: " + white);
            
            //if the number of white is bigger than black, then the winner is white.
            if(white > black)
                addLineToOutput("Winner is WHITE!");
            //if the number of black is bigger than white, then the winner is black
            else if(black > white)
                addLineToOutput("Winner is BLACK!");
            //otherwise, the  match will be considered as draw.
            else
                addLineToOutput("Draw game!");
            
            //Afterwards, we end the game by using the executing code.
            addLineToOutput("Game ended!");
            JOptionPane.showMessageDialog(null, "Game ended!");
        }
        else
            //If the game has not reached it end, then the changeTurn method will be called
            changeTurn();
    }
    
    
    //Override the inheritted checkEndGame method from the super class. 
    @Override
    protected boolean checkEndGame(int moveX, int moveY){
        
        //first we call the mustPass() method and store the return value in a variable called mustPassResult.
        boolean mustPassResult = mustPass();
        
        //If the the player must pass but it has reached the end of the game (no more blank box left), then gameEnded will become true.
        if (mustPassResult && endOfProgram){            
            gameEnded = true;
        }
        //Else If the player must pass but it has not reached the end of the game (blank box still available), then there will be change turn.
        else if (mustPassResult && !endOfProgram){
            changeTurn();
            addLineToOutput("Pass!");
            
            //After change turn, then we check whether there will be a consecutive pass or not. If yes, then the game will be ended. 
            if(mustPass() == true){
                changeTurn();
                addLineToOutput("Pass!");
                gameEnded = true;
            }
            //If there is no consecutive pass, then the game will continue.
            else{
                gameEnded = false;
            }
        //Else, the game will continue as usual if the player still have at least one possible move.
        }
        else{
            gameEnded = false;
        }
        
        //lastly, we return the value of gameEnded
        return gameEnded;
    }
    
    //This is a method to check whether or not the move is valid.
    protected boolean isValidMove(int x, int y){
        
        //declare some local variables that is important in this method.
        boolean validMoveChecker = false;
        String nonCurrentPlayer;
        Color flipColor;
        
        //define current player and non-current player in a partiular turn.
        if (currentPlayer.equals("BLACK")) {
            nonCurrentPlayer = "WHITE";
            flipColor = Color.BLACK;
        }
        else{
            nonCurrentPlayer = "BLACK";
            flipColor = Color.WHITE;
        }
        
        //If the box clicked is not blank, then it will return false
        if(!(pieces[x][y].getText().equals(BLANK)))
            return false;
        //If the box clicked is blank, then the check will continue.
        
        /*In this case, we will check the move validity by checking all 8 directions around the box.
        Using try-catch, we can eliminate the error related to array index out of bound exception.
        
        if there is a color of nonCurrentPlayer around the box clicked, then we will inspect whether there is any piece that can be flipped.
        If there is any piece that can be flipped, then the program will flip the piece directly and the validMoveChcker will be true.
        
        Since there are 8 directions to be checked and the logic are all the same, then detail explanation will only be provided in the first try-catch.
        */
        
        //Checking [x+1][y] array (the right-hand side the piece clicked)
        try{ 
            //if the color of piece in the right-hand direction is equal to the color of non-current player
            if(pieces[x+1][y].getText().equals(nonCurrentPlayer)){
                //We check in the same direction for all other array excluding the above checked array.
                for (int index = 1; index < xCount - x - 1; index++){
                    //When the program encounter BLANK, then the check for this directions done.
                    if (pieces[x+1+index][y].getText().equals(BLANK))
                        break;
                    /*When the program encounter the color of current player, then the flip action will be executed,
                    the validMoveChecker becomes true, and the check for this directions done.
                    */
                    if (pieces[x+1+index][y].getText().equals(currentPlayer)){
                        pieces[x][y].setBackground(flipColor);
                        pieces[x][y].setText(currentPlayer);
                        for(int index2 = 0; index2 <= index; index2++){
                            pieces[x+1+index2][y].setBackground(flipColor);
                            pieces[x+1+index2][y].setText(currentPlayer);
                        }
                        validMoveChecker = true;
                        break;
                    }
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        
        //checking [x-1][y] array (lefthand-side of the piece clicked)
        try{
            if (pieces[x-1][y].getText().equals(nonCurrentPlayer)){
                for (int index = 1; index < x; index++){
                    if (pieces[x-1-index][y].getText().equals(BLANK))
                        break;
                    if (pieces[x-1-index][y].getText().equals(currentPlayer)){
                        pieces[x][y].setBackground(flipColor);
                        pieces[x][y].setText(currentPlayer);
                        for(int index2 = 0; index2 <= index; index2++){
                            pieces[x-1-index2][y].setBackground(flipColor);
                            pieces[x-1-index2][y].setText(currentPlayer);
                        }
                        validMoveChecker = true;
                        break;
                    }
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        
        //checking [x][y-1] array (below the piece clicked)
        try{        
            if(pieces[x][y-1].getText().equals(nonCurrentPlayer)){
                for (int index = 1; index < y; index++){
                    if (pieces[x][y-1-index].getText().equals(BLANK))
                        break;
                    if (pieces[x][y-1-index].getText().equals(currentPlayer)){
                        pieces[x][y].setBackground(flipColor);
                        pieces[x][y].setText(currentPlayer);
                        for(int index2 = 0; index2 <= index; index2++){
                            pieces[x][y-1-index2].setBackground(flipColor);
                            pieces[x][y-1-index2].setText(currentPlayer);
                        }
                        validMoveChecker = true;
                        break;
                    }
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        
        //checking [x][y+1] array (above the piece clicked)
        try{
            if(pieces[x][y+1].getText().equals(nonCurrentPlayer)){
                for (int index = 1; index < yCount - y - 1; index++){
                    if (pieces[x][y+1+index].getText().equals(BLANK))
                        break;
                    if (pieces[x][y+1+index].getText().equals(currentPlayer)){
                        pieces[x][y].setBackground(flipColor);
                        pieces[x][y].setText(currentPlayer);
                        for(int index2 = 0; index2 <= index; index2++){
                            pieces[x][y+1+index2].setBackground(flipColor);
                            pieces[x][y+1+index2].setText(currentPlayer);
                        }
                        validMoveChecker = true;
                        break;
                    }
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        
        //Checking [x+1][y+1] array (diagonal to the top-right)
        try{
            if(pieces[x+1][y+1].getText().equals(nonCurrentPlayer)){
                for (int index = 1; index < Math.min(yCount - y - 1, xCount - x - 1); index++){
                    if (pieces[x+1+index][y+1+index].getText().equals(BLANK))
                        break;
                    if (pieces[x+1+index][y+1+index].getText().equals(currentPlayer)){
                        pieces[x][y].setBackground(flipColor);
                        pieces[x][y].setText(currentPlayer);
                        for(int index2 = 0; index2 <= index; index2++){
                            pieces[x+1+index2][y+1+index2].setBackground(flipColor);
                            pieces[x+1+index2][y+1+index2].setText(currentPlayer);
                        }
                        validMoveChecker = true;
                        break;
                    }
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        
        //Checking [x-1][y-1] array (diagonal to the bottom-left)
        try{
            if(pieces[x-1][y-1].getText().equals(nonCurrentPlayer)){
                for (int index = 1; index < Math.min(x, y); index++){
                    if (pieces[x-1-index][y-1-index].getText().equals(BLANK))
                        break;
                    if (pieces[x-1-index][y-1-index].getText().equals(currentPlayer)){
                        pieces[x][y].setBackground(flipColor);
                        pieces[x][y].setText(currentPlayer);
                        for(int index2 = 0; index2 <= index; index2++){
                            pieces[x-1-index2][y-1-index2].setBackground(flipColor);
                            pieces[x-1-index2][y-1-index2].setText(currentPlayer);
                        }
                        validMoveChecker = true;
                        break;
                    }
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        
        //Checking [x+1][y-1] array (diagonal to the bottom-right)
        try{
            if(pieces[x+1][y-1].getText().equals(nonCurrentPlayer)){
                for (int index = 1; index < Math.min(xCount - x - 1, y); index++){
                    if (pieces[x+1+index][y-1-index].getText().equals(BLANK))
                        break;
                    if (pieces[x+1+index][y-1-index].getText().equals(currentPlayer)){
                        pieces[x][y].setBackground(flipColor);
                        pieces[x][y].setText(currentPlayer);
                        for(int index2 = 0; index2 <= index; index2++){
                            pieces[x+1+index2][y-1-index2].setBackground(flipColor);
                            pieces[x+1+index2][y-1-index2].setText(currentPlayer);
                        }
                        validMoveChecker = true;
                        break;
                    }
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        
        //Checking [x-1][y+1] array (diagonal to the top-left)
        try{
            if(pieces[x-1][y+1].getText().equals(nonCurrentPlayer)){
                for (int index = 1; index < Math.min(x, yCount - y - 1); index++){
                    if (pieces[x-1-index][y+1+index].getText().equals(BLANK))
                        break;
                    if (pieces[x-1-index][y+1+index].getText().equals(currentPlayer)){
                        pieces[x][y].setBackground(flipColor);
                        pieces[x][y].setText(currentPlayer);
                        for(int index2 = 0; index2 <= index; index2++){
                            pieces[x-1-index2][y+1+index2].setBackground(flipColor);
                            pieces[x-1-index2][y+1+index2].setText(currentPlayer);
                        }
                        validMoveChecker = true;
                        break;
                    }
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e){}
        
        //return the value of validMoveChecker (whether the move is true or false)
        return validMoveChecker;

    }
    
    //mustPass() method is used for checking whether there is still a possible move or not.
    protected boolean mustPass(){
        //Declaring some local variables that is important for this method.
        String nonCurrentPlayerNextMove;
        String currentPlayerNextMove;
        int nonBlankCounter = 0;
        
        /*Since we want to check whether the next move need to be passed or not, 
        then the nonCurrentPlayerNextMove is the current player and the currentPlayerNextMove is the non-current Player
        */
        if (currentPlayer.equals("BLACK")){
            nonCurrentPlayerNextMove = "BLACK";
            currentPlayerNextMove = "WHITE";
        }
        else{
            nonCurrentPlayerNextMove = "WHITE";
            currentPlayerNextMove = "BLACK";
        }
        
        //Now, we try to access each array
        for (int y = 0; y < yCount; y++){
            for (int x = 0; x < xCount; x++){
                // We only check the piece which is still blank and see whether clicking that box/piece will become a valid movement or not.
                if (pieces[x][y].getText().equals(BLANK))
                {
                    /*In this case, we will check the next move validity by checking all 8 directions around the box.
                    Using try-catch, we can eliminate the error related to array index out of bound exception.
                    
                    if there is a color of nonCurrentPlayer around the box clicked, 
                    then there is a possibility that clicking on that particular box could be a valid movement.
                    Hence, further check will be processed.
                    
                    Since there are 8 directions to be checked and the logic are all the same, 
                    then detail explanation will only be provided in the first try-catch.
                    */
                    
                    //Checking [x+1][y] array (the right-hand side the piece clicked)
                    try{
                        //if the color of piece in the right-hand direction is equal to the color of non-current player
                        if(pieces[x+1][y].getText().equals(nonCurrentPlayerNextMove)){
                            //We check in the same direction for all other array excluding the above checked array.
                            for (int index = 1; index < xCount - x - 1; index++){
                                //When the program encounter BLANK, then the check for this directions done, which means according to this direction, the movement could be not valid.
                                if (pieces[x+1+index][y].getText().equals(BLANK))
                                    break;
                                /*When the program encounter the color of current player in the next move, 
                                it means that clicking this particular [x][y] array or piece is a valid movement, hence
                                the program will return false as there is no need to pass
                                */
                                if (pieces[x+1+index][y].getText().equals(currentPlayerNextMove))
                                    return false;
                            }
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    //checking [x][y-1] array (below the piece clicked)
                    try{
                        if (pieces[x-1][y].getText().equals(nonCurrentPlayerNextMove)){
                            for (int index = 1; index < x; index++){
                                if (pieces[x-1-index][y].getText().equals(BLANK))
                                    break;
                                if (pieces[x-1-index][y].getText().equals(currentPlayerNextMove))
                                   return false;
                            }
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    //checking [x][y-1] array (below the piece clicked)
                    try{        
                        if(pieces[x][y-1].getText().equals(nonCurrentPlayerNextMove)){
                            for (int index = 1; index < y; index++){
                                if (pieces[x][y-1-index].getText().equals(BLANK))
                                    break;
                                if (pieces[x][y-1-index].getText().equals(currentPlayerNextMove))
                                   return false;
                            }
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){}
                    
                    //checking [x][y+1] array (above the piece clicked)
                    try{
                        if(pieces[x][y+1].getText().equals(nonCurrentPlayerNextMove)){
                            for (int index = 1; index < yCount - y - 1; index++){
                                if (pieces[x][y+1+index].getText().equals(BLANK))
                                    break;
                                if (pieces[x][y+1+index].getText().equals(currentPlayerNextMove))
                                   return false;
                            }
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){}

                    //Checking [x+1][y+1] array (diagonal to the top-right)
                    try{
                        if(pieces[x+1][y+1].getText().equals(nonCurrentPlayerNextMove)){
                            for (int index = 1; index < Math.min(yCount - y - 1, xCount - x - 1); index++){
                                if (pieces[x+1+index][y+1+index].getText().equals(BLANK))
                                    break;
                                if (pieces[x+1+index][y+1+index].getText().equals(currentPlayerNextMove))
                                   return false;
                            }
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){}

                    //Checking [x-1][y-1] array (diagonal to the bottom-left)
                    try{
                        if(pieces[x-1][y-1].getText().equals(nonCurrentPlayerNextMove)){
                            for (int index = 1; index < Math.min(x, y); index++){
                                if (pieces[x-1-index][y-1-index].getText().equals(BLANK))
                                    break;
                                if (pieces[x-1-index][y-1-index].getText().equals(currentPlayerNextMove))
                                   return false;
                            }
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){}

                    //Checking [x+1][y-1] array (diagonal to the bottom-right)
                    try{
                        if(pieces[x+1][y-1].getText().equals(nonCurrentPlayerNextMove)){
                            for (int index = 1; index < Math.min(xCount - x - 1, y); index++){
                                if (pieces[x+1+index][y-1-index].getText().equals(BLANK))
                                    break;
                                if (pieces[x+1+index][y-1-index].getText().equals(currentPlayerNextMove))
                                   return false;
                            }
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){}

                    //Checking [x-1][y+1] array (diagonal to the top-left)
                    try{
                        if(pieces[x-1][y+1].getText().equals(nonCurrentPlayerNextMove)){
                            for (int index = 1; index < Math.min(x, yCount - y - 1); index++){
                                if (pieces[x-1-index][y+1+index].getText().equals(BLANK))
                                    break;
                                if (pieces[x-1-index][y+1+index].getText().equals(currentPlayerNextMove))
                                   return false;
                            }
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){}                    
                }
                //if a specific piece in an array does not equal to blank, then we add 1 to the nonBlankCounter
                else
                    nonBlankCounter++;
            }
        }
        //if the nonBlankCounter equals to 64, it means all the pieces are fully occupied and hence trigger the endOfProgram to become true and mustPass() to return true
        if (nonBlankCounter == 64){
            endOfProgram = true;
            return true;
        }
        //Otherwise, only mustPass() which is true, but it is not the end of the program
        else
            return true;
    }
    
    //Method to count pieces (both black and white) by accessing each array (using 2 for-loop for direction x and y)
    protected void countPieces(){
        for (int y = 0; y < yCount; y++){
            for (int x = 0; x < xCount; x++){
                if (pieces[x][y].getText().equals("BLACK"))
                    black++;
                if (pieces[x][y].getText().equals("WHITE"))
                    white++;
            }        
        }
    }

    public static void main(String[] args)
    {
        Reversi reversi;
        reversi = new Reversi();
        
        // TO-DO: run other classes, one by one
        System.out.println("You are running class Reversi");
        
        // TO-DO: study comment and code in other given classes
        
        // TO-DO: uncomment these two lines when your work is ready
        reversi.setLocation(400, 20);
        reversi.verbose = false;

        // the game has started and GUI thread will take over here
    }
}
