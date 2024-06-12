package com.mapfre.mifel.vida.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static com.mapfre.mifel.vida.utils.VidaULContants.*;

public class DateFormatUtil {
    private static final Logger logger = LogManager.getLogger(DateFormatUtil.class);

    /**
     *Metodo que procesa la fecha en el formato correspondiente
     * @param dateIn
     * @param formatDate
     * @return Date
     */
    public Date convertDate(String dateIn, String formatDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        Date date = null;
        try {

            date = formatter.parse(dateIn);

        } catch (ParseException e) {
            logger.info(dateIn + " is Invalid apply Date format");
        }

        if (date != null) {
            return date;
        } else {
            return this.truncateDate();
        }
    }

    /**
     * Nos procesa la fecha con el DATE_FORMAT_1, si no la puede convertir nos devuelve un valor truncado
     * @param dateIn
     * @return String con formato DATE_FORMAT_1 = "dd/MM/yyyy";
     */
    public String convertSDFormat1(String dateIn) {
        SimpleDateFormat formatter = new SimpleDateFormat(Dates.DATE_FORMAT_1);
        Date date = null;
        String newDate = "";
        try {
            date = formatter.parse(dateIn);
            newDate = formatter.format(date);
        } catch (ParseException e) {
            logger.info(dateIn + " is Invalid apply Date format");
        }

        if (!date.equals("")) {
            return newDate;
        } else {
            return this.truncateDate(Dates.DATE_FORMAT_1);
        }
    }

    /**
     * Nos procesa la fecha con el DATE_FORMAT_2, si no la puede convertir nos devuelve un valor truncado
     * @param dateIn
     * @return String con formato DATE_FORMAT_2 = "MM/dd/yyyy";
     */
    public String convertSDFormat2(String dateIn) {
        SimpleDateFormat formatter = new SimpleDateFormat(Dates.DATE_FORMAT_2);
        Date date = null;
        String newDate = "";
        try {
            date = formatter.parse(dateIn);
            newDate = formatter.format(date);
        } catch (ParseException e) {
            logger.info(dateIn + " is Invalid apply Date format");
        }

        if (!date.equals("")) {
            return newDate;
        } else {
            return this.truncateDate(Dates.DATE_FORMAT_2);
        }
    }

    /**
     * Trunca la fecha  igual a "1900,01,01"
     * @return DATE
     */
    public Date truncateDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(Dates.DATE_FORMAT_13);
        String dateIn = "1900,01,01";
        Date date = null;
        try {
            date = formatter.parse(dateIn);
        } catch (ParseException e) {
            logger.info(dateIn + " is Invalid apply Date format");
        }
        return date != null ? date : new Date();
    }


    /**
     * Trunca la fecha en el formato indicado "1900,01,01"
     * @param formatDate
     * @return String
     */
    private String truncateDate(String formatDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        String dateIn = "1900,01,01";
        Date date = null;
        String newDate="";
        try {
            date = formatter.parse(dateIn);
            newDate = formatter.format(date);
        } catch (ParseException e) {
            logger.info(dateIn + " is Invalid apply Date format");
        }
        return !newDate.equals("") ? newDate : getToday(formatDate);
    }


    /**
     * Retorna la fecha actual del sistema
     * @param formatDate
     * @return
     */
    private String getToday(String formatDate){
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        Date date = null;
        String newDate="";
        date = new Date();
        newDate = formatter.format(date);
        return newDate;
    }

    /**
     * Obtiene la fecha actual del sistema en el formato especificado
     * @param formatDate
     * @return String
     */
    public String getFechaActual(String formatDate) {
        String currentDate = "";
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatDate);
        currentDate = date.format(formatter);
        return currentDate;
    }

    /**
     * Obtiene el dia(s) siguiente(s) en el formato correspondiente
     * @param formatDate
     * @param numDia
     * @return String date
     */
    public String getDiaSiguiente(String formatDate, int numDia) {
        String currentDate = "";
        Calendar cal = Calendar.getInstance();
        switch (formatDate.toLowerCase()) {
            case "dd": {
                cal.add(Calendar.DAY_OF_MONTH, numDia);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                String dayOfMonthStr = String.valueOf(dayOfMonth);
                currentDate = dayOfMonthStr;
                break;
            }
            case "yyyy": {
                cal.add(Calendar.DAY_OF_MONTH, numDia);
                int intYear = cal.get(Calendar.YEAR);
                String year = String.valueOf(intYear);
                currentDate = year;
                break;
            }
            case "mm": {
                cal.add(Calendar.DAY_OF_MONTH, numDia);
                int intMonth = cal.get(Calendar.MONTH);
                String month = String.valueOf(intMonth);
                currentDate = month;
                break;
            }
            default: {
                currentDate = "";
                break;
            }
        }
        return currentDate;
    }
}
