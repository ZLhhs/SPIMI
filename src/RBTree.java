

public class RBTree {
    // class for RBTree
	
	int RBTreeNodeCount = 0;
	int RBTreeNodeMaxSize = 99999;
	RBTreeNode RBTreeRoot = null;
	
		

	
	public RBTreeNode createNode (String targetString, int docID) {
		RBTreeNode point = new RBTreeNode();
		point.color = true; // red
		point.left = null;
		point.right = null;
		point.parent = null;
		point.key = new indexHeadNode();
		point.key.targetString = targetString;
		point.key.docFrequent = 1;
		point.key.allWordFrequent = 1;
		point.key.p = new indexListNode();
		point.key.last = point.key.p;
		point.key.p.next = null;
		point.key.p.docID = docID;
		point.key.p.docWordFrequent = 1;
		return point;
	}
	
	public RBTreeNode rightRotate (RBTreeNode p) {
		RBTreeNode parent = p.parent;
	
		
		if (parent.parent != null) { // existence grandparent
			RBTreeNode grandParent = parent.parent;
			if (grandParent.left == parent) {
				grandParent.left = p;//1
			}
			else grandParent.right = p; // 1
			p.parent = grandParent; //3
		}
		else { // don't have a grandParent
			p.parent = null; // 3
			RBTreeRoot = p; // 3
		}
		parent.parent = p; // 2
		if (p.right != null) { // p existence right node
			RBTreeNode right = p.right;
			right.parent = parent; // 6
			parent.left = right; // 4
			
		}
		else { // not have a right node
			parent.left = null; //4 
		}
		p.right = parent; // 5
		//////////// for color///////
		p.color = false;
        parent.color = true;
        if (p.left!=null) p.left.color = true;
        return p;
	}
	
	public RBTreeNode leftRotate (RBTreeNode p) {
		RBTreeNode parent = p.parent;
	
		
		if (parent.parent != null) { // existence grandparent
			RBTreeNode grandParent = parent.parent;
			if (grandParent.left == parent) {
				grandParent.left = p;//1
			}
			else grandParent.right = p; // 1
			p.parent = grandParent; //3
		}
		else { // don't have a grandParent
			p.parent = null; // 3
			RBTreeRoot = p; // 3
		}
		parent.parent = p; // 2
		if (p.left != null) { // p existence left node
			RBTreeNode left = p.left;
			left.parent = parent; // 6
			parent.right = left; // 4
			
		}
		else { // not have a right node
			parent.right = null; //4 
		}
		p.left = parent; // 5
		//////////// for color///////
		p.color = false;
        parent.color = true;
        if (p.right!=null) p.right.color = true;
        return p;
	}
	
	
	public void RBTreeAdjust (RBTreeNode p) { // p point the new node
		if (p==null) 
			System.out.println("-----error-----");
		if (p.parent == null) // root
		{   p.color = false;         return;   }
		if (p.color == false)        return;
		if (p.parent.color==false)   return;
		
		assert 1==2;   assert p.color==true && p.parent.color==true;
		assert p.parent.parent!=null; // must have a grandparent
		// p is red and p.parent is red so we need adjust
		// p.color == p.parent.color
		RBTreeNode parent = p.parent;
		RBTreeNode grandParent = parent.parent;
		// left
		if (grandParent.left == parent ) {
			// case p is left
			/*      O           O
			       / \         / \
			      O   ?       O   O <- red? black?
			     / \         /
		    p-> O   O <-p   O
			*/
			if (grandParent.right!=null) {
				// case p is left and right is not null
				if (grandParent.right.color == true) {// true is red
					// case p is left and right is not null is red
					// black down;
					parent.color = false;// black
					grandParent.right.color=false;
					grandParent.color = true;
					p = grandParent;
					RBTreeAdjust(p);// adjust grandparent;
				}
			}
			else { // whatever right is black or just null,must do right-rotate
                if (parent.right == p)
                	p = leftRotate(p);
                   /* 
                    O                       O              O <-p
                   /                       /              / \
                  O           =>          O <-p    =>    O   O
                   \                     /   
                    O <-p               O  */
                else p = parent;
                p = rightRotate(p); // be careful p or p.parent
                ///////////////////////RBTreeAdjust(p);
			}
		} // if 
		else if (grandParent.right == parent ) { // right
			// case p is left
			/*      O           O
			       / \         / \
			      ?   O       O   O 
			         / \         / \
		            O   O       O   O
			*/
			if (grandParent.left!=null) {
				// case p is right and left is not null
				if (grandParent.left.color == true) {// true is red
					// case p is right and left is not null is red
					// black down;
					parent.color = false;// black
					grandParent.left.color=false;
					grandParent.color = true;
					p = grandParent;
					RBTreeAdjust(p);
				}
			}
			else { // whatever left is black or just null,must do left-rotate
                if (parent.left == p)
                	rightRotate(p);
                   /* 
                    O                       O                     O <-p
                     \                       \                   / \
                      O           =>          O <-p      =>     O   O
                     /                         \
                    O <-p                       O  */
                else p = parent;
                p = leftRotate(p); // be careful p or p.parent
                ////////////////////////RBTreeAdjust(p); // need ?
			}
		}
	}
	
