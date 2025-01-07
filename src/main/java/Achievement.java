public class Achievement {
	private String title;
	private String description;
	private boolean completed;

	public Achievement(String title, String description) {
		this.title = title;
		this.description = description;
		this.completed = false;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public boolean isCompleted() {
		return completed;
	}
}
