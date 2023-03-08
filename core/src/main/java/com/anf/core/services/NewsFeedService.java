package com.anf.core.services;

import com.anf.core.beans.NewsItemBean;
import java.util.List;

public interface NewsFeedService {
    List<NewsItemBean> getBlogDetails();
}
