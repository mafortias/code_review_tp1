package fr.isima.zz2.f5.ai.checkpassword;

//package fr.isima.codereview.tp1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * The AwesomePasswordChecker class provides methods for checking the strength
 * of passwords using a predefined set of cluster centers, as well as utilities
 * for computing the MD5 hash of a string. It utilizes a mask-based approach to
 * convert passwords into arrays for comparison. This class is designed to be
 * used as a singleton with a configuration file specifying the cluster centers.
 *
 * @author Ryadh B., Matheo F.
 */
public class AwesomePasswordChecker {

    private static AwesomePasswordChecker instance;
    private final List<double[]> clusterCenters = new ArrayList<>();

    /**
     * Returns the singleton instance of AwesomePasswordChecker using the given
     * file containing the cluster centers.
     *
     * @param file the file containing all the cluster centers.
     * @return the singleton instance of AwesomePasswordChecker.
     * @throws IOException if there is an issue reading the file.
     */
    public static AwesomePasswordChecker getInstance(File file) throws IOException {
        if (instance == null) {
            instance = new AwesomePasswordChecker(new FileInputStream(file));
        }
        return instance;
    }

    /**
     * Returns the singleton instance of AwesomePasswordChecker by loading the
     * default cluster centers from the classpath resource.
     *
     * @return the singleton instance of AwesomePasswordChecker.
     * @throws IOException if there is an issue reading the default cluster centers.
     */
    public static AwesomePasswordChecker getInstance() throws IOException {
        if (instance == null) {
            InputStream input = AwesomePasswordChecker.class.getClassLoader().getResourceAsStream("cluster_centers_HAC_aff.csv");
            instance = new AwesomePasswordChecker(input);
        }
        return instance;
    }

    /**
     * Private constructor for the AwesomePasswordChecker class
     * which initializes the cluster centers by reading a CSV file from the InputStream.
     * 
     * Each line in the file is expected to represent a cluster center.
     * The values are parsed (as doubles) and saved in the clusterCenters collection. 
     * @param input : The InputStream from which the CSV file will be read.
     *                  Each line should represent a cluster center as a list of numeric values.
     * @throws IOException if an Input/Output error occurs while reading the file.     */
    private AwesomePasswordChecker(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] values = line.split(",");
                double[] center = new double[values.length];

