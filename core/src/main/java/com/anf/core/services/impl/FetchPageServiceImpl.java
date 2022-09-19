package com.anf.core.services.impl;

import com.anf.core.services.FetchPageService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.*;

@Component(immediate = true, service = FetchPageService.class)
public class FetchPageServiceImpl implements FetchPageService {

    private static final Logger logger = LoggerFactory.getLogger(FetchPageServiceImpl.class);

    private static final String ROOT_PATH = "/content/anf-code-challenge/us/en";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    QueryManager queryManager;

    /**
     * QueryBuilder query which returns the first 10 pages under the ROOTPATH
     */

    @Override
    public String getPages() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "cq:Page");
        map.put("path", ROOT_PATH);
        map.put("property", "jcr:content/anfCodeChallenge");
        map.put("property.operation", "exists");
        map.put("p.guessTotal", "10");
        map.put("p.limit", "10");
        Map<String, Object> param = new HashMap<>();
        param.put(resourceResolverFactory.SUBSERVICE, "readService");
        SearchResult result;
        JSONObject json = new JSONObject();
        try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(param)){
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
            result = query.getResult();
            for (Hit hit : result.getHits()) {
                Node node = hit.getNode();
                json.put(node.getName(), node.getPath());
            }
        } catch (LoginException | RepositoryException | JSONException e) {
            logger.error("Exception in Fetch Page Service: {}", e.getMessage());
        }
        return json.toString();
    }


    /**
     * SQL2 query which returns the first 10 pages under the ROOTPATH
     */

    @Override
    public String getPagesSql() {

        JSONObject json = new JSONObject();
        Map<String, Object> param = new HashMap<>();
        param.put(resourceResolverFactory.SUBSERVICE, "readService");
        String sqlStatement = "SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE(page ,\"/content/anf-code-challenge/us/en\") " +
                "AND page.[jcr:content/anfCodeChallenge] IS NOT NULL";

        try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(param)){
            Session session = resourceResolver.adaptTo(Session.class);
            queryManager = session.getWorkspace().getQueryManager();
            javax.jcr.query.Query query = queryManager.createQuery(sqlStatement, "JCR-SQL2");
            query.setLimit(10);
            QueryResult result = query.execute();
            Iterator<Node> itr = result.getNodes();
            List<String> result1 = new ArrayList<>();
            while (itr.hasNext()) {
                Node node = itr.next();
                logger.debug("node: {}", node.getPath());
                result1.add(node.getPath());
                json.put(node.getName(), node.getPath());
            }
        } catch (RepositoryException | LoginException | JSONException e) {
            logger.error("Exception in getPageList method: {}", e);
        }
        return json.toString();
    }

}
