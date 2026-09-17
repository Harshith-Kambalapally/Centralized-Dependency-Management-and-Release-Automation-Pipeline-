import java.util.Arrays;

class Solution {
    public int minSumOfLengths(int[] arr, int target) {
        int n = arr.length;
        
        // minLen[i] will store the minimum length of a valid sub-array ending at or before index i
        int[] minLen = new int[n];
        Arrays.fill(minLen, Integer.MAX_VALUE);
        
        int ans = Integer.MAX_VALUE;
        int bestSoFar = Integer.MAX_VALUE;
        
        int left = 0;
        int currentSum = 0;
        
        for (int right = 0; right < n; right++) {
            currentSum += arr[right];
            
            // Shrink the sliding window if the current sum exceeds the target
            while (currentSum > target && left <= right) {
                currentSum -= arr[left];
                left++;
            }
            
            // If we find a valid sub-array that equals the target
            if (currentSum == target) {
                int currLen = right - left + 1;
                
                // Check if there is a valid non-overlapping sub-array completely to the left
                if (left > 0 && minLen[left - 1] != Integer.MAX_VALUE) {
                    ans = Math.min(ans, currLen + minLen[left - 1]);
                }
                
                // Update the shortest valid length found so far
                bestSoFar = Math.min(bestSoFar, currLen);
            }
            
            // Carry forward the best length up to the current index
            minLen[right] = bestSoFar;
        }
        
        // If ans is still MAX_VALUE, it means we couldn't find two such sub-arrays
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}