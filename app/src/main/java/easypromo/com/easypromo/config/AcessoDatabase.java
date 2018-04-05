package easypromo.com.easypromo.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AcessoDatabase {

    private static DatabaseReference dbFireBase;
    private static FirebaseAuth authFireBase;

    public static DatabaseReference getReferencia(){
        if (dbFireBase == null) dbFireBase = FirebaseDatabase.getInstance().getReference();
        return dbFireBase;
    }

    public static FirebaseAuth getAutenticacao(){
        if (authFireBase == null) authFireBase = FirebaseAuth.getInstance();
        return authFireBase;
    }
}
