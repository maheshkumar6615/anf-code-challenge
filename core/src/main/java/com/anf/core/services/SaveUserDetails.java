package com.anf.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

public interface SaveUserDetails {
	String commitUserDetails(SlingHttpServletRequest request);
}
