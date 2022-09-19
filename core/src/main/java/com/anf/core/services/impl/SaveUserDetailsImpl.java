package com.anf.core.services.impl;

import com.anf.core.services.SaveUserDetails;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = SaveUserDetails.class)
public class SaveUserDetailsImpl implements SaveUserDetails {

    private static final Logger logger = LoggerFactory.getLogger(SaveUserDetailsImpl.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private String ageInfoLocation;
    private String saveUserDetailsPath;

    @ObjectClassDefinition(name = "Save User Details Service", description = "Save User Details service Info")
    public @interface Config {
        @AttributeDefinition(name = "valid age info location", description = "This store the path of valid age criteria", type = AttributeType.STRING)
        String age_info_location() default "/etc/age";

        @AttributeDefinition(name = "save user details path", description = "This store the path where user details can be stored", type = AttributeType.STRING)
        String save_user_details_path() default "/var/anf-code-challenge/code-challenge";
    }

    @Activate
    protected void activate(SaveUserDetailsImpl.Config config) {
        ageInfoLocation = config.age_info_location();
        saveUserDetailsPath = config.save_user_details_path();
    }

    @Override
    public String commitUserDetails(SlingHttpServletRequest request) {
        Boolean isAgeValid = validateAge(request);
        if (isAgeValid) {
            return saveUserDetails(request);
        }
        return "You are not eligible";
    }

    /**
     * Gets User details from SlingHttpServletRequest and saves it in JCR.
     * @param request a SlingHttpServletRequest
     * @return a String
     */
    private String saveUserDetails(SlingHttpServletRequest request) {
        logger.debug("inside save user details");
        ResourceResolver resourceResolver = getResourceResolver();
        Resource resource = resourceResolver.getResource(saveUserDetailsPath);
        Session session = resourceResolver.adaptTo(Session.class);
        Node node = resource.adaptTo(Node.class);
        try {
            logger.debug("inside save user details try: {}", node.getPath());
            node.setProperty("firstName", request.getParameter("firstName"));
            node.setProperty("lastName", request.getParameter("lastName"));
            node.setProperty("age", request.getParameter("age"));
            node.setProperty("country", request.getParameter("country"));
            session.save();
        } catch (RepositoryException e) {
            logger.error("repository exception when saving user details: {}", e);
        } finally {
            session.logout();
            resourceResolver.close();
        }
        return "Successful";
    }

    /**
     * Reads the age from SlingHttpServletRequest.
     * @param request a SlingHttpServletRequest
     * @return a Boolean
     */
    private Boolean validateAge(SlingHttpServletRequest request) {

        String age = request.getParameter("age");
        final ResourceResolver resourceResolver = getResourceResolver();
        Resource resource = resourceResolver.getResource(ageInfoLocation);

        Node node = resource.adaptTo(Node.class);
        try {
            String minAge = node.hasProperty("minAge") ? node.getProperty("minAge").getString() : "";
            String maxAge = node.hasProperty("maxAge") ? node.getProperty("maxAge").getString() : "";
            logger.debug("age: {}, min age: {}, max age: {}", age, minAge, maxAge);
            return getIntegerValue(age) > getIntegerValue(minAge) && getIntegerValue(age) < getIntegerValue(maxAge);
        } catch (RepositoryException e) {
            logger.error("repository exception when validating age: {}", e);
        } finally {
            resourceResolver.close();
        }
        return false;
    }

    /**
     * Reads the integer.
     * @param age a String
     * @return int
     */
    private int getIntegerValue(String age) {
        return Integer.parseInt(age);
    }

    /**
     * Method to create resource resolver.
     * @return ResourceResolver
     */
    private ResourceResolver getResourceResolver() {
        Map<String, Object> param = new HashMap<>();
        param.put(resourceResolverFactory.SUBSERVICE, "writeService");
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        } catch (LoginException e) {
            logger.error("LoginException exception when creating ResourceResolver : {}", e);
        }
        return resourceResolver;
    }
}