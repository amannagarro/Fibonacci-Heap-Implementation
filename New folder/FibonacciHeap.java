import java.util.ArrayList;
import java.util.HashMap;

public class FibonacciHeap {
	static FibonacciNode maxNode;
	static HashMap<String,FibonacciNode> hmap = new HashMap<String,FibonacciNode>();
	
	//Add new key or increase count of existing key.
	static public void InsertOrIncreaseKey(String hashTagVal, int count)
	{		
		if(hmap.containsKey(hashTagVal))
		{
			IncreaseKey(hmap.get(hashTagVal),count);
		}
		else
		{
			InsertNewFibonacciNode(hashTagVal,count);			
		}		
	}
	
	//Insert new node in top level list and add new node in hashMap also.
	static public void InsertNewFibonacciNode(String hashTagVal, int count){
	  
		FibonacciNode newNode = CreateNewFibonacciNode(hashTagVal,count);
		InsertAtTopLevelTist(newNode);	

		//add newnode in the HashTable.
		hmap.put(newNode.hashtagValue, newNode);
	}
		
	//update key and perform Removekey operation if necessary and update MaxNode if necessary.
	static public void IncreaseKey(FibonacciNode node,int count)
	{
		if(node!=null)
		{
			//update key.
			node.count += count;		

			//if parent not null then perform childCut if necessary.
			if(node.parent != null && node.parent.count < node.count)
			{
				RemoveFibonacciNode(node,node.parent);
				PerformChildCut(node.parent);
			}	
		
			//update MaxNode if necessary.
			if(maxNode.count<node.count)
			{
				maxNode = node;
			}		
		}
	}
	
	//insert node at right of Max Node.
	static public void InsertAtTopLevelTist(FibonacciNode newNode)
	{
		if(newNode !=null)
		{
				//if this is first node in heap then point maxPointer on it.
				if(maxNode ==null)
				{
					maxNode=newNode;
				}
				else
				{					
					//else add it to the right of maxNode.
					newNode.parent = null;
					newNode.rightSibling = maxNode.rightSibling;
					newNode.leftSibling = maxNode;
					newNode.rightSibling.leftSibling = newNode;
					maxNode.rightSibling = newNode;
					
					//if this node has max value then make it MaxNode.
					if(newNode.count>maxNode.count)
					{
						maxNode = newNode;
					}					
				}	
		}
	}
	
	//remove node from its sibling list, insert it at top level, decrease parent degree, set child cut to false and parent child cut to true
	//and set parent child to the next sibling.
	static public void RemoveFibonacciNode(FibonacciNode node, FibonacciNode parent)
	{
		//remove from sibling List.
		if(parent.degree==1)
		{
			parent.child = null;
			node.leftSibling = node;
			node.rightSibling = node;
			InsertAtTopLevelTist(node);			
			node.childCut = false;
		}
		else
		{
			parent.child = node.rightSibling;
			node.leftSibling.rightSibling = node.rightSibling;
			node.rightSibling.leftSibling = node.leftSibling;
			InsertAtTopLevelTist(node);
			node.childCut = false;
		}
		
		//update parent degree.
		parent.degree = parent.degree-1;
	}
	
	//perform child cut.
	static public void PerformChildCut(FibonacciNode node)
	{
		//if child cut is already true then remove it from the list.
		if(node!=null) 
		{						
			if(node.childCut == false)
			{
				node.childCut = true;
			}
			else if(node.childCut == true && node.parent!=null )
			{
				RemoveFibonacciNode(node,node.parent);
				PerformChildCut(node.parent);
			}
		}
	}
	
	//get hashtag keys of n nodes with max count and reinsert the deleted items.
	static public ArrayList<FibonacciNode> GetTopNHashTag(int n)
	{
		ArrayList<FibonacciNode> topNHashTagList = new ArrayList<FibonacciNode>();

		FibonacciNode maxNode;
		for(int i=0; i<n; i++)
		{
		 maxNode = DeleteMaxNode();
		 topNHashTagList.add(i, maxNode);
		}
		
		//reinsert the deleted items in heap.
		ArrayList<FibonacciNode> output = new ArrayList<FibonacciNode>();
		for(int j=0;j<n ;j++)
		{
			output.add(topNHashTagList.get(j));
			InsertAtTopLevelTist(topNHashTagList.get(j));
		}
		return output;

	}
	
