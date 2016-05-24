import java.util.Random;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
// This is a class whihc will act as an object to be stored inside a double liked list.
// It will contain the information of the server and its successor and its predecessor.
class ServerNode
{
	String successor,predecessor,selfName;
	// Constructor
	public ServerNode(String selfName,String successor,String predecessor)
	{
		this.selfName=selfName;
		this.successor=successor;
		this.predecessor=predecessor;
	}
}
// A class which will serve as an object for the doubly circular linked list.
// It will store data, next pointer, previous pointer and the servernode object.
class Node
{
	Node next;
	Node previous;
	ServerNode containServer;
	int data;
	// Constructor
	public Node(int data,Node next,Node previous,ServerNode containServer)
	{
		this.data=data;
		this.next=next;
		this.previous=previous;
		this.containServer=containServer;
	}
}
// A doubly circular linked list which will be used to store information as a basis of distributed system.
class DoubleLinkedList
{
	private Node head;
	private Node tail;
	// Method addLast which will add the nodes at the last.
	public void addLast(int data)
	{
		Node tempNode = new Node(data,null,null,null);
		if(tail != null)
		{
			tail.next = tempNode;
			tail.next.previous = tail;
		}
		tail= tempNode;
		if(head == null)
		{
			head=tempNode;
			head.previous = tail;
		}
		head.previous = tail;
		tail.next=head;
	}
	// This getHead method will return head to the outside classes as it is declared private.
	public Node getHead()
	{
		return head;
	}
}
/*
* Main class where the execution of each step will take place. It contains the main method and supporting methods.
*/
public class GladosFile
{
	// Static hashmaps for the storage of random variables with respect to filename and server names.
	// Static Arraylist will provide the list of all the available servers.
	static Map<String,Integer> hmapFilesAndRandomNumbers=new HashMap<String,Integer>();
	static Map<String,Integer> hmapServersAndRandomNumbers=new HashMap<String,Integer>();
	static List<String> listOfServers = new ListOfServers().getServersList();
	// It is a  helper function which will display the options that the user have.
	public static void displayOptions()
	{
			System.out.println("a: Insert a file.");
			System.out.println("b: Search a file.");	
			System.out.println("c: Join a node.");
			System.out.println("d: Leave a node.");
			System.out.println("e: View a node.");
	}
	/*
	*This method is a helper function which generates random numbers taking into account the range and also the 
	* numbers which are also been used before.
	*/
	public static int generateRandomNumner(String str)
	{
		int sizeOfList=listOfServers.size();
		int randomNumber;
		Random r=new Random();
		// Case "a"
		// This case deals with generation of random numbers for the insertion of files.
		if(str.equals("a"))
		{
			randomNumber =r.nextInt(20);
			while(hmapFilesAndRandomNumbers.containsValue(randomNumber))
			{
				randomNumber =r.nextInt(20);
			}
		}
		// Case "ca" 
		// This case deals with the random number generation for the first time node join.
		else if(str.equals("ca"))
		{
			randomNumber =r.nextInt(20);
		}
		// Case "cb"
		// This case deals with the random number generation for the node joining.
		else
		{
			randomNumber =r.nextInt(20);
			while(hmapServersAndRandomNumbers.containsValue(randomNumber))
			{
				randomNumber =r.nextInt(20);
			}
		}
		return randomNumber;
	}
	/*
	* This helper function will return the node where the server is to be joined for the first time in the list.
	* It also takes care of the shorter way of reaching to the destination.
	*/
	public static Node targetNode(Node head,int randomNumber)
	{
		Node temp=head;
		if( head.data == randomNumber)
			return temp;
		else if((randomNumber > head.data) && (randomNumber < (head.data + 10)))
		{
			while(temp.data != randomNumber)
			{
				temp=temp.next;
			}
		}
		else
		{
			while(temp.data != randomNumber)
			{
				temp = temp.previous;
			}
		}
		return temp;
	}
	/*
	* This method generates the server name taking into account the list of available servers and the already used ones.
	*/
	public static String generateServerName()
	{
		Random r=new Random();
		int randomNumber =r.nextInt(listOfServers.size());
		while(hmapServersAndRandomNumbers.containsKey(listOfServers.get(randomNumber)))
		{
			randomNumber =r.nextInt(listOfServers.size());
		}
		return listOfServers.get(randomNumber);
	}
	/*
	* This method does most of the work in case of node joining(Not for the first time).
	* It finds the nodes that comes before the target node where the new node has to be eventually included,
	* the position where the new node is to be included and the next node after that.
	* The place where the new node is to be included is termed as present in the following code,
    * node before the present node is past node and the node after new node is future node.	
	* After this the nodes successor's and predecessor's are changed.
	* Then the files which needs to be transfered are done.
	*/
	public static void findSuccessor(Node entryNode, int randomNumber,String serverName)
	{
		Node temp=entryNode;
		Node past=entryNode;
		// This while loop finds the past node.
		while( temp.data != randomNumber)
		{
			if(temp.containServer != null)
			{
				past = temp;
			}
			temp=temp.next;
		}
		Node present=temp;
		// This loop finds the future node.
		while(temp.containServer == null)
		{
			temp=temp.next;
		}
		Node future=temp;
		// All the successor and predeccessor's changes.
		past.containServer.successor = serverName;
		System.out.println("Succesor of changed node:"+past.containServer.selfName);
		future.containServer.predecessor = serverName;
		System.out.println("Predecessor of changed node:"+future.containServer.selfName);
		present.containServer = new ServerNode(serverName,future.containServer.selfName,past.containServer.selfName);
		past=past.next;
		Client cl;
		// This loop will copy the files which were present in the future node to the present node and delete them
		// from the future directory.	
		while(past.data != present.data)
		{
			if(hmapFilesAndRandomNumbers.containsValue(past.data))
			{
				// Transfer file from future to present.
				for(Map.Entry<String,Integer> m:hmapFilesAndRandomNumbers.entrySet())
				{
					if(m.getValue() == past.data)
					{
						cl=new Client(present.containServer.selfName,future.containServer.selfName,m.getKey());
						System.out.println("File transfered:"+m.getKey());
					}
				}	
			}
			past = past.next;
		}
		//once more.
		// This is to check the file present at the presnet node location.
		if(hmapFilesAndRandomNumbers.containsValue(past.data))
			{
				for(Map.Entry<String,Integer> m:hmapFilesAndRandomNumbers.entrySet())
				{
					if(m.getValue() == past.data)
					{
						cl=new Client(present.containServer.selfName,future.containServer.selfName,m.getKey());
						System.out.println("File transfered:"+m.getKey());
					}
				}	
			}
	}
	/*
	* Similar to the upper function,this method does most of the work in case of node deleting.
	* It finds the present, past and future nodes based on the different criteria.
    * It checks if the node tobe deleted is the entry node or not. If yes, the it first goes backward anbd finds the previous node.
    * Otherwise it goes forward and finds the past node.The present and the future nodes are then calculated.	
	* After this files which needs to be transfered are done.
	* Then the nodes successor's and predecessor's are changed.
	*/
	public static void dropNode(Node entryNode, int randomNumber,String serverName)
	{
		Node temp=entryNode;
		Node past=entryNode;
		// This if loop will check if the entry node is the node to be deleted or not. 
		if(entryNode.containServer.selfName.equals(serverName))
		{
			temp=temp.previous;
			while(temp.containServer == null)
				temp=temp.previous;
			past=temp;
			temp=entryNode;
		}
		else
		{
			while( temp.data != randomNumber)
			{
				if(temp.containServer != null)
				{
					past = temp;
				}
				temp=temp.next;
			
			}
		}
		Node present=temp;
		temp=temp.next;
		// This loop finds the future node.
		while(temp.containServer == null)
		{
			temp=temp.next;
		}
		Node future=temp;
		Node past1 = past;
		past1=past1.next;
		Client cl;
		// This loop will copy the files which were present in the present node to the future node and delete them
		// from the present directory.
		while(past1.data != present.data)
		{
			if(hmapFilesAndRandomNumbers.containsValue(past1.data))
			{
				// Transfer file to future from present.
				for(Map.Entry<String,Integer> m:hmapFilesAndRandomNumbers.entrySet())
				{
					if(m.getValue() == past.data)
					{
						cl=new Client(future.containServer.selfName,present.containServer.selfName,m.getKey());
						System.out.println("File transfered:"+m.getKey());
					}
				}	
			}
			past1 = past1.next;
		}
		//once more.
		// This is to check the file present at the presnet node location.
		if(hmapFilesAndRandomNumbers.containsValue(past1.data))
			{
				for(Map.Entry<String,Integer> m:hmapFilesAndRandomNumbers.entrySet())
				{
					if(m.getValue() == past1.data)
					{
						cl=new Client(future.containServer.selfName,present.containServer.selfName,m.getKey());
						System.out.println("File transfered:"+m.getKey());
					}
				}	
			}
			// All the successor and predeccessor's changes.
			past.containServer.successor = future.containServer.selfName;
		System.out.println("Succesor of changed node:"+past.containServer.selfName);
		future.containServer.predecessor = past.containServer.selfName;
		System.out.println("Predecessor of changed node:"+future.containServer.selfName);
		present.containServer = null;
	}
	/*
	* In case of node deletion, after deleting the entry node we have to assign one of the present nodes to
	* be the entry node. This helper function does that.
	*/
	public static String selectRandomServer()
	{
		int siz=hmapServersAndRandomNumbers.size();
		Random r=new Random();
		int randomNumber =r.nextInt(siz);
		String server="";
		for(Map.Entry<String,Integer> m:hmapServersAndRandomNumbers.entrySet())
		{
			if(randomNumber == 0)
			{
				server = m.getKey();
			}
			randomNumber--;
		}
		return server;
	}
	/*
	* Afetr calculating the random server taht is to be the entry node, this function will return that by traversing through the list.
	*/
	public static Node pointNode(String server, Node head)
	{
		while(head.containServer == null || (!head.containServer.selfName.equals( server)))
		{
			head=head.next;
		}	
		return head;	
	}
	/*
	* This function does views every information about the server(Section e).
	* It displays the successor, predecessor, files present on the server by traversing through the system.
	*/
	public static void viewFunction(int randomNumber,String serverName,Node entryNode)
	{
		Node temp=entryNode;
		if(temp.data != randomNumber)
		{
			while(temp.data != randomNumber)
				temp=temp.next;
		}	
		System.out.println("Node Identifier:"+randomNumber);
		System.out.println("Successor:"+temp.containServer.successor);
		System.out.println("Predecessor:"+temp.containServer.predecessor);
		System.out.println("Files:");
		if(hmapFilesAndRandomNumbers.containsValue(temp.data))
		{
			for(Map.Entry<String,Integer> m:hmapFilesAndRandomNumbers.entrySet())
			{
				if(m.getValue() == temp.data)
					System.out.print(" "+m.getKey()+" ");
			}
		}
		temp=temp.previous;
		while( temp.containServer == null)
		{
			if(hmapFilesAndRandomNumbers.containsValue(temp.data))
			{
				for(Map.Entry<String,Integer> m:hmapFilesAndRandomNumbers.entrySet())
				{
					if(m.getValue() == temp.data)
						System.out.print(" "+m.getKey()+" ");
				}
			}
			temp=temp.previous;
		}
	}
	/*
	* This is a helper function for the file insrtion which will takes in the entry where the file is to be inserted and
	* returns the server where the file is to be stored.
	*/
	public static String findServer(int randomNumber,Node entryNode)
	{
		Node temp=entryNode;
		Node past = temp;
		if(temp.data == randomNumber)
			return temp.containServer.selfName;
		else
		{
			temp=temp.previous;
			while(temp.data != randomNumber)
			{
				if(temp.containServer != null)
				{
					past = temp;
				}
				temp = temp.previous;
			}
		}
		return past.containServer.selfName;
	}
	/*
	* This is the main function to be used during searching a  file by traversing through the system and 
	* finding out if the file is present or not.It first checks whether the file is at the entryNode location. 
	* Then it checks the other loactions and prints the result.
	*/
	public static void serachFile(String filename, Node entryNode)
	{
		boolean isTrue=false;
		Node temp=entryNode;
		Node past = temp;
		if(hmapFilesAndRandomNumbers.containsValue(temp.data))
		{
			for(Map.Entry<String,Integer> m:hmapFilesAndRandomNumbers.entrySet())
			{
				if(m.getValue() == temp.data && filename.equals(m.getKey()))
				{
					System.out.println("File Present");
					System.out.println("Peer: "+temp.containServer.selfName);
				}
			}
		}
		else
		{
			temp=temp.previous;
			while(temp.data != entryNode.data)
			{
				if(temp.containServer != null)
					past=temp;
				if(hmapFilesAndRandomNumbers.containsValue(temp.data))
				{
					for(Map.Entry<String,Integer> m:hmapFilesAndRandomNumbers.entrySet())
					{
						if(m.getValue() == temp.data && filename.equals(m.getKey()))
						{
							isTrue=true;
							System.out.println("File Present");
							System.out.println("Peer: "+ past.containServer.selfName);
							break;
						}
					}
				}
				if(isTrue)
					break;
				temp=temp.previous;
			}
			
		}
		if(!isTrue)
			System.out.println("File not present");	
	}
	/*
	* This is teh main function where the execution begins. It communicates with the user, uses the helper
	* functions to return the desired result.
	*/
	public static void main(String[] args)
	{
		DoubleLinkedList d=new DoubleLinkedList();
		for(int i=0;i<20;i++)
		{
			d.addLast(i);
		}
		int randomNumber;
		Node entryNode=null;
		String filename,serverName,tobeDropped,tobeViewed,tobeSearched;
		boolean entryNodeOrNot=false;
		char choice;
		do{
			Scanner sc=new Scanner(System.in);
			System.out.println("Enter your choice");
			displayOptions();
			char ch=sc.next().charAt(0);
			switch (ch)
			{
				// This case deals with the file insertion.
				case 'a': 
				{
					// This will prevent the user's from inserting the file when there is no node in the system.
					if(hmapServersAndRandomNumbers.isEmpty())
					{
						System.out.println("Can't insert the file");
						System.out.println("There is no node present");
					}
					// This statement is the general execution statemnet in case of file insertion.
					else if(hmapFilesAndRandomNumbers.size() < 20)
					{
						randomNumber = generateRandomNumner("a");
						System.out.println("Random:"+randomNumber);
						System.out.println("Insert File name");
						filename=sc.next();
						hmapFilesAndRandomNumbers.put(filename,randomNumber);
						// we have the random numbefr associated with the file. Now we have to insert the file in that particular location.
						String server=findServer(randomNumber,entryNode);
						Client cl=new Client(1,filename,server);
					}
					// This statement prevents users from storing more than 20 files in a system as our
					// system only have 20 different nodes.
					else
					{
						System.out.println("Can't insert more files. Failure.");
					}
					break;
				}
				// This case deals with file seraching.
				case 'b':
				{
					// This statement refines the cases when there is no node presnet in the system.
					if(hmapServersAndRandomNumbers.isEmpty())
					{
						System.out.println("There is no node present.");
						System.out.println("Try to insert node and file and then try to find it.");
					}
					// This statement considers the case of no file stored in teh system. 
					else if(hmapFilesAndRandomNumbers.isEmpty())
					{
						System.out.println("There is no file present in the system.");
					}
					// This is the general scenario of file searching.
					else
					{
						System.out.println("Enter the file name you wanna search");
						tobeSearched=sc.next();
						serachFile(tobeSearched,entryNode);
					}
					break;
				}
				// This case deals with node joining. 
				case 'c': 
				{
					// For Joining first node
					if(entryNode == null)
					{
						randomNumber = generateRandomNumner("ca");
						serverName=listOfServers.get(randomNumber % listOfServers.size());
						hmapServersAndRandomNumbers.put(serverName,randomNumber);
						Node node = targetNode(d.getHead(),randomNumber);
						entryNode = node;
						node.containServer = new ServerNode(serverName,serverName,serverName);
						System.out.println("Node added at:"+randomNumber);
						System.out.println("Server Name:"+serverName);
					}
					// When all the available servers are added.
					else if(hmapServersAndRandomNumbers.size() == listOfServers.size())
					{
						System.out.println("Failure. The Node can't be added. There are no servers remaining");
					}
					// General case scenario of node joining is handled here.
					else
					{
						randomNumber = generateRandomNumner("cb");
						System.out.println("Random Number"+randomNumber);
						serverName = generateServerName();
						System.out.println("Server Name"+serverName);
						hmapServersAndRandomNumbers.put(serverName,randomNumber);
						findSuccessor(entryNode,randomNumber,serverName);
						System.out.println("Node added at:"+randomNumber);
					}
					break;
				}
				// This case deals with the node deletion.
				case 'd':	
				{
					// It handles the scenario when there are no nodes in the system. 
					if(hmapServersAndRandomNumbers.isEmpty())
					{
						System.out.println("Failure. Node can't leave as there are no nodes to be removed.");
					}
					// This is a general case for the node deletion when the no of nodes are more than 2 in the system.
					else if(hmapServersAndRandomNumbers.size() >= 2)
					{
						System.out.println("Choose the node to be dropped.");
						System.out.println(hmapServersAndRandomNumbers.keySet());
						tobeDropped = sc.next();
						if(entryNode.containServer.selfName.equals(tobeDropped))
							entryNodeOrNot=true;
						dropNode(entryNode,hmapServersAndRandomNumbers.get(tobeDropped),tobeDropped);
						hmapServersAndRandomNumbers.remove(tobeDropped);
						if(entryNodeOrNot)
						{
							String server = selectRandomServer();
							entryNode = pointNode(server,d.getHead());
						}
					}
					// This case deals with the deletion of only node present in the system.
					else
					{
						for(Map.Entry<String,Integer> m:hmapFilesAndRandomNumbers.entrySet())
						{
							Client cl= new Client(entryNode.containServer.selfName,m.getKey());
						}
						System.out.println("There is only one node present.");
						System.out.println("Its deleted "+entryNode.containServer.selfName);
						entryNode.containServer = null;
						entryNode=null;
						hmapFilesAndRandomNumbers.clear();
						hmapServersAndRandomNumbers.clear();
					}
					break;
				}
				// This case deals with the display of the node and its entire data.
				case 'e':
				{
					System.out.println("Choose the node to be viewed");
					System.out.println(hmapServersAndRandomNumbers.keySet());
					tobeViewed = sc.next();
					randomNumber = hmapServersAndRandomNumbers.get(tobeViewed);
					viewFunction(randomNumber,tobeViewed,entryNode);
					break;
				}
			}
			System.out.println();
			System.out.println("Do you want to continue:");
			choice=sc.next().charAt(0);
		}while(choice=='Y' || choice=='y');	
		
	}		
}