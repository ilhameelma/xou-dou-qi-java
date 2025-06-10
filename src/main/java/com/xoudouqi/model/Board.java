package com.xoudouqi.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int WIDTH = 7;
    public static final int HEIGHT = 9;
    private Piece[][] grid;
    private List<String> moveHistory;

    public Board() {
        grid = new Piece[WIDTH][HEIGHT];
        moveHistory = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialisation des pièces pour le joueur 1 (en haut)
        placePiece(0, 0, new Piece(Animal.LION, 1));
        placePiece(6, 0, new Piece(Animal.TIGER, 1));
        placePiece(1, 1, new Piece(Animal.DOG, 1));
        placePiece(5, 1, new Piece(Animal.CAT, 1));
        placePiece(0, 2, new Piece(Animal.RAT, 1));
        placePiece(6, 2, new Piece(Animal.PANTHER, 1));
        placePiece(1, 3, new Piece(Animal.WOLF, 1));
        placePiece(5, 3, new Piece(Animal.ELEPHANT, 1));

        // Initialisation des pièces pour le joueur 2 (en bas)
     // Initialisation des pièces pour le joueur 2 (en bas)
        placePiece(0, 8, new Piece(Animal.LION, 2));
        placePiece(6, 8, new Piece(Animal.TIGER, 2));
        placePiece(1, 7, new Piece(Animal.DOG, 2));
        placePiece(5, 7, new Piece(Animal.CAT, 2));
        placePiece(0, 6, new Piece(Animal.RAT, 2));
        placePiece(6, 6, new Piece(Animal.PANTHER, 2));
        placePiece(1, 5, new Piece(Animal.WOLF, 2));
        placePiece(5, 5, new Piece(Animal.ELEPHANT, 2));

        // Miroir des positions du joueur 1
    }

    private void placePiece(int x, int y, Piece piece) {
        grid[x][y] = piece;
        piece.setPosition(new Position(x, y));
    }

    public boolean isValidMove(Position from, Position to, int player) {
        // Implémentation des règles de déplacement
        return true;
    }

    public boolean isGameWon() {
        // Vérifie si un joueur a gagné
        return false;
    }
    private String formatPos(Position pos) {
        return (char)('A' + pos.getX()) + "" + (pos.getY() + 1);
    }

    public void movePiece(Position from, Position to) {
        Piece movingPiece = grid[from.getX()][from.getY()];
        Piece targetPiece = grid[to.getX()][to.getY()];

        // Affichage pour le suivi
        System.out.println("Déplacement de " + movingPiece.getAnimal() + " (" + movingPiece.getPlayer() + ") de " + formatPos(from) + " à " + formatPos(to));

        // Capture si une pièce adverse est présente
        if (targetPiece != null && targetPiece.getPlayer() != movingPiece.getPlayer()) {
            System.out.println("La pièce " + targetPiece.getAnimal() + " a été capturée !");
        }

        // Déplacement
        grid[to.getX()][to.getY()] = movingPiece;
        grid[from.getX()][from.getY()] = null;
        movingPiece.setPosition(to);
    }


    public void display() {
        System.out.println("\n   A B C D E F G");
        for (int y = 0; y < HEIGHT; y++) {
            System.out.print((y + 1) + " ");
            for (int x = 0; x < WIDTH; x++) {
                if (grid[x][y] != null) {
                    Piece piece = grid[x][y];
                    System.out.print(piece.getAnimal().getSymbol() + piece.getPlayer());
                } else {
                    System.out.print(" .");
                }
            }
            System.out.println();
        }
    }
}