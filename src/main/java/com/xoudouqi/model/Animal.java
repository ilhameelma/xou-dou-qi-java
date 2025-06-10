package com.xoudouqi.model;

public enum Animal {
    ELEPHANT(8, "E"),
    LION(7, "L"),
    TIGER(6, "T"),
    PANTHER(5, "P"),
    DOG(4, "D"),
    WOLF(3, "W"),
    CAT(2, "C"),
    RAT(1, "R");

    private final int rank;
    private final String symbol;

    Animal(int rank, String symbol) {
        this.rank = rank;
        this.symbol = symbol;
    }

    public int getRank() {
        return rank;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean canCapture(Animal other, boolean isInTrap) {
        if (isInTrap) return true;
        if (this == RAT && other == ELEPHANT) return true;
        if (this == ELEPHANT && other == RAT) return false;
        return this.rank >= other.rank;
    }
}
