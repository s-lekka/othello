//LEKKA STAVROULA 3090108

import java.util.Scanner;

public class Main {

    public enum Disk {
        DARK, LIGHT
    }

    public static void main(String[] args) {

        System.out.println("\n[ O T H E L L O ]");
                
        Scanner in = new Scanner(System.in); // for user input
        String line; // input coordinates
        int selection, depth, darks, lights, score, c, r;
        boolean exit = false; // "close program" parameter
        boolean rules = false; // "show rules" parameter
        boolean invalid;
        Disk pdisk, cdisk; // player's disk color, computer's disk color
        
        do { // main loop

            System.out.println();
            System.out.print("0.Exit\n"); 
            System.out.print("1.New Game\n"); 
            System.out.print("2.Show Rules\n"); 
            System.out.print(">"); 
            selection = in.nextInt();
            in.nextLine(); //clean input
            System.out.println();
            switch (selection) {
            case 0:
                exit = true;
                break;
            case 1:
                break;
            case 2:
                rules = true;
                break;
            default:
                System.out.println("Wrong input. The program will close.");
                exit = true;
                break;
            }

            if (!exit) {

                // show rules
                if (rules) {

                    System.out.println("------------------------------------------------"); 
                    System.out.println("                  R u l e s");
                    System.out.println("------------------------------------------------");
                    System.out.println("1. Player chooses color for their disks between \ndark and light.");
                    System.out.println("2. Dark always plays first.");
                    System.out.println(
                            "3. Each player must place a piece on the board, \nin such a position that there exists at least \none straight occupied line (horizontal, vertical, \nor diagonal) of one or more contiguous opponent's \npieces between the new piece and another piece \nof the player.");
                    System.out.println("4. The pieces in between are captured and change \nsides (color).");
                    System.out.println(
                            "5. If one player has no available valid moves, \nplay passes back to the other player.");
                    System.out.println("6. When neither player can move and/or the board \nis full, the game ends.");
                    System.out
                            .println("7. The player with the most pieces on the board \nat the end of the game wins.");
                    System.out.println("------------------------------------------------");

                    rules = false; // reset

                } else {

                    // new game
                    System.out.println("Insert maximum depth."); // Difficulty Menu
                    System.out.print(
                            "Recommended values:\n1 for beginners\n3 for normal gameplay\n5 for a challenge\n>");
                    depth = in.nextInt();
                    in.nextLine(); //clean input
                    if (depth < 1) { // check input
                        System.out.println("Invalid depth. \nValue set to 3 (Medium Difficulty)");
                        depth = 3;
                    } else { // print difficulty
                        System.out.print("Value set to ");
                        System.out.print(depth);
                        switch (depth) {
                        case 1, 2 -> System.out.println(" (Beginner Difficulty)");
                        case 3, 4 -> System.out.println(" (Medium Difficulty)");
                        case 5, 6 -> System.out.println(" (Hard Difficulty)");
                        default -> System.out.println(" (Master Difficulty)");
                        }
                    }

                    System.out.println(); // Disk Menu
                    System.out.print("Select disk color:\n1.Dark |x|\n2.Light |o|\n>");
                    selection = in.nextInt();
                    in.nextLine(); //clean input
                    System.out.println();
                    switch (selection) {
                    case 1 -> { pdisk = Disk.DARK; cdisk = Disk.LIGHT; }
                    case 2 -> { pdisk = Disk.LIGHT; cdisk = Disk.DARK; }
                    default -> { pdisk = Disk.DARK; cdisk = Disk.LIGHT;
                        System.out.println("Wrong input. Player's default disk color: Dark."); }
                    }

                    System.out.println("------------------------------------------------");
                    System.out.println("                N e w  G a m e");
                    System.out.println("------------------------------------------------");

                    // creation of board and players
                    Board board = new Board();

                    Player computer = new Player(depth, cdisk);

                    if (pdisk == Disk.DARK) board.setLastPlayer(0);
                    else board.setLastPlayer(1);

                    board.print(); // print starting state of the board
                    System.out.println();
                    
                    while (!board.isTerminal()) { // game loop
                       
                        switch (board.getLastPlayer()) {
                        case 0: // next: player
                            System.out.println("Player's turn:");

                            //if there are valid moves for the player
                            if (board.validMoves(pdisk)) {
                                do {

                                    System.out.print("Please enter column (a-h) & row (1-8), e.g. a1\n>");
                                    line = in.nextLine();
                                    invalid = true;// initialize
                                    r=8;// initialize
                                    c=8;// initialize
                                    //if
                                    if (line.length()==2) {
                                        //turn input into 2D array coordinates
                                        r = Character.getNumericValue(line.charAt(1)) - Character.getNumericValue('0') - 1;
                                        c = Character.getNumericValue(line.charAt(0)) - Character.getNumericValue('a');
                                        invalid = false;
                                        if ((r < 0) || (r > 7) || (c < 0) || (c > 7)) {
                                            System.out.println("Invalid Move. Please choose a tile inside the board.");
                                            invalid = true;// if tile out of bounds, move invalid, ask new move
                                        } else if (board.getValids(r, c) == 0) { // if game move is invalid print hints
                                            System.out.println("Invalid Move. Please choose another tile.");
                                            board.hints();
                                            invalid = true; //ask new move
                                        }
                                    } else {
                                        System.out.println("Invalid Input Format.");
                                    }//end if

                                } while (invalid);
                                Move Pmove = new Move(r, c, pdisk);
                                board.makeMove(Pmove);
                            } else {
                                //pass play to the computer
                                System.out.println("No available moves for player.");
                            }
                            //next player:
                            board.setLastPlayer(1);
                            //single play test one liner, comment above line and uncomment below
                            //board.setLastPlayer(0); if (pdisk==Disk.DARK) pdisk=Disk.LIGHT; else pdisk=Disk.DARK;
                           
                            break;
                        case 1: // next: computer
                            System.out.println("Computer's turn:");
                            //if there are valid moves for the computer
                            if (board.validMoves(cdisk)) {

                                //computer's turn
                                Move CMove = computer.MiniMax(board);
                                CMove.setColor(cdisk);
                                System.out.print("Computer moved to ");
                                System.out.print(Character.toString(CMove.getCol()+'a'));
                                System.out.println(CMove.getRow()+1); 
                            
                                board.makeMove(CMove);
                                
                            } else {
                                //pass play to the player
                                System.out.println("No available moves for computer.");
                            }

                            board.setLastPlayer(0);
                            break;
                        default:
                            break;
                        }

                        board.print();

                    }

                    //calculate and print score
                    board.calcSums(); // game score
                    darks = board.getDarks();
                    lights = board.getLights();
                    score = darks - lights;
                    if (score > 0) {
                        System.out.println("Dark wins");
                    } else {
                        if (score < 0) {
                            System.out.println("Light wins");
                        } else {
                            System.out.println("Draw");
                        }
                    }
                    System.out.print(darks);
                    System.out.print(" - ");
                    System.out.println(lights);
                    
                }//end of new game

            }
        } while (!exit);

        in.close();
        System.out.println();
        System.out.println("------------------------------------------------");

    }

}