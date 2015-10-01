package org.magnum.VideoRepository;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.magnum.Video.Video;

public class NoDuplicatesVideoRepository implements VideoRepository {

	// Sets only store one instance of each object and will not
	// store a duplicate instance if two objects are .equals()
	// to each other.
	//
	private Set<Video> videoSet = Collections.newSetFromMap(
	        new ConcurrentHashMap<Video, Boolean>());
	
	@Override
	public boolean addVideo(Video v) {
		return videoSet.add(v);
	}

	@Override
	public Collection<Video> getVideos() {
		return videoSet;
	}

	// Search the list of videos for ones with  matching titles.
	@Override
	public Collection<Video> findByTitle(String title) {
		Set<Video> matches = new HashSet<>();
		for(Video video : videoSet){
			if(video.getTitle().equals(title)){
				matches.add(video);
			}
		}
		return matches;
	}
	
	public Video findById(Long id) {
		
		for(Video video : videoSet){
			if(video.getId() == id ) {
				System.out.println("Success: Found a match through findById for video #" + id  );
				return video ;
			}
		} 
		System.out.println("Failure: Didn't find a match through findById for video #" + id   );
		return null ; 
	}

}
