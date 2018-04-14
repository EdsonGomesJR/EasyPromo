package easypromo.com.easypromo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import easypromo.com.easypromo.fragment.PromocaoFragment;
import easypromo.com.easypromo.fragment.UsuariosFragment;

public class TabAdapter extends FragmentStatePagerAdapter{

    String[] tituloAbas = {"PROMOÇÕES", "USUÁRIOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragmento = null;

        switch (position){
            case 0:
                fragmento = new PromocaoFragment();
                break;
            case 1:
                fragmento = new UsuariosFragment();
                break;
        }

        return fragmento;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
