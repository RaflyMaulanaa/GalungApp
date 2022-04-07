package id.example.galungapp.MenuUtama;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.example.galungapp.MenuDrawer.DaftarSawahActivity;
import id.example.galungapp.R;
import id.example.galungapp.RegistrasiLogin.RegisterKonsumenFragment;
import id.example.galungapp.RegistrasiLogin.RegisterPetaniFragment;

public class TabFragment extends Fragment {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    String berhasilInput=null,menu=null,dataSawah=null;

    public TabFragment(String berhasilInput, String menu, String dataSawah){
        this.berhasilInput = berhasilInput;
        this.menu = menu;
        this.dataSawah = dataSawah;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);


        adapter = new TabAdapter(getFragmentManager());
        if(menu.equals("Gabahku")) {
            adapter.addFragment(new FragmentList("GabahInfoHarga"), "Info Harga");
            adapter.addFragment(new FragmentListTambah("DataJualGabah"), "Dijual");
            adapter.addFragment(new FragmentList("DataGabahTerjual"), "Terjual");
            adapter.addFragment(new FragmentList("DataGabahDibatalkan"), "Dibatalkan");
        }
        if(menu.equals("Gadai Sawah")){
            adapter.addFragment(new FragmentListTambah("DataGadaiSawah"), "Gadai");
            adapter.addFragment(new FragmentList("DataGadaiSawahTergadai"), "Tergadai");
            adapter.addFragment(new FragmentList("DataGadaiSawahDibatalkan"), "Dibatalkan");
            adapter.addFragment(new FragmentList("DataGadaiSawahSelesai"), "Selesai");
        }
        if(menu.equals("Modal Tanam")){
            adapter.addFragment(new FragmentListTambah("DataModalTanam"), "Modal Tanam");
            adapter.addFragment(new FragmentList("DataDimodalTanam"), "Dimodali");
            adapter.addFragment(new FragmentList("DataModalTanamDibatalkan"), "Dibatalkan");
            adapter.addFragment(new FragmentList("DataModalTanamSelesai"), "Selesai");
        }

        viewPager.setAdapter(adapter);
        if(berhasilInput != null && menu.equals("Gabahku")){
            viewPager.setCurrentItem(1);
        }

        if(dataSawah != null && dataSawah.equals("kosong")){
            Intent intent = new Intent(getContext(), DaftarSawahActivity.class);
            intent.putExtra("data_sawah",dataSawah);
            startActivity(intent);
        }

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }


}
