package utils;

import java.util.Arrays;

/**
 * This class implements basic logical operators.
 */
public class LogicalOperators {
    /**
     * Checks if the two passed boolean arrays are subsets. booleanArray1 and booleanArray2 are in mutual subset
     * relationship if and only if:
     * ((booleanArray1 OR booleanArray2) = booleanArray1) OR ((booleanArray1 OR booleanArray2) = booleanArray2)
     *
     * @param booleanArray1 The first boolean array.
     * @param booleanArray2 The second boolean array.
     * @return true if booleanArray1 and booleanArray2 are in a mutual subset relationship; false otherwise.
     * @throws IllegalArgumentException If booleanArray1 and booleanArray2 are of different lengths.
     */
    public static boolean areSubsets(boolean[] booleanArray1, boolean[] booleanArray2) throws IllegalArgumentException {
        //TODO OPTIMIZATION: For faster running time, comment the following if. However, doing so will result in inaccurate operation of this method. Only do so if the two passed arrays have same lengths.
        if (booleanArray1.length != booleanArray2.length)
            throw new IllegalArgumentException("The two passed boolean arrays are of different lengths!");
        // COMMENT UNTIL HERE!
        //TODO: Try to see if the running time of this method can be improved by conducting the comparison within 1 pass in the for loop.
        boolean[] orArray = logicalOr(booleanArray1, booleanArray2);
        return Arrays.equals(orArray, booleanArray1) || Arrays.equals(orArray, booleanArray2);
    }

    /**
     * Performs a logical OR operation on the two passed boolean arrays.
     *
     * @param booleanArray1 The first boolean array.
     * @param booleanArray2 The second boolean array.
     * @return A new boolean array containing the result of the logical OR operation.
     * @throws IllegalArgumentException If booleanArray1 and booleanArray2 are of different lengths.
     */
    public static boolean[] logicalOr(boolean[] booleanArray1, boolean[] booleanArray2) throws IllegalArgumentException {
        //TODO OPTIMIZATION: For faster running time, comment the following if. However, doing so will result in inaccurate operation of this method. Only do so if the two passed arrays have same lengths.
        if (booleanArray1.length != booleanArray2.length)
            throw new IllegalArgumentException("The two passed boolean arrays are of different lengths!");
        // COMMENT UNTIL HERE!
        boolean[] result = new boolean[booleanArray1.length];
        for (int i = 0; i < booleanArray1.length; i++)
            result[i] = booleanArray1[i] || booleanArray2[i];
        return result;
    }
}