import java.math.BigInteger;    // used for BigIntegers
/*=================================================================================================================================*
 ||
 ||  Class LLInt 
 ||
 ||         Author:  Alejandro Zaragoza
 ||        
 ||          Class:  CSC 345
 ||
 ||        Purpose:  Represents an integer as a collection of linked-list nodes.
 ||
 |+-----------------------------------------------------------------------
 ||
 ||      Constants:  RADIX - dictates the range of number to be stored per node
 ||
 |+-----------------------------------------------------------------------
 ||
 ||   Constructors:  LLInt (int in)
 ||                  LLInt (String in)
 ||                  LLInt (LLInt in)
 ||
 ||  Class Methods:  add
 ||                  multiply
 ||                  toString
 ||                  prepend
 ||                  append
 ||
 ||  Inst. Methods:  getHead
 ||                  getTail
 ||
 =================================================================================================================================*/
public class LLInt 
{
    static final int RADIX = 10;             // dictates the amount per node
    private Node head;                       // holds the start of the int
    private Node tail;                       // holds the end of the list
    
   /*=================================================================================================================================
   |  LLInt (int in)
   |
   |        Purpose: Constructor that creats a LLInt object from a given integer.
   |
   *=================================================================================================================================*/
    LLInt (int in)
    {
        if (in < 0)
          throw new IllegalArgumentException("negative input");
        
        head = null;
        tail = null;
        
        String str = "";
        str = str + in;
        
        for (int i = 0; i < str.length(); i++)
        {
            int res = str.charAt(i) - '0';     // parse the char to int
            Node temp = new Node(res);
            append(temp);
        } // end for

                
    } // end int constructor
    
    
   /*=================================================================================================================================
   |  LLInt (String in)
   |
   |        Purpose: Constructor that creats a LLInt object from a given string containing numbers as chars.
   |
   *=================================================================================================================================*/
    LLInt (String in)
    {
        head = null;
        tail = null;
        
        
        if (RADIX != 10)
            throw new IllegalArgumentException("RADIX doesn't equal 10");
        
        for (int i = 0; i < in.length(); i++)
        {
            if (in.charAt(i) < '0' || in.charAt(i) > '9')   // chars that aren't numbers
                throw new NumberFormatException("non-digits found");
            
            int temp = in.charAt(i) - '0';    // parse the char to int
            Node newNode = new Node(temp);
            append(newNode);
            
        } // end for loop
        
    } // end string constructor
    
    
   /*=================================================================================================================================
   |  LLInt (LLInt in)
   |
   |        Purpose: Constructor that creats a LLInt object from a given LLInt object.
   |
   *=================================================================================================================================*/
    LLInt (LLInt in)
    {
        head = in.getHead();
        tail = in.getTail();
    } // end LLInt constructor
    
   /*=================================================================================================================================
   |  public LLInt multiply (LLInt in)
   |
   |        Purpose: This method multiplies this LLInt from the LLInt being passed in.
   |
   *=================================================================================================================================*/
    public LLInt multiply (LLInt in)
    {
        String result = "";              // the result of multiplying
        String arg1 = this.toString();   // this LLint
        String arg2 = in.toString();     // the LLInt passed in
        
        BigInteger num1 = new BigInteger(arg1);
        BigInteger num2 = new BigInteger(arg2);
        BigInteger res = num1.multiply(num2);
        result = result + res;
        
        
        
        
        return new LLInt(result);
    } // end multiply method
    
     
    
   /*=================================================================================================================================
   |  public LLInt add (LLInt in)
   |
   |        Purpose: This method adds this LLInt with the LLInt being passed in.
   |
   *=================================================================================================================================*/
    public LLInt add (LLInt in)
    {
        String result = "";             // hold the result
        String arg1 = this.toString();  // this LLInt
        String arg2 = in.toString();    // the LLInt passed in
        
        BigInteger num1 = new BigInteger(arg1);
        BigInteger num2 = new BigInteger(arg2);
        BigInteger res = num1.add(num2);
        result = result + res;
        
        
        
        return new LLInt(result);
    } // end add method
    
    
   /*=================================================================================================================================
   |  public String toString ()
   |
   |        Purpose: Simply returns this LLInt as a String.
   |
   *=================================================================================================================================*/
    public String toString ()
    {
        String str = "";      // result of toString
        
        if (RADIX != 10)
            throw new IllegalArgumentException("RADIX does not equal 10");
        
        for (Node temp = head; temp != null; temp = temp.getNext())
            str = str + temp.getData();    // append the numbers to the end of the string
        
        return str;
    } // end toString
    
    
    
