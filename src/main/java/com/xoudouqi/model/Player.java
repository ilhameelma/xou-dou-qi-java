package com.xoudouqi.model;

public class Player {
    private int id;
    private String username;
    private String password;
    private int wins;
    private int losses;
    private int draws;

    // Constructeurs
    public Player() {}

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Player(int id, String username) {
        this.id = id;
        this.username = username;
    }


    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

   
}