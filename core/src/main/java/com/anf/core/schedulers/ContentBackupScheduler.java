package com.aem.platform.schedulers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.AssetManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(immediate = true, service = ContentBackupScheduler.class)
@Designate(ocd = ContentBackupSchedulerConfiguration.class)
public class ContentBackupScheduler implements Runnable {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ContentBackupScheduler.class);
	

	/**
	 * Scheduler instance injected
	 */
	@Reference
	private Scheduler scheduler;
	
	@Reference
	private ResourceResolverFactory resourceResolverFactory;
	
	
	private ContentBackupSchedulerConfiguration config;
	
	private int schedulerId;
	
	 private static final Map<String, Object> DYNAMIC_MEDIA_AUTH =
		      Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "dynamicMediaService");
	/**
	 * Activate method to initialize stuff
	 * 
	 * @param config
	 */
	@Activate
	protected void activate(ContentBackupSchedulerConfiguration config) {
		log.info("scheduler activeted at :{}",new Date().toString());
		log.info("scheduler activeted with config root path:{}",config.rootPagePath());
		log.info("scheduler activeted with config root path:{}",config.cronExpression());
		this.config = config;
		addScheduler(config);
		log.info("scheduler aadded in activate");

	}
	
	
	/**
	 * Modifies the scheduler id on modification
	 * 
	 * @param config
	 */
	@Modified
	protected void modified(ContentBackupSchedulerConfiguration config) {
		
		/**
		 * Removing the scheduler
		 */
		removeScheduler();
		
		/**
		 * Updating the scheduler id
		 */
		schedulerId = config.schdulerName().hashCode();
		
		/**
		 * Again adding the scheduler
		 */
		addScheduler(config);
	}
	
	/**
	 * This method deactivates the scheduler and removes it
	 * @param config
	 */
	@Deactivate
	protected void deactivate(ContentBackupSchedulerConfiguration config) {
		
		/**
		 * Removing the scheduler
		 */
		removeScheduler();
	}
	
	/**
	 * This method removes the scheduler
	 */
	private void removeScheduler() {
		
		log.info("Removing scheduler: {}", schedulerId);
		
		/**
		 * Unscheduling/removing the scheduler
		 */
		scheduler.unschedule(String.valueOf(schedulerId));
	}
	
	/**
	 * This method adds the scheduler
	 * 
	 * @param config
	 */
	private void addScheduler(ContentBackupSchedulerConfiguration config) {
		
		/**
		 * Check if the scheduler is enabled
		 */
		if(config.enabled()) {
			
			/**
			 * Scheduler option takes the cron expression as a parameter and run accordingly
			 */
			ScheduleOptions scheduleOptions = scheduler.EXPR(config.cronExpression());
			
			/**
			 * Adding some parameters
			 */
			scheduleOptions.name(config.schdulerName());
			scheduleOptions.canRunConcurrently(false);
			log.info("Scheduling the scheduler :{}, with cron {}",config.schdulerName(), config.cronExpression());
			/**
			 * Scheduling the job
			 */
			scheduler.schedule(this, scheduleOptions);
			
			log.info("Scheduler added");
			
		} else {
			
			log.info("Scheduler is disabled");
			
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try (ResourceResolver resourceResolver =
		        resourceResolverFactory.getServiceResourceResolver(DYNAMIC_MEDIA_AUTH)) {
			log.info("scheduler started at :{}",new Date().toString());
			log.info("scheduler started with config root path:{}",config.rootPagePath());
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);	
			
			
			
			Page rootPage = pageManager.getPage(config.rootPagePath());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd_HHMMSS");
			String filename = "published_page_info_"+sdf.format(new Date())+".csv";
			File csvFile = new File(filename);
	      
			FileWriter fileWriter = new FileWriter(csvFile);
			StringBuilder line = new StringBuilder();
			line.append("\"s.no\",\"Page Path\",\"Page Title\",\"hide in Nav\",\"Prop1\",\"Prop2\",\"Prop3\"");
			line.append("\n");
			if(null!=rootPage) {
				Iterator<Page> pageIter = rootPage.listChildren(null,true);
				int count = 0;
				int totalCount=0;
				List<String> list = new ArrayList<String>();
				while(pageIter.hasNext()) {
					
					Page page = pageIter.next();
					ValueMap vm = page.getProperties();
					log.info("Total Count :{}", totalCount++);
					log.info("Page :{}", page.getPath());
					log.info("Hide in nav :{}", vm.get("hideInNav"));
					log.info("cq:lastReplicationAction :{}", vm.get("cq:lastReplicationAction"));
	
	
					if(vm.containsKey("hideInNav") && vm.get("hideInNav").toString().equals("true") && 
							vm.containsKey("cq:lastReplicationAction") && 
							vm.get("cq:lastReplicationAction").toString().equals("Activate")){
						list.add(page.getPath());
						
						
						line.append("\"");
		                line.append(++count);
		                line.append("\",");
		                
		                line.append("\"");
		                line.append(page.getPath());
		                line.append("\",");
		                
		                line.append("\"");
		                line.append(page.getTitle().replaceAll("\"","\"\""));
		                line.append("\",");
		                
		                line.append("\"");
		                line.append(vm.get("hideInNav").toString());
		                line.append("\",");
		                
		                line.append("\"");
		                line.append(new Date().toString());
		                line.append("\",");
		                
		                line.append("\"");
		                line.append(vm.get("hideInNav").toString());
		                line.append("\",");
		                
		                line.append("\"");
		                line.append(vm.get("hideInNav").toString());
		                line.append("\"");
		                
						log.info("Count :{}", count);
						line.append("\n");
			            
					}
				}
				fileWriter.write(line.toString());
				fileWriter.close();
				log.info("<<=========================================>>");
				log.info("Pages pulled are : {}, {}",count, list);
				log.info("<<=========================================>>");
			} else {
				log.info("Root page is not avialable : {}",config.rootPagePath());
			}
			InputStream fis = new FileInputStream(filename);
			AssetManager manager = resourceResolver.adaptTo(AssetManager.class);
			manager.createAsset("/content/dam/backup/"+filename, fis, "text/csv", true);
			
			
		} catch (LoginException e) {
		      log.error("Error while getting dynamicMediaService resource resolver,{}", e.getMessage());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
