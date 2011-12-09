package org.oddjob.webapp.servlets;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class MockServletConfig  implements ServletConfig {

	public String getInitParameter(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public Enumeration getInitParameterNames() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public ServletContext getServletContext() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public String getServletName() {
		throw new RuntimeException("Unexpected from " + getClass());
	}
}
