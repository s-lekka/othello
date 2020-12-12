//LEKKA STAVROULA 3090108

import java.util.ArrayList;
import java.util.Random;

public class Player { // contains minimax algorithm

	private final int maxDepth; // max depth for minimax algorithm
	private Main.Disk playerColor; // player's color (dark/light) (0/1)

	public Player(int depth, Main.Disk color) {
		this.maxDepth = depth;
		this.playerColor = color;
	}

	
	// MiniMax Algorithm
	public Move MiniMax(Board board) {
		// 'X' (dark) tries to achieve MAX of evaluate
		if (playerColor == Main.Disk.DARK) {
			return miniMAX(new Board(board), 0); //initial depth 0
		}
		// 'O' (light) tries to achieve MIN of evaluate
		else {
			return MINImax(new Board(board), 0); //initial depth 0
		}
	}

	// miniMAX function
	public Move miniMAX(Board board, int depth) {
		Random random = new Random();

		// if terminal or max depth calculate and return move according to evaluate function
		if ((board.isTerminal()) || (depth >= maxDepth)) {
			return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
		}

		if (!board.validMoves(Main.Disk.DARK)) { //if board not terminal but DARK have no valid moves/children
			return miniMAX(board, depth+1); //LIGHT play again
		}

		// calculate lastMove children

		ArrayList<Board> children = new ArrayList<>(board.getChildren(Main.Disk.DARK));
		Move maxMove = new Move(Integer.MIN_VALUE); // MIN_VALUE for comparison

		for (Board child : children) {
			// call MINImax for each child for depth depth+1
			Move move = MINImax(child, depth + 1);

			// return child with greatest value
			if (move.getValue() >= maxMove.getValue()) {
				if ((move.getValue() == maxMove.getValue())) {
					// if more than one children with same value return in random
					if (random.nextInt(2) == 0) {
						maxMove.setRow(child.getLastMove().getRow());
						maxMove.setCol(child.getLastMove().getCol());
						maxMove.setValue(move.getValue());
					}
				} else {
					maxMove.setRow(child.getLastMove().getRow());
					maxMove.setCol(child.getLastMove().getCol());
					maxMove.setValue(move.getValue());
				}
			}
		}
		
		return maxMove;
	}

	// MINImax function
	public Move MINImax(Board board, int depth) {
		Random random = new Random();

		if ((board.isTerminal()) || (depth >= maxDepth)) {

			return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
		}

		if (!board.validMoves(Main.Disk.LIGHT)) { //if board not terminal but LIGHT have no valid moves/children
			return MINImax(board, depth+1); //DARK play again
		}
		
		ArrayList<Board> children = new ArrayList<>(board.getChildren(Main.Disk.LIGHT));

		Move minMove = new Move(Integer.MAX_VALUE); // MAX_VALUE for comparison

		for (Board child : children) {

			Move move = miniMAX(child, depth + 1);

			// return child with least value
			if (move.getValue() <= minMove.getValue()) {
				if ((move.getValue() == minMove.getValue())) {
					if (random.nextInt(2) == 0) {
						minMove.setRow(child.getLastMove().getRow());
						minMove.setCol(child.getLastMove().getCol());
						minMove.setValue(move.getValue());
					}
				} else {
					minMove.setRow(child.getLastMove().getRow());
					minMove.setCol(child.getLastMove().getCol());
					minMove.setValue(move.getValue());
				}
			}
		}

		return minMove;
	}

}