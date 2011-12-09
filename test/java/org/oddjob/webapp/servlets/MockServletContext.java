package org.oddjob.webapp.servlets;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

public class MockServletContext implements ServletContext {

	public Object getAttribute(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public Enumeration getAttributeNames() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public ServletContext getContext(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public String getInitParameter(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public Enumeration getInitParameterNames() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public int getMajorVersion() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public String getMimeType(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public int getMinorVersion() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public RequestDispatcher getNamedDispatcher(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public String getRealPath(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public RequestDispatcher getRequestDispatcher(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public URL getResource(String arg0) throws MalformedURLException {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public InputStream getResourceAsStream(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public Set getResourcePaths(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public String getServerInfo() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public Servlet getServlet(String arg0) throws ServletException {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public String getServletContextName() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public Enumeration getServletNames() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public Enumeration getServlets() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public void log(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public void log(Exception arg0, String arg1) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public void log(String arg0, Throwable arg1) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public void removeAttribute(String arg0) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	public void setAttribute(String arg0, Object arg1) {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	@Override
	public String getContextPath() {
		throw new RuntimeException("Unexpected from " + getClass());
	}

	@Override
	public Dynamic addFilter(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dynamic addFilter(String arg0, Filter arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(Class<? extends EventListener> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends EventListener> void addListener(T arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			Servlet arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			Class<? extends Servlet> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> arg0)
			throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> arg0)
			throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> arg0)
			throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void declareRoles(String... arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEffectiveMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEffectiveMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilterRegistration getFilterRegistration(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletRegistration getServletRegistration(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setInitParameter(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> arg0)
			throws IllegalStateException, IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

}
