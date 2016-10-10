package www.miztonapp.mx.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Saulo on 31/08/2016.
 */

/**
 * Adaptador para el uso de paginados en fragment
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    RecyclerView rv;
    Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
                return PlaceHolderFragmentOrdenes.newInstance(context);
//            case 1:
//                return PlaceHolderFragmentOrdenes.newInstance(context);
        }
        return null;
    }

    @Override
    public int getCount() {
        // Total de páginas
        return 3;
    }


    /**
     * Si se desea cambiar el caption al tab
     * Aquí es donde puede hacerse
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Ordenes de trabajo";
            case 1:
                return "Resumen";
            case 2:
                return "Avisos";

        }
        return null;
    }
}