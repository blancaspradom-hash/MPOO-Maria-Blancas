public class LongestPeak {
    public static int longestPeak(int[] array) {
        int maxLength = 0;
        int i = 1;

        while (i < array.length - 1) {
            boolean isPeak = array[i - 1] < array[i] && array[i] > array[i + 1];

            if (!isPeak) {
                i++;
                continue;
            }

            int leftIdx = i - 2;
            while (leftIdx >= 0 && array[leftIdx] < array[leftIdx + 1]) {
                leftIdx--;
            }

            int rightIdx = i + 2;
            while (rightIdx < array.length && array[rightIdx] < array[rightIdx - 1]) {
                rightIdx++;
            }

            int currentPeakLength = rightIdx - leftIdx - 1;
            if (currentPeakLength > maxLength) {
                maxLength = currentPeakLength;
            }

            i = rightIdx;
        }

        return maxLength;
    }

    public static void main(String[] args) {
        int[] array = {1, 2, 3, 3, 4, 0, 10, 6, 5, -1, -3, 2, 3};
        System.out.println("Pico más largo: " + longestPeak(array));
    }
}