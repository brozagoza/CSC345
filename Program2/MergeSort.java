/*=================================================================================================================================*
 ||
 ||  Class MergeSort 
 ||
 ||         Author:  Alejandro Zaragoza
 ||        
 ||          Class:  CSC 345
 ||
 ||        Purpose:  This code simply uses the MergeSort methods to do just that: mergesort. This was included in a different
 ||                  class to keep the SortTime code cleaner.
 ||
 =================================================================================================================================*/
public class MergeSort
{
    /*=================================================================================================================================
     |  merge_sort_master (int[] array)
     |
     |        Purpose: TTHis algoirthm uses the sort call to do merge sort.
     |
     *=================================================================================================================================*/
    public static void merge_sort_master (int[] array)
    {
        int [] tmp = new int[array.length];
        sort(array, tmp, 0, array.length-1);
    } // end merge_sort_master
    
    /*=================================================================================================================================
     |  sort (int[] array, int length)
     |
     |        Purpose: Seperates the array into two and sorts those arrays.
     |
     *=================================================================================================================================*/
    private static void sort(int[] array, int[] tmp, int left, int right)
    {
        if (left<right) // while left does not cross right
        {
            int center = (left + right) / 2;
            sort(array, tmp, left, center); // left array
            sort(array, tmp, center+1, right); // right array
            merge(array, tmp, left, center+1, right); // join arrays
        }
    } // end sort
    
    /*=================================================================================================================================
     |  merge (int[] array, int length)
     |
     |        Purpose: Merges the two arrays according to the left, right, and rightend indexes. Storred in original array. 
     |
     *=================================================================================================================================*/
    private static void merge (int[] array, int[] tmp, int left, int right, int rightEnd)
    {
        int leftEnd = right - 1;                // the end of the left array
        int k = left;                           // starting index
        int num = rightEnd - left + 1;          // 
        
        while (left <= leftEnd && right <= rightEnd)
        {
            if (array[left] <= array[right]) 
                tmp[k++] = array[left++];
            else
                tmp[k++] = array[right++];
        } // end while
        
        // Copy rest of first half
        while (left <= leftEnd)
            tmp[k++] = array[left++];
        
        // Copy rest of right half
        while (right <= rightEnd)
            tmp[k++] = array[right++];
        
        // Copy temp
        for (int i = 0; i < num; i++, rightEnd--)
            array[rightEnd] = tmp[rightEnd];
    }
    
} // end MergeSort