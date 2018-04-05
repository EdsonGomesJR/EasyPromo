package easypromo.com.easypromo.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;

    private final String NOME_ARQUIVO = "easypromo.preferencias";
    private final String CHAVE_EMAIL = "email";
    private final String CHAVE_SENHA = "senha";
    private final int MODE = 0;

    public Preferencias(Context contextoParametro){
        contexto = contextoParametro;
        preferencias = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferencias.edit();
    }

    public void setPreferencias(String email, String senha){
        editor.putString(CHAVE_EMAIL, email);
        editor.putString(CHAVE_SENHA, senha);
        editor.commit();
    }

    public HashMap<String, String> getPreferencias(){
        HashMap<String, String> listaPreferencias = new HashMap<>();
        listaPreferencias.put(CHAVE_EMAIL, preferencias.getString(CHAVE_EMAIL, null));
        listaPreferencias.put(CHAVE_SENHA, preferencias.getString(CHAVE_SENHA, null));
        return listaPreferencias;
    }
}
