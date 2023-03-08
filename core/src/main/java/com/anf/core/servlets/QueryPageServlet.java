package com.anf.core.servlets;

import com.anf.core.services.FetchPageService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = {Servlet.class})
@SlingServletPaths(
        value = "/bin/getPages"
)
public class QueryPageServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);
    @Reference
    private FetchPageService fetchPageService;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response) {
        try {
            if (request.getParameter("a").equalsIgnoreCase("queryBuilder")) {
                response.getWriter().write(fetchPageService.getPages());
            } else {
                response.getWriter().write(fetchPageService.getPagesSql());
            }
        } catch (IOException e) {
            logger.error("IOException in Query Page Servlet", e.getMessage());
        }
    }
}
