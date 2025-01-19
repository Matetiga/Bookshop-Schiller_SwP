package kickstart.Inventory;
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

	private String genreName;

	// default constructor for JPA

	/**
	 * default constructor for JPA
	 */
	protected Genre() {}
	private Genre(String genreName) {
		this.genreName = genreName;
	}

	// it kinda follows the Singleton pattern

	/**
	 *
	 * @param name
	 * @return
	 */
	public static Genre createGenre(String name){
		if (name == null) {
			throw new NullPointerException("Genre creator - Genre cannot be null");
		}
		if(name.isBlank()){
			throw new IllegalArgumentException("Genre creator - Genre cannot be empty");
		}

		String norm_name = name.replaceAll("\\s", "").toLowerCase();
		for(Genre genreName: genres){
			String norm_genre = genreName.getGenre().replaceAll("\\s", "").toLowerCase();
			if(norm_genre.equals(norm_name)){
				// if the genre already exists, return it
				return genreName;
			}
		}

		Genre newGenre = new Genre(name);
		genres.add(newGenre);
		return newGenre;
	}



	// Setters

	/**
	 *
	 * @param genreName
	 */
	public static void deleteGenre(Genre genreName) {
		if (genreName == null) {
			throw new NullPointerException("Genre deleter - Genre cannot be null");
		}

		if (!genres.contains(genreName)) {
			throw new IllegalArgumentException("Genre does not exist");
		}
		genres.remove(genreName);
	}


	// Getters
	// this must be static for it to be reached from the whole system

	/**
	 *
	 * @return
	 */
	public static Set<Genre> getAllGenres() {
		return genres;
	}

	/**
	 *
	 * @return
	 */
	public String getGenre() {
		return genreName;
	}

	/**
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Genre objGenre ) {
			// Compare two Genre objects
			String normalizedGenre = genreName.replaceAll("\\s", "").toLowerCase();
			String normalizedObjGenre = objGenre.getGenre().replaceAll("\\s", "").toLowerCase();
			return normalizedGenre.equals(normalizedObjGenre);
		} else if (obj instanceof String objString) {
			// Compare a Genre object with a String
			String normalizedGenre = genreName.replaceAll("\\s", "").toLowerCase();
			String normalizedString = objString.replaceAll("\\s", "").toLowerCase();
			return normalizedGenre.equals(normalizedString);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return genreName.hashCode();
	}

}
