package kickstart.Inventory;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

// this @Embeddable should be because it is a part of the ShopProduct
@Embeddable
public class Genre {
	// used to store a list of simple values inside of an entity/embeddable
	@ElementCollection
	private List<String> genres = new ArrayList<>();

	// default constructor for JPA
	protected Genre() {}

	public Genre(String genre) {
		if (genre == null || genre.isEmpty()) {
			throw new IllegalArgumentException("Genre cannot be empty");
		}
		genres.add(genre);
	}

	// is an addMethod required, if the constructor is already adding them

	public List<String> getGenres() {
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
