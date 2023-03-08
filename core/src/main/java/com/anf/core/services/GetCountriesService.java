package com.anf.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

public interface GetCountriesService {
    SlingHttpServletRequest getDataFromSource(SlingHttpServletRequest request);
}
