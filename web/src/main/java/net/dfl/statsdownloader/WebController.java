package net.dfl.statsdownloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.dfl.statsdownloader.exception.JobSubmittedException;
import net.dfl.statsdownloader.model.Job;
import net.dfl.statsdownloader.service.JobService;

@Controller
public class WebController {
	
	private static final String INDEX_VIEW = "index";
	private static final String JOBS_FRAGMENT = "results::jobsList";
	private static final String REDIRECT_INDEX = "redirect:/";

	@Autowired
	private JobService jobService;
	
	@ModelAttribute
	public Job prepareJobModel() {
		return new Job();
	}
		
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@ModelAttribute("errorMessage") String errorMessage, Model model) {	
    		if(errorMessage != null) {
    			model.addAttribute("errorMessage", errorMessage);
    		}
    	
    		prepareJobModel();

        return INDEX_VIEW;
    }
    
    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public String getJobs(Model model){
    		model.addAttribute("jobs", jobService.getJobsList());
        return JOBS_FRAGMENT;
    }
    
    @RequestMapping(value = "/jobs/submit", method = RequestMethod.POST)
    public String submitJob(Job newJob, Model model){
    		jobService.submitNewJob(newJob);
        return REDIRECT_INDEX;
    }
    
	@ExceptionHandler({JobSubmittedException.class})
	public String handleDatabaseError(JobSubmittedException e, RedirectAttributes redirectAttrs) {	
		redirectAttrs.addFlashAttribute("errorMessage", "error.jobSubmitted.exists");
		return REDIRECT_INDEX;
	}
}
