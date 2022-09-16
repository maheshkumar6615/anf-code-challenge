package com.anf.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

public interface FetchDataService {
    SlingHttpServletRequest getDataFromSource(SlingHttpServletRequest request);
}
