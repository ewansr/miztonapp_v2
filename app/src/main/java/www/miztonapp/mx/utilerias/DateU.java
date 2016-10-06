package www.miztonapp.mx.utilerias;

import java.util.Calendar;

/**
 * Created by Saul on 16/02/2016.
 */
public class DateU {
    static int vYear;
    static int vMonth;
    static int vDay;
    static Calendar cal;

    //Obtiene la fecha de inicio de la semana de la fecha actual
    public String StartOfWeek() {
        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        //cal.set(Calendar.YEAR , Calendar.MONTH, Calendar.DAY_OF_MONTH);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal.set(cal.DAY_OF_WEEK, cal.MONDAY);

        vYear = cal.get(cal.YEAR);
        vMonth = cal.get(cal.MONTH)+1;
        vDay = cal.get(cal.DAY_OF_MONTH);
        return Integer.toString(vYear) + "-" + Integer.toString(vMonth) + "-" + Integer.toString(vDay);
    }

    //Obtiene el fin de semana de la fecha actual
    public String EndOfWeek(){
        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);

        //cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal.set(cal.DAY_OF_WEEK, cal.SUNDAY);

        vYear = cal.get(cal.YEAR);
        vMonth = cal.get(cal.MONTH)+1;
        vDay = cal.get(cal.DAY_OF_MONTH);
        return Integer.toString(vYear) + "-" + Integer.toString(vMonth) + "-" + Integer.toString(vDay);
    }

    //obtiene el inicio de la semana de la fecha establecida
    public static String StartOfWeek(int Year, int Month, int Day) {
        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        cal.set(Year, Month, Day);
        cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));
        cal.set(cal.DAY_OF_WEEK, cal.MONDAY);

        vYear = cal.get(cal.YEAR);
        vMonth = cal.get(cal.MONTH)+1;
        vDay = cal.get(cal.DAY_OF_MONTH);
        return Integer.toString(vYear) + "-" + Integer.toString(vMonth) + "-" + Integer.toString(vDay);
    }

    //obtiene el fin de semana de la fecha establecida
    public static String EndOfWeek(int Year, int Month, int Day){
        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        cal.set(Year, Month, Day);
        cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH));
        cal.set(cal.DAY_OF_WEEK, cal.SUNDAY);

        vYear = cal.get(cal.YEAR);
        vMonth = cal.get(cal.MONTH)+1;
        vDay = cal.get(cal.DAY_OF_MONTH);
        return Integer.toString(vYear) + "-" + Integer.toString(vMonth) + "-" + Integer.toString(vDay);
    }



}
