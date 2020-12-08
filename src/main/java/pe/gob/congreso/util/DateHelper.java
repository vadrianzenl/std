package pe.gob.congreso.util;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateHelper {

    public static int getDayOfTheWeek(String descripcion) {
        int dia = 0;

        switch (descripcion.trim()) {
            case "Lunes":
                dia = 2;
                break;
            case "Martes":
                dia = 3;
                break;
            case "Miércoles":
                dia = 4;
                break;
            case "Jueves":
                dia = 5;
                break;
            case "Viernes":
                dia = 6;
                break;
            case "Sábado":
                dia = 7;
                break;
            case "Domingo":
                dia = 1;
                break;

        }

        return dia;
    }

    public static String getMonthString(int month) {
        String monthReturn = "";
        switch (month) {
            case 1:
                monthReturn = "Enero";
                break;
            case 2:
                monthReturn = "Febrero";
                break;
            case 3:
                monthReturn = "Marzo";
                break;
            case 4:
                monthReturn = "Abril";
                break;
            case 5:
                monthReturn = "Mayo";
                break;
            case 6:
                monthReturn = "Junio";
                break;
            case 7:
                monthReturn = "Julio";
                break;
            case 8:
                monthReturn = "Agosto";
                break;
            case 9:
                monthReturn = "Setiembre";
                break;
            case 10:
                monthReturn = "Octubre";
                break;
            case 11:
                monthReturn = "Noviembre";
                break;
            case 12:
                monthReturn = "Diciembre";
                break;
        }

        return monthReturn;
    }

    public static int getDayOfTheWeek(Date d) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    //Los resultados van del 1 = Domingo, 2 = Lunes

    public static int diferenciaEnDias(Date fechaMayor, Date fechaMenor) {
        long diferencia = fechaMayor.getTime() - fechaMenor.getTime();
        long dias = diferencia / (1000 * 60 * 60 * 24);
        return (int) dias;
    }

    public static Date deStringToDate(String fecha) {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaEnviar = null;
        try {
            fechaEnviar = formatoDelTexto.parse(fecha);
            return fechaEnviar;
        } catch (ParseException ex) {
             System.out.print(ex);
            return null;
        }
    }

	/**
	 * Retorna la diferencia de fechas en dias. Una fecha es solo Date (dd/mm/yyyy)  
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
    public static int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);        
        try {        	
        	fechaInicial = df.parse(df.format(fechaInicial));
            fechaFinal = df.parse(df.format(fechaFinal));
            long fechaInicialMs = fechaInicial.getTime();
            long fechaFinalMs = fechaFinal.getTime();
            long diferencia = fechaFinalMs - fechaInicialMs;
            double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
            return ((int) dias);
        } catch (ParseException ex) {
        	return -1;
        }
        
	}
    
    public static int getYear(Date date){
    	if (date == null){
    		return 0;
    	}else{
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
    		return Integer.parseInt(dateFormat.format(date));
    	}
    }

}
