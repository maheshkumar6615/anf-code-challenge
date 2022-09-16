package com.anf.core.services;

import com.anf.core.beans.NewsItemBean;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface NewsFeedService {
    public List<NewsItemBean> getBlogDetails();
}
