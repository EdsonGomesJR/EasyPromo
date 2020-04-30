package easypromo.com.easypromo.fragment;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuItemView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.activity.AdicionarOfertaActivity;
import easypromo.com.easypromo.activity.AdminActivity;
import easypromo.com.easypromo.activity.LoginActivity;
import easypromo.com.easypromo.activity.PrincipalActivity;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Base64Custom;
import easypromo.com.easypromo.helper.UniversalImageLoader;
import easypromo.com.easypromo.helper.Utilidades;
import easypromo.com.easypromo.model.Usuario;

import static android.app.Activity.RESULT_OK;

public class MenuNavegacaoFragment extends Fragment {

    private TextView nomeUsuario;

    private TextView administracao;
    private TextView addOferta;

    private ImageView foto;
    private DrawerLayout mDrawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private View mFragmentContainerView;

    private FirebaseAuth autenticacao;
    private DatabaseReference dbReference;

    private Usuario usuarioRecuperado;

    public MenuNavegacaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_menu_navegacao, container, false);
        recuperarUsuarioLogado();

        nomeUsuario = inflate.findViewById(R.id.menu_nv_nomeUsuario);

        administracao = inflate.findViewById(R.id.menu_nv_admin);
        administracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAdmin();
            }
        });

        addOferta =  inflate.findViewById(R.id.menu_nv_add);
        addOferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaNovaOferta();
            }
        });

        foto = inflate.findViewById(R.id.menu_nv_user_foto);
        initImageLoader();
        //setProfileImage();

        return inflate;
    }

    private void setProfileImage() {
        String imgURL = "i.pinimg.com/originals/de/f6/96/def69643889ee29e232637646e839064.jpg";
        UniversalImageLoader.setImage(imgURL, foto,null,"https://");
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    public void setUp(int n, Toolbar toolbar, DrawerLayout mDrawerLayout) {
        this.mFragmentContainerView = this.getActivity().findViewById(n);
        this.mDrawerLayout = mDrawerLayout;
        this.mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(getActivity(), this.mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        this.mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity)this.getActivity()).getSupportActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        return this.mDrawerToggle.onOptionsItemSelected(menuItem) || super.onOptionsItemSelected(menuItem);
    }

    private void recuperarUsuarioLogado(){
        autenticacao = AcessoDatabase.getAutenticacao();
        if (autenticacao.getCurrentUser() != null){

            dbReference = AcessoDatabase.getReferencia()
                    .child("usuarios")
                    .child(Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail()));

            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if( dataSnapshot.getValue() != null ){

                        usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                        nomeUsuario.setText("Olá, " + usuarioRecuperado.getNome());

                       if (usuarioRecuperado.getTipoPerfil().equals("1")){
                            administracao.setVisibility(View.VISIBLE);
                        }else{

                           administracao.setVisibility(View.GONE);
                       }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
        }
    }

    private void abrirTelaAdmin(){
        Intent intent = new Intent(getActivity(), AdminActivity.class);
        startActivity(intent);
    }

    private void abrirTelaNovaOferta(){
        Intent intent = new Intent(getActivity(), AdicionarOfertaActivity.class);
        startActivity(intent);
    }
}



