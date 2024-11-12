package kickstart.Inventory.Products;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

// TODO Should this class have the Singleton template method?
// this @Embeddable is used because is a part of the Book entity
@Embeddable
public class Genre {
	// used to store a list of simple values inside an entity/embeddable
	// STATIC attributes only exist once per class!!!
	// TODO should this Set be of Strings or of Genres
	@ElementCollection
	private static Set<String> genres = new HashSet<>();

	// default constructor for JPA
	protected Genre() {}

	public Genre(String genre) {
		if (genre == null) {
			throw new NullPointerException("In Genre - Genre cannot be empty");
		}
		if(genre.isBlank()){
			throw new IllegalArgumentException("In Genre - Genre cannot be empty");
		}

		genres.add(genre.toLowerCase());
	}
	// TODO add tests !!!!!!!!!!!!!!!
	// TODO implement the equals and hashcode methods
	// to compare the genres by their name
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Genre genre)) {
			return false;
		}
		return genre.toString().equals(this.toString());
	}


	// Is this implementation correct
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	public Set<String> getGenres() {
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
