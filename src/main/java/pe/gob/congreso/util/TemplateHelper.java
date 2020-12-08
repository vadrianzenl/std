package pe.gob.congreso.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class TemplateHelper {
    public static String getDateTemplate(Date fechaDocumento) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaDocumento);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return "Lima, " + day + " de " + DateHelper.getMonthString(month) + " de " + year;
    }

    public static String generateExtensionPDF(String nombreWord) {
        return nombreWord.substring(0, nombreWord.indexOf(".")) + ".pdf";
    }

    public static String generateFileName(Integer tipoDocumento, Integer fichaDocumento, String extension) {
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return String.valueOf(tipoDocumento) + "-" + String.valueOf(fichaDocumento) + "-"
                + String.valueOf(year) + String.valueOf(month) + String.valueOf(day) + String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second) + extension;
    }

    public static String getAbreviaturas(String descripcion) {
        String abreviatura = "";
        StringTokenizer st = new StringTokenizer(descripcion);
        while(st.hasMoreTokens()) {
            abreviatura += st.nextToken().charAt(0);
        }
        return abreviatura;
    }
}
