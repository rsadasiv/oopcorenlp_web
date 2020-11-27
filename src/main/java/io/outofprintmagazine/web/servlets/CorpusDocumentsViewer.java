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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@WebServlet("/CorpusDocumentsViewer")
public class CorpusDocumentsViewer extends AbstractOOPServlet {
	private static final long serialVersionUID = 1L;
       

    public CorpusDocumentsViewer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String corpus = request.getParameter("Corpus");
		ArrayNode documentsArray = (ArrayNode) getStorage().listCorpusDocuments(corpus);
		ArrayNode documentsNode = getMapper().createArrayNode();
		for (int i=0;i<documentsArray.size();i++) {
			String document = documentsArray.get(i).asText();
			ObjectNode documentNode = getStorage().getCorpusDocumentOOPMetadata(corpus, document).deepCopy();
			documentNode.put("CorpusSimilarity", calculateSimilarity(corpus, corpus, document));
			documentsNode.add(documentNode);
		}
		request.setAttribute("documents", documentsNode);
		request.getSession().getServletContext().getRequestDispatcher("/jsp/CorpusDocumentsViewer.jsp").forward(request, response);
	}
}
