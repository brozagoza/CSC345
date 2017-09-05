/*=======================================================================================================================================
 |   Assignment:  Program #4: Bigram Filler
 |       Author:  Alejandro Zaragoza & Andrew Predoehl (supplied template code)
 |       Course:  CSC 345
 |
 |   Instructor:  Andrew Predoehl
 |     Due Date:  6:00AM July 19, 2016
 |
 |  Description:  This is a build on from project 3. Now it fills the words in between.
 |                
 | Operational :  Program must be ran with a few arguments as follows:
 | Requirements   
 |
 |     Compiler:  Compiled with javac 1.6
 |
 | Deficencies :  none
 |                
 *=====================================================================================================================================*/
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.LinkedList;

/*=================================================================================================================================
 |  class Treap
 |
 |        Purpose: This class is a representation of a treap. It contains multiple methods to be used by the pastiche program in
 |                 general. This class also includes extra methods such as insert_bigram, random_successor, and delete to be used
 |                 in accordance to the Pastiche program.
 |
 *=================================================================================================================================*/
class Treap {
    
    /*=================================================================================================================================
     |  private class Node
     |
     |        Purpose: Representation of a node. Contains the key as a string and an ArrayList that contains the connectecting 
     |                 bigrams. Also contains references to the children and it's parent. Contains a priority variable to be used
     |                 in accordance with the max-heap properties.
     |
     *=================================================================================================================================*/
    public class Node {
        String key; // first word of a bigram
        ArrayList<String> nextword; // possible second words
        
        Node left, right, parent;
        int priority;
        
        /*
         * NEW PRIMITIVES FOR PROJECT 4
         */
        boolean visited; // indicates this node was visited
        Node graphParent; // parent in terms of BFS
        
    } // end class Node
    
    public Node root;
    
    /*=================================================================================================================================
     |  public String random_successor (String first)
     |
     |        Purpose: Takes in a key representing a node in the treap. Locates this node, then from its collection of 'nextwords'
     |                 chooses one at random and returns this string. If the delete flag is indicated when running this program, then
     |                 a successor that is not a banned_word will be located from the collect of 'nextwords.' If no non banned words
     |                 are available successors, then the empty string is returned for the program to end.
     |
     |     Parameters: Strings first - the string of the node to search for successors with
     |                 ArrayList<String> banned_words - a collection of the banned words as indicated from runtime
     |
     *=================================================================================================================================*/
    public String random_successor(String first, ArrayList<String> banned_words) {
        Node nd = exists(root, first);
        Random randy = new Random();
        int r = 0;
        boolean deleted_word = true;
        
        if (nd == null)
            throw new IllegalArgumentException(first+" does not exists in the text.");
        
        if (banned_words.size() == 0) // usual return statement without --delete flag
            return nd.nextword.get( randy.nextInt( nd.nextword.size() ));
        
        // if delete flag indicated, find non-banned successor
        while (deleted_word)
        {
            if (nd.nextword.size() == 0)
                break;
            
            r = randy.nextInt(nd.nextword.size());
            deleted_word = false;
            
            for (int i = 0; i < banned_words.size(); i++)
            {
                if (nd.nextword.get(r).compareTo( banned_words.get(i) ) == 0) // if the word matches a banned_word
                {
                    nd.nextword.remove(r);
                    deleted_word = true;
                    break;
                }
            } // end for
        } // end while
        
        if (nd.nextword.size() == 0) // if key has no usable successors, return empty string
            return "";
        
        return nd.nextword.get(r);
    } // end random_successor
    
    /*=================================================================================================================================
     |  public void add_next_word (Node nd, String second)
     |
     |        Purpose: The string second is taken in to get appended to the arraylist of the node nd. If that string already exists
     |                 then it isn't appended since this would cause issues for deletion later.
     |
     |     Parameters: Node nd - the node with the nextword ArrayList to be added to
     |                 String second - the string to be added to the list
     *=================================================================================================================================*/
    private void add_next_word(Node nd, String second)
    {
        for (int i = 0; i < nd.nextword.size(); i++)
            if ( second.compareTo( nd.nextword.get(i) ) == 0) // since the word already exists, simply return without appending
            return;
        
        nd.nextword.add(second);
    } // end add_next_word
    
