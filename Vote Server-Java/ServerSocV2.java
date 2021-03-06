import java.net.*; 
import java.io.*;
import java.util.*;

//Globals--------------------------------------------------------------------------------
class Lists
{
	public static String [] tracks = new String[100]; //User friendly Song names
	public static String [] songs = new String[100]; //Directory location of songs
	public static String [] votes = new String[5]; //Current Vote Options
	public static int voteCast = 0; //Number of votes cast
	public static int clientNum = 0; //Number of Active Clients
	public static boolean firstSong = true; //Boolean to check if first song 
	public static int [] choice = new int [5]; //holds the vote results
}

//Main Class----------------------------------------------------------------------------
class ServerSocV2
{
	//RNG for five vote options
	public static int RnG(int i)
	{
		Random rn = new Random();
		return (rn.nextInt(18)+0);
	}

	//Main where Lists are filled and Thread is started
	public static void main(String[] args) 
	{
		try //Try catch block for sockets
		{
			
			int count = 0; //number of total songs
			
			//Directory location of songs	
			File songF = new File("songs.txt");
			Scanner read = new Scanner(songF);
			while(read.hasNextLine())
			{
				Lists.songs[count]=read.nextLine();
				count++;
			}

			//User friendly Song names
			File trackF = new File("tracks.txt");
			Scanner scan = new Scanner(trackF);
			count=0;
			while(scan.hasNextLine())
			{
				Lists.tracks[count]=scan.nextLine();
				count++;
			}

			//filling Vote list for first time
			for (int i = 0; i < 5; i++)
			{
				Lists.votes[i] = Lists.tracks[RnG(count)];
			}

			//Start Lisnting for connections
			System.out.println("Listening for connections");
			boolean listening= true;
			ServerSocket serverSocket= new ServerSocket(7000);

			while(listening)
			{
				Socket s = new Socket();
				s = serverSocket.accept();
				Lists.clientNum++; //Increment Client after connection established
				new ClientHandler(s).start(); //Start the thread

				//check if all votes cast
				
				System.out.println(Lists.voteCast + " space " + Lists.clientNum);
				
			}
		}
		catch(IOException e)
		{
			System.out.println("couldn't listen on port 7000\n" + e);
		}
	}
}
//Custom Thread----------------------------------------------------------------------------
class ClientHandler extends Thread
{
	private Socket socket = null;
	
	//RnG for filling vote options
	public static int RnG(int i)
	{
		Random rn = new Random();
		return (rn.nextInt(i)+0);
	}


	public ClientHandler(Socket socket)
	{
		super("ClientHandler");
		this.socket=socket;
	}

	public void run()
	{
		
	
		try
		{
			int k = 0;
			while(k == 0)
			{
			//Output Song for Vote
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				for(int i=0;i<5;i++)
				{
		           		out.writeUTF(Lists.votes[i]);
		           		System.out.println(Lists.votes[i]); //Print songs server-side
	          		}
	
			  	//Get vote option
				DataInputStream in = new DataInputStream(socket.getInputStream());
				String song=""+in.readUTF();
				
				//Assigning vote to correct song
				for(int i=0;i<5;i++)
				{
					if(song.equals(Lists.votes[i]))
						Lists.choice[i]++;
				}
	
				Lists.voteCast = Lists.voteCast + 1; //Inc VoteCast
				
				//Print updated stats
				System.out.println(Lists.voteCast);
				System.out.println(Lists.clientNum);

				System.out.println(song); //Print Vote
				
				//loop to wait for other clients to vote
				while(Lists.voteCast != Lists.clientNum)
				{}
				
				
				try
				{
					//If no song playing
					if(Lists.firstcount)
					{
						int finalchoice = 0; //Most voted song
						
						//Find most popular choice
						for(int i = 1; i<5; i++)
						{
							if(Lists.choice[i]>Lists.choice[finalchoice])
								finalchoice=i;
						}
					
						//Get File name of song
						for(int i=0;i<Lists.tracks.length;i++)
						{		
							if(Lists.votes[finalchoice].equals(Lists.tracks[i]))
							{
								song = Lists.songs[i];
							}
						}
						
						//Play song
						Process proc = Runtime.getRuntime().exec("C:\\Program Files (x86)\\foobar2000\\foobar2000.exe  C:\\Users\\Luke\\Desktop\\music\\"+song);
						Lists.firstcount = false;
					}
					
					//If song playing - queue next vote
					else
					{
						int finalchoice = 0; //Most voted song
						
						//Find most popular choice
						for(int i = 1; i<5; i++)
						{
							if(Lists.choice[i]>Lists.choice[finalchoice])
								finalchoice=i;
						}
					
						//Get File name of song
						for(int i=0;i<Lists.tracks.length;i++)
						{		
							if(Lists.votes[finalchoice].equals(Lists.tracks[i]))
							{
								song = Lists.songs[i];
							}
						}
						
						//Add song to currently playing playlist
						Process proc = Runtime.getRuntime().exec("C:\\Program Files (x86)\\foobar2000\\foobar2000.exe /add  C:\\Users\\Luke\\Desktop\\music\\"+song);
					}
					
					//Wait
					this.sleep(120000);
				}
				catch (Throwable e) 
				{
					System.out.println("Oh Lovely");
				}
				if (Lists.voteCast == Lists.clientNum)
				{
					//New vote options
					for (int i = 0; i < 5; i++)
					{
						Lists.votes[i] = Lists.tracks[RnG(18)];
					}
					Lists.voteCast = 0; //Reset vote count
				}

			}
		
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
