package saveToFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SaveToExcel {

	private static Workbook workbook;
	
	public static void saveDataToFileIF(String fileName, List<String[]> data) {
		workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		int rowCount = 0;

		// 파일이 이미 존재하는 경우 Workbook을 읽어옵니다.
	    File file = new File(fileName);
	    if (file.exists()) {
	        try (FileInputStream inputStream = new FileInputStream(fileName)) {
	            workbook = new XSSFWorkbook(inputStream);
	            sheet = workbook.getSheetAt(0);
	            rowCount = sheet.getLastRowNum() + 1;
	        } catch (IOException e) {
	            System.err.println("Error reading existing file: " + fileName);
	            e.printStackTrace();
	            return;
	        }
	    } else {
	        // 파일이 없는 경우 새로운 Workbook과 Sheet를 생성합니다.
	        workbook = new XSSFWorkbook();
	        sheet = workbook.createSheet("Sheet1");
	        rowCount = 0;
	    }
		
		for (String[] row : data) {
			Row sheetRow = sheet.createRow(rowCount);

			for (int i = 0; i < row.length; i++) {
				Cell cell = sheetRow.createCell(i);
				cell.setCellValue(row[i]);
			}

			rowCount++;
		}

		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
			workbook.write(outputStream);

		} catch (IOException e) {
			System.err.println("Error writing to file: " + fileName);
			e.printStackTrace();
		}
	}
	

	public static void saveDataToFileRC(String fileName, List<String[]> data) {
		workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		int rowCount = 0;

		for (String[] row : data) {
			Row sheetRow = sheet.createRow(rowCount);

			for (int i = 0; i < row.length; i++) {
				Cell cell = sheetRow.createCell(i);
				cell.setCellValue(row[i]);
			}

			rowCount++;
		}

		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
			workbook.write(outputStream);

		} catch (IOException e) {
			System.err.println("Error writing to file: " + fileName);
			e.printStackTrace();
		}
	}
	
	
	
}