/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.HTMLReader.FormAction;

import org.oddjob.monitor.action.ExecuteAction;
import org.oddjob.monitor.action.HardResetAction;
import org.oddjob.monitor.action.SoftResetAction;
import org.oddjob.monitor.action.StopAction;
import org.oddjob.monitor.actions.ExplorerAction;
import org.oddjob.monitor.context.ExplorerContext;

/**
 * Collect together actions.
 *
 * @author Rob Gordon.
 */
public class WebJobActions {

	private final Map<String, ExplorerAction> options = 
		new LinkedHashMap<String, ExplorerAction>();
	
	public WebJobActions() {
		// maybe this will be configurable one day.
		add(new ExecuteAction());
		add(new StopAction());
		add(new SoftResetAction());
		add(new HardResetAction());
	}
	
	void add(ExplorerAction option) {
		options.put(option.getName(), option);
	}
	
	public void select(Object component, ExplorerContext explorerContext) {
		for (Map.Entry<String, ExplorerAction> entry : options.entrySet()) {
			ExplorerAction option = entry.getValue();
			option.setSelectedContext(explorerContext);
			option.prepare();
		}
	}
	
	public Collection<String> commands() {
		return options.keySet();
	}
	
	public boolean isEnabled(String command) {
		ExplorerAction option = options.get(command);
		return option.isEnabled();
	}
	
	public void action(String command) throws Exception {
		ExplorerAction option = options.get(command);
		if (option instanceof FormAction) {
			throw new UnsupportedOperationException("Can't do forms yet.");
		} else {
			option.action();
		}
	}
}
