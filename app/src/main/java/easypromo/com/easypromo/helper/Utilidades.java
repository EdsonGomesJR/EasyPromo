package easypromo.com.easypromo.helper;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilidades {

    public static String getHoraAtual(){

        SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date data = new Date();
        return dtFormat.format(data);
    }

    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static String enderecoLoja(String url){
        int posicaoHttp = url.indexOf("//");
        String urlSemBarra = url.replace("//","");
        int posicaoNomeLoja = urlSemBarra.indexOf("/");

        return url.substring(posicaoHttp+2, posicaoNomeLoja+2);
    }

    public static String precoFormatado(Double valor){
        return String.format("%.2f",valor);
    }
}
