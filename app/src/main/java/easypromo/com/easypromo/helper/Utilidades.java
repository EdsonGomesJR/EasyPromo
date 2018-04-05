package easypromo.com.easypromo.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades {

    public static String getHoraAtual(){

        SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date data = new Date();
        return dtFormat.format(data);
    }
}
