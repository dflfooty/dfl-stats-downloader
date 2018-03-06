package net.dfl.statsdownloader.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.dfl.statsdownloader.service.JobService;

@Component
public class JobCleanerTask {

	@Autowired
	JobService jobService;
	
	public void cleanJobs() {
		jobService.cleanUncompletedJobs();
	}	
}
