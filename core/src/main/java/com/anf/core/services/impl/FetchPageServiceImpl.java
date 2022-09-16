package com.anf.core.services.impl;

import com.anf.core.services.FetchPageService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = FetchPageService.class)
public class FetchPageServiceImpl implements FetchPageService {

    private static final Logger logger = LoggerFactory.getLogger(FetchPageServiceImpl.class);

    private static final String ROOTPATH = "/content/anf-code-challenge/us/en";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    public SearchResult getPages() {

        Map<String, String> map = new HashMap<>();
        map.put("path", ROOTPATH);
        map.put("1_property", "anfCodeChallenge");
        map.put("1_property.value", "true");
        map.put("orderby", "path");
        map.put("p.limit", "10");
        Map<String, Object> param = new HashMap<>();
        param.put(resolverFactory.SUBSERVICE, "readService");
        ResourceResolver resourceResolver = null;
        SearchResult result = null;
        try {
            resourceResolver = resolverFactory.getServiceResourceResolver(param);
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
            result = query.getResult();
        } catch (LoginException e) {
            logger.error("Exception in Fetch Page Service: {}", e.getMessage());
        }
        logger.debug("result string: {}", result.getHits().toString());
        return result;
    }

}
