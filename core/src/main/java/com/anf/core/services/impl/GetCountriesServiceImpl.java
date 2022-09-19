package com.anf.core.services.impl;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.anf.core.services.GetCountriesService;
import com.day.cq.dam.api.Asset;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component(service = GetCountriesService.class, immediate = true, scope = ServiceScope.SINGLETON)
public class GetCountriesServiceImpl implements GetCountriesService {

    private static final Logger logger = LoggerFactory.getLogger(GetCountriesServiceImpl.class);

    private String countriesJsonDamPath;

    @ObjectClassDefinition(name = "Get Countries List service", description = "countries json location path")
    public @interface Config {
        @AttributeDefinition(name = "Countries Json Location", description = "This store the path countries json", type = AttributeType.STRING)
        String countries_dam_path() default "/content/dam/anf-code-challenge/exercise-1/countries.json";

    }

    @Activate
    protected void activate(GetCountriesServiceImpl.Config config) {
        countriesJsonDamPath = config.countries_dam_path();
    }

    @Override
    public SlingHttpServletRequest getDataFromSource(final SlingHttpServletRequest request) {

        final ResourceResolver resolver = request.getResourceResolver();
        try (final InputStream jsonStream = getJsonStreamFromDam(countriesJsonDamPath, resolver)) {
            if (jsonStream != null) {
                request.setAttribute(DataSource.class.getName(), streamToDataSource(jsonStream, resolver));
                logger.debug("Datasource from <{}> created", countriesJsonDamPath);
            }
        } catch (final IOException e) {
            logger.error("Could not close JSON input stream from node <{}>", countriesJsonDamPath, e);
        }
        return request;
    }

    /**
     * Reads an inputStream.
     *
     * @param inputStream inputStream of JSON file content
     * @param resourceResolver a resource resolver
     * @return a DataSource
     */

    private DataSource streamToDataSource(InputStream inputStream, ResourceResolver resourceResolver) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> result = objectMapper.readValue(inputStream, HashMap.class);

        return new SimpleDataSource(
                new TransformIterator<>(result.keySet().iterator(), (Transformer) o -> {
                    String value = (String) o;
                    ValueMap valueMap = new ValueMapDecorator(new HashMap<>());
                    valueMap.put("value", result.get(value));
                    valueMap.put("text", value);
                    return new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", valueMap);
                }));
    }

    /**
     * Reads a JSON file in the JCR and reads it as an InputStream.
     *
     * @param path     the path of the JSON file in the JCR
     * @param resolver a resource resolver
     * @return an InputStream containing the contents of the JSON file
     */
    private InputStream getJsonStreamFromDam(final String path, final ResourceResolver resolver) {
        Resource resource = resolver.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("Resource is null <" + path + "> does not exist");
        }
        Asset asset = resource.adaptTo(Asset.class);
        Resource original = asset.getOriginal();
        return original.adaptTo(InputStream.class);
    }

}
