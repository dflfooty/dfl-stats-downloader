package net.dfl.statsdownloader;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.dfl.statsdownloader.helpers.StatsHelper;
import net.dfl.statsdownloader.model.Job;
import net.dfl.statsdownloader.service.GoogleDriveService;
import net.dfl.statsdownloader.service.JobService;

@Service
public class WorkerService {
	
	@Autowired
	JobService jobService;
	
	@Autowired
	GoogleDriveService googleDriveService;
		
	public void work(Job job) {

		String status = "Completed";
		
		try {
			job = jobService.setJobRunning(job.getId());
		
			StatsHelper statsHelper = new StatsHelper(job.getRound().toString(), job.getYear().toString());
			Path statsCsvFile = statsHelper.execute();
			
			String csvFileUrl = googleDriveService.saveToGoogleDrive(statsCsvFile);
						
			job.setFile(csvFileUrl);
		} catch (Exception e) {
			status = "Failed";
			e.printStackTrace();
		} finally {
			jobService.completeJob(job, status);
		}
	}
}
