package scrapingList;

public class UrlInfo {

	private String position;
	private String season;
	private String team;
	private String url;
	private String innerUrl = "div.box-body.no-padding.table-responsive > table.table.table-striped.table-responsive.table-condensed.no-space.table-bordered > tbody > tr > td:nth-child(2) > a";
	private String plyrName = "section:nth-child(3) > div > div:nth-child(1) > div > div:nth-child(1) > div > div:nth-child(2) > button > font";
	private String plyrNumber="section:nth-child(3) > div > div:nth-child(1) > div > div:nth-child(1) > div > div:nth-child(1) > div > div:nth-child(2)";
	private String plyrInfo = "div > div:nth-child(1) > div > div:nth-child(2) > ul > li";
	private String plyrList = "div:nth-child(2) > div > div:nth-child(3) > div > div > table > tbody > tr:not([class*=colhead_stz0])";
	private String plyrRC = "div:nth-child(2) > div > div:nth-child(3) > div > div > table > tbody > tr:not([class*=colhead_stz0])";
	
	
	
	
	public String getPlyrName() {
		return plyrName;
	}

	public void setPlyrName(String plyrName) {
		this.plyrName = plyrName;
	}

	public String getPlyrNumber() {
		return plyrNumber;
	}

	public void setPlyrNumber(String plyrNumber) {
		this.plyrNumber = plyrNumber;
	}

	public String getPlyrList() {
		return plyrList;
	}

	public void setPlyrList(String plyrList) {
		this.plyrList = plyrList;
	}

	public void setUrl(String position, String season, String team, String sort) {
		this.position = position;
		this.season = season;
		this.team = team;

		this.url = "http://www.statiz.co.kr/stat.php?opt=0&sopt=0&re=" + position + "&ys=" + season + "&ye=" + season
				+ "&se=0&te=" + team + "&tm=&ty=0&qu=10&po=0&as=&ae=&hi=&un=&pl=&da=1" + sort
				+ "&de=1&lr=0&tr=&cv=&ml=1&sn=50&si=&cn=";
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInnerUrl() {
		return innerUrl;
	}

	public void setInnerUrl(String innerUrl) {
		this.innerUrl = innerUrl;
	}

	public String getPlyrInfo() {
		return plyrInfo;
	}

	public void setPlyrInfo(String plyr_info) {
		this.plyrInfo = plyr_info;
	}

	public String getPlyrRC() {
		return plyrRC;
	}

	public void setPlyrRC(String plyrRC) {
		this.plyrRC = plyrRC;
	}

}
