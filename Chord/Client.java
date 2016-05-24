import java.net.*;
import java.io.*;
import java.util.*;
/*
* Client class which deals in both the transfering and receiving of files with the server.
*/
public class Client 
{
	Socket s=null;
	DataInputStream dataIStream;
	DataOutputStream dataOStream;
	// Client constructors dealing with multiple scenario's. Each constructor is created to handle specific case.
	public Client(int a, String filename,String serverName)
	{
		try
		{
			int portNo = 4334;
			s = new Socket(serverName, portNo);
			dataIStream = new DataInputStream( s.getInputStream());
			dataOStream =new DataOutputStream( s.getOutputStream());
			dataOStream.writeUTF("Store File");	
			dataOStream.writeUTF(serverName);	
			dataOStream.writeUTF(filename);	
			transferFile(filename);
		}
		catch (UnknownHostException e)
		{
			System.out.println("Sock:"+e.getMessage());
		}
		catch (EOFException e)
		{
			System.out.println("EOF:"+e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO:"+e.getMessage());
		}
		finally 
		{
			if(s!=null)
				try 
				{
					s.close();
				}
				catch (IOException e)
				{/*close failed*/}
		}
	}
	public Client(String futureName,String fileName)
	{
		try
		{
			int portNo = 4334;
			s = new Socket(futureName, portNo);
			dataIStream = new DataInputStream( s.getInputStream());
			dataOStream =new DataOutputStream( s.getOutputStream());
			dataOStream.writeUTF("Copy File");	
			dataOStream.writeUTF(futureName);	
			dataOStream.writeUTF(fileName);	
			transferFile(fileName);
		}
		catch (UnknownHostException e)
		{
			System.out.println("Sock:"+e.getMessage());
		}
		catch (EOFException e)
		{
			System.out.println("EOF:"+e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO:"+e.getMessage());
		}
		finally 
		{
			if(s!=null)
				try 
				{
					s.close();
				}
				catch (IOException e)
				{/*close failed*/}
		}
	}
	public Client(String presentName,String futureName,String fileName)
	{
		try
		{
			int portNo = 4334;
			s = new Socket(futureName, portNo);
			dataIStream = new DataInputStream( s.getInputStream());
			dataOStream =new DataOutputStream( s.getOutputStream());
			dataOStream.writeUTF("Send file");
			dataOStream.writeUTF(fileName);	
			dataOStream.writeUTF(futureName);	
			receiveFile(s,fileName,presentName);
		}
		catch (UnknownHostException e)
		{
			System.out.println("Sock:"+e.getMessage());
		}
		catch (EOFException e)
		{
			System.out.println("EOF:"+e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO:"+e.getMessage());
		}
		finally 
		{
			if(s!=null)
				try 
				{
					s.close();
				}
				catch (IOException e)
				{/*close failed*/}
		}
	}
	// This method transfers the file to the server
	public void transferFile(String fileName)
	{
		try
		{
			byte[] byteArray=new byte[(int)fileName.length()];
			BufferedInputStream br = new BufferedInputStream(new FileInputStream(fileName));
			br.read(byteArray, 0, byteArray.length);
			OutputStream o = s.getOutputStream();
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
	// This method receives the file from the server.
	public static void receiveFile(Socket s,String file,String presentName)
	{
		try
		{
			byte[] byteArray = new byte[1024];
			InputStream inp = s.getInputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(presentName + "." +file);
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
}