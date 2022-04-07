package id.example.galungapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import id.example.galungapp.Adapter.AdapterListJual;
import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.Model.DataGabah;
import id.example.galungapp.Api.Model.DataGadaiSawah;
import id.example.galungapp.Api.Model.DataHargaGabah;
import id.example.galungapp.Api.Model.DataJual;
import id.example.galungapp.Api.Model.DataJual_respon;
import id.example.galungapp.Api.Model.DataModalTanam;
import id.example.galungapp.Api.Model.DataUser;
import id.example.galungapp.LihatSemua.LihatSemuaActivity;
import id.example.galungapp.MenuUtama.MenuUtamaAcitivty;
import id.example.galungapp.RegistrasiLogin.LoginRegisterActivity;
import id.example.galungapp.Sliders.FragmentSlider;
import id.example.galungapp.Sliders.SliderIndicator;
import id.example.galungapp.Sliders.SliderPagerAdapter;
import id.example.galungapp.Sliders.SliderView;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BerandaFragment extends Fragment {

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;
    private SliderView sliderView;

    private LinearLayout mLinearLayout;

    private CardView cv_gabahku,cv_gadaisawah,cv_modaltanam,cv_beras,cv_bibitpupuk,cv_alat;

    private InterfaceGet interfaceGet;
    private AdapterListJual adapterListJual;

    private RecyclerView recyclerBeras, recyclerBibitPupuk, recyclerAlat;
    private LinearLayoutManager layoutManagerBeras, layoutManagerBibitPupuk, layoutManagerAlat;

    private SwipeRefreshLayout swiperefresh;

    private List<DataJual> dataJual = new ArrayList();

    private TextView tv_lihatsemuaberas, tv_lihatsemuabibitpupuk, tv_lihatsemualat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        sliderView = (SliderView) view.findViewById(R.id.sliderView);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.pagesContainer);
        cv_gabahku = view.findViewById(R.id.cv_gabahku);
        cv_gadaisawah = view.findViewById(R.id.cv_gadaisawah);
        cv_modaltanam = view.findViewById(R.id.cv_modaltanam);
        cv_beras = view.findViewById(R.id.cv_beras);
        cv_bibitpupuk = view.findViewById(R.id.cv_bibitpupuk);
        cv_alat = view.findViewById(R.id.cv_alat);

        swiperefresh = view.findViewById(R.id.swiperefresh);

        tv_lihatsemuaberas = view.findViewById(R.id.tv_lihatsemuaberas);
        tv_lihatsemuabibitpupuk = view.findViewById(R.id.tv_lihatsemuabibitpupuk);
        tv_lihatsemualat = view.findViewById(R.id.tv_lihatsemuaalat);

        interfaceGet = (InterfaceGet) Client.getClient().create(InterfaceGet.class);

        recyclerBeras = (RecyclerView) view.findViewById(R.id.recyclerBeras);
        recyclerBibitPupuk = (RecyclerView) view.findViewById(R.id.recyclerBibitPupuk);
        recyclerAlat = (RecyclerView) view.findViewById(R.id.recyclerAlat);

        layoutManagerBeras = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerBibitPupuk = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerAlat = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerBeras.setLayoutManager(layoutManagerBeras);
        recyclerBibitPupuk.setLayoutManager(layoutManagerBibitPupuk);
        recyclerAlat.setLayoutManager(layoutManagerAlat);

        setupSlider();

        cv_gabahku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAcitivtyMenu(MenuUtamaAcitivty.class, "Gabahku","petani");
            }
        });

        cv_gadaisawah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAcitivtyMenu(MenuUtamaAcitivty.class, "Gadai Sawah","petani");
            }
        });

        cv_modaltanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAcitivtyMenu(MenuUtamaAcitivty.class, "Modal Tanam","petani");
            }
        });

        cv_beras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LihatSemuaActivity.class);
                intent.putExtra("data","Beras");
                startActivity(intent);
            }
        });

        cv_bibitpupuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LihatSemuaActivity.class);
                intent.putExtra("data","BibitPupuk");
                startActivity(intent);
            }
        });

        cv_alat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LihatSemuaActivity.class);
                intent.putExtra("data","Alat");
                startActivity(intent);
            }
        });

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataJual.clear();
                getDataBeras();
                getDataBibitPupuk();
                getDataAlat();
            }
        });

        tv_lihatsemuaberas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LihatSemuaActivity.class);
                intent.putExtra("data","Beras");
                startActivity(intent);
            }
        });

        tv_lihatsemuabibitpupuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LihatSemuaActivity.class);
                intent.putExtra("data","BibitPupuk");
                startActivity(intent);
            }
        });

        tv_lihatsemualat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LihatSemuaActivity.class);
                intent.putExtra("data","Alat");
                startActivity(intent);
            }
        });

        getDataBeras();
        getDataBibitPupuk();
        getDataAlat();

        return view;
    }

    private void setupSlider() {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("https://lenteratoday.com/wp-content/uploads/2020/01/lahan-pertanian-madiun.jpg"));
        fragments.add(FragmentSlider.newInstance("https://matabanua.co.id/wp-content/uploads/2020/02/5-Bulog-Khawatir-Padi-Gagal-Panen-1024x538.jpg"));
        fragments.add(FragmentSlider.newInstance("https://cdn2.tstatic.net/makassar/foto/bank/images/petani-menanam-padi-di-samata.jpg"));

        mAdapter = new SliderPagerAdapter(getChildFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getContext(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }

    private void  SweetalertFailed(String ContentText){
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops")
                .setContentText(ContentText)
                .show();
    }

    private void getAcitivtyMenu(Class activity, String menu,String role){
        if(SharedPMUser.getmInstance(getContext()).isUserReady()) {
            if(role.equals("petani")){
                final DataUser dataUser = SharedPMUser.getmInstance(getContext()).getUser();
                if(dataUser.getRole().equals("petani")) {
                    if(dataUser.getPetani_verified().equals("1")) {
                        Intent intent = new Intent(getContext(), activity);
                        intent.putExtra("menu", menu);
                        startActivity(intent);
                    }else{
                        SweetalertFailed("Anda belum melakukan verifikasi. Silahkan lakukan verifikasi akun");
                    }
                }else{
                    SweetalertFailed("Hanya user petani yang dapat menggunakan fitur "+menu);
                }
            }else{
                Intent intent = new Intent(getContext(), activity);
                intent.putExtra("menu", menu);
                startActivity(intent);
            }
        }else{
            Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
            intent.putExtra("position", 1);
            startActivity(intent);
        }
    }

    private void getDataBeras(){
        interfaceGet.getDataBeras(1).enqueue(new Callback<DataJual_respon>() {
            @Override
            public void onResponse(Call<DataJual_respon> call, Response<DataJual_respon> response) {
                if(response.isSuccessful()) {
                    DataJual_respon mDataJual_respon = (DataJual_respon) response.body();
                    if(mDataJual_respon.isStatus()){
                        adapterListJual = new AdapterListJual(((DataJual_respon) response.body()).getData(), getContext(),"Beras");
                        recyclerBeras.setAdapter(adapterListJual);
                        swiperefresh.setRefreshing(false);
                    }else{
//                        Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                        swiperefresh.setRefreshing(false);
                    }
                }else{
//                    Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataJual_respon> call, Throwable t) {
//                Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getDataBibitPupuk(){
        interfaceGet.getBibitPupuk(1).enqueue(new Callback<DataJual_respon>() {
            @Override
            public void onResponse(Call<DataJual_respon> call, Response<DataJual_respon> response) {
                if(response.isSuccessful()) {
                    DataJual_respon mDataJual_respon = (DataJual_respon) response.body();
                    if(mDataJual_respon.isStatus()){
                        adapterListJual = new AdapterListJual(((DataJual_respon) response.body()).getData(), getContext(),"BibitPupuk");
                        recyclerBibitPupuk.setAdapter(adapterListJual);
                        swiperefresh.setRefreshing(false);
                    }else{
//                        Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                        swiperefresh.setRefreshing(false);
                    }
                }else{
//                    Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataJual_respon> call, Throwable t) {
//                Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getDataAlat(){
        interfaceGet.getAlat(1).enqueue(new Callback<DataJual_respon>() {
            @Override
            public void onResponse(Call<DataJual_respon> call, Response<DataJual_respon> response) {
                if(response.isSuccessful()) {
                    DataJual_respon mDataJual_respon = (DataJual_respon) response.body();
                    if(mDataJual_respon.isStatus()){
                        adapterListJual = new AdapterListJual(((DataJual_respon) response.body()).getData(), getContext(),"Alat");
                        recyclerAlat.setAdapter(adapterListJual);
                        swiperefresh.setRefreshing(false);
                    }else{
//                        Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                        swiperefresh.setRefreshing(false);
                    }
                }else{
//                    Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataJual_respon> call, Throwable t) {
//                Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void notValidAksesToken(){
        if(SharedPMUser.getmInstance(getContext()).isUserReady()) {
            SharedPMUser.getmInstance(getContext()).clear();
        }
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("statusLogin",0);
        intent.putExtra("statusfailed","accessToken");
        startActivity(intent);
    }

}
