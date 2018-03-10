package net.dfl.statsdownloader.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.dfl.statsdownloader.exception.JobSubmittedException;
import net.dfl.statsdownloader.model.Job;
import net.dfl.statsdownloader.model.JobRespository;
import net.dfl.statsdownloader.pubsub.RedisMessagePublisher;

@Service
public class JobService {
	
	private static final Logger log = LoggerFactory.getLogger(JobService.class);

	@Autowired
	private JobRespository jobRespositry;
	
	@Autowired
	private RedisMessagePublisher jobPublisher;
		
	public List<Job> getJobsList() {
		List<Job> jobs = jobRespositry.findAll();
		return jobs;
	}
	
	public Job submitNewJob(Job job) {
		if(jobRespositry.submittedJobExists(job)) {
			log.info("Job exists: {}", job);
			throw new JobSubmittedException();
		}
		
		Date now = new Date();
		job.setCreatedAt(now);
		job.setUpdatedAt(now);
		job.setStatus("Submitted");
		
		Job savedJob = jobRespositry.saveAndFlush(job);
		jobPublisher.publish(savedJob);
		
		log.info("Published job: {}", savedJob);
		
		return savedJob;
	}
	
	public Job setJobRunning(Long id) {
		Job job = jobRespositry.findOne(id);		
		
		Date now = new Date();
		job.setUpdatedAt(now);
		job.setStatus("Running");
		
		Job savedJob = jobRespositry.saveAndFlush(job);
		
		log.info("Running job: {}", savedJob);
		
		return savedJob;
	}
	
	public Job completeJob(Job job, String status) {
		Date now = new Date();
		job.setUpdatedAt(now);
		job.setStatus(status);
		
		Job savedJob = jobRespositry.saveAndFlush(job);
		
		log.info("Completed job: {}", savedJob);
		
		return savedJob;
	}
	
	public void cleanUncompletedJobs() {
		List<Job> uncompletedJobs = jobRespositry.findUncompletedJobs();
		for(Job job : uncompletedJobs) {
			Date now = new Date();
			job.setUpdatedAt(now);
			job.setStatus("Cancelled");
			jobRespositry.save(job);
			log.info("Cleaned job: {}", job);
		}
		jobRespositry.flush();
	}
}
