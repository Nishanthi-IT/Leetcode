class Solution {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        int m = nums1.length;
        int n = nums2.length;

        int[] merged = new int[m + n];

        int i = 0, j = 0, k = 0;

        while (i < m && j < n) {

            if (nums1[i] < nums2[j]) {
                merged[k] = nums1[i];
                i++;
            } else {
                merged[k] = nums2[j];
                j++;
            }

            k++;
        }

        while (i < m) {
            merged[k] = nums1[i];
            i++;
            k++;
        }

        while (j < n) {
            merged[k] = nums2[j];
            j++;
            k++;
        }

        int total = m + n;

        if (total % 2 == 1) {
            return merged[total / 2];
        } else {
            return (merged[total / 2] + merged[(total / 2) - 1]) / 2.0;
        }
    }

    public static void main(String[] args) {

        Solution obj = new Solution();

        int[] nums1 = {1, 2};
        int[] nums2 = {3, 4};

        double result = obj.findMedianSortedArrays(nums1, nums2);

        System.out.println(result);
    }
}