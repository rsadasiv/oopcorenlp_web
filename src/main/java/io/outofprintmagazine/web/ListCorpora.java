package io.outofprintmagazine.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class ListCorpora
 */
@WebServlet("/ListCorpora")
public class ListCorpora extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListCorpora() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File[] directories = new File(request.getSession().getServletContext().getRealPath("/Corpora")).listFiles(File::isDirectory);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		ArrayNode corporaNode = json.putArray("Corpora");
		for (int i=0;i<directories.length;i++) {
			corporaNode.add(directories[i].getName());
		}
		response.setContentType("application/json");
		response.getWriter().write(mapper.writeValueAsString(json));
		response.flushBuffer();
	}

}
