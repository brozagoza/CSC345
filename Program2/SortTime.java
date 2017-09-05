/*=================================================================================================================================*
 ||
 ||  Class SortTime 
 ||
 ||         Author:  Alejandro Zaragoza
 ||        
 ||          Class:  CSC 345
 ||
 ||        Purpose:  This code is to be used to calculate the amount of time each sorting algoirthm takes,
 ||                  to sort arrays from sizes of 100 to 100,000. To run this code, enter the size of array you would like
 ||                  to go up to as the first argument and the sorting algo as the second argument. This program goes up array sizes
 ||                  in increments of 100 or 1000 so a max such as 1540 will stop array growing at 1500.
 ||
 ||     How to Use:  Run from the command-line with the first argument being the maximum array size to go up to, and the 
 ||                  second argument being a number which corresponds to which sorting method to run. The methods are as follows:
 ||
 ||                  0 - Robo Sort
 ||                  1 - Bubble Sort
 ||                  2 - Insertion Sort
 ||                  3 - Heapsort
 ||                  4 - Mergesort
 ||                  5 - Quicksort
 ||                  (any greater number will cause system exit)
 ||
 =================================================================================================================================*/
public class SortTime
{   
    public static void main( String[] argv )
    {
        if (argv.length < 2) // not enough arguments
            throw new IllegalArgumentException("java SortTime \"max_test_size\" \"sort_number\"");
        
        // size of test from user
        final int max = Integer.parseInt( argv[0] );
        final int s = Integer.parseInt( argv[1] );
        
        
           int REPS = 1000,          // average a few results (1000 for reps under 1000, decreases to 100 later)
            TRIGGER = REPS/10,   // number of "warmup" reps
            SEED = 123579;       // seed for random num generator
        
        String sortName = sort_name(s); // will return the sort algo's name
        java.util.Random gen = new java.util.Random(SEED);
        
        System.out.printf("=====%s Sort=====\n", sortName);
        System.out.printf("Size\tTime\n");
        
        int n = 100; // starting size array... will increment
        
        while (n <= max) // continue until array size is at max indicated
        {
            if (n > 0) {
                
                if (n == 1000) // decrease REPS since now at array sizes of 1000
                {
                    REPS = 100;
                    TRIGGER = REPS/10;
                } // end if
                
                
                // create random data -- REPS arrays each of size n.
                int[][] data = new int[REPS][n];
                for (int j = 0; j < REPS; ++j)
                    for (int i = 0; i < n; ++i)
                    data[j][i] = gen.nextInt();
                
                long starttime = 0;
                
                for (int j = 0; j < REPS; ++j) {
                    // 10% of time is to warm up the compiler, 90% of time is test time.
                    if (j == TRIGGER) {
                        java.lang.System.gc();
                        java.lang.System.runFinalization();
                        starttime = java.lang.System.currentTimeMillis(); // start clock
                    }
                    
                    // Perform the sorting!
                    switch (s)
                    {
                        case 0: // robo sort
                            robo_reach_sort(data[j],n);
                            sortName = "Robo";
                            break;
                            
                        case 1: // bubblesort
                            bubble_sort(data[j],n);
                            sortName = "Bubble";
                            break;
                            
                        case 2: // insertion sort
                            insert_sort(data[j],n);
                            sortName = "Insertion";
                            break;
                            
                        case 3: // heapsort
                            heap_sort(data[j],n);
                            sortName = "Heap";
                            break;
                            
                        case 4: // mergesort
                            merge_sort(data[j],n);
                            sortName = "Merge";
                            break;
                            
                        case 5: // quick sort
                            quick_sort(data[j],n);
                            sortName = "Quick";
                            break;
                            
                            
                        default:
                            System.exit(0);
                            break;
                            
                    } // end switch
                    
                    
                } // end for loop
                
                
                long endtime = java.lang.System.currentTimeMillis(); // stop clock
                System.out.print(n+"\t"+(endtime-starttime)/(float)(REPS - TRIGGER) + "\n");
                
                // check the postcondition -- you write the predicate.
                for (int j = 0; j < REPS; ++j)
                    assert is_sorted(data[j], 0, n);
            } // end if statement
            
            
            if (n < 10000)
                n += 100;
            else
                n += 1000;
            
        } // end while
    } // end main
    
    
    /*=================================================================================================================================
     |  static boolean is_sorted (int[] data, int in, int n)
     |
     |        Purpose: This function goes through the given array and makes sure that all is sorted.
     |
     *=================================================================================================================================*/
    static boolean is_sorted(int[] data, int in, int n)
    {
        for (int i = 1; i < data.length; i++)
        {
            if (data[i-1] > data[i])
                return false;
        }
        
        return true;
    } // end is_sorted
    
    
    /*=================================================================================================================================
     |  static String sort_name (int s)
     |
     |        Purpose: Simply returns the name of the sorting algo to be used as a string.
     |
     *=================================================================================================================================*/
    static String sort_name (int s)
    {
                switch (s)
                    {
                        case 0: // robo sort
                            return "Robo";
                            
                        case 1: // bubblesort
                            return "Bubble";
                            
                        case 2: // insertion sort
                            return "Insertion";
                            
                        case 3: // heapsort
                            return "Heap";
                            
                        case 4: // mergesort
                            return "Merge";
                            
                        case 5: // quick sort
                            return "Quick";
                            
                            
                        default:
                            System.out.println("Number indicated does not correspond to sorting algo. Exiting.");
                            System.exit(0);
                            break;
                            
                    } // end switch
                return "";
    } // end sort_name
    
    
    
