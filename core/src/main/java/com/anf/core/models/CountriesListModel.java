package com.anf.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import com.anf.core.services.GetCountriesService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = {SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class CountriesListModel {

    @OSGiService
    private GetCountriesService getCountriesService;

    @Self
    private SlingHttpServletRequest request;

    @PostConstruct
    protected void init(){
        request = getCountriesService.getDataFromSource(request);
    }
}
