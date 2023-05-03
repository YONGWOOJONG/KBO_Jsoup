package scrapingFunction;

import java.util.List;
import scraping.Form_scd;
import scrapingList.UrlInfo;

public interface ScrapingInterface {
	void scraping(UrlInfo url, String POSITION, String SEASON, String TEAM, int fileCount)
			throws InterruptedException;

}
