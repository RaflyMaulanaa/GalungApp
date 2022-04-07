package id.example.galungapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.example.galungapp.MenuDrawer.DaftarSawahActivity;
import id.example.galungapp.MenuUtama.FragmentList;
import id.example.galungapp.MenuUtama.FragmentListTambah;
import id.example.galungapp.MenuUtama.TabAdapter;

public class TabPesananFragment extends Fragment {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);


        adapter = new TabAdapter(getFragmentManager());
        adapter.addFragment(new FragmentListPesanan("PesananDiproses"), "Diproses");
        adapter.addFragment(new FragmentListPesanan("PesananSelesai"), "Selesai");
        adapter.addFragment(new FragmentListPesanan("PesananDibatalkan"), "Dibatalkan");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }


}