    /*=================================================================================================================================
     |  public void insert_bigram (String first, String second)
     |
     |        Purpose: Reads in two strings: 'first' being the owner of the node and 'second' being the word paired with it. Creates
     |                 a new node if 'first' does not already have a node in the treap. If it does, then that node is simply located
     |                 and 'second' is appended to it's list of nextwords. If parameter 'second' is the empty string, that denotes the
     |                 special end-of-text bigram.
     |
     |       Precondition: Parameter 'first' must not be the empty string.
     |
     *=================================================================================================================================*/
    public void insert_bigram(String first, String second) {
        
        Node this_bigram = exists(root, first); // Checks to see if node with matching 'first' already exists
        
        if (this_bigram == null)                // If node does not already exists, create a new node
        {
            this_bigram = new Node();
            this_bigram.priority = Math.abs(( new Random() ).nextInt());      // assign the random priority for the treap
            this_bigram.key = first;
            this_bigram.nextword = new ArrayList<String>();      
            add_next_word(this_bigram, second);                                    // append to the collection of nextwords
            
            if (root != null)
                append(root, this_bigram);           // append to the treap data structure
            else
                root = this_bigram;
        }
        else
            add_next_word(this_bigram, second);   // since node already exists, append to 'nextword' collection
        
        
    } // end insert_bigram
    
    /*=================================================================================================================================
     |  public void delete (String first)
     |
     |        Purpose: Reads in the string of the node that must be deleted from the treap. Locates the node and sets it priority to
     |                 -1 so that it may be rotated into a leaf. Once it is a leaf, it is removed from the treap.
     |
     |
     *=================================================================================================================================*/
    public void delete(String first)
    {
        Node nd = exists(root, first); // find the node in the treap
        
        if (nd == null) // node doesn't exist
            return;
        
        nd.priority = -1; // small priority
        
        // While loop; continue to do rotations until it is a leaf and deleted
        while (true)
        {
            if (nd.left != null && nd.right != null) // if has two children, rotate with which has higher priority
            {
                if (nd.left.priority > nd.right.priority)
                    rotate(nd.left);
                else
                    rotate(nd.right);
                
            } // end if
            
            else if (nd.left == null && nd.right != null) // rotate with only child
                rotate(nd.right);
            
            else if (nd.left != null && nd.right == null) // rotate with only child
                rotate(nd.left);
            
            else // it is now a leaf, remove any connection to it so it is "deleted"
            {
                if (nd.parent.left == nd)
                    nd.parent.left = null;
                else if (nd.parent.right == nd)
                    nd.parent.right = null;
                
                break;
            } // end else
        } // end while loop
        
    } // end delete
    
