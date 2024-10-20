import java.util.Random;

public class SeaBattle {

    public static void main(String[] args) {
        int rows = 7;
        int cols = 7;
        int[][] matrix = new int[rows][cols];
        Random random = new Random();

        int[][] blockSizes = {{1, 3}, {1, 2}, {1, 2}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
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
}
