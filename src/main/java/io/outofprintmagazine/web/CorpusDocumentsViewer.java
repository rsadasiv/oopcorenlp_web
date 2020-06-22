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
package io.outofprintmagazine.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@WebServlet("/CorpusDocumentsViewer")
public class CorpusDocumentsViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public CorpusDocumentsViewer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pCorpus = request.getParameter("Corpus");
		File[] documents = new File(request.getSession().getServletContext().getRealPath("/Corpora/"+pCorpus+"/")).listFiles(File::isFile);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		ArrayNode corporaNode = json.putArray("Documents");
		for (int i=0;i<documents.length;i++) {
			if (documents[i].getName().substring(0, documents[i].getName().lastIndexOf(".")).startsWith("OOP_")) {
				ObjectNode documentNode = mapper.createObjectNode();
				documentNode.put("DocID", documents[i].getName().substring(4, documents[i].getName().lastIndexOf(".")));
	            BufferedReader br = new BufferedReader(new FileReader(documents[i]));
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode stats = objectMapper.readTree(br);
            	documentNode.put("Author", stats.findValue("AuthorAnnotation").asText());
            	documentNode.put("Date", stats.findValue("DocDateAnnotation").asText());
            	documentNode.put("Title", stats.findValue("DocTitleAnnotation").asText());
            	if (stats.findValue("OOPThumbnailAnnotation")!= null) {
            		documentNode.put("Thumbnail", stats.findValue("OOPThumbnailAnnotation").asText());
            	}
            	corporaNode.add(documentNode);

				br.close();
			}
		}
		request.setAttribute("corpora", json);
		request.getSession().getServletContext().getRequestDispatcher("/CorpusDocumentsViewer.jsp").forward(request, response);
	}
}