    /*=================================================================================================================================
     |  private Node exists (Node comp, String first)
     |
     |        Purpose: Takes in a node to compare it's string key to the string being read in as 'first.' If the string of the comp 
     |                 node matches the 'first' string, then that node is returned. If when being compared 'first' is less than the key, 
     |                 this method is recursively called on comp's left subtree. If greater, the same goes for the right subtree. Null
     |                 will be returned when the node does not exist.
     |
     |       Precondition: Parameter 'comp' must not be null, eitherwise this may indicate empty treap.
     |
     *=================================================================================================================================*/
    private Node exists(Node comp, String first)
    {
        if (comp == null) // Precondition
            return null;
        
        if (comp.key.compareTo(first) == 0)     // 'first' string already has a node in the treap
            return comp;
        else if (comp.key.compareTo(first) > 0) // Node may reside in left subtree
            return exists(comp.left, first);
        else if (comp.key.compareTo(first) < 0) // Node may reside in right subtree
            return exists(comp.right, first);
        
        return null;
    } // end exists
    
    
    /*=================================================================================================================================
     |  private void append (Node comp, Node nd)
     |
     |        Purpose: Takes in a node to be compared to as 'comp', and a new node to be appended to the treap data structure as 'nd'. 
     |                 This method appends 'nd' as a leaf to the treap following BST's guidelines. After appending as a leaf, 'rotate'
     |                 is called to rotate the nodes according to treap guidelines if neccessary.
     |
     |       Precondition: Parameter 'nd' must not be null.
     |
     *=================================================================================================================================*/
    private void append(Node comp, Node nd)
    {
        if (nd == null) // Precondition
            return;
        
        if (nd.key.compareTo(comp.key) < 0) // nd is less than comp
        {
            if (comp.left != null) // sub tree exists, therefore recursively call on left subtree's root
                append (comp.left, nd);
            else
            {
                comp.left = nd;
                nd.parent = comp;
                
                rotate(nd); // rotate the node to follow treap max heap priority
            }
        } // end if
        
        else if (nd.key.compareTo(comp.key) > 0) // nd is greater than comp
        {
            if (comp.right != null) // sub tree exists, therefore recursively call on right subtree's root
                append (comp.right, nd);
            else
            {
                comp.right = nd;
                nd.parent = comp;
                
                rotate(nd); // rotate the node to follow treap max heap priority
            }
        } // end else if
        
        
    } // end append
    
    /*=================================================================================================================================
     |  private void rotate (Node nd)
     |
     |        Purpose: Takes in a node nd and rotates it with it's parent. Depending whether it is a left or right child, the
     |                 corresponding subtree gets rotated as so. If the parent's priority is larger, then no need to rotate.
     |
     |       Precondition: Parent of node 'nd' must not be null
     |
     *=================================================================================================================================*/
    private void rotate (Node nd)
    {
        
        if (nd.parent == null) // no need to rotate since no parent
            return;
        
        
        if (nd.parent.priority >= nd.priority) // no rotation neccessary
            return;
        
        Node subleft = nd.left,
            subright = nd.right,
            exParent = nd.parent,
            restOfTree = exParent.parent;
        
        if (nd.parent.left == nd) // right rotation
        {
            exParent.left = subright;
            exParent.parent = nd;
            nd.right = exParent;
            nd.parent = restOfTree;
            
            if (subright != null)
                subright.parent = exParent;
            
        } // end if
        else if (nd.parent.right == nd) // left rotation
        {
            exParent.right = subleft;
            exParent.parent = nd;
            nd.left = exParent;
            nd.parent = restOfTree;
            
            if (subleft != null)
                subleft.parent = exParent;
            
        } // end else if
        
        
        if (restOfTree != null && restOfTree.left == exParent)
            restOfTree.left = nd;
        
        else if (restOfTree != null && restOfTree.right == exParent)
            restOfTree.right = nd;
        
        else if (restOfTree == null)
            root = nd;
        
        rotate(nd); // calls again incase there needs to be another rotation
        
        
        
    } // end rotate
    
