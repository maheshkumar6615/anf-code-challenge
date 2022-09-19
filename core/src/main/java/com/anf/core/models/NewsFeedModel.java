package com.anf.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import com.anf.core.beans.NewsItemBean;
import com.anf.core.services.NewsFeedService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class},
        resourceType = NewsFeedModel.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class NewsFeedModel {

    protected static final String RESOURCE_TYPE = "anf-code-challenge/components/newsfeed";
    private List<NewsItemBean> newsItemsList;
    private String date;

    @OSGiService
    private NewsFeedService newsFeedService;

    public List<NewsItemBean> getNewsItemsList() {
        return newsFeedService.getBlogDetails();
    }

    public String getDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy");
        return dateFormat.format(date);
    }
}
