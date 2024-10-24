import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SeaBattle {
    static ArrayList<Player> players = new ArrayList<>();

    public static void main(String[] args) {
        int rows = 7;
        int cols = 7;
        int[][] matrix = new int[rows][cols];
        int[][] shots = new int[rows][cols];

        Random random = new Random();

        int[][] blockSizes = {{1, 3}, {1, 2}, {1, 2}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};

        Scanner sc = new Scanner(System.in);
        boolean gameContinues = true;

        while (gameContinues) {

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = 0;
                    shots[i][j] = 0;
                }
            }

            for (int[] blockSize : blockSizes) {
                placeRandomBlock(matrix, random, blockSize[0], blockSize[1]);
            }

            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            while(name.isBlank()){
                System.out.print("You didn't enter name. Try again: ");
                name = sc.nextLine();
            }
            Player player = getPlayer(name);

            if(player == null){
                player = new Player(name);
                players.add(player);
            }

            showEmptyField(rows, cols);
            int numberOfAttempts = 30;

            while(numberOfAttempts > 0) {
                System.out.println("Attempts left: " + numberOfAttempts);
                int[] coordinates = null;

                while(coordinates == null) {
                    coordinates = getPlayerInput();
                }

                int row = coordinates[0];
                int col = coordinates[1];

                if (shots[row][col] == 0) {
                    processPlayerShot(matrix, shots, row, col);

                    if (matrix[row][col] == 1) {
                        if (isShipSunk(matrix, shots, row, col)) {
                            System.out.println("You've sunk a ship! \uD83D\uDCA5");
                            markSunkShip(matrix, shots, row, col);
                        }
                    }

                    System.out.println();
                    showUpdatedField(matrix, shots);

                    if (checkAllShipsSunk(matrix, shots)) {
                        System.out.println("Congratulations! You've sunk all ships!");
                        player.incrementWins();
                        break;
                    }

                    numberOfAttempts--;

                } else {
                    System.out.println("You've already shot at this position. Try again.");
                }
            }

            if (numberOfAttempts == 0) {
                System.out.println("\nGame over! You've used all your attempts.");
                System.out.println("This is where the ships were located:");
                showField(matrix);
                player.incrementLosses();
            }

            boolean gameOver = true;
            while(gameOver){
                System.out.print("\nDo you want to play again? (yes/no): ");
                String answer = sc.nextLine();
                if (answer.equalsIgnoreCase("no")) {
                    gameOver = false;
                    gameContinues = false;
                    System.out.println("\nHere are the statistics of all players:");
                    displayLeaderboard();
                    System.out.println("\nThanks for playing!");
                    System.out.println("Goodbye!");
                } else if (answer.equalsIgnoreCase("yes")){
                    gameOver = false;
                    continue;
                } else{
                    System.out.print("Try again!");
                }
            }
        }
    }


    public static void placeRandomBlock(int[][] matrix, Random random, int blockHeight, int blockWidth) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean placed = false;

        while (!placed) {
            boolean isHorizontal = random.nextBoolean();
            int blockH = isHorizontal ? blockHeight : blockWidth;
            int blockW = isHorizontal ? blockWidth : blockHeight;

            int randomRow = random.nextInt(rows);
            int randomCol = random.nextInt(cols);

            if (canPlaceBlock(matrix, randomRow, randomCol, blockH, blockW, isHorizontal)) {
                for (int i = 0; i < blockH; i++) {
                    for (int j = 0; j < blockW; j++) {
                        matrix[randomRow + i][randomCol + j] = 1;
                    }
                }
                placed = true;
            }
        }
    }


    public static boolean canPlaceBlock(int[][] matrix, int row, int col, int blockHeight, int blockWidth, boolean isHorizontal) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        if (isHorizontal) {
            if (col + blockWidth > cols || row + blockHeight > rows) {
                return false;
            }
            for (int i = row - 1; i <= row + blockHeight; i++) {
                for (int j = col - 1; j <= col + blockWidth; j++) {
                    if (i >= 0 && i < rows && j >= 0 && j < cols && matrix[i][j] != 0) {
                        return false;
                    }
                }
            }
        } else {
            if (row + blockHeight > rows || col + blockWidth > cols) {
                return false;
            }
            for (int i = row - 1; i <= row + blockHeight; i++) {
                for (int j = col - 1; j <= col + blockWidth; j++) {
                    if (i >= 0 && i < rows && j >= 0 && j < cols && matrix[i][j] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void showField(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        System.out.print("   ");
        for (char c = 'A'; c < 'A' + cols; c++) {
            System.out.print(c + "  ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    System.out.print("⬜ ");
                } else {
                    System.out.print("\uD83D\uDEE5\uFE0F ");
                }
            }
            System.out.println();
        }
    }

    public static void showEmptyField(int rows, int cols) {
        System.out.print("   ");
        for (char c = 'A'; c < 'A' + cols; c++) {
            System.out.print(c + "  ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < cols; j++) {
                System.out.print("⬜ ");
            }
            System.out.println();
        }
    }

    public static int[] getPlayerInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the coordinates (for example, B3):");
        String input = scanner.nextLine().toUpperCase();

        if (input.length() < 2 || input.length() > 3) {
            System.out.println("Incorrect format. Try again.");
            return null;
        }

        char columnChar = input.charAt(0);
        String rowStr = input.substring(1);

        boolean isNumeric = true;
        for (int i = 0; i < rowStr.length(); i++) {
            if (!Character.isDigit(rowStr.charAt(i))) {
                isNumeric = false;
                break;
            }
        }

        if (!isNumeric) {
            System.out.println("Row must be a number. Try again.");
            return null;
        }

        int col = columnChar - 'A';
        int row = Integer.parseInt(rowStr) - 1;

        if (col < 0 || col > 6 || row < 0 || row > 6) {
            System.out.println("Coordinates are out of range. Try again.");
            return null;
        }
        return new int[]{row, col};
    }

    public static void processPlayerShot(int[][] matrix, int[][] shots, int row, int col) {
        if (shots[row][col] != 0) {
            System.out.println("You've already shot at this position. Try again.");
        } else {
            if (matrix[row][col] == 1) {
                System.out.println("Hit! 🔥");
                shots[row][col] = 2;
            } else {
                System.out.println("Missed. ⬛");
                shots[row][col] = 1;
            }
        }
    }

    public static boolean isShipSunk(int[][] matrix, int[][] shots, int row, int col) {
        if (isHorizontalShip(matrix, row, col)) {
            int startCol = col;
            while (startCol >= 0 && matrix[row][startCol] == 1) {
                if (shots[row][startCol] != 2) {
                    return false;
                }
                startCol--;
            }
            startCol = col + 1;
            while (startCol < matrix[0].length && matrix[row][startCol] == 1) {
                if (shots[row][startCol] != 2) {
                    return false;
                }
                startCol++;
            }

        } else {
            int startRow = row;
            while (startRow >= 0 && matrix[startRow][col] == 1) {
                if (shots[startRow][col] != 2) {
                    return false;
                }
                startRow--;
            }
            startRow = row + 1;
            while (startRow < matrix.length && matrix[startRow][col] == 1) {
                if (shots[startRow][col] != 2) {
                    return false;
                }
                startRow++;
            }
        }
        return true;
    }

    public static void markSunkShip(int[][] matrix, int[][] shots, int row, int col) {
        if (isHorizontalShip(matrix, row, col)) {
            int startCol = col;
            while (startCol >= 0 && matrix[row][startCol] == 1) {
                shots[row][startCol] = 3;
                startCol--;
            }
            startCol = col + 1;
            while (startCol < matrix[0].length && matrix[row][startCol] == 1) {
                shots[row][startCol] = 3;
                startCol++;
            }
        } else {
            int startRow = row;
            while (startRow >= 0 && matrix[startRow][col] == 1) {
                shots[startRow][col] = 3;
                startRow--;
            }
            startRow = row + 1;
            while (startRow < matrix.length && matrix[startRow][col] == 1) {
                shots[startRow][col] = 3;
                startRow++;
            }
        }
    }

    public static boolean isHorizontalShip(int[][] matrix, int row, int col) {
        int cols = matrix[0].length;
        if (col > 0 && matrix[row][col - 1] == 1) {
            return true;
        }
        if (col < cols - 1 && matrix[row][col + 1] == 1) {
            return true;
        }
        return false;
    }

    public static void showUpdatedField(int[][] matrix, int[][] shots) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        System.out.print("   ");
        for (char c = 'A'; c < 'A' + cols; c++) {
            System.out.print(c + "  ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < cols; j++) {
                if (shots[i][j] == 2) {
                    System.out.print("🔥 ");
                } else if (shots[i][j] == 1) {
                    System.out.print("⬛ ");
                } else if (shots[i][j] == 3) {
                    System.out.print("❌ ");
                } else {
                    System.out.print("⬜ ");
                }
            }
            System.out.println();
        }
    }

    public static boolean checkAllShipsSunk(int[][] matrix, int[][] shots) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 1 && shots[i][j] != 3) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    static class Player {
        private String name;
        private int wins;
        private int losses;

        public Player(String name) {
            this.name = name;
            this.wins = 0;
            this.losses = 0;
        }

        public String getName() {
            return name;
        }

        public int getWins() {
            return wins;
        }

        public int getLosses() {
            return losses;
        }

        public void incrementWins() {
            wins++;
        }

        public void incrementLosses() {
            losses++;
        }
    }

    public static void sortPlayersByWins(Player[] players) {
        int n = players.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (players[j].getWins() < players[j + 1].getWins()) {
                    Player temp = players[j];
                    players[j] = players[j + 1];
                    players[j + 1] = temp;
                }
            }
        }
    }

    public static void displayLeaderboard() {
        Player[] playerArray = new Player[players.size()];
        players.toArray(playerArray);

        sortPlayersByWins(playerArray);

        for (Player p : playerArray) {
            System.out.println(p.getName() + " - Wins: " + p.getWins() + ", Losses: " + p.getLosses());
        }
    }
}