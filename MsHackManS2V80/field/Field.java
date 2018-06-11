package field;

import java.awt.*;
import java.util.ArrayList;

import move.MoveType;

/**
 * field.Field
 *
 * Stores all information about the playing field and
 * contains methods to perform calculations about the field
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Field {

    protected final String EMTPY_FIELD = ".";
    protected final String BLOCKED_FIELD = "x";

    private String myId;
    private String opponentId;
    private int width;
    private int height;

    private String[][] field;
    private Point myPosition; //returns my coordinates from java.awt
    private Point opponentPosition; //returns enemy location
    private ArrayList<Point> enemyPositions;
    private ArrayList<Point> snippetPositions;
    private ArrayList<Point> bombPositions;
    private ArrayList<Point> tickingBombPositions;

    public Field() { //default constructor that Initializes ArrayList
        this.enemyPositions = new ArrayList<>();
        this.snippetPositions = new ArrayList<>();
        this.bombPositions = new ArrayList<>();
        this.tickingBombPositions = new ArrayList<>();
    }

    /**
     * Initializes field
     * @throws Exception: exception
     */
    public void initField() throws Exception {
        try {
            this.field = new String[this.width][this.height];
        } catch (Exception e) {
            throw new Exception("Error: trying to initialize field while field "
                    + "settings have not been parsed yet.");
        }
        clearField();
    }

    /**
     * Clears the field
     */
    public void clearField() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.field[x][y] = "";
            }
        }

        this.myPosition = null;
        this.opponentPosition = null;
        this.enemyPositions.clear();
        this.snippetPositions.clear();
        this.bombPositions.clear();
        this.tickingBombPositions.clear();
    }

    /**
     * Parses input string from the engine and stores it in
     * this.field. Also stores several interesting points.
     * @param input String input from the engine
     */
    public void parseFromString(String input) {
        clearField();

        String[] cells = input.split(",");
        int x = 0;
        int y = 0;

        for (String cellString : cells) {
            this.field[x][y] = cellString;

            for (String cellPart : cellString.split(";")) {
                switch (cellPart.charAt(0)) {
                    case 'P':
                        parsePlayerCell(cellPart.charAt(1), x, y);
                        break;
                    case 'e':
                        // TODO: store spawn points
                        break;
                    case 'E':
                        parseEnemyCell(cellPart.charAt(1), x, y);
                        break;
                    case 'B':
                        parseBombCell(cellPart, x, y);
                        break;
                    case 'C':
                        parseSnippetCell(x, y);
                        break;
                }
            }

            if (++x == this.width) {
                x = 0;
                y++;
            }
        }
    }

    /**
     * Stores the position of one of the players, given by the id
     * @param id Player ID
     * @param x X-position
     * @param y Y-position
     */
    private void parsePlayerCell(char id, int x, int y) {
        if (id == this.myId.charAt(0)) {
            this.myPosition = new Point(x, y);
        } else if (id == this.opponentId.charAt(0)) {
            this.opponentPosition = new Point(x, y);
        }
    }

    /**
     * Stores the position of an enemy. The type of enemy AI
     * is also given, but not stored in the starterbot.
     * @param type Type of enemy AI
     * @param x X-position
     * @param y Y-position
     */
    private void parseEnemyCell(char type, int x, int y) {
        this.enemyPositions.add(new Point(x, y));
    }

    /**
     * Stores the position of a bomb that can be collected or is
     * about to explode. The amount of ticks is not stored
     * in this starterbot.
     * @param cell The string that represents a bomb, if only 1 letter it
     *             can be collected, otherwise it will contain a number
     *             2 - 5, that means it's ticking to explode in that amount
     *             of rounds.
     * @param x X-position
     * @param y Y-position
     */
    private void parseBombCell(String cell, int x, int y) {
        if (cell.length() <= 1) {
            this.bombPositions.add(new Point(x, y));
        } else {
            this.tickingBombPositions.add(new Point(x, y));
        }
    }

    /**
     * Stores the position of a snippet
     * @param x X-position
     * @param y Y-position
     */
    private void parseSnippetCell(int x, int y) {
        this.snippetPositions.add(new Point(x, y));
    }

    /**
     * Return a list of valid moves for my bot, i.e. moves does not bring
     * player outside the field or inside a wall
     * @return A list of valid moves
     */
    public ArrayList<Point> getValidMoveTypes() {
        ArrayList<Point> validMoveTypes = new ArrayList<>();
        int myX = this.myPosition.x;
        int myY = this.myPosition.y;
       // int potentialX = 0;
        //int potentialY = 0;
        

        Point up = new Point(myX, myY - 1);
        Point down = new Point(myX, myY + 1);
        Point left = new Point(myX - 1, myY);
        Point right = new Point(myX + 1, myY);
       
      /* 
        potentialX = getSnippetPositions().get(0).x - myX;
        potentialY = getSnippetPositions().get(0).y - myY;
        
        //validMoveTypes.clear();
       //OPTIMIZE CODE LATER
       //if up is best move 
        if (potentialY < 0 )
        {   
            if(isPointValid(up)) {
                //if up is valid
                validMoveTypes.add(MoveType.UP);
            }
            else {
                if(isPointValid(left)) 
                {
                    validMoveTypes.add(MoveType.LEFT);
                }
                if (isPointValid(right))
                {
                    validMoveTypes.add(MoveType.RIGHT);
                }
            }
        }
        
        //if down is the best move
       if (potentialY > 0 )
        {   
            if(isPointValid(down)) {
                //if down is valid
                validMoveTypes.add(MoveType.DOWN);
            }
            else {
                if(isPointValid(left)) 
                {
                    validMoveTypes.add(MoveType.LEFT);
                }
                if (isPointValid(right))
                {
                    validMoveTypes.add(MoveType.RIGHT);
                }
            }
        }
        
        //if left is best MoveType
       if (potentialX < 0 )
        {   
            if(isPointValid(left)) {
                //if left is valid
                validMoveTypes.add(MoveType.LEFT);
            }
            else {
                if(isPointValid(up)) 
                {
                    validMoveTypes.add(MoveType.UP);
                }
                if (isPointValid(down))
                {
                    validMoveTypes.add(MoveType.DOWN);
                }
            }
        }
        
         if (potentialX > 0 )
        {   
            if(isPointValid(right)) {
                //if right is valid
                validMoveTypes.add(MoveType.RIGHT);
            }
            else {
                if(isPointValid(up)) 
                {
                    validMoveTypes.add(MoveType.UP);
                }
                if (isPointValid(down))
                {
                    validMoveTypes.add(MoveType.DOWN);
                }
            }
        }
        */
        //validMoveTypes.clear();
        //get possible moveable points closest to snippets
        if (isPointValid(up) ) validMoveTypes.add(up);
        if (isPointValid(down) ) validMoveTypes.add(down);
        if (isPointValid(left) ) validMoveTypes.add(left);
        if (isPointValid(right) ) validMoveTypes.add(right);
        
      
        //does it remove snippet if player gets it? **
        //System.out.println("Lily from getValidMoveTypes Debugs: "+ potentialX + " / " + potentialY + " / " ); 

        return validMoveTypes;
    }
    
    //Pick the best move type out of getValidMoveTypes
    public MoveType getBestMoveTypes() {
        ArrayList<Point> bestMoveTypes = getValidMoveTypes();
        Point destination = getSnippetPositions().get(0);
        int leastDistance = distance(destination, bestMoveTypes.get(0));
        Point bestMove = bestMoveTypes.get(0);
        
        for(int i = 0; i < bestMoveTypes.size(); i++) {
            if(distance(destination, bestMoveTypes.get(i)) < leastDistance){
                bestMove = bestMoveTypes.get(i);
            }
        }
        
        //System.out.println("Lily in bestmovetype: " + bestMoveTypes);
        return whichMoveType(bestMove);
    }
    
    /*
    * find relative distance between potential next move and destination
    */
    public int distance(Point destination, Point potential){
        int distance = 0;
        int deltaX = 0;
        int deltaY = 0;
        
        deltaX = destination.x - potential.x;
        deltaY = destination.y - potential.y;
        
        distance = (int) Math.sqrt((int) Math.pow (deltaX , 2) + (int) Math.pow (deltaY , 2));
        
        return distance;
    }
    
    /*
    * return movetype according to the coordinates
    * movetype limited to left, right, up, and down
    */
    public MoveType whichMoveType(Point point){
        int myX = this.myPosition.x;
        int myY = this.myPosition.y;
        
        if(point.x == myX && point.y == myY - 1){
            return MoveType.UP;
        }
        else if(point.x == myX && point.y == myY + 1){
            return MoveType.DOWN;
        }
        else if(point.x == myX - 1 && point.y == myY){
            return MoveType.LEFT;
        }
        else if(point.x == myX + 1 && point.y == myY){
            return MoveType.RIGHT;
        }
        
        return null;
    }
    
    /**
     * Returns whether a point on the field is valid to stand on.
     * @param point Point to test
     * @return True if point is valid to stand on, false otherwise
     */
    public boolean isPointValid(Point point) {
        int x = point.x;
        int y = point.y;

        return x >= 0 && x < this.width && y >= 0 && y < this.height &&
                !this.field[x][y].contains(BLOCKED_FIELD);
    }

    public void setMyId(int id) {
        this.myId = id + "";
    }

    public void setOpponentId(int id) {
        this.opponentId = id + "";
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Point getMyPosition() {
        return this.myPosition;
    }

    public Point getOpponentPosition() {
        return this.opponentPosition;
    }

    public ArrayList<Point> getEnemyPositions() {
        return this.enemyPositions;
    }

    public ArrayList<Point> getSnippetPositions() {
        
        System.out.println("Lily Debugs Snippet Position: " + this.snippetPositions);
           
        return this.snippetPositions;
    }

    public ArrayList<Point> getBombPositions() {
        return this.bombPositions;
    }

    public ArrayList<Point> getTickingBombPositions() {
        return this.tickingBombPositions;
    }
}
