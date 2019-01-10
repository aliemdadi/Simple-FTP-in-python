import java.net.*;
import java.io.*;
import java.util.*;


class FTPClient
{
	public static void main(String args[]) throws Exception
	{
		 
		Socket soc=new Socket("127.0.0.1",1000);
		transferfileClient t=new transferfileClient(soc);
		t.displayMenu();
		
	}
}

class transferfileClient
{
	
	Socket ClientSoc;

	DataInputStream din;
	DataOutputStream dout;
	BufferedReader br;
	transferfileClient(Socket soc)
	{
		
		
		try
		{
			ClientSoc=soc;
			din=new DataInputStream(ClientSoc.getInputStream());
			dout=new DataOutputStream(ClientSoc.getOutputStream());
			br=new BufferedReader(new InputStreamReader(System.in));
		}
		catch(Exception ex)
		{
		}		
	}
	void ChangeDirectory() throws Exception
	{		
		
			String directory;
			String back;
			System.out.print("Back to parent directory?  y/n?");
			back=br.readLine();
		
			if (back.equals("y")) 
			 {
					dout.writeUTF("BACK");
					System.out.print("Direcotry changed to parent path");
			}
		
			 else if(back.equals("n"))
			{
					 System.out.print("Enter directory path :");
					 directory=br.readLine();
					 dout.writeUTF(directory);
					String cd=din.readUTF();
					 if(cd.equals("DCH"))
					{
					System.out.println("Directory changed");
					}
					else if(cd.equals("DC"))
					{
					System.out.println("Directory created");
					}
			}
		
		}
		
		
	
	
	void ReceiveFile() throws Exception
	{
		String fileName;
		System.out.print("Enter File Name :");
		fileName=br.readLine();
		dout.writeUTF(fileName);
		String msgFromServer=din.readUTF();
		
		if(msgFromServer.compareTo("File Not Found")==0)
		{
			System.out.println("File not found on Server ...");
			return;
		}
		else if(msgFromServer.compareTo("READY")==0)
		{
			System.out.println("Receiving File ...");
			File f=new File(fileName);
			if(f.exists())
			{
				String Option;
				System.out.println("File Already Exists. Want to OverWrite (y/n) ?");
				Option=br.readLine();			
				if(Option=="n")	
				{
					dout.flush();
					return;	
				}				
			}
			FileOutputStream fout=new FileOutputStream(f);
			int ch;
			String temp;
			do
			{
				temp=din.readUTF();
				ch=Integer.parseInt(temp);
				if(ch!=-1)
				{
					fout.write(ch);					
				}
			}while(ch!=-1);
			fout.close();
			System.out.println(din.readUTF());
				
		}
		
		
	}
	 void List() throws Exception
	{
		 String option;
		String ls=din.readUTF();
		System.out.println("Files and Folders listed in current path are:");
		System.out.println(ls);
		while(true)
		{
		System.out.println("\nDo you want to continue?  y/n?");
		option=br.readLine();
		if(option.equals("y"))
		 {
			return;
		 } 
		else
		  {}
		
		}
		
	}

	public void displayMenu() throws Exception
	{
		  
		while(true)
		{	
			System.out.println("[ MENU ]");
			System.out.println("cd for changing directory");
			System.out.println("ret for retrieve");
			System.out.println("list for listing files an folders exists in current path");
			System.out.println("exit for close connection");
			System.out.println("\nEnter Choice :");
			  BufferedReader command = new BufferedReader(new InputStreamReader(System.in));
		        
		        String inputstring = command.readLine();
		        
		       
			  
          
			 
			if(inputstring.equals("cd"))
			{
				
				dout.writeUTF("CD");
				ChangeDirectory();
			}
			else if(inputstring.equals("ret"))
			{
				dout.writeUTF("GET");
				ReceiveFile();
			}
			else if(inputstring.equals("list"))
			{
				dout.writeUTF("LIST");
				List();
			}
			else if(inputstring.equals("exit"))
			{
				dout.writeUTF("DISCONNECT");
				this.ClientSoc.close();
				System.exit(1);
			}
			else
			{
				System.out.println("your command does not define for me");
			}
			
			
		}
	}
}