	//if only one node is present;delete it and set its child on top
	//first delete Max Root children and add it to the top level list and then remove Max Root.
	static public FibonacciNode DeleteMaxNode()
	{		
		//delete all children of MaxNode and add to the top-level list.		
		PutChildrenOfMaxNodeAtToplevel();
		
		//get Max Root.
		FibonacciNode max = removeMaxNode();
		
		ArrayList<FibonacciNode> topLevelNodesList = new ArrayList<FibonacciNode>();
		
		//fill all toplevel nodes in a list.
		FibonacciNode current = maxNode.rightSibling;
		do{
			topLevelNodesList.add(current);			
			current = current.rightSibling;
		}while(current!=maxNode);
		
		if(topLevelNodesList.size()>1)
		{
			//do pairWise combine.
			PerformPairWiseCombine(topLevelNodesList);
		}		
		return max;
	}

	//delete all its children and add to the top-level list.	
	public static void PutChildrenOfMaxNodeAtToplevel()
	{	
		if(maxNode.child !=null)
		{
			FibonacciNode child = maxNode.child;
			//set parent of all child nodes null.
			FibonacciNode curr = child;
			do{
				curr.parent = null;
				curr = curr.rightSibling;
			}while(curr!=child);
		
			FibonacciNode nextNodeToMax = maxNode.rightSibling;
			nextNodeToMax.leftSibling = child.leftSibling;
			child.leftSibling.rightSibling = nextNodeToMax;
			maxNode.rightSibling = child;
			child.leftSibling = maxNode;
		}
	}
	
	//remove Max root from the list.
	static public FibonacciNode removeMaxNode()
	{
		FibonacciNode node = maxNode;
		
		if(maxNode.rightSibling == maxNode)
		{
			maxNode = null;
		}
		else
		{
			maxNode.leftSibling.rightSibling = maxNode.rightSibling;
			maxNode.rightSibling.leftSibling = maxNode.leftSibling;
			// make next element the new Max Node.
			maxNode = maxNode.rightSibling;
			node.rightSibling=node;
			node.leftSibling = node;
			node.childCut =false;
			node.parent = null;
			node.degree = 0;
			node.child = null;
		}
		return node;
	}
	
	//perform pair wise combine.
	static public void PerformPairWiseCombine(ArrayList<FibonacciNode> topLevelNodesList)
	{
		//HashTable to maintain  trees with same degree so that we can perform pairwise combine on them.
		HashMap<Integer,FibonacciNode> pairWiseList = new HashMap<Integer,FibonacciNode>();

		for(FibonacciNode current : topLevelNodesList)
		{	
			if(current.count > maxNode.count)
			{
				maxNode = current;
			}
			
			//if no node of same degree exist in pairWiseList then just add it.
			if(pairWiseList.get(current.degree)==null)
			{
				FibonacciNode node = current;
				pairWiseList.put(current.degree,node);
			}
			else
			{
				//if node of same degree exists in pairWiseList then perform recursively combine untill no node of same degree is found in list.
				FibonacciNode currentNode = current;
				while(pairWiseList.get(currentNode.degree)!=null)
				{					
					FibonacciNode nodeToMerge = pairWiseList.get(currentNode.degree);					
					pairWiseList.remove(nodeToMerge.degree);
					FibonacciNode parent = (currentNode.count > nodeToMerge.count) ? currentNode:nodeToMerge;
					FibonacciNode child = (currentNode.count >  nodeToMerge.count) ? nodeToMerge:currentNode;
					
					//once node has become child then it should be removed from pairwise list.
					child.leftSibling.rightSibling = child.rightSibling;
					child.rightSibling.leftSibling = child.leftSibling;
					currentNode = AddNodeToSiblingList(parent,child);
				}
				pairWiseList.put(currentNode.degree,currentNode);	
				//In case where our MaxNode has become child of current node because count was same, then set current as MaxNode.
				if(currentNode.count >= maxNode.count)
				{
					maxNode = currentNode;
				}
			}
		}
	}
	
	//add child to the parent's child list.
	static public FibonacciNode AddNodeToSiblingList(FibonacciNode parent,FibonacciNode node)
	{
		node.leftSibling = node;
		node.rightSibling = node;
		if(parent.degree>0)
		{
			FibonacciNode child = parent.child;
			node.rightSibling = child.rightSibling;
			child.rightSibling = node;
			node.leftSibling = child;
			node.rightSibling.leftSibling = node;
			parent.childCut = false;
		}
		else
		{
			parent.child = node;
		}	
		node.parent = parent;
		
		//increase parent degree by 1.
		parent.degree +=1;
		
		return parent;			
	}
	
	//Create new node with default values.
	static FibonacciNode CreateNewFibonacciNode(String hashTagVal, int count){
		FibonacciNode newNode = new FibonacciNode();
		newNode.hashtagValue = hashTagVal;
		newNode.count = count;
		newNode.degree = 0;
		newNode.childCut = false;
		newNode.leftSibling = newNode;
		newNode.rightSibling = newNode;
		newNode.parent = null;
		return newNode;
	}
}
