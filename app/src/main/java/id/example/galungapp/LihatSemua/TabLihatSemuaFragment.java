package id.example.galungapp.LihatSemua;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.example.galungapp.MenuUtama.TabAdapter;
import id.example.galungapp.R;

public class TabLihatSemuaFragment extends Fragment {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String data;

    public TabLihatSemuaFragment(String data){
        this.data=data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getFragmentManager());
        adapter.addFragment(new ListItemFragment("Beras"), "Beras");
        adapter.addFragment(new ListItemFragment("BibitPupuk"), "Bibit & Pupuk");
        adapter.addFragment(new ListItemFragment("Alat"), "Alat");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if(data.equals("Beras")){
            viewPager.setCurrentItem(0);
        }else if(data.equals("BibitPupuk")){
            viewPager.setCurrentItem(1);
        }else if(data.equals("Alat")){
            viewPager.setCurrentItem(2);
        }

        return view;

    }
}
