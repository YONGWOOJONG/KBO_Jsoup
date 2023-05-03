package scraping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date convertStringToDate(String inputDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = inputFormat.parse(inputDate);
        String outputDateString = outputFormat.format(date);
        return outputFormat.parse(outputDateString);
    }
}