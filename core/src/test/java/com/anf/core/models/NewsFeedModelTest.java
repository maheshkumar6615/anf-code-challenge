package com.anf.core.models;

import com.anf.core.beans.NewsItemBean;
import com.anf.core.services.impl.NewsFeedServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class NewsFeedModelTest {

    private NewsFeedModel newsfeed;
    private final AemContext context = new AemContext();

    @Mock
    NewsFeedServiceImpl newsFeedService;

    @Mock
    ResourceResolverFactory resourceResolverFactory;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(NewsFeedModel.class);
        context.registerService(ResourceResolverFactory.class, resourceResolverFactory);
        context.registerInjectActivateService(newsFeedService);
    }

    @Test
    void testGetDate() {
        newsfeed = context.request().adaptTo(NewsFeedModel.class);
        String newsfeedDate = newsfeed.getDate();
        assertNotNull(newsfeedDate);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy");
        String currentDate = dateFormat.format(date);
        assertEquals(currentDate, newsfeedDate);
    }

    @Test
    void testNewsFeedList() {
        newsfeed = context.request().adaptTo(NewsFeedModel.class);
        NewsItemBean bean1 = new NewsItemBean("News feed1", "Mahesh", "mahesh description", "/content/dam/photo.jpeg");
        NewsItemBean bean2 = new NewsItemBean("News feed2", "Mahesh", "mahesh description", "/content/dam/photo.jpeg");
        List<NewsItemBean> list = new ArrayList<NewsItemBean>();
        list.add(bean1);
        list.add(bean2);
        when(newsFeedService.getBlogDetails()).thenReturn(list);
        assertNotNull(newsfeed.getNewsItemsList());
        assertEquals(2, newsfeed.getNewsItemsList().size());
    }
}
