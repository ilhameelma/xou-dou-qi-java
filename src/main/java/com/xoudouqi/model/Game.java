package com.xoudouqi.model;

import java.util.Date;

public class Game {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    
    private Player winner;
    private Board board;
    private String movesHistory;
    private Date startDate;
    private Date endDate;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.board = new Board();
        this.startDate = new Date();
        this.movesHistory = "";
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    // Getters et setters
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
        this.endDate = new Date();
    }

    public String getMovesHistory() {
        return movesHistory;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void setMovesHistory(String movesHistory) {
		this.movesHistory = movesHistory;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
        return endDate;
    }
	public void addMoveToHistory(Player player, Position from, Position to) {
	    movesHistory += player.getUsername() + ": " + formatPos(from) + " -> " + formatPos(to) + "\n";
	}

	private String formatPos(Position pos) {
	    return (char)('A' + pos.getX()) + "" + (pos.getY() + 1);
	}
	public Player getOtherPlayer(Player player) {
	    if (player.equals(player1)) {
	        return player2;
	    } else if (player.equals(player2)) {
	        return player1;
	    } else {
	        return null; // Ou lever une exception si le joueur ne fait pas partie de la partie
	    }
	}


	
}