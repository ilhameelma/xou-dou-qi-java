package com.xoudouqi;

import com.xoudouqi.dao.GameDao;
import com.xoudouqi.dao.PlayerDao;
import com.xoudouqi.model.*;
import com.xoudouqi.utils.ConsoleUtils;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PlayerDao playerDao = new PlayerDao();
    private static final GameDao gameDao = new GameDao();
    private static Player currentPlayer1;
    private static Player currentPlayer2;

    public static void main(String[] args) {
        ConsoleUtils.clearConsole();
        System.out.println("=== Xou Dou Qi ===");
        
        while (true) {
            ConsoleUtils.displayMainMenu();
            int choice = getIntInput("Votre choix : ");

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    private static void login() {
        System.out.print("\nNom d'utilisateur: ");
        String username = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        Optional<Player> playerOpt = playerDao.findByUsername(username);
        if (playerOpt.isPresent() && playerOpt.get().getPassword().equals(password)) {
            if (currentPlayer1 == null) {
                currentPlayer1 = playerOpt.get();
                System.out.println("\nJoueur 1 connecté: " + currentPlayer1.getUsername());
            } else if (currentPlayer2 == null && !currentPlayer1.getUsername().equals(username)) {
                currentPlayer2 = playerOpt.get();
                System.out.println("\nJoueur 2 connecté: " + currentPlayer2.getUsername());
                startGame();
            } else {
                System.out.println("\nVous êtes déjà connecté ou vous ne pouvez pas jouer contre vous-même.");
            }
        } else {
            System.out.println("\nIdentifiants incorrects.");
        }
    }

    private static void register() {
        System.out.print("\nNouveau nom d'utilisateur: ");
        String username = scanner.nextLine();
        System.out.print("Nouveau mot de passe: ");
        String password = scanner.nextLine();

        if (username.length() < 3 || password.length() < 3) {
            System.out.println("\nLe nom d'utilisateur et le mot de passe doivent contenir au moins 3 caractères.");
            return;
        }

        Player newPlayer = new Player(username, password);
        if (playerDao.create(newPlayer)) {
            System.out.println("\nCompte créé avec succès! Vous pouvez maintenant vous connecter.");
        } else {
            System.out.println("\nErreur lors de la création du compte. Le nom d'utilisateur existe peut-être déjà.");
        }
    }

    private static void startGame() {
        Game game = new Game(currentPlayer1, currentPlayer2);
        Board board = new Board();
        boolean gameOver = false;

        while (!gameOver) {
            ConsoleUtils.clearConsole();
            Player currentPlayer = game.getCurrentPlayer();
            System.out.println("\n=== Tour de " + currentPlayer.getUsername() + " ===");
            board.display();

            ConsoleUtils.displayGameMenu(currentPlayer.getUsername());
            int choice = getIntInput("Votre choix : ");

            switch (choice) {
                case 1:
                    if (makeMove(board, game, currentPlayer)) {
                        game.switchPlayer();
                    }
                    break;
                case 2:
                    displayHistory(currentPlayer);
                    break;
                case 3:
                    gameOver = confirmResignation(game, currentPlayer);
                    break;
                default:
                    System.out.println("Choix invalide.");
                    pressEnterToContinue();
            }

            if (board.isGameWon()) {
                gameOver = true;
                Player winner = game.getCurrentPlayer();
                System.out.println("\n" + winner.getUsername() + " a gagné la partie!");
                game.setWinner(winner);
                updatePlayerStats(winner, game.getOtherPlayer(winner));
                pressEnterToContinue();
            }
        }

        gameDao.saveGame(game);
        currentPlayer1 = null;
        currentPlayer2 = null;
    }

    private static boolean makeMove(Board board, Game game, Player player) {
        while (true) {
            System.out.print("\nEntrez votre mouvement (ex. A2 A3) ou 'annuler' pour revenir: ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equalsIgnoreCase("annuler")) {
                return false;
            }

            try {
                String[] parts = input.split(" ");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Format invalide");
                }

                Position from = parsePosition(parts[0]);
                Position to = parsePosition(parts[1]);

                if (board.isValidMove(from, to, player.equals(game.getPlayer1()) ? 1 : 2)) {
                    board.movePiece(from, to);
                    game.addMoveToHistory(player, from, to);
                    return true;
                } else {
                    System.out.println("Mouvement invalide. Veuillez réessayer.");
                }
            } catch (Exception e) {
                System.out.println("Format incorrect. Utilisez la notation 'LettreChiffre LettreChiffre' (ex. A2 A3)");
            }
        }
    }

    private static Position parsePosition(String pos) {
        if (pos.length() != 2) {
            throw new IllegalArgumentException();
        }
        int x = pos.charAt(0) - 'A';
        int y = Character.getNumericValue(pos.charAt(1)) - 1;
        return new Position(x, y);
    }

    private static void displayHistory(Player player) {
        ConsoleUtils.clearConsole();
        System.out.println("\n=== Historique des parties ===");
        List<Game> games = gameDao.getGamesByPlayer(player.getId());

        if (games.isEmpty()) {
            System.out.println("Aucune partie jouée pour le moment.");
        } else {
            System.out.printf("%-20s %-15s %-15s %-10s %-15s\n", 
                "Date", "Joueur 1", "Joueur 2", "Résultat", "Durée");
            System.out.println("---------------------------------------------------------------");
            
            for (Game game : games) {
                String result;
                if (game.getWinner() == null) {
                    result = "Égalité";
                } else if (game.getWinner().equals(player)) {
                    result = "Victoire";
                } else {
                    result = "Défaite";
                }

                String duration = formatGameDuration(game);
                
                System.out.printf("%-20s %-15s %-15s %-10s %-15s\n",
                    game.getStartDate().toString().substring(0, 16),
                    game.getPlayer1().getUsername(),
                    game.getPlayer2().getUsername(),
                    result,
                    duration);
            }
        }
        pressEnterToContinue();
    }

    private static String formatGameDuration(Game game) {
        if (game.getEndDate() == null) return "N/A";
        long diff = game.getEndDate().getTime() - game.getStartDate().getTime();
        long minutes = (diff / (1000 * 60)) % 60;
        long seconds = (diff / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private static boolean confirmResignation(Game game, Player currentPlayer) {
        System.out.print("\nÊtes-vous sûr de vouloir abandonner? (Oui/Non): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("oui") || response.equals("o")) {
            Player winner = game.getOtherPlayer(currentPlayer);
            System.out.println("\n" + currentPlayer.getUsername() + " a abandonné. " + 
                              winner.getUsername() + " gagne la partie!");
            game.setWinner(winner);
            updatePlayerStats(winner, currentPlayer);
            return true;
        }
        return false;
    }

    private static void updatePlayerStats(Player winner, Player loser) {
        winner.setWins(winner.getWins() + 1);
        loser.setLosses(loser.getLosses() + 1);
        playerDao.updateStats(winner);
        playerDao.updateStats(loser);
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    private static void pressEnterToContinue() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }
}