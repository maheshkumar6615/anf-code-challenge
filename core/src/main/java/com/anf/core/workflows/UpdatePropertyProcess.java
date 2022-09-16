package com.anf.core.workflows;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

/**
 * Workflow process which add a property pageCreated : true on the payload
 */

@Component(immediate = true, service = WorkflowProcess.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Add a property to the page",
        Constants.SERVICE_VENDOR + "=A & F",
        "process.label=Add Property Process"
})
public class UpdatePropertyProcess implements WorkflowProcess {

    private static final Logger logger = LoggerFactory.getLogger(UpdatePropertyProcess.class);

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {

        String path = workItem.getWorkflowData().getPayload().toString();
        final ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);
        Resource resource = resourceResolver.getResource(path);
        Node node = resource.adaptTo(Node.class);
        try {
            if (node.hasNode("jcr:content")) {
                node.getNode("jcr:content").setProperty("pageCreated", "true", PropertyType.BOOLEAN);
            }
        } catch (RepositoryException e) {
            logger.error("Repository Exception in workflow process {}", e);
        } finally {
            resourceResolver.close();
        }
    }
}
