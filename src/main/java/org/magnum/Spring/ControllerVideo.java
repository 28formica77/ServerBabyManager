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
package org.magnum.Spring;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.Utilities.Constants;
import org.magnum.Video.Video;
import org.magnum.Video.VideoFileManager;
import org.magnum.Video.VideoStatus;
import org.magnum.VideoRepository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import retrofit.http.Streaming;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
public class ControllerVideo  {
 
	@Autowired
	private VideoRepository videoDirectory;

	@RequestMapping(value = "/", method = GET)
	public @ResponseBody String homepage() {
		return "BANANAAAAAAAAAA";
	}

	@RequestMapping(value = Constants.VIDEO_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideos() {
		return videoDirectory.getVideos();
	}	
	
	@RequestMapping(value = Constants.VIDEO_SVC_PATH, method = RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video video) {	
		
		System.out.println("--- Entered addVideo(...)---" );
		
		video.setId(Long.valueOf(videoDirectory.getVideos().size()) +1) ; // This is not very scalable, but it works nonetheless for now :)   
		video.setDataUrl(getDataUrl(video.getId())); 
		
		videoDirectory.addVideo(video);
				
			System.out.println("Video ID: " + video.getId()  );
			System.out.println("Data URL: " + video.getDataUrl()  );		
			System.out.println("ContentType: " +  video.getContentType()  );
			System.out.println("Duration: " + video.getDuration()   );
			System.out.println("Subject : " + video.getSubject()   );
			System.out.println("Title: " +  video.getTitle()  );

		return video;
	}	
	
    private String getDataUrl(long videoId){
        //String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
    	String url = "http://192.168.43.144/:8080" + "/video/" + videoId + "/data";  //TODO: Check the IP address here!  Make sure it matches ipconfig 
    	return url;
    }

 	private String getUrlBaseForLocalServer() {
	   HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	   
	   String base =   request.getServerName()   + ((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
	   return base;
	}

 	@RequestMapping(value = Constants.VIDEO_DATA_PATH, method = RequestMethod.POST)
 	public @ResponseBody VideoStatus setVideoData(
            @PathVariable(Constants.ID_PARAMETER) Long id,
            @RequestPart(Constants.DATA_PARAMETER) MultipartFile videoData,
            HttpServletResponse response) 
            		throws IOException {
 		
	
	 		System.out.println("--- Entered setVideoData ---" );
	 		
			System.out.println("Video ID: " + id  );
			System.out.println("Video ID: " + videoData  );
		
		response.setHeader("Content-Type", "application/json");
		
		if (videoDirectory.findById(id) == null) {
			response.setStatus(404); 
			return null ;
		}
		
 		try {
 			VideoFileManager videoFileManager = VideoFileManager.get() ; 	 	
 	 		videoFileManager.saveVideoData(videoDirectory.findById(id),  videoData.getInputStream()) ;  //This seems OK  	 		

 	 		return new VideoStatus(VideoStatus.VideoState.READY) ; 
 		} catch (Exception e) {
 			return null ;  
 		}		 
 	}
 	
 	@RequestMapping(value = Constants.VIDEO_DATA_PATH, method = RequestMethod.GET)
	@Streaming
	public @ResponseBody void getData(@PathVariable(Constants.ID_PARAMETER) Long id, HttpServletResponse response) throws IOException {

	 		System.out.println("--- Entered getData ---" );
	 		System.out.println("mapped ID: " + id  );
 		
 		if (videoDirectory.findById(id) == null) {
 			response.setStatus(404); 
 		} else {
			System.out.println("Video location: " + videoDirectory.findById(id).getLocation()  );
		
			VideoFileManager.get().copyVideoData(videoDirectory.findById(id), response.getOutputStream()) ; 

 		}

    }
 	
 	@RequestMapping(value = Constants.VIDEO_SEND_RATING_PATH, method = RequestMethod.POST)
	public @ResponseBody void sendRatingToVideo(@PathVariable(Constants.ID_PARAMETER) Long id, @RequestPart(Constants.DATA_PARAMETER) int rating, HttpServletResponse response) throws IOException {

 		
	 		System.out.println("--- Entered sendRatingToVideo ---" );
	 		System.out.println("mapped ID: " + id  );
	 		System.out.println("mapped Rating: " + rating  );
	 		
 		if (videoDirectory.findById(id) == null) {
 			response.setStatus(404); 
 		} else {
 			System.out.println("--- Here is where I should do stuff ---" );
 			VideoFileManager.get().addRatingToVideo(videoDirectory.findById(id), rating) ;  
 			videoDirectory.findById(id).calculateAverageRating() ;   
 			
 		}

 		//return true ; 
    }
 	
	@RequestMapping(value = Constants.VIDEO_GET_VIDEO_OBJECT, method = RequestMethod.GET)
	public @ResponseBody Video getVideoObject(@PathVariable(Constants.ID_PARAMETER) Long id) {
		return videoDirectory.findById(id); 	
	}	
	 
}
