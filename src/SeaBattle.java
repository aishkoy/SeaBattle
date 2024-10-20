import java.util.Random;
import java.util.Scanner;

public class SeaBattle {

    public static void main(String[] args) {
        int rows = 7;
        int cols = 7;
        int[][] matrix = new int[rows][cols];
        Random random = new Random();

        int[][] blockSizes = {{1, 3}, {1, 2}, {1, 2}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};

        for (int[] blockSize : blockSizes) {
            placeRandomBlock(matrix, random, blockSize[0], blockSize[1]);
        }
//        showField(matrix);
        showEmptyField(rows, cols);

        int[][] shots = new int[rows][cols];

        int numberOfAttempts = 25;
        while(numberOfAttempts > 0) {
            System.out.println("Attempts left: " + numberOfAttempts);
            int[] coordinates = null;

            while(coordinates == null) {
                coordinates = getPlayerInput();
            }

            int row = coordinates[0];
            int col = coordinates[1];

            processPlayerShot(matrix, shots, row, col);

            if (matrix[row][col] == 1) {
                if (isShipSunk(matrix, shots, row, col)) {
                    System.out.println("You've sunk a ship! \uD83C\uDFF3\uFE0F\u200D");
                    markSunkShip(matrix, shots, row, col);
                }
            }
            showUpdatedField(matrix, shots);

            numberOfAttempts -= 1;

            if (checkAllShipsSunk(matrix, shots)) {
                System.out.println("Congratulations! You've sunk all ships!");
                break;
            }
        }

        if (numberOfAttempts == 0) {
            System.out.println("Game over! You've used all your attempts.");
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

        int col = columnChar - 'A';
        int row = Integer.parseInt(rowStr) - 1;

        if (col < 0 || col > 6 || row < 0 || row > 6) {
            System.out.println("Coordinates are out of range. Try again.");
            return null;
        } else {
            System.out.println("You've entered the coordinates: " + columnChar + rowStr + " (row " + (row + 1) + ", column " + (col + 1) + ")");
        }
        return new int[]{row, col};
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

    public static void processPlayerShot(int[][] matrix, int[][] shots, int row, int col) {
        if (matrix[row][col] == 1) {
            System.out.println("Hitted! 🔥");
            shots[row][col] = 2;
        } else {
            System.out.println("Missed. ⬛");
            shots[row][col] = 1;
        }
    }

    public static boolean isShipSunk(int[][] matrix, int[][] shots, int row, int col) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 1 && shots[i][j] != 2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void markSunkShip(int[][] matrix, int[][] shots, int row, int col) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 1 && shots[i][j] == 2) {
                    matrix[i][j] = 3;
                    shots[i][j] = 3;
                }
            }
        }
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
                } else if (matrix[i][j] == 3) {
                    System.out.print("\uD83C\uDFF3\uFE0F\u200D ");
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
                if (matrix[i][j] == 1 && shots[i][j] != 2) {
                    return false;
                }
            }
        }
        return true;
    }
}

