package kickstart.Inventory;

public class Book extends Product{
	private int ISBN;
	private String author;
	private String publisher;
	private String coverImage;

	public Book(String name, int price, String description, int ISBN, String author, String publisher, String coverImage ){
		super(name, price, description);

		if(author == null){
			throw new NullPointerException("Author can not be null");
		}
		if(publisher == null){
			throw new NullPointerException("publisher can not be null");
		}
		if(coverImage == null){
			throw new NullPointerException("coverImage can not be null");
		}

		if(author.isBlank()){
			throw new IllegalArgumentException("There must be an author");
		}

		// Is this a String or something else?
		if(coverImage.isBlank()){
			throw new IllegalArgumentException("There must be a coverImage");
		}
		if(publisher.isBlank()){
			throw new IllegalArgumentException("There must be a publisher");
		}


		this.ISBN = ISBN;
		this.author = author;
		this.publisher = publisher;
		this.coverImage = coverImage;
	}

	public String getAuthor() {
		return author;
	}

	public int getISBN(){
		return ISBN;
	}

	public String getPublisher(){
		return publisher;
	}

	public String getCoverImage(){
		return coverImage;
	}
}