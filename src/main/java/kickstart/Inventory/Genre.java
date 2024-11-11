package kickstart.Inventory;

import java.util.ArrayList;

import jakarta.persistence.Embeddable;

// this @Embeddable should be because it is a part of the ShopProduct
@Embeddable
public class Genre {
	private String genre;
	private ArrayList<String> genres;

	protected Genre() {}

	public Genre(String genre) {
		if (genre == null || genre.isEmpty()) {
			throw new IllegalArgumentException("Genre cannot be empty");
		}
		genres = new ArrayList<String>();
		this.genre = genre;
		// like this or make an own method for adding?
		genres.add(genre);
	}

	// check the related problem
	public ArrayList<String> getGenres() {
		return genres;
	}


	public void deleteGenre(String genre) {
		if (genre == null || genre.isEmpty()) {
			throw new IllegalArgumentException("Genre cannot be empty");
		}
		if (!genres.contains(genre)) {
			throw new IllegalArgumentException("Genre does not exist");
		}
		genres.remove(genre);
	}

}
