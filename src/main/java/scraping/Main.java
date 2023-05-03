package scraping;

import scrapingFunction.ScrapingFunc;
import scrapingFunction.ScrapingInterface;
import scrapingList.UrlInfo;
import saveToFile.ExcelToDB;


import java.util.ArrayList;
import java.util.List;

public class Main {

	// 스크래핑할 포지션 0 = 타자(타격), 1 = 투수(투구)
	final static String[] POSITION = { "0", "1" };
//	final static String[] POSITION = { "1" };
	final static String[] SORT = { "&o1=WAR_ALL_ADJ&o2=TPA", "&o1=WAR&o2=OutCount" };
//	final static String[] SORT = {"&o1=WAR&o2=OutCount" };
	// 스크래핑할 시즌
	final static String[] SEASON = { "2022", "2021" };
//	final static String[] SEASON = { "2021" };
	// 스크래핑할 팀
	final static String[] TEAM = { "두산", "롯데", "삼성", "키움", "한화", "KIA", "KT", "LG", "NC", "SSG"};
//	final static String[] TEAM = {"SK"};


	public static void main(String[] args) throws InterruptedException {

		// url 클래스
		UrlInfo url = new UrlInfo();


		// 크롤링 함수
		ScrapingInterface sc = new ScrapingFunc();

		// 파일 번호를 추적하는 변수 추가
		int fileCount = 1;

		// 포지션 순회
		for (int i = 0; i < POSITION.length; i++) {

			// 시즌 순회
			for (int j = 0; j < SEASON.length; j++) {

				// 팀 순회
				for (int k = 0; k < TEAM.length; k++) {
					url.setUrl(POSITION[i], SEASON[j], TEAM[k], SORT[i]);
					System.out.println(url.getUrl());

					sc.scraping(url, POSITION[i], SEASON[j], TEAM[k], fileCount);

				}
			}
		}
		System.out.println("크롤링 종료");
	}
}
