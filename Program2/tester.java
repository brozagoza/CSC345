public class tester
{
    
    public static int funct(int in)
    {
        return (2*in + 5)%11;
    }
    
    public static int [] counting(int [] in, int mod)
    {
        int [] c = new int[8];
        
        
        for (int i = 0; i < in.length; i++)
        {
            System.out.println(in[i] % mod+" for "+in[i]);
            //c[ in[i]%mod ] ++;
            
        } // end for loop
        
        for (int i = 0; i < c.length; i++)
            System.out.println(c[i]);
        
        return in;
    }
    
    
    public static void main (String [] args)
    {
        int [] arr = new int[11];
        arr = {2503, 5057, 6352, 2720, 5134, 2203, 6606, 0166, 3353, 7675, 2746};
        counting(arr, 10);
        
        //counting(
    }
} // end tester
