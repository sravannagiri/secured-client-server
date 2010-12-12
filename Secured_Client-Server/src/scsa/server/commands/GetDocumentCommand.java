package scsa.server.commands;

import scsa.server.ServerCommand;
import scsa.server.UsersStore;

public class GetDocumentCommand extends ServerCommand {

	private String[] documents = null;
	
	public GetDocumentCommand(UsersStore users) {
		super(users);
	}

	public String process(String[] params) {
		return "[getdocument RESULT]";
	}
	
}
