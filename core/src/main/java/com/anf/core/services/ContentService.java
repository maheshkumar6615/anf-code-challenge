package com.anf.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

public interface ContentService {
	String commitUserDetails(SlingHttpServletRequest request);
}
