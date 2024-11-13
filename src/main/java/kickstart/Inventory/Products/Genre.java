package kickstart.Inventory.Products;
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

	private String genre;

	// default constructor for JPA
	protected Genre() {}
	public Genre(String genre) {
		if (genre == null) {
			throw new NullPointerException("Genre Constructor - Genre cannot be empty");
		}
		if(genre.isBlank()){
			throw new IllegalArgumentException("Genre Constructor - Genre cannot be empty");
		}
		this.genre = genre;
		genres.add(genre.trim().toLowerCase());
	}


	// Setters

	public void deleteGenre(String genre) {
		if (genre == null) {
			throw new NullPointerException("Genre deleter - Genre cannot be null");
		}
		if(genre.isBlank()){
			throw new IllegalArgumentException("Genre deleter - Genre cannot be empty");
		}
		if (!genres.contains(genre)) {
			throw new IllegalArgumentException("Genre does not exist");
		}
		genres.remove(genre);
	}


	// Getters

	// this must be static for it to be reached from the whole system
	// this does not quite feel good, for it to be a Set of Strings
	// should it be a Set of Genres?
	public static Set<String> getAllGenres() {
		return genres;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Genre)) {
			return false;
		}
		return genre.equals(this.getGenre());
	}

	@Override
	public int hashCode() {
		return genre.hashCode();
	}

	// it does not feel right that the return type is a String and not a Genre
	public Set<String> getGenresSet() {
		return genres;
	}

	public String getGenre() {
		return genre;
	}


}
