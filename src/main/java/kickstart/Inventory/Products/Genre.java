package kickstart.Inventory.Products;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

// this @Embeddable is used because is a part of the Book entity
@Embeddable
public class Genre {
	// used to store a list of simple values inside an entity/embeddable
	// STATIC attributes only exist once per class!!!
	@ElementCollection
	private static Set<Genre> genres = new HashSet<>();

	private String genre;

	// default constructor for JPA
	protected Genre() {}
	private Genre(String genre) {
		this.genre = genre;
	}

	// it kinda follows the Singleton pattern
	public static Genre createGenre(String name){
		if (name == null) {
			throw new NullPointerException("Genre creator - Genre cannot be null");
		}
		if(name.isBlank()){
			throw new IllegalArgumentException("Genre creator - Genre cannot be empty");
		}

		String norm_name = name.replaceAll("\\s", "").toLowerCase();
		for(Genre genre: genres){
			String norm_genre = genre.getGenre().replaceAll("\\s", "").toLowerCase();
			if(norm_genre.equals(norm_name)){
				// if the genre already exists, return it
				return genre;
			}
		}

		Genre newGenre = new Genre(name);
		genres.add(newGenre);
		return newGenre;
	}



	// Setters

	public static void deleteGenre(Genre genre) {
		if (genre == null) {
			throw new NullPointerException("Genre deleter - Genre cannot be null");
		}

		if (!genres.contains(genre)) {
			throw new IllegalArgumentException("Genre does not exist");
		}
		genres.remove(genre);
	}


	// Getters
	// this must be static for it to be reached from the whole system
	public static Set<Genre> getAllGenres() {
		return genres;
	}

	public String getGenre() {
		return genre;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Genre obj_genre)) {
			return false;
		}
		String normalized_genre = genre.replaceAll("\\s", "").toLowerCase();
		String normalized_obj_genre = obj_genre.getGenre().replaceAll("\\s", "").toLowerCase();
		return normalized_obj_genre.equals(normalized_genre);
	}

	@Override
	public int hashCode() {
		return genre.hashCode();
	}

}
