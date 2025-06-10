package com.xoudouqi.utils;

public class ConsoleUtils {
    public static void displayMainMenu() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Se connecter");
        System.out.println("2. Créer un compte");
        System.out.println("3. Quitter");
        System.out.print("Votre choix : ");
    }

    public static void displayGameMenu(String username) {
        System.out.println("\n=== Tour de " + username + " ===");
        System.out.println("1. Déplacer une pièce");
        System.out.println("2. Voir l'historique");
        System.out.println("3. Abandonner");
        System.out.print("Votre choix : ");
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Impossible de nettoyer la console: " + e.getMessage());
        }
    }
}
