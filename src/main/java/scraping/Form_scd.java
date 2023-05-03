package scraping;

public class Form_scd {
	
    private String title;
    private String contents;
    private String writer;
    private String date;
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setClear() {
		this.title = null;
		this.contents = null;
		this.writer = null;
		this.date = null;
	}
    
}
