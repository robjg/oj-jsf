package org.oddjob.webapp.servlets;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.oddjob.Oddjob;

/**
 * Encapsulate run or loading Oddjob.
 * 
 * @author rob
 *
 */
public class OddjobRunOrLoad {

	/** Oddjob. */
	private final Oddjob oddjob;

	/**
	 * The actions. Is this better than an if statement?
	 */
	private final Map<String, Runnable> actions  =
			new HashMap<String, Runnable>();
	{
		actions.put("run", new Runnable() {
				public void run() {
					oddjob.load();
					oddjob.getOddjobExecutors(
							).getPoolExecutor().execute(oddjob);
				}
			});
		actions.put("load", new Runnable() {
			public void run() { 
				oddjob.load();
			}
		});
	}		
	
	/**
	 * Constructor.
	 * 
	 * @param oddjob
	 */
	public OddjobRunOrLoad(Oddjob oddjob) {
		this.oddjob = oddjob;
	}
	
	/**
	 * Perform the action. Either run or load.
	 * 
	 * @param action
	 * @throws ServletException
	 */
	public void runOrLoad(String action) throws ServletException {
		if (action == null) {
			return;
		}
		
		Runnable runnable = actions.get(action);
		if (runnable == null) {
			throw new ServletException("No Oddjob action " + action);
		}
			
		runnable.run();
	}
}
