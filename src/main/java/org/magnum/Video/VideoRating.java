package org.magnum.Video;

public class VideoRating {

	
	private int Rating ; 
	
	public int getRating() {
		return Rating;
	}

	public VideoRating () { }
	
	public VideoRating (int rating) {
		this.Rating = rating ; 
	}
	
	public void setRating(int Rating) {
		CheckRatingValidity(Rating); 
		this.Rating = Rating;
	}
	
	private void CheckRatingValidity (int Rating) {
		if (Rating < 1 | Rating > 5) 
			throw new IllegalArgumentException("Ratings must be integers ranging between 1 and 5") ;
	}
	
}

