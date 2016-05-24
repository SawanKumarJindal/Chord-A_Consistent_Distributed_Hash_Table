import java.net.*;
import java.io.*;
import java.util.*;
/*
* Server class which deals in both the transfering and receiving of files with the client.
*/
public class Server
{
	// main method whihc will start the server.
	public static void main (String args[]) 
	{
		try
		{
			int portNo = 4334;
			ServerSocket serverSocket = new ServerSocket(portNo);
			while(true)
			{
				Socket clientSocket = serverSocket.accept();
				Connection c = new Connection(clientSocket);
			}
		}
		catch(IOException e)
		{
			System.out.println("Listen :"+e.getMessage());
		}
	}
}
// This class will act as a thread for each client request.
class Connection extends Thread 
{
	DataInputStream inputStream;
	DataOutputStream outputStream;
	Socket clientSocket;
	File f;
	public Connection (Socket clientSocket)
	{
		try 
		{
			this.clientSocket = clientSocket;
			inputStream = new DataInputStream( clientSocket.getInputStream());
			outputStream =new DataOutputStream( clientSocket.getOutputStream());
			this.start();
		}
		catch(IOException e)
		{
			System.out.println("Connection:"+e.getMessage());
		}
	}
	// This run method handles the dofferent cases thrown by the client.
	public void run()
	{
		try
		{ 
			String defaultPath="/home/stu14/s4/sj6390/Desktop/";
			String dataReceived = inputStream.readUTF();
			String filename,hostName;
			if(dataReceived.equals("Send file"))
			{
				filename=inputStream.readUTF();
				hostName=inputStream.readUTF();
				boolean filePresentVar=processData(defaultPath+hostName+"."+filename);
					transferFile(hostName+"."+filename);
				fileDelete(defaultPath+hostName+"."+filename);
			}
			else if(dataReceived.equals("Delete"))
			{
				hostName=inputStream.readUTF();
				filename=inputStream.readUTF();
				fileDelete(defaultPath+hostName+"."+filename);
			}
			else if(dataReceived.equals("Copy File"))
			{
				hostName=inputStream.readUTF();
				filename=inputStream.readUTF();
					receiveFile(filename,hostName);
			}
			else if(dataReceived.equals("Store File"))
			{
				hostName=inputStream.readUTF();
				filename=inputStream.readUTF();
					receiveFile(filename,hostName);
			}
		}
		catch(EOFException e) 
		{
			System.out.println("EOF:"+e.getMessage());
		}
		catch(IOException e) 
		{
			System.out.println("IO:"+e.getMessage());
		}
		finally
		{ 
			try
			{
				clientSocket.close();
			}
			catch (IOException e)
			{/*close failed*/}
		}
	}	
	// This method return if the file is present in the system or not. 
	public boolean processData(String dataReceived)
	{
		f = new File(dataReceived);
		return f.exists();
	} 
	// This method deletes the file presnt in the system.
	public void fileDelete(String filename)
	{
		f = new File(filename);
		f.delete();
	}
	// This method receives the file from the client
	public  void receiveFile(String file,String root)
	{
		try
		{
			byte[] byteArray = new byte[100000];
			InputStream inp = clientSocket.getInputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(root + "." +file);
			BufferedOutputStream br = new BufferedOutputStream(fileOutputStream);
			int readBytes = inp.read(byteArray, 0, byteArray.length);
			br.write(byteArray, 0, readBytes);
			br.close();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}	
	}
	// This method transfers the file to the client
	public void transferFile(String f)
	{
		try
		{
			byte[] byteArray=new byte[(int)f.length()];
			BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
			br.read(byteArray, 0, byteArray.length);
			OutputStream o = clientSocket.getOutputStream();
			o.write(byteArray, 0, byteArray.length);
			o.flush();
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
}