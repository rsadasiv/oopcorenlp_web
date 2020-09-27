package io.outofprintmagazine.web.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractOOPCacheableServlet extends AbstractOOPServlet {

	private static final long serialVersionUID = 1L;
	
	protected long lastModified = System.currentTimeMillis();

	public AbstractOOPCacheableServlet() {
		super();
	}
	
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	lastModified = System.currentTimeMillis();
    }
    
    @Override
    protected long getLastModified(HttpServletRequest request) {
    	return lastModified;
    }
    
	protected void setCacheControl(HttpServletResponse response) {
		response.setHeader("Cache-Control", "public");
    }
}
