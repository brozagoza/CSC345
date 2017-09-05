/*=================================================================================================================================*
 ||
 ||  Class HeapSort 
 ||
 ||         Author:  Alejandro Zaragoza
 ||        
 ||          Class:  CSC 345
 ||
 ||        Purpose:  This code simply uses the heapsort methods to do just that: heapsort. This was included in a different
 ||                  class to keep the SortTime code cleaner.
 ||
 =================================================================================================================================*/
public class HeapSort
{
    
    /*=================================================================================================================================
     |  heap_sort_master (int[] data, int length)
     |
     |        Purpose: This function is the master of the heapsort. Calls the corresponding functinos as well as goes through the for
     |                 loop to place the max values of the array at the end of array.
     |
     *=================================================================================================================================*/
    public static void heap_sort_master (int[] array, int length)
    {
        int N = 0;
        
        N = build_heap(array, N); // construct the heap
        for (int i = N; i > 0; i--)
        {
            int tmp = array[0]; // swap with max
            array[0] = array[i];
            array[i] = tmp;
            
            N = N-1; // move cursor over
            
            max_heap(array, 0, N);
            
        }
    } // end heap_sort_master
    
    /*=================================================================================================================================
     |  build_heap (int[] array, int N)
     |
     |        Purpose: Function that is called from the begginning to build the heap itself. Returns the new value of N.
     |
     *=================================================================================================================================*/
    public static int build_heap (int[] array, int N)
    {
        N = array.length-1;
        for (int i = N/2; i >= 0; i--) // start at middle
            max_heap(array, i, N);
        
        return N;
    } // end build_heap
    
    /*=================================================================================================================================
     |  max_heap (int[] array, int i, int N)
     |
     |        Purpose: This function swaps the largest element in the heap.
     |
     *=================================================================================================================================*/
    public static void max_heap(int[] array, int i, int N)
    {
        int left = 2*i; // left child
        int right = 2*i + 1; // right child
        int max = i;
        
        if (left <= N && array[left] > array[i])
            max = left;
        
        if (right <= N && array[right] > array[max])
            max = right;
        
        if (max != i)
        {
            int tmp = array[i]; // swap
            array[i] = array[max];
            array[max] = tmp;
            
            max_heap(array, max, N);
            
        }
        
    } // end max_heap
    
} // end HeapSort