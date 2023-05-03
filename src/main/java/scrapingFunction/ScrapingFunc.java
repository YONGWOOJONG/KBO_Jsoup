package scrapingFunction;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import saveToFile.ExcelToDB;
import saveToFile.SaveToExcel;
import scraping.DateUtils;
import scrapingList.UrlInfo;

public class ScrapingFunc implements ScrapingInterface {

	String opt = "opt=3";
	String ID;
	String name;
	// 메인 클래스에서 ExcelToDB 사용
	ExcelToDB excelToDB = new ExcelToDB();

	// 크롤링 메서드를 호출하고 반환된 데이터를 리스트에 추가
	List<String[]> newDataIF = new ArrayList<String[]>();
	// 크롤링 메서드를 호출하고 반환된 데이터를 리스트에 추가
	List<String[]> newDataRC = new ArrayList<String[]>();

	@Override
	public void scraping(UrlInfo url, String POSITION, String SEASON, String TEAM, int fileCount) throws InterruptedException {

		try {

			// URL에 연결
			Connection conn = Jsoup.connect(url.getUrl()).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
					+ "AppleWebKit/537.36 (KHTML, like Gecko) " + "Chrome/58.0.3029.110 Safari/537.36");

			Document document = conn.get();
			Elements linkElements = document.select(url.getInnerUrl());
			System.out.println("돌고있다.");
			for (int l = 0; l < linkElements.size(); l++) {

				// 내부 URL로 이동
				final String innerurl = linkElements.get(l).attr("abs:href");
				Connection innerConn = Jsoup.connect(innerurl);
				Document innerDocument = innerConn.get();

				// URL 수정
				String currentPageUrl = innerDocument.location();
				int questionMarkIndex = currentPageUrl.indexOf('?');
				String modifiedUrl = currentPageUrl;

				if (questionMarkIndex != -1) {
					modifiedUrl = currentPageUrl.substring(0, questionMarkIndex + 1) + "opt=3&"
							+ currentPageUrl.substring(questionMarkIndex + 1) +"&re=" + POSITION + "&se=&da=&year=" + SEASON + "&cv=";
				}

				// 수정된 URL에 연결
				Connection modifiedConn = Jsoup.connect(modifiedUrl)
						.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
								+ "AppleWebKit/537.36 (KHTML, like Gecko) " + "Chrome/58.0.3029.110 Safari/537.36");
				Document plyrRCDocument = modifiedConn.get();

				System.out.println("돌고있다.2");
				Thread.sleep((int) (Math.random() * 500) + 50);

				try {
					Elements plyrNameElements = plyrRCDocument.select(url.getPlyrName());
					Elements plyrNumberElements = plyrRCDocument.select(url.getPlyrNumber());
					Elements plyrInfoElements = plyrRCDocument.select(url.getPlyrInfo());
					Elements plryListElements = plyrRCDocument.select(url.getPlyrRC());

					if (plyrInfoElements.size() > 0) {
						// 선수 이름, 번호, 정보를 저장할 행
						String[] rowData = new String[13];

						// 선수 이름
						System.out.println("이름: " + plyrNameElements.get(0).text());
						rowData[1] = plyrNameElements.get(0).text();
						name= plyrNameElements.get(0).text();

						// 선수 번호
						System.out.println("번호: " + plyrNumberElements.get(0).text());
						rowData[2] = plyrNumberElements.get(0).text();
						
					    // 선수 정보
						for (int m = 0; m < plyrInfoElements.size(); m++) {
							plyrInfoElements.get(m).children().remove();
							
							if (m == 0) {
								try {
									Date formattedDate = DateUtils.convertStringToDate(plyrInfoElements.get(0).text());
						            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
						            String formattedDateString = outputFormat.format(formattedDate);
						            Element formattedDateElement = new Element(Tag.valueOf("div"), "");
						            formattedDateElement.appendText(formattedDateString);
						            plyrInfoElements.set(0, formattedDateElement);
						            System.out.println(formattedDate);
						        } catch (ParseException e) {
						            System.err.println("Error parsing date: " + plyrInfoElements.get(0).text());
						            e.printStackTrace();
						        }
							}
							
							System.out.println("정보: " + plyrInfoElements.get(m).text());
							rowData[m + 3] = plyrInfoElements.get(m).text();
						}
						ID = plyrNameElements.get(0).text() + "_" + rowData[3];
						rowData[0] = ID;
						
						// 선수 이름, 번호, 정보를 포함하는 행을 newDataIF 리스트에 추가
					    newDataIF.add(rowData);
					    System.out.println(plryListElements.size()+1+"경기기록 수");
						
					    
					    
					    
					    /// 선수 기록
						for (int m = 0; m < plryListElements.size(); m++) {
							String[] rowDataRC = plryListElements.get(m).text().split(" "); // 공백을 기준으로 행 데이터 분리
//							System.out.println(plryListElements.get(m).text());
							newDataRC.add(rowDataRC); // 크롤링한 행 데이터를 리스트에 추가

						}
					}
				} catch (IndexOutOfBoundsException e) {
					System.out.println(
							"IndexOutOfBoundsException occurred while accessing titleElements. Skipping this element.");
				}

				// 새로운 데이터를 파일에 저장
				String fileNameIF = "C:\\Users\\INSD\\Documents\\Excel\\" + POSITION + "\\" + TEAM + "\\" + SEASON + "_" + TEAM
						+ "plyrIF.xlsx";
				String fileNameRC = "C:\\Users\\INSD\\Documents\\Excel\\" + POSITION + "\\" + TEAM + "\\" + SEASON + "_" + fileCount + "_" + "plyrRC_"
						+ name  + ".xlsx";
				
				// 선수 기록이 1개 이상인 경우에만 저장
				if (newDataRC.size() > 1) {
				    SaveToExcel.saveDataToFileIF(fileNameIF, newDataIF);
				    SaveToExcel.saveDataToFileRC(fileNameRC, newDataRC);
				}

				// 데이터 초기화
				newDataIF.clear();
				newDataRC.clear();
				

				// Excel 파일에서 데이터를 읽고 MySQL 데이터베이스에 저장
				String excelFilePathIF = "C:\\Users\\INSD\\Documents\\Excel\\" + POSITION + "\\" + TEAM + "\\" + SEASON + "_" + TEAM
						+ "plyrIF.xlsx";
				String excelFilePathRC =  "C:\\Users\\INSD\\Documents\\Excel\\" + POSITION + "\\" + TEAM + "\\" + SEASON + "_" + fileCount + "_" + "plyrRC_"
						+ name  + ".xlsx";
				
				
				String jdbcUrl = "jdbc:mysql://localhost:3306/KBO_DB_SSS?useSSL=false";
				String dbUser = "root";
				String dbPassword = "1234";

				// Excel 파일에서 데이터 읽기
				List<String[]> dataIF = excelToDB.readExcelDataIF(excelFilePathIF, TEAM);
				List<String[]> dataRC = excelToDB.readExcelDataRC(excelFilePathRC, SEASON, TEAM, ID);

				// 데이터베이스에 데이터 삽입
				if (dataRC.size() > 1) {
				    excelToDB.insertDataIntoDBIF(jdbcUrl, dbUser, dbPassword, dataIF, SEASON);
				    excelToDB.insertDataIntoDBRC(jdbcUrl, dbUser, dbPassword, dataRC, POSITION);
				}
				
				// 데이터 초기화
				dataIF.clear();
				dataRC.clear();

				fileCount++; // 파일 번호 증가
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
