package net.dfl.statsdownloader.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

@Service
public class GoogleDriveService {

	private static final String APPLICATION_NAME = "dfl-stats-downloader";
	private static final String JSON_KEY = System.getenv("GOOGLE_JSON_KEY");

	private static final String UPLOAD_FOLDER = System.getenv("GOOGLE_UPLOAD_FOLDER");
	private static final String DRIVE_OWNER = System.getenv("GOOGLE_DRIVE_OWNER");

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static HttpTransport HTTP_TRANSPORT;
	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	private GoogleCredential authorize() throws Exception {

		InputStream jsonKeyStream = new ByteArrayInputStream(JSON_KEY.getBytes());
		GoogleCredential credential = GoogleCredential.fromStream(jsonKeyStream, HTTP_TRANSPORT, JSON_FACTORY)
				.createScoped(SCOPES);

		return credential;
	}

	private Drive getDriveService() throws Exception {
		GoogleCredential credential = authorize();
		Drive googleDrive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.build();

		return googleDrive;
	}

	public String saveToGoogleDrive(Path filePath) throws Exception {
		String fileUrl;
		
		Drive driveService = getDriveService();
		
		String filter = "name='" + UPLOAD_FOLDER + "' and mimeType='application/vnd.google-apps.folder'";
		
		FileList result = driveService.files().list()
						  .setQ(filter)
						  .setFields("files(id)")
						  .execute();
		
		String uploadFolderId = result.getFiles().get(0).getId(); 
		
		System.out.println("Upload folder ID: " + uploadFolderId);
		
		File fileMetadata = new File();
		fileMetadata.setName(filePath.getFileName().toString());
		fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
		fileMetadata.setParents(Collections.singletonList(uploadFolderId));

		java.io.File localFile = filePath.toFile();
		FileContent mediaContent = new FileContent("text/csv", localFile);
		
		File googleDriveFile = driveService.files().create(fileMetadata, mediaContent)
					.setFields("id, parents, webContentLink, webViewLink")
					.execute();
		
		Permission anyonePermission = new Permission()
				  .setType("anyone")
				  .setRole("reader");
		
		
		driveService.permissions().create(googleDriveFile.getId(), anyonePermission)
		  						  .setFields("id")
		  						  .execute();
		
		Permission ownerPermission = new Permission()
			    						.setType("user")
			    						.setRole("owner")
			    						.setEmailAddress(DRIVE_OWNER);
		
		driveService.permissions().create(googleDriveFile.getId(), ownerPermission)
								  .setFields("id")
								  .setTransferOwnership(true)
								  .execute();
		
		googleDriveFile.setShared(true);
		
		fileUrl = googleDriveFile.getWebViewLink();

		return fileUrl;
	}
}
