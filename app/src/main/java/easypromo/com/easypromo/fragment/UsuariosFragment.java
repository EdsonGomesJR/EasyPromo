package easypromo.com.easypromo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Base64Custom;
import easypromo.com.easypromo.helper.Utilidades;
import easypromo.com.easypromo.model.Usuario;

public class UsuariosFragment extends Fragment {

	// region Variáveis
	private EditText emailUsuario;
	private Button btProcurarUsuario;
	private ScrollView svResultadoPesquisa;

	private TextView nome;
	private TextView email;
	private TextView dataCadastro;
	private RadioButton rbPerfilUsuario;
	private RadioButton rbPerfilAdmin;
	private Button btSalvar;
	private Button btExcluir;

	private String idUsuario;
	private DatabaseReference dbReference;
	// endregion

	public UsuariosFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

		emailUsuario = view.findViewById(R.id.etEmailUsuario);
		svResultadoPesquisa = view.findViewById(R.id.svResultadoPesquisa);

		nome = view.findViewById(R.id.txtNome);
		email = view.findViewById(R.id.txtEmail);
		dataCadastro = view.findViewById(R.id.txtData);
		rbPerfilUsuario = view.findViewById(R.id.rbPerfilUsuario);
		rbPerfilAdmin = view.findViewById(R.id.rbPerfilAdmin);

		btProcurarUsuario = view.findViewById(R.id.btProcurarUsuario);
		btProcurarUsuario.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Utilidades.hideKeyboard(getContext(), view);

				dbReference = AcessoDatabase.getReferencia()
						.child("usuarios")
						.child(Base64Custom.codificarBase64(emailUsuario.getText().toString()));

				dbReference.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {

						if( dataSnapshot.getValue() != null ){
							emailUsuario.setText("");

							Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

							svResultadoPesquisa.setVisibility(View.VISIBLE);

							nome.setText(usuarioRecuperado.getNome());
							email.setText(usuarioRecuperado.getEmail());
							dataCadastro.setText(usuarioRecuperado.getDataCadastro());

							if (usuarioRecuperado.getTipoPerfil() == "1"){
								rbPerfilAdmin.setChecked(true);
							}
							else{
								rbPerfilUsuario.setChecked(true);
							}
						}
						else {
							Toast.makeText(getActivity(), "Usuário não cadastrado", Toast.LENGTH_LONG)
									.show();
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
			}
		});

		return view;
	}
}