package com.anf.core.servlets;

import com.anf.core.services.FetchPageService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletPaths(
        value = "/bin/getPages"
)
public class QueryPageServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    @Reference
    private FetchPageService fetchPageService;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws IOException {
        if(request.getParameter("a").equalsIgnoreCase("queryBuilder")){
            response.getWriter().write(fetchPageService.getPages());
        }else{
            response.getWriter().write(fetchPageService.getPagesSql());
        }

    }
}
