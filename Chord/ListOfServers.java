import java.util.List;
import java.util.ArrayList;
/*
* This class returns teh list of available servers.
*/
class ListOfServers
{
	private List<String> serverList;
	// this constructor adds the servers in the arraylist.
	public ListOfServers()
	{
		serverList=new ArrayList<String>();
		serverList.add("maine.cs.rit.edu");
		//serverList.add("yes.cs.rit.edu");
        serverList.add("newyork.cs.rit.edu");
		serverList.add("doors.cs.rit.edu");
		//serverList.add("kinks.cs.rit.edu");
		//serverList.add("joplin.cs.rit.edu");
		//serverList.add("buddy.cs.rit.edu");
	}
	// This getServersList function gives user the access of this arraylist.
	public List<String> getServersList()
	{
		return serverList;
	}
}	
	