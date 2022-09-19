package com.anf.core.servlets;

import com.anf.core.services.SaveUserDetails;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component(service = {Servlet.class})
@SlingServletPaths(
        value = "/bin/saveUserDetails"
)
public class UserServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);
    @Reference
    private SaveUserDetails saveUserDetails;

    @Override
    protected void doPost(final SlingHttpServletRequest request,
                          final SlingHttpServletResponse response) {
        try {
            /*response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));*/
            response.getWriter().write(saveUserDetails.commitUserDetails(request));
        } catch (IOException e) {
            logger.error("IO Exception in User Servlet: {}", e.getMessage());
        }

    }
}
