package com.aem.platform.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "ContentBackupSchedulerConfiguration", description = "Sling scheduler configuration")
public @interface ContentBackupSchedulerConfiguration {
	/**
	 * This method will return the name of the Scheduler
	 * 
	 * @return {@link String}
	 */
	@AttributeDefinition(
			name = "Scheduler name", 
			description = "Name of the scheduler", 
			type = AttributeType.STRING)
	public String schdulerName() default "Content Backup Scheduler Configuration";

	/**
	 * This method will check if the scheduler is concurrent or not
	 * 
	 * @return {@link Boolean}
	 */
	@AttributeDefinition(
			name = "Enabled", 
			description = "True, if scheduler service is enabled", 
			type = AttributeType.BOOLEAN)
	public boolean enabled() default true;
	
	/**
	 * This method returns the Cron expression which will decide how the scheduler will run
	 * 
	 * @return {@link String}
	 */
	@AttributeDefinition(
			name = "Cron Expression", 
			description = "Cron expression used by the scheduler", 
			type = AttributeType.STRING)
	public String cronExpression() default "0 * * * * ?";

	/**
	 * This method returns a custom parameter just to show case the functionality
	 * 
	 * @return {@link String}
	 */
	@AttributeDefinition(
			name = "Root page path", 
			description = "Root page path which pages content to be taken backup", 
			type = AttributeType.STRING)
	public String rootPagePath() default "/content/bdl";
}