    // SORT METHODS BEGIN HERE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /*=================================================================================================================================
     |  static void robo_reach_sort (int[] array, int length)
     |
     |        Purpose: Sorts the input array using the extra-terrestrial robo reach sort.
     |
     *=================================================================================================================================*/
    static void robo_reach_sort (int[] array, int length)
    {   
        int robotic_arm_size=1;
        // extend robotic arm
        while (robotic_arm_size <= length) 
            robotic_arm_size = 1+3*robotic_arm_size;
        // sort with a reach defined by the robotic arm
        while (robotic_arm_size > 2) {
            // retract arm
            robotic_arm_size /= 3;
            for (int i = 1+robotic_arm_size; i <= length; ++i) {
                int claw_grasp = array[i-1], j = i;
                while (array[j-robotic_arm_size-1] > claw_grasp) { 
                    array[j-1] = array[j - robotic_arm_size - 1];
                    j -= robotic_arm_size;
                    if (j <= robotic_arm_size) break;
                }                       
                array[j-1] = claw_grasp;
            }                   
        }                       
    }  // end robo_reach_sort
    
    
    /*=================================================================================================================================
     |  static void bubble_sort (int[] array, int length)
     |
     |        Purpose: Bubble sort is done below. This sorting algoirthm is terrible.
     |                 coded using my brain then referring to Shaffer.
     |
     *=================================================================================================================================*/
    static void bubble_sort (int[] array, int length)
    {
        int tmp = 0;
        
        for (int i = 0; i < length-1; i++)
            for (int j = 1; j < length-i; j++)
        {
            if (array[j-1] > array[j])
            {
                tmp = array[j-1];
                array[j-1] = array[j];
                array[j] = tmp;
            }
        } // end for
        
    } // end bubble_sort
    
    /*=================================================================================================================================
     |  static void insert_sort (int[] array, int length)
     |
     |        Purpose: This algorithm is insertion sort. At first was going to use my own implementation but ultimately
     |                 resorted to using Shaffer's.
     |
     *=================================================================================================================================*/
    static void insert_sort (int[] array, int length)
    {
        int tmp = 0;
        
        for (int i = 1; i < length; i++)
            for (int j = i; (j > 0) && (array[j] < array[j-1]); j--)
        {
            tmp = array[j];
            array[j] = array[j-1];
            array[j-1] = tmp;
        }
    } // end insert_sort
    
    /*=================================================================================================================================
     |  static void heap_sort (int[] array, int length)
     |
     |        Purpose: This algorithm uses heap sort to sort the array that is passed in.
     |
     *=================================================================================================================================*/
    static void heap_sort (int[] array, int length)
    {
        HeapSort.heap_sort_master(array, length);
    } // end heap_sort
    
    /*=================================================================================================================================
     |  static void merge_sort (int[] array, int length)
     |
     |        Purpose: This algorithm uses merge sort to sort the given array.
     |
     *=================================================================================================================================*/
    static void merge_sort (int[] array, int length)
    {
        MergeSort.merge_sort_master(array);
    } // end merge_sort
    
    /*=================================================================================================================================
     |  static void quick_sort (int[] array, int length)
     |
     |        Purpose: This algorithm uses quick sort to sort the given array.
     |
     *=================================================================================================================================*/
    static void quick_sort (int [] array, int length)
    {
        QuickSort.quick_sort_master(array, 0, length-1);
    } // end quick_sort
    
    
    // SORT METHODS END HERE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    
} // end class SortTime