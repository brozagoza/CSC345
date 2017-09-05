/* Driver class, has a main()),
 * reads from command line and computes the answer.
 */
public class Fibonacci {

    /* Fibonacci computation */
    private static LLInt fastFibo( int n )
        throws InstantiationException
    {
        if (n < 0)
            throw new IllegalArgumentException("neg. Fibonacci");

        LLInt u0 = new LLInt(0), u1 = new LLInt(1);
        return new Mat2x2( u0,u1,u1,u1 ).pow(n).get12();
    }

    /* User interface. */
    public static void main( String[] argv )
        throws InstantiationException
    {
        int n = Integer.parseInt( argv[0] );
        System.out.println( "Result for " + n + " = " + fastFibo(n) );
    }
}