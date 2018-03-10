function retrieveJobs() {
	var url = '/jobs';
	$("#jobsBlock").load(url, function() {
		$('#jobs').DataTable({
			"columnDefs": [
				{ "orderable": false, "targets": '_all' },
				{ "type": "date", "targets": 3 },
				{ "type": "date", "targets": 4 }
			],
			"searching": false,
			"order": [ 4, 'desc' ],
			"pageLength": 20,
			"lengthChange": false
		});
	});
}