    /*=================================================================================================================================
     |  private void set_unvisited
     |
     |        Purpose: Resets the neccessary visited pointer to false so that a new BFS can be done.
     |
     |       Precondition: visited may be true
     |
     *=================================================================================================================================*/
    private void set_unvisited (Node nd)
    {
        if (nd == null)
            return;
        
        nd.visited = false;
        
        set_unvisited(nd.left);
        set_unvisited(nd.right);
        
    } // end set_unvisited
    
    
    /*=================================================================================================================================
     |  public void BFS (String in, String target)
     |
     |        Purpose: Finds the shortest path from the 'in' string to the 'target' string using BFS.
     |
     |       Precondition: The two "names" must exist in the text that was given.
     |
     *=================================================================================================================================*/
    public void BFS(String in, String target)
    {
        Node nd = exists(root, in);
        
        if (nd == null)
        {
            System.err.printf("%s doesn't exist in the text.\n", in);
            System.exit(-1);
        }
        
        if (exists(root, target) == null)
        {
            System.err.printf("%s doesn't exist in the text.\n", target);
            System.exit(-1);
        }
        
        set_unvisited(root); // reset the nodes to unvisited
        
        LinkedList<Node> q = new LinkedList<Node>(); // representation of the queue
        nd.visited = true; // current node has been visited
        nd.graphParent = null;
        q.add(nd); // add the starting place 
        
        
        while (q.size() != 0)
        {
            
            Node w = q.remove();
            
            for (int i = 0; i < w.nextword.size(); i++)
            {
                Node temp = exists(root, w.nextword.get(i)); // find the node in the treap
                
                if (temp == null)
                    continue;
                
                if (temp.visited == false)
                {
                    temp.visited = true;
                    temp.graphParent = w;
                    
                    if (temp.key.compareTo(target) == 0) // target was found
                    {
                        printPath(temp); // print the path
                        return;
                    } // end if
                    
                    q.add(temp);
                }
                
            } // end for loop
            
        } // end while
        
        
        
    } // end BFS
    
    /*=================================================================================================================================
     |  private void printPath (Node nd)
     |
     |        Purpose: Simply prints the path starting at the destination node.
     |
     |       Precondition: 
     |
     *=================================================================================================================================*/
    private void printPath (Node nd)
    {
        Node temp = nd;
        ArrayList<String> t = new ArrayList<String>();
        
        while (temp != null)
        {
            t.add(temp.key);
            temp = temp.graphParent;
        } // end while
        
        for (int i = t.size() - 1; i >= 1; i--) // prints all the words in the path except the starter
            System.out.printf("%s ", t.get(i));
        
    } // end printPath
    
    
    
} // end Treap


class Superseq {
    
    // Insert bigrams from file named in "filename" into tree.
    private static void insert_words(Treap tree, String filename)
        throws java.io.FileNotFoundException, java.io.IOException
    {
        // Open file for reading
        FileReader f = new FileReader(filename);
        BufferedReader b = new BufferedReader(f);
        //System.err.println("reading " + filename);
        String previous_token = "";
        boolean first_word = true;
        
        // Read each line l from file, then each word from line l
        for (String l = b.readLine(); l != null; l = b.readLine())
            for (StringTokenizer t = new StringTokenizer(l); t.hasMoreTokens(); ) {
            String token = t.nextToken();
            if (first_word)
                first_word = false;
            else
                tree.insert_bigram(previous_token, token);
            previous_token = token;
        }
        
        // Last word on last line is also valid, and naturally ends the pastiche.
        tree.insert_bigram(previous_token, "");
        //tree.printTree(tree.root);
    } // end insert_words
    
    
    /*
     * This method is used to fill the words that will connect the arguments given
     */
    private static boolean fill_words(Treap tree, String [] argv, int j)
    {
        
        
        for (;j < argv.length-1; j++)
        {
            if (argv[j].compareTo(argv[j+1]) == 0) // duplicates in a row
            {
                System.err.printf("Duplicates\n");
                System.exit(-1);
            }
            
            tree.BFS(argv[j], argv[j+1]);
        } // end for
        
        System.out.printf("%s\n", argv[j]);
        
        return true;
    }
    
    
    public static void main(String[] argv)
        throws java.io.FileNotFoundException, java.io.IOException
    {
        Treap tree = new Treap();
        final int FILL=0, INSERT=1;
        int mode = INSERT;
        for (int j = 0; j < argv.length; j++) {
            if (0 == argv[j].compareTo("--fill"))
                mode = FILL;
            else if (0 == argv[j].compareTo("--insert"))
                mode = INSERT; // not neccessary. but harmless
            else if (INSERT == mode)
                insert_words(tree, argv[j]);
            else if (FILL == mode) {
                boolean success = fill_words(tree, argv, j);
                //if (!success)
                //  System.exit(1);
                j = argv.length; // it eats all remaining arguments
            }
            else {
                System.err.println("unreachable code");
                assert false;
            }
        }
    } // end main
}

