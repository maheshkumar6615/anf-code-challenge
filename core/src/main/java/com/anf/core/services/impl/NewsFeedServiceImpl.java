package com.anf.core.services.impl;

import com.anf.core.beans.NewsItemBean;
import com.anf.core.services.NewsFeedService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.*;

@Component(service = NewsFeedService.class, immediate = true)
@Designate(ocd = NewsFeedServiceImpl.Config.class)
public class NewsFeedServiceImpl implements NewsFeedService {

    private static final Logger logger = LoggerFactory.getLogger(NewsFeedServiceImpl.class);

    private String newsFeedPath;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @ObjectClassDefinition(name = "NewsFeed service", description = "News feed location config")
    public @interface Config {
        @AttributeDefinition(name = "News item location", description = "This store the path news items", type = AttributeType.STRING)
        String news_feed_items() default "/var/commerce/products/anf-code-challenge/newsData";

    }

    @Activate
    protected void activate(Config config) {
        newsFeedPath = config.news_feed_items();
    }

    @Override
    public List<NewsItemBean> getBlogDetails() {
        logger.debug("get newsItems called: {}", newsFeedPath);
        List<NewsItemBean> newsItemsList = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put(resourceResolverFactory.SUBSERVICE, "readService");
        try {
            ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
            Resource resource = resourceResolver.getResource(newsFeedPath);
            Node node = resource.adaptTo(Node.class);
            if (node.hasNodes()) {
                Iterator<Node> itr = node.getNodes();
                while (itr.hasNext()) {
                    Node childNode = itr.next();
                    String title = childNode.hasProperty("title") ? childNode.getProperty("title").getString() : "";
                    String author = childNode.hasProperty("author") ? childNode.getProperty("author").getString() : "";
                    String description = childNode.hasProperty("description") ? childNode.getProperty("description").getString() : "";
                    String image = childNode.hasProperty("urlImage") ? childNode.getProperty("urlImage").getString() : "";
                    NewsItemBean newsItemBean = new NewsItemBean(title, author, description, image);
                    newsItemsList.add(newsItemBean);
                }
            }
        } catch (RepositoryException | LoginException e) {
            logger.error("Exception in news feed service: {}", e.getMessage());
        }
        return newsItemsList;
    }
}
