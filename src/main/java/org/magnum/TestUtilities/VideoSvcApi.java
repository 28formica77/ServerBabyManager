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
package org.magnum.TestUtilities;

import java.util.Collection;

import org.magnum.Video.Video;
import org.magnum.Video.VideoStatus;
import org.magnum.Utilities.Constants;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;


public interface VideoSvcApi {

 
	@GET(Constants.VIDEO_SVC_PATH)
	public Collection<Video> getVideoList();
 
	@POST(Constants.VIDEO_SVC_PATH)
	public Video addVideo(@Body Video v);
	
	@Multipart
	@POST(Constants.VIDEO_DATA_PATH)
	public VideoStatus setVideoData(@Path(Constants.ID_PARAMETER) long id, @Part(Constants.DATA_PARAMETER) TypedFile videoData);
	
	@Multipart
	@POST(Constants.VIDEO_SEND_RATING_PATH)
	public Response sendRatingToVideo(@Path(Constants.ID_PARAMETER) Long id, @Part(Constants.DATA_PARAMETER) int rating );
	 
	@GET(Constants.VIDEO_GET_VIDEO_OBJECT)
	public Video getVideoObject(@Path(Constants.ID_PARAMETER) Long id);
	
	@Streaming
    @GET(Constants.VIDEO_DATA_PATH)
    Response getData(@Path(Constants.ID_PARAMETER) long id);
	
}
