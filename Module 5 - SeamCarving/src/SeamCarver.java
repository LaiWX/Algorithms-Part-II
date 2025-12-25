import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture picture;
    private int width, height;
    private double[][] energy;
    private int[][] rgbs;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("picture can't be null");

        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1)
            throw new IllegalArgumentException("index out of range");

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return 1000.0;

        int deltaXSqua = colorDiffrernce(x + 1, y, x - 1, y);
        int deltaYSqua = colorDiffrernce(x, y + 1, x, y - 1);

        return Math.sqrt(deltaXSqua + deltaYSqua);
    }

    private int colorDiffrernce(int x1, int y1, int x2, int y2) {
        int deltaRed = getColorComponent(x1, y1, 0) - getColorComponent(x2, y2, 0);
        int deltaGreen = getColorComponent(x1, y1, 1) - getColorComponent(x2, y2, 1);
        int deltaBlue = getColorComponent(x1, y1, 2) - getColorComponent(x2, y2, 2);

        return deltaRed * deltaRed + deltaGreen * deltaGreen + deltaBlue * deltaBlue;
    }

    /**
     * @param color 0: red 1: green 2: blue.
     */
    private int getColorComponent(int x, int y, int color) {
        int rgb;
        if (rgbs != null) {
            rgb = rgbs[x][y];
        }
        else {
            rgb = picture.getRGB(x, y);
        }

        if (color == 0) {
            return (rgb >> 16) & 0xFF;
        }
        else if (color == 1) {
            return (rgb >> 8) & 0xFF;
        }
        else {
            return (rgb) & 0xFF;
        }
    }

    private void transpose() {
        Picture transedPic = new Picture(height, width);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixelRGB = picture.getRGB(x, y);
                transedPic.setRGB(y, x, pixelRGB);
            }
        }

        picture = transedPic;

        int tmp = width;
        width = height;
        height = tmp;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] result = findVerticalSeam();
        transpose();

        return result;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        energy = new double[width][height];

        rgbs = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rgbs[x][y] = picture.getRGB(x, y);
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energy[x][y] = energy(x, y);
            }
        }

        rgbs = null;

        double[][] distTo = new double[2][width];
        int[][][] pathTo = new int[width][height][2];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                relax(distTo, pathTo, x, y);
            }
            distTo[0] = distTo[1];
            distTo[1] = new double[width];
        }

        int minXIndex = 0;
        double minDist = distTo[0][0];
        for (int x = 1; x < width; x++) {
            if (distTo[0][x] < minDist) {
                minXIndex = x;
                minDist = distTo[0][x];
            }
        }

        int[] result = new int[height];
        for (int i = height - 1; i >= 0; i--) {
            result[i] = minXIndex;
            minXIndex = pathTo[minXIndex][i][0];
        }

        energy = null;

        return result;
    }

    private void relax(double[][] distTo, int[][][] pathTo, int x, int y) {
        if (y == 0) {
            distTo[1][x] = energy[x][y];
            return;
        }

        if (x != 0) relaxEdge(distTo, pathTo, x - 1, y - 1, x, y);
        relaxEdge(distTo, pathTo, x, y - 1, x, y);
        if (x != width - 1) relaxEdge(distTo, pathTo, x + 1, y - 1, x, y);
    }

    private void relaxEdge(double[][] distTo, int[][][] pathTo, int fromX, int fromY, int toX,
                           int toY) {
        double newDist = distTo[0][fromX] + energy[toX][toY];

        if (newDist < distTo[1][toX] || distTo[1][toX] == 0) {
            distTo[1][toX] = newDist;
            pathTo[toX][toY][0] = fromX;
            pathTo[toX][toY][1] = fromY;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width <= 1) throw new IllegalArgumentException("picture size <= 1");

        checkSeam(seam);

        Picture newPic = new Picture(width - 1, height);

        for (int y = 0; y < height; y++) {
            int shift = 0;
            for (int x = 0; x < width - 1; x++) {
                if (x == seam[y]) shift = 1;
                int pixel = picture.getRGB(x + shift, y);
                newPic.setRGB(x, y, pixel);
            }
        }

        width = width - 1;
        picture = newPic;
    }

    private void checkSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("seam can't be null");

        if (seam.length != height)
            throw new IllegalArgumentException("wrong size of seam");

        int lastValue = seam[0];
        if (lastValue < 0 || lastValue > width - 1) {
            throw new IllegalArgumentException("value out of range at seam[0]");
        }

        for (int i = 1; i < height; i++) {
            if (seam[i] < 0 || seam[i] > width - 1) {
                throw new IllegalArgumentException("value out of range at seam[" + i + "]");
            }

            if (Math.abs(seam[i] - lastValue) > 1) {
                throw new IllegalArgumentException("two adjacent entries differ by more than 1");
            }

            lastValue = seam[i];
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver seamCarver = new SeamCarver(picture);

        int[] h = seamCarver.findHorizontalSeam();
        double energyOfH = 0;
        for (int x = 0; x < h.length; x++) {
            energyOfH += seamCarver.energy(x, h[x]);
        }

        int[] v = seamCarver.findVerticalSeam();
        double energyOfV = 0;
        for (int y = 0; y < v.length; y++) {
            energyOfV += seamCarver.energy(v[y], y);
        }

        StdOut.println("energy of h: " + energyOfH);
        StdOut.println("energy of v: " + energyOfV);
    }
}