   /*=================================================================================================================================
   |  public Node getHead () 
   |
   |        Purpose: Simply returns the leading node of this LLInt.
   |
   *=================================================================================================================================*/
    public Node getHead ()
    {
        return head;
    } // end getHead
    
    
    
    /*=================================================================================================================================
   |  public Node getTail () 
   |
   |        Purpose: Simply returns the trailing node of this LLInt.
   |
   *=================================================================================================================================*/
    public Node getTail ()
    {
        return tail;
    } // end getTail
    
    
    
   /*=================================================================================================================================
   |  private void prepend (Node newNode)
   |
   |        Purpose: Appends a new node to the front of the list of digits. To be used by the constructors for building this
   |                 LLInt object.
   |
   *=================================================================================================================================*/
    private void prepend (Node newNode)
    {
        if (head == null)
        {
            newNode.setNext(null);
            newNode.setPrev(null);
            head = newNode;
            tail = newNode;
        }
        else
        {
            newNode.setNext(head);
            newNode.setPrev(null);
            head = newNode;
        }
        
    } // end prepend
    
    
    /*=================================================================================================================================
   |  private void append (Node newNode)
   |
   |        Purpose: Appends a new node to the back of the list of digits. To be used by the constructors for building this
   |                 LLInt object.
   |
   *=================================================================================================================================*/
    private void append (Node newNode)
    {
        if (tail == null)
        {
            newNode.setNext(null);
            newNode.setPrev(null);
            head = newNode;
            tail = newNode;
        }
        else
        {
            newNode.setPrev(tail);
            newNode.setNext(null);
            tail.setNext(newNode);
            tail = newNode;
        }
    } // end append
    
    
} // end LLInt






/* =================================================================
 * This class is used for the individual nodes of the LLInt class.
 ===================================================================*/
class Node
{
    Node next;
    Node prev;
    int data;
    
    /*=================================================================================================================================
   |  Constructor Node (LLInt inData)
   |
   |        Purpose: Creates a node object with the data that is passed in.
   |
   |     Parameters: LLInt inData - the data that this node will contain
   |
   |    Pre-Condition: Data passed in
   |   Post-Condition: Node created
   |
   |        Returns:  (none)
   *=================================================================================================================================*/
    Node (int inData)
    {
        data = inData;
        next = null;
        prev = null;
    } // end Node constructor
    
    /*=================================================================================================================================
   |  LLInt getData ()
   |
   |        Purpose: Return the data of this node.
   |
   |        Returns:  data
   *=================================================================================================================================*/
    public int getData ()
    {
        return data;
    }
    
    /*=================================================================================================================================
   |  Node getNext ()
   |
   |        Purpose: Return the next node this node points to.
   |
   *=================================================================================================================================*/
    public Node getNext ()
    {
        return next;
    } // end getNext
    
    /*=================================================================================================================================
   |  Node getPrev ()
   |
   |        Purpose: Return the previous node this node points to.
   |
   *=================================================================================================================================*/
    public Node getPrev ()
    {
        return prev;
    } // end getNext
    
    /*=================================================================================================================================
   |  void setData ()
   |
   |        Purpose: Reset the data of this node.
   |
   *=================================================================================================================================*/
    public void setData (int in)
    {
        data = in;
    } // end setData
    
    /*=================================================================================================================================
   |  void setNext ()
   |
   |        Purpose: Set the next pointer to the node passed in.
   |
   *=================================================================================================================================*/
    public void setNext (Node in)
    {
        next = in;
    } // end setNext
    
    /*=================================================================================================================================
   |  void setPrev ()
   |
   |        Purpose: Set the prev pointer to the node passed in.
   |
   *=================================================================================================================================*/
    public void setPrev (Node in)
    {
        prev = in;
    } // end setNext
    
} // end Node