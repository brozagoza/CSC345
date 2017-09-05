/*=======================================================================================================================================
 |   Assignment:  Program #3: Pastiche
 |       Author:  Alejandro Zaragoza & Andrew Predoehl (supplied template code)
 |       Course:  CSC 345
 |
 |   Instructor:  Andrew Predoehl
 |     Due Date:  6:00AM July 6, 2016
 |
 |  Description:  This program reads in a text file and stores the words from the text file as bigrams (pairs of words, essentially).
 |                These bigrams are stored in a 'Treap' data structure to implement a new data structure taught in class and to gain
 |                practice writing the code for and using it. This program then takes in a single word from the command line following
 |                a '--generate' flag that will print out a pastiche given this word. The program will continue to print out bigrams
 |                starting with the given word and randomly selecting pairs until either 10,000 words have been produced or the final
 |                word of the text file was printed. An optional delete text file may be included as well. This text file will contain
 |                words that the user wishes to remove from the collection of bigrams. This results in the printed pastiche not including
 |                and of the requested deleted words.
 |                
 | Operational :  Program must be ran with a few arguments as follows:
 | Requirements   java -ea Pastiche --insert "TEXT FILE" (optional) --delete "TEXT FILE" (optional) --generate "WORD"
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
    private class Node {
        String key; // first word of a bigram
        ArrayList<String> nextword; // possible second words
        
        Node left, right, parent;
        int priority;
        
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
    
} // end Treap


class Pastiche {
    
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
    
    
    
    // Extra credit:  eliminate bigrams involving words in given file.
    private static void delete_words(Treap tree, String filename, ArrayList<String> banned_words)
        throws java.io.FileNotFoundException, java.io.IOException
    {
        // Open file for reading
        FileReader in = new FileReader(filename);
        BufferedReader buff = new BufferedReader(in);
        
        
        for (String word = buff.readLine(); word != null; word = buff.readLine())
            for (StringTokenizer tok = new StringTokenizer(word); tok.hasMoreTokens(); )
        {
            String token = tok.nextToken();
            
            tree.delete(token);      // remove the node from the tree
            banned_words.add(token); // add current word to banned_words
        }
        
    } // end delete_words
    
    private static void generate_pastiche(Treap tree, String key, int count, ArrayList<String> banned_words)
    {
        
        for (int j = 0; j < count; ++j) {
            // Throw in some newlines just to show mercy on the user's eyes.
            if (0 == j % 10)
                System.out.println();
            
            // Print the next word of the pastiche.
            System.out.print(key + " "); 
            // Find a successor word.
            String suc = tree.random_successor(key, banned_words);
            if (0 == suc.length())
                break; // natural end to the pastiche
            
            // Save it for next time.
            key = suc;
        }
        System.out.println();
    } // end generate_pastiche
    
    public static void main(String[] argv)
        throws java.io.FileNotFoundException, java.io.IOException
    {
        
        Treap tree = new Treap();
        final int DELETE=0, INSERT=1, GENERATE=2, P_MAX_SIZE = 10000;
        int mode = -1;
        ArrayList<String> banned_words = new ArrayList<String>();
        
        // Process each command-line argument:
        for (int j = 0; j < argv.length; ++j) {
            
            // Mode switch in response to a flag:
            if (0 == argv[j].compareTo("--delete"))
                 mode = DELETE;
            else if (0 == argv[j].compareTo("--insert"))
                mode = INSERT;
            else if (0 == argv[j].compareTo("--generate"))
                mode = GENERATE;
            
            // Tree operation (insert, delete, generate)
            else if (DELETE == mode)
                delete_words(tree, argv[j], banned_words);
            else if (INSERT == mode)
                insert_words(tree, argv[j]);
            else if (GENERATE == mode)
                generate_pastiche(tree, argv[j], P_MAX_SIZE, banned_words);
            
            // Failure -- no mode specified.
            else {
                System.err.println("Must specify an --insert or --delete "
                                       + "flag before a filename,\n"
                                       + "and a --generate flag before a start word.");
                System.exit(1);
            }
        }
    }
}

