package net.dfl.statsdownloader.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.dfl.statsdownloader.service.JobService;

@Component
public class JobCleanerTask {

	private static final Logger log = LoggerFactory.getLogger(JobCleanerTask.class);
	
	@Autowired
	JobService jobService;
	
	@Scheduled(fixedRate=300000)
	public void cleanJobs() {
		log.info("Cleaning tasks .... ");
		jobService.cleanUncompletedJobs();
	}	
}
