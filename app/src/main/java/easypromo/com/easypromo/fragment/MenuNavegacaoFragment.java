package easypromo.com.easypromo.fragment;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
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

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.activity.AdicionarOfertaActivity;
import easypromo.com.easypromo.activity.LoginActivity;
import easypromo.com.easypromo.activity.PrincipalActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuNavegacaoFragment extends Fragment {

 private TextView add_oferta;
 private ImageView foto;
 private DrawerLayout mDrawerLayout;
 private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
 private View mFragmentContainerView;

    public MenuNavegacaoFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_menu_navegacao, container, false);


        add_oferta =  inflate.findViewById(R.id.menu_nv_add);
        add_oferta.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getActivity(), AdicionarOfertaActivity.class);
              startActivity(intent);


          }
      });

      foto = inflate.findViewById(R.id.menu_nv_user_foto);
      foto.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(getActivity(),"Teste", Toast.LENGTH_LONG).show();
          }
      });

        return inflate;
    }


    public void setUp(int n, Toolbar toolbar, DrawerLayout mDrawerLayout) {
        this.mFragmentContainerView = this.getActivity().findViewById(n);
        this.mDrawerLayout = mDrawerLayout;
         this.mDrawerLayout.setDrawerShadow((int) R.drawable.drawer_shadow, (int) GravityCompat.START);

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(getActivity(), this.mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Toast.makeText(getActivity(),"Fechou Nav", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(getActivity(),"Abriu Nav", Toast.LENGTH_LONG).show();

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
}



