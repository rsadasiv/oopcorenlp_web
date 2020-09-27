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
package io.outofprintmagazine.web.rest.browse;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.fasterxml.jackson.databind.node.ArrayNode;

import io.outofprintmagazine.web.servlets.AbstractOOPCacheableServlet;

@Path("/Corpora/{Corpus}/{Document}")
public class CorpusDocument extends AbstractOOPCacheableServlet {
	
	private static final long serialVersionUID = 1L;
	@Context
	private ServletConfig servletConfig;
	@Context
	private ServletContext servletContext;
	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpServletResponse httpServletResponse;

    @GET
    @Produces("application/json; charset=utf-8")
    public String doGet(
    		@PathParam("Corpus") String corpus,
    		@PathParam("Document") String document
    		) throws ServletException, IOException {
    	if (getStorage() == null) {
    		super.init(servletConfig);
    	}
    	ArrayNode retval = getMapper().createArrayNode();
    	retval.add("AGGREGATES");
    	retval.add("OOP");
    	retval.add("PIPELINE");
    	retval.add("STANFORD");
    	retval.add("TXT");
		return getMapper().writeValueAsString(
			retval
		);
	}
}
