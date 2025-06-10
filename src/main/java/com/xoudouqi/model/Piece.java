package com.xoudouqi.model;

public class Piece {
    private Animal animal;
    private int player;
    private Position position;

    public Piece(Animal animal, int player) {
        this.animal = animal;
        this.player = player;
    }

    // Getters et setters
    public Animal getAnimal() {
        return animal;
    }

    public int getPlayer() {
        return player;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
