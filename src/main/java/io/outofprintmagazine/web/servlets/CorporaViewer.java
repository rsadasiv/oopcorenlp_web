/*******************************************************************************
 * Copyright (C) 2020 Ram Sadasiv
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package io.outofprintmagazine.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


@WebServlet(name="CorporaViewer", urlPatterns={"/index.html","/CorporaViewer"})
public class CorporaViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CorporaViewer() {
        super();
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayNode corpora = getStorage().listCorpora();
		request.setAttribute(
				"corpora", 
				corpora
		);
		ObjectNode batches = getMapper().createObjectNode();
		for (JsonNode corpusNode : corpora) {
			String corpusName = corpusNode.asText();
			try {
				batches.set(corpusName, getStorage().getCorpusBatchJson(corpusName));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute(
				"batches", 
				batches
		);
        request.getSession().getServletContext().getRequestDispatcher("/jsp/CorporaViewer.jsp").forward(request, response);
	}
}
