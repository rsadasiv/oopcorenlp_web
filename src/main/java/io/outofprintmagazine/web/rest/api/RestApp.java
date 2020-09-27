package io.outofprintmagazine.web.rest.api;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/rest/api")
public class RestApp extends ResourceConfig {
	public RestApp() {
		packages("io.outofprintmagazine.web.rest.api");
	}
	
}
