/*=================================================================================================================================*
 ||
 ||  Class QuickSort 
 ||
 ||         Author:  Alejandro Zaragoza
 ||        
 ||          Class:  CSC 345
 ||
 ||        Purpose:  This code simply uses the quicksort methods to do just that: quicksort. This was included in a different
 ||                  class to keep the SortTime code cleaner.
 ||
 =================================================================================================================================*/
public class QuickSort
{
    /*=================================================================================================================================
     |  partition (int[] array, int length)
     |
     |        Purpose: Splits the array according to the left and right and uses the pivot.
     |
     *=================================================================================================================================*/
    public static int partition (int[] array, int left, int right)
    {
        int i = left;
        int j = right;
        int tmp;
        int pivot = array[ (left+right) / 2]; // pivot is the middle
        
        while (i <= j) // left of right
        {
            while (array[i] < pivot)
                i++;
            
            while (array[j] > pivot)
                j--;
            
            if (i <= j)
            {
                tmp = array[i]; // swap
                array[i] = array[j];
                array[j] = tmp;
                i++;
                j--;
            } // end if
        } // end while
        
        return i;
    } // end partition
    
    /*=================================================================================================================================
     |  static void quick_sort_master (int[] array, int length)
     |
     |        Purpose: Recursively calls itself to sort the array.
     |
     *=================================================================================================================================*/
    public static void quick_sort_master (int[] array, int left, int right)
    {
        int index = partition(array, left, right);
        
        if (left < index - 1)
            quick_sort_master(array, left, index-1); // left side
        
        if (index < right)
            quick_sort_master(array, index, right); // right side
        
    } // end quick_sort_master
    
} // end QuickSort