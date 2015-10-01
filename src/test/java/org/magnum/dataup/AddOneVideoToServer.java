/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.magnum.autograder.junit.Rubric;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.magnum.TestUtilities.VideoSvcApi;
import org.magnum.Video.Video;
import org.magnum.Video.VideoStatus;
import org.magnum.Video.VideoStatus.VideoState;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class AddOneVideoToServer {

	private static final String SERVER = "http://localhost:8080";

	private File testVideoData = new File(
			"src/test/resources/test.mp4");
	
	private Video video = Video.create().withContentType("video/mp4")
			.withDuration(123).withSubject(UUID.randomUUID().toString())
			.withTitle("SampleVideo").build();

	private VideoSvcApi videoSvc = new RestAdapter.Builder()
			.setEndpoint(SERVER).build()
			.create(VideoSvcApi.class);


	

	@Rubric(
			value="Ensuring that setting a new rating to a video works as intended",
			goal="Goal here ",  points=10.0, reference="Reference here"
			)
	@Test
	public void AddNewVideoRatings() throws Exception {	
			System.out.println("---Entered AddNewVideoRating() ---- ");
		
		Video addedVideo = videoSvc.addVideo(video);
		
		videoSvc.sendRatingToVideo(addedVideo.getId(), 2); 
		videoSvc.sendRatingToVideo(addedVideo.getId(), 4); 
		videoSvc.sendRatingToVideo(addedVideo.getId(), 1); 
		videoSvc.sendRatingToVideo(addedVideo.getId(), 5); 
		
		Video downloadedVideoObject = videoSvc.getVideoObject(addedVideo.getId()); 
		
		System.out.println("Rating 1: " + downloadedVideoObject.getRatingsList().get(1).getRating());

		assertEquals(3, downloadedVideoObject.getAverageRating() );
	}
	
	
	
	
	
	


}
