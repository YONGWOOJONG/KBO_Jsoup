package saveToFile;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExcelToDB {

	public List<String[]> readExcelDataIF(String filePath, String TEAM) {
		List<String[]> data = new ArrayList<>();
		try (FileInputStream inputStream = new FileInputStream(filePath)) {
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);

			// Get the last row in the sheet
			int lastRowIndex = sheet.getLastRowNum();
			Row lastRow = sheet.getRow(lastRowIndex);

			String[] rowData = new String[lastRow.getLastCellNum()];
			for (int i = 0; i < lastRow.getLastCellNum(); i++) {
				Cell cell = lastRow.getCell(i);
				rowData[i] = cell.toString();
			}

			data.add(rowData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public List<String[]> readExcelDataRC(String filePath, String SEASON, String TEAM, String ID) {
		List<String[]> data = new ArrayList<>();
		try (FileInputStream inputStream = new FileInputStream(filePath)) {
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				// 연, 월, 일로 분할하여 추가 열을 할당
				String[] rowData = new String[row.getLastCellNum() + 4];
				
				for (int i = 0; i < row.getLastCellNum(); i++) {
					Cell cell = row.getCell(i);
					if (i == 0) { // 날짜 열이라면
						String[] splitDate = cell.toString().split("-");

						rowData[i + 1] = SEASON + splitDate[0] + splitDate[1]; // 월을 저장
//						rowData[i + 2] = splitDate[0]; // 일을 저장
//						rowData[i + 3] = splitDate[1]; // 일을 저장
					} else if (i == 1) { // 상대팀 열이라면
						String opponent = cell.toString();
						boolean isHome = opponent.startsWith("@");
						rowData[i + 1] = isHome ? "STADIUM_" + opponent.substring(1) : "STADIUM_" + TEAM;
						rowData[i + 2] = TEAM;
						rowData[i + 3] = isHome ? opponent.substring(1) : opponent;
					} else if (i == 2) {
						rowData[i + 3] = cell.toString();// 경기결과
					} else if (i == 3) {
						String[] splitDate = cell.toString().split(":");
						rowData[i + 3] = splitDate[0];// 점수
						rowData[i + 4] = splitDate[1];// 상대점수

					} else {
						rowData[0] = ID;
						rowData[i + 4] = cell.toString();
					}
				}
				data.add(rowData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void insertDataIntoDBIF(String url, String user, String password, List<String[]> data, String SEASON) {
		String insertQuery = "INSERT INTO PLYR_INFO ("
				+ "    PLYR_ID, PLYR_NM, PLYR_NUMBER, BDAY_YMD, PLYR_HIT_POS, PLYR_POS,"
				+ "    PLYR_ACTIVE_YEARS, PLYR_ACTIVE_TEAM, CLUB_ID, RC_YEAR, LD_DT" + ") VALUES ("
				+ "    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW()" + ");";

		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);

			for (String[] row : data) {

				preparedStatement.setString(1, row[0]);
				preparedStatement.setString(2, row[1]);
				preparedStatement.setString(3, row[2]);
				preparedStatement.setString(4, row[3]);
				preparedStatement.setString(5, row[4]);
				preparedStatement.setString(6, row[10]);
				preparedStatement.setString(7, row[6]);
				preparedStatement.setString(8, row[7]);
				preparedStatement.setString(9, "CLUB_" + row[9]); // CLUB_ID
				preparedStatement.setString(10, SEASON);
					
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertDataIntoDBRC(String url, String user, String password, List<String[]> data, String POSITION) {
//		String insertQuery = "INSERT INTO game_data ("
//				+ "    plyrID, year, month, day, stadium_id, CLUB_ID, opponent, result, score, order_in_lineup, position, starter, at_bat,"
//				+ "    runs, hits, doubles, triples, home_runs, total_bases, runs_batted_in,"
//				+ "    stolen_bases, caught_stealing, walks, hit_by_pitch, intentional_walks,"
//				+ "    strikeouts, ground_into_double_play, sacrifice_hits, sacrifice_flies,"
//				+ "    batting_average, on_base_percentage, slugging_percentage, ops," + "    pitches, avli, re24, wpa"
//				+ ") VALUES ("
//				+ "   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		String insertQueryHIT = "INSERT INTO HIT_RC ("
				+ "    PLYR_ID, DAY_ID, STADIUM_ID, CLUB_ID, OPPS_CLUB_ID, " //7
				+ "    RESULT, TEAM_SCORE, OPPS_TEAM_SCORE, HIT_BL, HIT_ST, HIT_AB, HIT_R, HIT_H, `HIT_2B`, `HIT_3B`, HIT_HR, HIT_TB," // 19
				+ "    HIT_RBI, HIT_SB, HIT_CS, HIT_BB, HIT_HBP, HIT_BOB, HIT_SO, HIT_DP, HIT_SH, HIT_SF, LD_DT" + ") VALUES ("
				+ "    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW()" + ");";
		
		String insertQueryPIT = "INSERT INTO PIT_RC ("
				+ "    PLYR_ID, DAY_ID, STADIUM_ID, CLUB_ID, OPPS_CLUB_ID, RESULT, TEAM_SCORE, OPPS_TEAM_SCORE, PIT_ST, PIT_IP, PIT_R, PIT_ER, PIT_Batter, PIT_AB, PIT_H, PIT_2B, PIT_3B, PIT_HR, PIT_BB, PIT_HBP, PIT_BOB, PIT_SO, PIT_PITCH, LD_DT"
				+ ") VALUES ("
				+ "    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  NOW()"
				+ ");";

		
	if(POSITION=="0") {
		try (Connection conn = DriverManager.getConnection(url, user, password)) {

			// 타자, 투수 테이블 구분
			PreparedStatement preparedStatement = conn.prepareStatement(insertQueryHIT);

			
			for (String[] row : data) {
//				for (int i=0; i < row.length; i++) {
//					System.out.println(row[i]);
//				}
			
				
				preparedStatement.setString(1, row[0]);
				preparedStatement.setString(2, row[1]);//일
				preparedStatement.setString(3, row[2]);
				preparedStatement.setString(4, "CLUB_" + row[3]);
				preparedStatement.setString(5, "CLUB_" + row[4]);
				preparedStatement.setString(6, row[5]);
				preparedStatement.setString(7, row[6]);

				preparedStatement.setString(8, row[7]); 
				preparedStatement.setString(9, row[8]);
//				preparedStatement.setString(12, row[0]);
				preparedStatement.setString(10, row[10]); 
				preparedStatement.setString(11, row[11]);

				preparedStatement.setString(12, row[12]); 
				preparedStatement.setString(13, row[13]);
				preparedStatement.setString(14, row[14]);
				preparedStatement.setString(15, row[15]);
				preparedStatement.setString(16, row[16]);
				preparedStatement.setString(17, row[17]);
				preparedStatement.setString(18, row[18]);
				preparedStatement.setString(19, row[19]);
				preparedStatement.setString(20, row[20]);
				preparedStatement.setString(21, row[21]);
				preparedStatement.setString(22, row[22]);
				preparedStatement.setString(23, row[23]);
				preparedStatement.setString(24, row[24]);
				preparedStatement.setString(25, row[25]);
				preparedStatement.setString(26, row[26]);
				preparedStatement.setString(27, row[27]);
//				preparedStatement.setString(28, row[28]);
//				preparedStatement.setString(29, row[29]);

				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}else {
		try (Connection conn = DriverManager.getConnection(url, user, password)) {

			// 타자, 투수 테이블 구분
			PreparedStatement preparedStatement = conn.prepareStatement(insertQueryPIT);

			for (String[] row : data) {

				preparedStatement.setString(1, row[0]);
				preparedStatement.setString(2, row[1]); //일
				preparedStatement.setString(3, row[2]);
				preparedStatement.setString(4, "CLUB_" + row[3]); 
				preparedStatement.setString(5, "CLUB_" + row[4]);
				preparedStatement.setString(6, row[5]);
				preparedStatement.setString(7, row[6]);

				preparedStatement.setString(8, row[7]); // 팀스코어
				preparedStatement.setString(9, row[8]); //상대팀스코어
				preparedStatement.setString(10, row[9]);	//이닝
				preparedStatement.setString(11, row[10]); 
				preparedStatement.setString(12, row[11]);

				preparedStatement.setString(13, row[12]); 
				preparedStatement.setString(14, row[13]);
				preparedStatement.setString(15, row[14]);
				preparedStatement.setString(16, row[15]);
				preparedStatement.setString(17, row[16]);
				preparedStatement.setString(18, row[17]);
				preparedStatement.setString(19, row[18]);
				preparedStatement.setString(20, row[19]);
				preparedStatement.setString(21, row[20]);
				preparedStatement.setString(22, row[21]);
				preparedStatement.setString(23, row[22]);
//				preparedStatement.setString(24, row[23]);
//				preparedStatement.setString(24, row[24]);
//				preparedStatement.setString(25, row[25]);
//				preparedStatement.setString(26, row[26]);
		
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
		
	}
}