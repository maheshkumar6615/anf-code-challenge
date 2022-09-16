package com.anf.core.services;

import com.anf.core.services.impl.NewsFeedServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewsFeedServiceTest {

    AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    NewsFeedService newsFeedService;
    ResourceResolverFactory resourceResolverFactory;
    ResourceResolver resourceResolver;
    Resource resource;
    Node node;
    NodeIterator nodeIterator;
    Property property;

    @BeforeEach
    public void setUp() throws RepositoryException {
        node = mock(Node.class);
        nodeIterator = mock(NodeIterator.class);
        resourceResolverFactory = mock(ResourceResolverFactory.class);
        resourceResolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        property = mock(Property.class);
        node.setProperty("author","author-name");
        //resource = context.resourceResolver().getResource("/var/commerce/products/anf-code-challenge/newsData");
        context.registerService(ResourceResolverFactory.class, resourceResolverFactory);
        newsFeedService = context.registerInjectActivateService(new NewsFeedServiceImpl());
    }

/*    @Test
    public void Validate() throws LoginException, RepositoryException {
        Map<String, Object> param = new HashMap<>();
        param.put(resourceResolverFactory.SUBSERVICE, "readService");
        //Resource resource = context.resourceResolver().getResource("/resource1");
        when(resourceResolverFactory.getServiceResourceResolver(param)).thenReturn(resourceResolver);
        when(resourceResolver.getResource("/var/commerce/products/anf-code-challenge/newsData")).thenReturn(resource);
        when(resource.adaptTo(Node.class)).thenReturn(node);
        when(node.hasNodes()).thenReturn(true);
        when(node.getNodes()).thenReturn(nodeIterator);
        when(nodeIterator.hasNext()).thenReturn(true);
        when(nodeIterator.next()).thenReturn(node);
        when(node.hasProperty(anyString())).thenReturn(true);
        when(node.getProperty(anyString())).thenReturn(property);
        when(property.getString()).thenReturn(anyString());
        //assertEquals("author-name", newsFeedService.getBlogDetails().get(0).getAuthor());
        Assertions.assertDoesNotThrow(() -> newsFeedService.getBlogDetails());
        //Assertions.assertDoesNotThrow(newsFeedService.getBlogDetails());
    }*/

}
