package tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    public static String format(String s){
        String d = "";
        long t = Long.parseLong(s);
        Date date = new Date(t);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        d = df.format(date.getTime());
        return d;
    }

}