	public void insertNode (String targetString, int docID) {
		RBTreeNode p = RBTreeRoot;
		if (p == null) { // first node, just the root
			RBTreeRoot = createNode (targetString, docID);
		    RBTreeRoot.color = false; // true is red,false is black
		}
		else { // not the first one
			while ( true ) {
				int compare = p.key.targetString.compareTo(targetString);
				if ( compare == 0 ) { // existence and get
					addList(p, docID);
					break;
				}
				else if ( compare >0 ) { // go left
					if (p.left == null) { // left is null
						p.left = createNode(targetString, docID);
						p.left.parent = p;//createNode and copy key value;
						p = p.left;
						//if (p.color == p.parent.color) 
						RBTreeAdjust(p); 
						    // adjust the red black tree,keep p point Node
						break;
					}
					else p = p.left;
				} 
				else { // compare > 0 ,should go right
					if (p.right == null) {
						p.right = createNode(targetString, docID);//createNode and copy key value;
						p.right.parent = p;
						p = p.right;
						//if (p.color == p.parent.color)
						RBTreeAdjust(p); 
						    // adjust the red black tree,keep p point Node
						break;
					}
					else p = p.right;
				}
			} // while (true)
			
		} // else  (insert a node)
	
	} // public insert function
	

	public void addList(RBTreeNode p, int docID) {
		p.key.allWordFrequent ++;
		if (p.key.last.docID == docID) { 
			// come from the same file,don't need to create new node
			p.key.last.docWordFrequent++;
		}
		else {  // we need create new node
			p.key.docFrequent ++ ;
			p.key.last.next = new indexListNode();
			p.key.last = p.key.last.next;
			p.key.last.next = null;
			p.key.last.docID =docID;
			p.key.last.docWordFrequent = 1;
		}
	}
	
    public void midVisit(RBTreeNode p) {
    	if (p != null) {
    		midVisit(p.left);
    		System.out.print(p.key.targetString+",");
    		/*indexListNode x = p.key.p;
    		for (int i = 0 ; i<p.key.docFrequent; i++) {
    			System.out.println(x.docID);
    			x = x.next;		
    		}*/
    		midVisit(p.right);
    	}
    }
	
    public void firstVisit(RBTreeNode p) {
    	if (p != null) {
    		System.out.print(p.key.targetString+",");
    		firstVisit(p.left);
    		/*indexListNode x = p.key.p;
    		for (int i = 0 ; i<p.key.docFrequent; i++) {
    			System.out.println(x.docID);
    			x = x.next;		
    		}*/
    		firstVisit(p.right);
    	}
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        RBTree RBT = new RBTree();
        int testnum = 6;
        String [] testString = new String[testnum];
        testString[0] = "1";testString[1] = "2";
        testString[2] = "2";testString[3] = "1";
        testString[4] = "3";testString[5] = "3";
        //testString[6] = "de";testString[7] = "de";
        int [] testID = new int [testnum];
        testID[0] = 1;testID[1] = 1;testID[2] = 1;
        testID[3] = 1;testID[4] = 1;testID[5] = 2;
        //testID[6] = 2;testID[7] = 2;
        
        
        for (int i = 0 ; ; i++) {
        	int x = (int)(Math.random()*2000000000);
        	RBT.insertNode(Integer.toString(x), 1);
        	//RBT.midVisit(RBT.RBTreeRoot);
        	System.out.println(i);
        	//RBT.firstVisit(RBT.RBTreeRoot);
        	//System.out.println("\n----------");
        }
        //System.out.println("create ok~");
        //RBT.midVisit(RBT.RBTreeRoot);
	}

}


class RBTreeNode {
	boolean color = true; // true == red color
	RBTreeNode left = null;
	RBTreeNode right = null;
	RBTreeNode parent = null;
	indexHeadNode key = null;
}

class indexHeadNode {
	String targetString = "";
	int docFrequent = -1;
	int allWordFrequent = -1;
	indexListNode p = null;
	indexListNode last = null;
}

class indexListNode {
	int docID = -1;
	int docWordFrequent = -1;
	indexListNode next = null;
}