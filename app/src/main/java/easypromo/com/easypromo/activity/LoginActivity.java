package easypromo.com.easypromo.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button btEntrar;

    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.etEmail);
        senha = findViewById(R.id.etSenha);

        limparCampos();

        btEntrar = findViewById(R.id.btEntrar);
        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!verificarPreenchimento()) return;

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarLogin();
            }
        });
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    private void validarLogin(){
        autenticacao = AcessoDatabase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    abrirTelaPrincipal();

                } else{

                    String erroExcecao = "";

                    try{
                        throw task.getException();

                    } catch (FirebaseAuthInvalidUserException e){
                        erroExcecao = getString(R.string.loginException_InvalidUser);

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = getString(R.string.loginException_InvalidCredentials);

                    } catch (Exception e) {
                        erroExcecao = getString(R.string.loginException_Default);
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,
                            erroExcecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void limparCampos(){
        email.setText("");
        senha.setText("");
    }

    private boolean verificarPreenchimento(){
        if (!email.getText().toString().isEmpty() &&
                !senha.getText().toString().isEmpty()) return true;

        Toast.makeText(LoginActivity.this,
                getString(R.string.errorFillAllFields), Toast.LENGTH_SHORT).show();
        return false;
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