                for (int i = 0; i < values.length; ++i) {
                    center[i] = Double.parseDouble(values[i]);
                }
                clusterCenters.add(center);
            }
        }
    }

    /**
     * Converts a given password into a mask array based on predefined rules.
     * The password characters are mapped to integer values representing their
     * type (e.g., lowercase letter, uppercase letter, special character, etc.).
     *
     * @param password the password to convert to a mask.
     * @return an integer array representing the masked version of the password.
     */
    public int[] maskAff(String password) {
        int[] maskArray = new int[28];
        int limit = Math.min(password.length(), 28);

        for (int i = 0; i < limit; ++i) {
            char charInPasswd = password.charAt(i);
            switch (charInPasswd) {
                case 'e':
                case 's':
                case 'a':
                case 'i':
                case 't':
                case 'n':
                case 'r':
                case 'u':
                case 'o':
                case 'l':
                    maskArray[i] = 1;
                    break;
                case 'E':
                case 'S':
                case 'A':
                case 'I':
                case 'T':
                case 'N':
                case 'R':
                case 'U':
                case 'O':
                case 'L':
                    maskArray[i] = 3;
                    break;
                case '>':
                case '<':
                case '-':
                case '?':
                case '.':
                case '/':
                case '!':
                case '%':
                case '@':
                case '&':
                    maskArray[i] = 6;
                    break;
                default:
                    if (Character.isLowerCase(charInPasswd)) {
                        maskArray[i] = 2;
                    } else if (Character.isUpperCase(charInPasswd)) {
                        maskArray[i] = 4;
                    } else if (Character.isDigit(charInPasswd)) {
                        maskArray[i] = 5;
                    } else {
                        maskArray[i] = 7;
                    }
            }
        }

        return maskArray;
    }

    /**
     * Calculates the minimum Euclidean distance between a given password's mask
     * array and the predefined cluster centers. This can be used to estimate
     * how close a password is to a "weak" or "strong" password cluster.
     *
     * @param password the password to compute the distance for.
     * @return the minimum distance between the password and the cluster centers.
     */
    public double getDistance(String password) {
        int[] maskArray = maskAff(password);
        double minDistance = Double.MAX_VALUE;
        for (double[] center : clusterCenters) {
            minDistance = Math.min(euclideanDistance(maskArray, center), minDistance);
        }

        return minDistance;
    }

    /**
     * Computes the Euclidean distance between two arrays.
     *
     * @param a the first array.
     * @param b the second array.
     * @return the Euclidean distance between the two arrays.
     */
    private double euclideanDistance(int[] vectorA, double[] vectorB) {
        double sum = 0;
        for (int i = 0; i < vectorA.length; i++) {
            sum += (vectorA[i] - vectorB[i]) * (vectorA[i] + vectorB[i]);
        }

        return Math.sqrt(sum);
    }

    /**
     * Computes the MD5 hash of a given input string. This method implements the
     * MD5 algorithm manually by processing the input in blocks and applying the
     * MD5 transformation steps.
     *
     * @param input the input string to hash.
     * @return the MD5 hash of the input string as a hexadecimal string.
     */
    public static String computeMd5(String input) {
        byte[] message = input.getBytes();
        int messageLenBytes = message.length;
        int temp;

        int numBlocks = ((messageLenBytes + 8) >>> 6) + 1;
        int totalLen = numBlocks << 6;
        byte[] paddingBytes = new byte[totalLen - messageLenBytes];
        paddingBytes[0] = (byte) 0x80;

        long messageLenBits = (long) messageLenBytes << 3;
        ByteBuffer lengthBuffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(messageLenBits);
        byte[] lengthBytes = lengthBuffer.array();

        byte[] paddedMessage = new byte[totalLen];
        System.arraycopy(message, 0, paddedMessage, 0, messageLenBytes);
        System.arraycopy(paddingBytes, 0, paddedMessage, messageLenBytes, paddingBytes.length);
        System.arraycopy(lengthBytes, 0, paddedMessage, totalLen - 8, 8);

        int[] h1 = {
            0x67452301,
            0xefcdab89,
            0x98badcfe,
            0x10325476
        };

        int[] k1 = {
            0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
            0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
            0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, 0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
            0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a,
            0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
            0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05, 0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665,
            0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039, 0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
            0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391
        };

        int[] r1 = {
            7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
            5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
            4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
            6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
        };

        for (int i = 0; i < numBlocks; i++) {
            int[] w1 = new int[16];
            for (int j = 0; j < 16; j++) {
                w1[j] = ByteBuffer.wrap(paddedMessage, (i << 6) + (j << 2), 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            }

            int a1 = h1[0];
            int b1 = h1[1];
            int c1 = h1[2];
            int d1 = h1[3];

            for (int j = 0; j < 64; j++) {
                int f1;
                int g1;
                if (j < 16) {
                    f1 = (b1 & c1) | (~b1 & d1);
                    g1 = j;
                } else if (j < 32) {
                    f1 = (d1 & b1) | (~d1 & c1);
                    g1 = (5 * j + 1) % 16;
                } else if (j < 48) {
                    f1 = b1 ^ c1 ^ d1;
                    g1 = (3 * j + 5) % 16;
                } else {
                    f1 = c1 ^ (b1 | ~d1);
                    g1 = (7 * j) % 16;
                }
                temp = d1;
                d1 = c1;
                c1 = b1;
                b1 = b1 + Integer.rotateLeft(a1 + f1 + k1[j] + w1[g1], r1[j]);
                a1 = temp;
            }

            h1[0] += a1;
            h1[1] += b1;
            h1[2] += c1;
            h1[3] += d1;
        }

        ByteBuffer md5Buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
        md5Buffer.putInt(h1[0]).putInt(h1[1]).putInt(h1[2]).putInt(h1[3]);
        byte[] md5Bytes = md5Buffer.array();

        StringBuilder md5Hex = new StringBuilder();
        for (byte b : md5Bytes) {
            md5Hex.append(String.format("%02x", b));
        }

        return md5Hex.toString();
    }
    
    public static void main(String[] argv){
        System.out.println("oui");
        System.out.println(computeMd5("romainaznar"));
        try{
            AwesomePasswordChecker a = AwesomePasswordChecker.getInstance();
            System.out.println(a.getDistance("romainaznar"));
        }catch(IOException e){
            System.out.println("erreur");
        }
       
    }
}
