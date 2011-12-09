/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;
import org.oddjob.webapp.model.JobInfoLookup;
import org.oddjob.webapp.model.WebJobActions;

/**
 * The backing bean for the command buttons.
 *  
 * @author Rob Gordon.
 */
@ManagedBean(name="commands")
public class JobActionsBean extends DetailBase {
	private static final Logger logger = Logger.getLogger(JobActionsBean.class);
	
	/**
	 * Execute an action.
	 * 
	 * @param command
	 */
	private void execute(String command) {

		logger.debug("Performing action [" + command
				+ "] for [" + getRefId() + "]" );
		
		JobInfoLookup lookup = getJobInfoLookup();
		
		WebJobActions actions = lookup.actionsFor(getRefId());
		
		if (actions.isEnabled(command)) {
			try {
				actions.action(command);
			} catch (Exception e) {
				// TODO Populate message
				logger.error(e);
			}
		} else {
			logger.debug("Command no longer available.");
		}			
	}
	
	/**
	 * Get the commands.
	 * 
	 * @return
	 */
	public Collection<Command> getCommands() {
		
		JobInfoLookup lookup = getJobInfoLookup();
		
		WebJobActions actions = lookup.actionsFor(getRefId());
		
		List<Command> actionList = new ArrayList<Command>();
		
		for (String command : actions.commands()) {
			
			Command jobAction = new Command(
					command, actions.isEnabled(command));
			
			actionList.add(jobAction);
		}
		
		return actionList;
	}
	
	/**
	 * The nested class allows each command button to have it's own 
	 * action method.
	 *
	 */
	public class Command {

		private final String name;
		
		private final boolean enabled;
		
		public Command(String name, boolean enabled) {
			this.name = name;
			this.enabled = enabled;
		}
		
		public String getName() {
			return name;
		}
		
		public boolean getEnabled() {
			return enabled;
		}
		
		public String action() {
			execute(name);
			return "index.xhtml?faces-redirect=true";
		}
	}
}