//LEKKA STAVROULA 3090108

import java.util.ArrayList;

public class Board {

    public static final int EMPTY = 0; //empty tile
    public static final int X = 1; //darks
    public static final int O = -1; //lights
    public static final int H = 2; //hints

    //text color
    //not used
    public static final String GREEN_TILE = "\033[0;102m"; //for tile background
    public static final String WHITE_DISK = "\033[1;97m"; //for white disks
    public static final String BLACK_DISK = "\033[1;30m"; //for dark disks
    public static final String WHITE_TILE = "\033[47m"; //for hinted tiles
    public static final String RESET = "\033[0m";  // text and background color reset


    private int[][] gameBoard;
    private int[][] validMoves; // image of board with 1s where a move is valid and 0s when invalid
    private int lastPlayer;
    private int darksum = 0; // sum of dark disks
    private int lightsum = 0; // sum of light disks 
    private Move lastMove;

    // initial state of the board according to Othello rules
    public Board() {

        lastMove = new Move();
        this.gameBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j] = EMPTY;
            } // end for
        } // end for

        gameBoard[4][3] = X;
        gameBoard[3][4] = X;
        gameBoard[3][3] = O;
        gameBoard[4][4] = O;

    }//end constructor

    //state of the board after a move
    public Board(Board board) {
        this.lastMove = board.lastMove;

        //copy board
        this.gameBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.gameBoard[i][j] = board.gameBoard[i][j];
            } // end for
        } // end for

        //this.gameBoard = board.gameBoard.clone();

    }//end constructor

    public Move getLastMove() {
        return lastMove;
    }

    // creates validMoves table and returns true if there are valid moves for given disk color
    public boolean validMoves(Main.Disk color) {

        boolean moves = false;
        validMoves = new int[8][8];
       
        Move move = new Move(color);

        for (int i = 0; i < 8; i++) { // fill the table
            for (int j = 0; j < 8; j++) {
                move.setRow(i);
                move.setCol(j);

                if (isMoveValid(move)) {
                    validMoves[i][j] = H;
                    moves = true; // if at least one valid move, returns true
                } else {
                    validMoves[i][j] = 0;
                }
            } // end for
        } // end for
        return moves;
    }

    public boolean isMoveValid(Move move) {
        // checks all tiles around chosen move tile
        // if there is at least one enemy line that can be captured returns true
        int i;
        boolean out_of_bounds;
        int row = move.getRow();
        int col = move.getCol();
        int clr = move.getColor();

        if (gameBoard[row][col] != EMPTY) {
            return false;
        } // if tile unavailable, invalid move

        //move validity algorithm
        /*
            playing disk to be positioned for the move = pd
            each line to be checked for capture = line
            (lines to be checked: above and below, left and right, and all four diagonals of pd)

            if next of pd in line there is a disk and it is of different color than pd's
            check whether after one or more consecutive disks of different color
            exists disk of pd's color (1st case)
            or if empty tile (or end of board) is reached (2nd case)

            1st case: line can be captured
            2nd case: line is not to be captured

            if at least one line of eight can be captured
            move is valid.
        */

        // check upper left diagonal
        out_of_bounds = false;// initialize
        //by default, anything above the 3rd row and before the 3rd column can't capture anything on upper left diagonal
        if ((row > 1) && (col > 1)) {
            i = 1;// initialize
            if ((gameBoard[row - i][col - i] != EMPTY) && (gameBoard[row - i][col - i] != clr)) {
                do {
                    i++;
                    if ((row - i != -1) && (col - i != -1)) {
                        if (gameBoard[row - i][col - i] == clr) {
                            return true;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row - i][col - i] != EMPTY));
            }
        }

        // check up
        out_of_bounds = false;// initialize
        //by default, anything above the 3rd row can't capture anything above it
        if (row > 1) {
            i = 1;// initialize
            if ((gameBoard[row - i][col] != EMPTY) && (gameBoard[row - i][col] != clr)) {
                do {
                    i++;
                    if (row - i != -1) {
                        if (gameBoard[row - i][col] == clr) {
                            return true;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row - i][col] != EMPTY));
            }
        }

        // check upper right diagonal
        out_of_bounds = false;// initialize
        //by default, anything above the 3rd row and after the 6th column can't capture anything on upper right diagonal
        if ((row > 1) && (col < 6)) {
            i = 1;// initialize
            if ((gameBoard[row - i][col + i] != EMPTY) && (gameBoard[row - i][col + i] != clr)) {
                do {
                    i++;
                    if ((row - i != -1) && (col + i != 8)) {
                        if (gameBoard[row - i][col + i] == clr) {
                            return true;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row - i][col + i] != EMPTY));

            }
        }

        // check right
        out_of_bounds = false;// initialize
        //by default, anything after the 6th column can't capture anything on the right
        if (col < 6) {
            i = 1;// initialize
            if ((gameBoard[row][col + i] != EMPTY) && (gameBoard[row][col + i] != clr)) {
                do {
                    i++;
                    if (col + i != 8) {
                        if (gameBoard[row][col + i] == clr) {
                            return true;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row][col + i] != EMPTY));
            }
        }

        // check lower right diagonal
        out_of_bounds = false;// initialize
        //by default, anything below the 6th row and after the 6th column can't capture anything on lower right diagonal
        if ((row < 6) && (col < 6)) {
            i = 1;// initialize
            if ((gameBoard[row + i][col + i] != EMPTY) && (gameBoard[row + i][col + i] != clr)) {
                do {
                    i++;
                    if ((row + i != 8) && (col + i != 8)) {
                        if (gameBoard[row + i][col + i] == clr) {
                            return true;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row + i][col + i] != EMPTY));

            }
        }

        // check down
        out_of_bounds = false;// initialize
        //by default, anything below the 6th row can't capture anything below it
        if (row < 6) {
            i = 1;// initialize
            if ((gameBoard[row + i][col] != EMPTY) && (gameBoard[row + i][col] != clr)) {
                do {
                    i++;
                    if (row + i != 8) {
                        if (gameBoard[row + i][col] == clr) {
                            return true;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row + i][col] != EMPTY));
            }
        }

        // check lower left diagonal
        out_of_bounds = false;// initialize
        //by default, anything below the 6th row and before the 3rd column can't capture anything on lower left diagonal
        if ((row < 6) && (col > 1)) {
            i = 1;// initialize
            if ((gameBoard[row + i][col - i] != EMPTY) && (gameBoard[row + i][col - i] != clr)) {
                do {
                    i++;
                    if ((row + i != 8) && (col - i != -1)) {
                        if (gameBoard[row + i][col - i] == clr) {
                            return true;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row + i][col - i] != EMPTY));

            }
        }

        // check left
        out_of_bounds = false;// initialize
        //by default, anything before the 3rd column can't capture anything before it
        if (col > 1) {
            i = 1;// initialize
            if ((gameBoard[row][col - i] != EMPTY) && (gameBoard[row][col - i] != clr)) {
                do {
                    i++;
                    if (col - i != -1) {
                        if (gameBoard[row][col - i] == clr) {
                            return true;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row][col - i] != EMPTY));
            }
        }

        return false;
    }

    public int getValids(int i, int j) {
        return this.validMoves[i][j];
    }

    public void setLastPlayer(int lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public int getLastPlayer() {
        return this.lastPlayer;
    }

    public void calcSums() {
        int tile;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tile = gameBoard[i][j];
                if (tile != EMPTY) {
                    if (tile == X) {
                        darksum++;
                    } else {
                        lightsum++;
                    }
                }
            } // end for
        } // end for
    }

    public int getDarks() {
        return this.darksum;
    }

    public int getLights() {
        return this.lightsum;
    }

    public ArrayList<Board> getChildren(Main.Disk color) { // generates the children of the state
        ArrayList<Board> children = new ArrayList<>();
        Move move = new Move(color);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                move.setCol(col);
                move.setRow(row);
                if (isMoveValid(move)) { // any empty valid square results to a child

                    Board child = new Board(this);
                    child.makeMove(move);
                    children.add(child);

                }
            }
        }
        return children;
    }// end getChildren

    public int evaluate() {
        int V=0;
        int i, j;

        //board is separated into six tile-groups of different value
        //tile value = (max number of lines it can capture) + 2*(min number of line capture it is defended from)
        //board value = value of tiles occupied by dark disks - value of tiles occupied by light disks
        /*
             | |a|b|c|d|e|f|g|h| |
             |1|A|C|B|B|B|B|C|A|1|
             |2|C|F|E|E|E|E|F|C|2|
             |3|B|E|D|D|D|D|E|B|3|
             |4|B|E|D|D|D|D|E|B|4|
             |5|B|E|D|D|D|D|E|B|5|
             |6|B|E|D|D|D|D|E|B|6|
             |7|C|F|E|E|E|E|F|C|7|
             |8|A|C|B|B|B|B|C|A|8|
             | |a|b|c|d|e|f|g|h| |
         */

        //A. corner tiles have 19 value
        V=V+(gameBoard[0][0]+gameBoard[0][7]+gameBoard[7][7]+gameBoard[7][0])*19;

        //B. border tiles (except corner tiles and those adjusted to them) have 17 value
        //vertical
        for (i=2; i<6; i++) V=V+(gameBoard[i][0]+gameBoard[i][7])*17;
        //horizontal
        for (j=2; j<6; j++) V=V+(gameBoard[0][j]+gameBoard[7][j])*17;

        //C. border tiles adjusted to corner tiles have 15 value
        //top
        V=V+(gameBoard[0][1]+gameBoard[0][6]+gameBoard[1][0]+gameBoard[1][7])*15;
        //bottom
        V=V+(gameBoard[6][0]+gameBoard[6][7]+gameBoard[7][1]+gameBoard[7][6])*15;

        //D. 5x5 center tiles have 8 value
        for (i=1; i<7; i++) {
            for (j=1; j<7; j++) {
                V=V+gameBoard[i][j]*8;
            }
        }

        //E. inner border tiles (except those adjusted to corner tiles) have 5 value
        //vertical
        for (i=2; i<6; i++) V=V+(gameBoard[i][1]+gameBoard[i][6])*5;
        //horizontal
        for (j=2; j<6; j++) V=V+(gameBoard[1][j]+gameBoard[6][j])*5;

        //F. tiles diagonally adjusted to corner tiles have 3 value
        V=V+(gameBoard[1][1]+gameBoard[1][6]+gameBoard[6][6]+gameBoard[6][1])*3;


        return V;
    }

    // Make a move; it places a letter(disk) in the board
    public void makeMove(Move move) {
        int row = move.getRow();
        int col = move.getCol();
        int clr = move.getColor();

        int k, i; // disks to change, counter
        boolean out_of_bounds;

        gameBoard[row][col] = clr;

        lastMove = new Move(row, col);

        if (clr == Move.X) lastMove.setColor(Main.Disk.DARK);
        else lastMove.setColor(Main.Disk.LIGHT);
        
        // capture opponent disks
        // upper left diagonal
        out_of_bounds = false;// initialize
        if ((row > 1) && (col > 1)) {
            i = 1;// initialize
            if ((gameBoard[row - i][col - i] != EMPTY) && (gameBoard[row - i][col - i] != clr)) {
                do {
                    i++;
                    if ((row - i != -1) && (col - i != -1)) {
                        if (gameBoard[row - i][col - i] == clr) {
                            for (k = 0; k < i; k++) {
                                gameBoard[row - k][col - k] = clr;
                            }
                            break;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row - i][col - i] != EMPTY));
            }
        }

        // up
        out_of_bounds = false;// initialize
        if (row > 1) {
            i = 1;// initialize
            if ((gameBoard[row - i][col] != EMPTY) && (gameBoard[row - i][col] != clr)) {
                do {
                    i++;
                    if (row - i != -1) {
                        if (gameBoard[row - i][col] == clr) {
                            for (k = 0; k < i; k++) {
                                gameBoard[row - k][col] = clr;
                            }
                            break;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row - i][col] != EMPTY));
            }
        }

        // upper right diagonal
        out_of_bounds = false;// initialize
        if ((row > 1) && (col < 6)) {
            i = 1;// initialize
            if ((gameBoard[row - i][col + i] != EMPTY) && (gameBoard[row - i][col + i] != clr)) {
                do {
                    i++;
                    if ((row - i != -1) && (col + i != 8)) {
                        if (gameBoard[row - i][col + i] == clr) {
                            for (k = 0; k < i; k++) {
                                gameBoard[row - k][col + k] = clr;
                            }
                            break;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row - i][col + i] != EMPTY));

            }
        }

        // right
        out_of_bounds = false;// initialize
        if (col < 6) {
            i = 1;// initialize
            if ((gameBoard[row][col + i] != EMPTY) && (gameBoard[row][col + i] != clr)) {
                do {
                    i++;
                    if (col + i != 8) {
                        if (gameBoard[row][col + i] == clr) {
                            for (k = 0; k < i; k++) {
                                gameBoard[row][col + k] = clr;
                            }
                            break;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row][col + i] != EMPTY));
            }
        }

        // lower right diagonal
        out_of_bounds = false;// initialize
        if ((row < 6) && (col < 6)) {
            i = 1;// initialize
            if ((gameBoard[row + i][col + i] != EMPTY) && (gameBoard[row + i][col + i] != clr)) {
                do {
                    i++;
                    if ((row + i != 8) && (col + i != 8)) {
                        if (gameBoard[row + i][col + i] == clr) {
                            for (k = 0; k < i; k++) {
                                gameBoard[row + k][col + k] = clr;
                            }
                            break;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row + i][col + i] != EMPTY));

            }
        }

        // down
        out_of_bounds = false;// initialize
        if (row < 6) {
            i = 1;// initialize
            if ((gameBoard[row + i][col] != EMPTY) && (gameBoard[row + i][col] != clr)) {
                do {
                    i++;
                    if (row + i != 8) {
                        if (gameBoard[row + i][col] == clr) {
                            for (k = 0; k < i; k++) {
                                gameBoard[row + k][col] = clr;
                            }
                            break;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row + i][col] != EMPTY));
            }
        }

        // lower left diagonal
        out_of_bounds = false;// initialize
        if ((row < 6) && (col > 1)) {
            i = 1;// initialize
            if ((gameBoard[row + i][col - i] != EMPTY) && (gameBoard[row + i][col - i] != clr)) {
                do {
                    i++;
                    if ((row + i != 8) && (col - i != -1)) {
                        if (gameBoard[row + i][col - i] == clr) {
                            for (k = 0; k < i; k++) {
                                gameBoard[row + k][col - k] = clr;
                            }
                            break;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row + i][col - i] != EMPTY));

            }
        }

        // left
        out_of_bounds = false;// initialize
        if (col > 1) {
            i = 1;// initialize
            if ((gameBoard[row][col - i] != EMPTY) && (gameBoard[row][col - i] != clr)) {
                do {
                    i++;
                    if (col - i != -1) {
                        if (gameBoard[row][col - i] == clr) {
                            for (k = 0; k < i; k++) {
                                gameBoard[row][col - k] = clr;
                            }
                            break;
                        }
                    } else {
                        out_of_bounds = true;
                    }
                } while ((!out_of_bounds) && (gameBoard[row][col - i] != EMPTY));
            }
        }

    }

    public boolean isTerminal() { // end of game check

        boolean empty_tile = false;
        int row, col;

        // check existence of empty tile(s)
        for (row = 0; row < 8; row++) {
            for (col = 0; col < 8; col++) {
                if (gameBoard[row][col] == EMPTY) {
                    empty_tile = true;
                    break;
                }
                if (empty_tile) break;
            }
        }

        if (empty_tile) {

            // check for valid moves
            // true if no available moves for both players

            return (!validMoves(Main.Disk.DARK)) && (!validMoves(Main.Disk.LIGHT));
        }

        return true; // no empty tiles
    }// end isTerminal

    public void hints() { // print board with hints
        
        int s;

        System.out.println();
        System.out.println("Hint: Available moves are shown as 'v'");
        System.out.println("| |a|b|c|d|e|f|g|h| |");

        for (int i = 0; i < 8; i++) {

            System.out.print("|");
            System.out.print(i + 1);

            for (int j = 0; j < 8; j++) {
                System.out.print("|");

                if (validMoves[i][j] == H){
                    System.out.print(colored('v'));
                }
                else {

                    s = gameBoard[i][j];
                    switch(s) {
                        //case EMPTY -> System.out.print("_");
                        case X -> System.out.print(colored('x'));
                        case O -> System.out.print(colored('o'));
                        default -> System.out.print(colored('_'));
                    }

                }
            }
            System.out.print("|");
            System.out.print(i + 1);
            System.out.println("|");

        }

        System.out.println("| |a|b|c|d|e|f|g|h| |");

    }// end print

    public void print() { // print board

        int s;

        System.out.println();
        System.out.println("| |a|b|c|d|e|f|g|h| |");

        for (int i = 0; i < 8; i++) {

            System.out.print("|");
            System.out.print(i + 1);

            for (int j = 0; j < 8; j++) {
                System.out.print("|");

                s = gameBoard[i][j];
                switch(s) {
                    //case EMPTY -> System.out.print("_");
                    case X -> System.out.print(colored('x'));
                    case O -> System.out.print(colored('o'));
                    default -> System.out.print(colored('_'));
                }
            }
            System.out.print("|");
            System.out.print(i + 1);
            System.out.println("|");

        }

        System.out.println("| |a|b|c|d|e|f|g|h| |");

    }// end print

    public String colored(char c) {

        //for coloring if it works uncomment this block
        /*
        if (c=='x') return GREEN_TILE+ BLACK_DISK +"0"+RESET;
        else if (c=='o') return GREEN_TILE+ WHITE_DISK +"0"+RESET;
        else if (c=='v') return WHITE_TILE+"v"+RESET;
        else return GREEN_TILE+" "+RESET;
        */

        //and comment the next one
        return Character.toString(c);
    }

}