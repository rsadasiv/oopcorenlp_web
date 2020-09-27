package io.outofprintmagazine.web.rest.browse;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/rest/browse")
public class RestApp extends ResourceConfig {
	public RestApp() {
		packages("io.outofprintmagazine.web.rest.browse");
	}
	
}
