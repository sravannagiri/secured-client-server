package scsa.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CommandProcessor {

	private AccessManager accessManager = null;
	private HashMap<String, ServerCommand> commands = null;
	
	public CommandProcessor(AccessManager access) {
		this.accessManager = access;
		this.commands = new HashMap<String, ServerCommand>();
	}
	
	public void addCommand(String command, ServerCommand p) {
		if ( commands.get(command) == null )
			commands.put(command, p);
	}
	
	public String processCommand(String user, String input) {
		
		String[] params = initParams(input);
		
		if ( commands.get(params[0]) != null ) {
			
			if ( accessManager.isCommandAllowed(user, params[0]) )
				return commands.get(params[0]).process(params);
			else
				return params[0] + ": permission denied";
				
		} else {
			return params[0] + ": no such command";
		}
	}
	
	public String[] initParams(String commandString) {
		String[] result = {};
		ArrayList<String> array = new ArrayList<String> ();
		
		StringTokenizer st = new StringTokenizer(commandString);
		if ( st.countTokens() > 1 ) {
			while ( st.hasMoreTokens() ) 
				array.add( st.nextToken() );
		} else {
			return new String[] { commandString };
		}
		result = array.toArray(result);
		return result;
	}
	
}
