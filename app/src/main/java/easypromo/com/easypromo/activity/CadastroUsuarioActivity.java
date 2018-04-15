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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Base64Custom;
import easypromo.com.easypromo.helper.Utilidades;
import easypromo.com.easypromo.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    // region Variáveis
    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText confirmaSenha;
    private Button btCadastrar;

    private Usuario usuario;
    private FirebaseAuth autenticacao;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = findViewById(R.id.etNome);
        email = findViewById(R.id.etEmailUsuario);
        senha = findViewById(R.id.etSenha);
        confirmaSenha = findViewById(R.id.etConfirmaSenha);

        limparCampos();

        btCadastrar = findViewById(R.id.btCadastrar);
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utilidades.hideKeyboard(CadastroUsuarioActivity.this, v);
                if (!verificarPreenchimento()) return;
                if (!confirmarSenha()) return;
                setAtributos();
                cadastrarUsuario();
            }
        });
    }

    private void limparCampos(){
        nome.setText("");
        email.setText("");
        senha.setText("");
        confirmaSenha.setText("");
    }

    private boolean verificarPreenchimento(){
        if (!nome.getText().toString().isEmpty() &&
                !email.getText().toString().isEmpty() &&
                !senha.getText().toString().isEmpty()) return true;

        Toast.makeText(CadastroUsuarioActivity.this,
                getString(R.string.errorFillAllFields), Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean confirmarSenha(){
        if (senha.getText().toString().equals(confirmaSenha.getText().toString())) return true;

        Toast.makeText(CadastroUsuarioActivity.this,
                getString(R.string.userError_WrongConfirmPass), Toast.LENGTH_SHORT).show();
        return false;
    }

    private void setAtributos(){
        usuario = new Usuario(
                nome.getText().toString(),
                email.getText().toString(),
                senha.getText().toString(),
                "");
    }

    private void cadastrarUsuario(){
        autenticacao = AcessoDatabase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    usuario.cadastrar(Base64Custom.codificarBase64(email.getText().toString()));

                    Toast.makeText(CadastroUsuarioActivity.this,
                            "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                    abrirLoginUsuario();
                }
                else{

                    String erroExcecao = "";

                    try{
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = getString(R.string.userException_WeakPassword);

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = getString(R.string.userException_InvalidCredentials);

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = getString(R.string.userException_UserCollision);

                    } catch (Exception e) {
                        erroExcecao = getString(R.string.userException_Default);
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this,
                            erroExcecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
