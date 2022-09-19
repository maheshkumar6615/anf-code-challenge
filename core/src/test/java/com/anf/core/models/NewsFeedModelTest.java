package com.anf.core.models;

import com.anf.core.services.impl.NewsFeedServiceImpl;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class NewsFeedModelTest {


    private static final String TEST_CONTENT_JSON = "/newsfeed.json";
    private static final String TEST_CONTENT_DATA_JSON = "/newsfeeddata.json";

    private NewsFeedModel newsfeed;

    private Page page;
    private Resource resource;
    private final AemContext ctx = new AemContext();

    @InjectMocks
    NewsFeedServiceImpl newsFeedService;

    @Mock
    ResourceResolverFactory resourceResolverFactory;

    @Mock
    private ResourceResolver resolver;

    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(NewsFeedModel.class);
        ctx.load().json(TEST_CONTENT_DATA_JSON, "/var/commerce/products/anf-code-challenge");
        ctx.load().json(TEST_CONTENT_JSON, "/content");
        ctx.registerService(ResourceResolverFactory.class, resourceResolverFactory);
        ctx.registerService(new NewsFeedServiceImpl());

    }

    @Test
    void testGetDate() {
        newsfeed = ctx.request().adaptTo(NewsFeedModel.class);
        String newsfeedDate = newsfeed.getDate();
        assertNotNull(newsfeedDate);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy");
        String currentDate = dateFormat.format(date);
        assertEquals(currentDate, newsfeedDate);
    }

    @Test
    void testNewsFeedlist() throws Exception {

        newsfeed = ctx.request().adaptTo(NewsFeedModel.class);
        final Map<String, Object> READ_AUTH =
                Collections.singletonMap(resourceResolverFactory.SUBSERVICE, "readService");
        lenient().when(resourceResolverFactory.getServiceResourceResolver(READ_AUTH)).thenReturn(resolver);

        Resource resource = ctx.currentResource("/var/commerce/products/anf-code-challenge/newsData");
        lenient().when(resolver.getResource("/var/commerce/products/anf-code-challenge/newsData")).thenReturn(resource);
       // assertNotNull(newsfeed.getNewsItemsList());
    }
}
