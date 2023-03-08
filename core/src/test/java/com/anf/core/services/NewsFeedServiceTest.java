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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewsFeedServiceTest {

    AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    NewsFeedService newsFeedService;
    private static final String TEST_CONTENT_DATA_JSON = "/newsfeeddata.json";
    ResourceResolverFactory resourceResolverFactory;
    ResourceResolver resourceResolver;
    Resource resource;
    Node node;


    @BeforeEach
    public void setUp() {
        context.load().json(TEST_CONTENT_DATA_JSON, "/var/commerce/products/anf-code-challenge/newsData");
        resourceResolverFactory = mock(ResourceResolverFactory.class);
        resourceResolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        node = mock(Node.class);
        context.registerService(ResourceResolverFactory.class, resourceResolverFactory);
        newsFeedService = context.registerInjectActivateService(new NewsFeedServiceImpl());
    }


    @Test
    public void Validate() throws LoginException {
        Map<String, Object> param = new HashMap<>();
        param.put(resourceResolverFactory.SUBSERVICE, "readService");
        when(resourceResolverFactory.getServiceResourceResolver(param)).thenReturn(resourceResolver);
        when(resourceResolver.getResource("/var/commerce/products/anf-code-challenge/newsData")).thenReturn(resource);
        when(resource.adaptTo(Node.class)).thenReturn(node);
        assertEquals("Mahesh", newsFeedService.getBlogDetails().get(0).getAuthor());
        Assertions.assertDoesNotThrow(() -> newsFeedService.getBlogDetails());
    }

}
