package hw4;

import java.util.Random;
import java.util.Stack;

public class Treap<E extends Comparable<E>> {
	public static class Node<E> {
		
		// data fields
		public E data;
		public int priority;
		public Node<E> left;
		public Node<E> right;
		
		// constructors
		/**
		 * Create a new node
		 * @param data - the data stored within the node
		 * @param priority - the priority of the node for sorting 
		 */
		public Node(E data, int priority) {
			if(data == null) {
				throw new IllegalArgumentException("data must not be null");
			}
			else {
				this.data = data;
				this.priority = priority;
				right = null;
				left = null;
			}
		}
		
		//methods
		
		/**
		 * performs a right rotation on the node
		 * @return returns node that is now the root of that subtree
		 */
		Node<E> rotateRight() {
			Node<E> temp = new Node<E>(this.data,this.priority);
			temp.left = this.left;
			temp.right = this.right;
			Node<E> l = this.left;
			
	        temp.left = l.right;
	        l.right = temp;
	        
	        this.data = l.data;
			this.priority = l.priority;
			this.left = l.left;
			this.right = l.right;
			
	        return l;
		}
		
		/**
		 * performs a left rotation on the node
		 * @return returns node that is now the root of that subtree
		 */
		Node <E> rotateLeft() {
			Node<E> temp = new Node<E>(this.data,this.priority);
			temp.left = this.left;
			temp.right = this.right;
			Node<E> r = this.right;
			
		    temp.right = r.left;
		    r.left = temp;
		        
		    this.data = r.data;
		    this.priority = r.priority;
		    this.left = r.left;
		    this.right = r.right;
		    
		    return r;
		}
		
		public String toString() {
			return "(key=" + data.toString() + ", " + "priority = " + Integer.toString(priority) + ")"; 
		}
	}
	
	// data fields
	private Random priorityGenerator;
	private Node<E> root;
	
	// constructors
	/**
	 * creates a new Treap
	 */
	public Treap() {
		priorityGenerator = new Random();
		this.root = null;
	}
	
	/**
	 * creates a new Treap with a seed for its random PriorityGenerator
	 * @param seed - seed for the random priorityGenerator
	 */
	public Treap(long seed) {
		priorityGenerator = new Random(seed);
		this.root = null;
	}
	
	// methods
	
	/**
	 * inserts a node with data field (key) and a random priority to the Treap
	 * @param key - data field of the new node
	 * @return boolean - returns if the node was successfully added to the Treap
	 */
	boolean add(E key) {
		return add(key,this.priorityGenerator.nextInt());
	}

	/**
	 * inserts a node with key as its data and priority as its priority into the Treap
	 * @param key - data field of the new node
	 * @param priority - priority of the new node
	 * @return boolean - returns if the node was successfully added to the Treap
	 */
	boolean add(E key, int priority) {
	    Node<E> newNode = new Node<E>(key,priority);  
	    Stack<Node<E>> nodeStack = new Stack<Node<E>>();
	    Node<E> current = root;  
	    Node<E> trail = null;
	    
	    while (current != null) {
	    	nodeStack.add(current);
	    	trail = current;
	    	if (key.compareTo(current.data) == 0) {
	    		return false;
	    	}
	    	if (key.compareTo(current.data) < 0) {
	    		current = current.left;
	    	}
	    	else {
	    	current = current.right;
	    	}
	    }
	    
	    if (trail == null)  {
	    	trail = newNode;
	    	root = newNode;
	    }
	    else if (key.compareTo(trail.data) < 0) {
	    	trail.left = newNode;
	    }
	    else {
	    	trail.right = newNode; 
	    }
	        
	    reheap(nodeStack);
	    
	    return true;
	}
	
	private void reheap(Stack<Node<E>> nodeStack){
		while(nodeStack.size() > 1) {
			Node<E> current = nodeStack.pop();
			
			if(current.left != null && current.left.priority > current.priority) {
				current.rotateRight();
			}
			if(current.right != null && current.right.priority > current.priority) {
				current.rotateLeft();
			}
		}
		while(nodeStack.size() == 1) {
			Node<E> current = nodeStack.pop();
			
			if(current.left != null && current.left.priority > current.priority) {
				root = current.left;
				current.rotateRight();
			}
			if(current.right != null && current.right.priority > current.priority) {
				root = current.right;
				current.rotateLeft();
			}
		}
	}
	
	/**
	 * deletes a node with a given data value (key)
	 * @param key - data value of the node to be removed
	 * @return boolean - returns if the node was removed from the tree
	 */
	boolean delete(E key) {
		if (!find(key)) {
			//key was not in tree to be removed
			return false;
		}
		if(key.compareTo(root.data) == 0 && root.left == null && root.right == null) {
			root = null;
			return true;
		}
		
		Node<E> current = root;
		Node<E> trail = current;
		
		while(key.compareTo(current.data) != 0) {
			int c = key.compareTo(current.data);
			if(c<0) {
				current = current.left;
			}
			if(c>0) {
				current = current.right;
			}
		}
		
		while(current.left != null || current.right != null) {
			trail = current;
			//System.out.println("entered");
			if(current.right == null) {
				//System.out.println("nothing right");
				current.rotateRight();
				current = current.right;
			}
			else if(current.left == null) {
				//System.out.println("nothing left");
				current.rotateLeft();
				current = current.left;
			}
			else if(current.left != null && current.right != null){
				//System.out.println("everything everywhere");
				if(current.left.priority >= current.right.priority) {
					current.rotateRight();
					current = current.right;
				}
				else {
					current.rotateLeft();
					current = current.left;
				}
			}
		}
		
		if(key.compareTo(trail.right.data) ==0) {
			trail.right = null;
		}
		else if (key.compareTo(trail.left.data) ==0){
			trail.left = null;
		}
		
		return true;
		
	}
	
	
	private boolean find(Node<E> root, E key) {
		if(root == null) {
			return false;
		}
		int c = key.compareTo(root.data);
		if(c == 0) {
			//key found at current node
			return true;
		}
		if(c < 0) {
			//key is less than current node
			if(root.left == null) {
				return false;
			}
			else {
				return find(root.left,key);
			}
		}
		else{
			//key is greater than current node
			if(root.right == null) {
				return false;
			}
			else {
				return find(root.right,key);
			}
		}
	}
	
	/**
	 * tries to find a node within the tree
	 * @param key - the data value of the node you want to find
	 * @return boolean - returns if the node is in the treap
	 */
	public boolean find(E key) {
		return find(root,key);
	}
	
	private StringBuilder toString(Node<E> current, int i) {
		StringBuilder r = new StringBuilder() ;
		for (int j=0; j<i; j++) {
			r.append("	");
		}
		
		if (current==null) {
			r.append("null\n");
		} else {
			r.append(current.toString()+"\n");
			r.append(toString(current.left,i+1));
			r.append(toString(current.right,i+1));
			
		}
		return r;
		
	}
	
	/**
	 * toString method
	 * @return returns the String representation of a Treap
	 */
	public String toString() {
		return toString(root,0).toString();
	}
	
	public static void main(String[] args) {
		Treap<Integer> testTree = new Treap<Integer>();
		System.out.println(testTree.add(1,1));
		System.out.println(testTree.delete(1));
		System.out.println(testTree);
		
	}
}
