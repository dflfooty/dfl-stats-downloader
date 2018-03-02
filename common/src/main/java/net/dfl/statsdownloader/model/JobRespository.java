package net.dfl.statsdownloader.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRespository extends JpaRepository<Job, Long> {
	@Query("select case when count(j) > 0 then true else false end from Job j where j.year = :#{#job.year} and j.round = :#{#job.round} and j.status = 'Submitted'")
	boolean submittedJobExists(@Param("job") Job job);
	
}