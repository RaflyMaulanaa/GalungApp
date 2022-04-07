package id.example.galungapp.LihatSemua;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.example.galungapp.Adapter.AdapterListJual;
import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.Model.DataJual;
import id.example.galungapp.Api.Model.DataJual_respon;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListItemFragment extends Fragment {

    private RecyclerView listView;
    private GridLayoutManager gridLayoutManager;

    private AdapterListJual adapterListJual;
    private InterfaceGet interfaceGet;
    private String data;
    private SwipeRefreshLayout swiperefresh;

    private ConstraintLayout constraint_info;
    private ImageView iv_info;
    private TextView tv_title, tv_subtitle;
    private Button btn_submit;

    private ProgressDialog dialog;

    private List<DataJual> dataJual = new ArrayList();

    public ListItemFragment(String data){
        this.data=data;
    }


    private int visibleItemCountBibitPupuk;
    private int totalItemCountBibitPupuk;
    private int pastVisableItemsBibitPupuk;
    private boolean isLoadingBibitPupuk = true;
    private int previous_totalBibitPupuk = 0;
    private int page_numberBibitPupuk = 1;
    private int view_thresholdBibitPupuk = 8;

    private int visibleItemCountAlat;
    private int totalItemCountAlat;
    private int pastVisableItemsAlat;
    private boolean isLoadingAlat = true;
    private int previous_totalAlat = 0;
    private int page_numberAlat = 1;
    private int view_thresholdAlat = 8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView = (RecyclerView) view.findViewById(R.id.listView);

        constraint_info = (ConstraintLayout) view.findViewById(R.id.constraint_info);
        iv_info = (ImageView) view.findViewById(R.id.iv_info);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        dialog = new ProgressDialog(getContext(), "Please Wait..");

        interfaceGet = (InterfaceGet) Client.getClient().create(InterfaceGet.class);

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        listView.setLayoutManager(gridLayoutManager);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataJual.clear();
                if(data.equals("Beras")){
                    swiperefresh.setRefreshing(true);
                    getDataBeras();
                }else if(data.equals("BibitPupuk")){
                    isLoadingBibitPupuk = true;
                    previous_totalBibitPupuk = 0;
                    page_numberBibitPupuk = 1;
                    swiperefresh.setRefreshing(true);
                    getDataBibitPupuk();
                }else if(data.equals("Alat")){
                    isLoadingAlat = true;
                    previous_totalAlat = 0;
                    page_numberAlat = 1;
                    swiperefresh.setRefreshing(true);
                    getDataAlat();
                }
            }
        });

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(data.equals("BibitPupuk")) {
                    visibleItemCountBibitPupuk = gridLayoutManager.getChildCount();
                    totalItemCountBibitPupuk = gridLayoutManager.getItemCount();
                    pastVisableItemsBibitPupuk = gridLayoutManager.findFirstVisibleItemPosition();
                    if (dy > 0) {
                        if (isLoadingBibitPupuk && totalItemCountBibitPupuk > previous_totalBibitPupuk) {
                            isLoadingBibitPupuk = false;
                            previous_totalBibitPupuk = totalItemCountBibitPupuk;
                        }
                        if (!isLoadingBibitPupuk && totalItemCountBibitPupuk - visibleItemCountBibitPupuk <= pastVisableItemsBibitPupuk + view_thresholdBibitPupuk) {
                            page_numberBibitPupuk = page_numberBibitPupuk + 1;
                            loadPageBibitPupuk(page_numberBibitPupuk);
                            isLoadingBibitPupuk = true;
                        }
                    }
                }else if(data.equals("Alat")){
                    visibleItemCountAlat = gridLayoutManager.getChildCount();
                    totalItemCountAlat = gridLayoutManager.getItemCount();
                    pastVisableItemsAlat = gridLayoutManager.findFirstVisibleItemPosition();
                    if (dy > 0) {
                        if (isLoadingAlat && totalItemCountAlat > previous_totalAlat) {
                            isLoadingAlat = false;
                            previous_totalAlat = totalItemCountAlat;
                        }
                        if (!isLoadingAlat && totalItemCountAlat - visibleItemCountAlat <= pastVisableItemsAlat + view_thresholdAlat) {
                            page_numberAlat = page_numberAlat + 1;
                            loadPageAlat(page_numberAlat);
                            isLoadingAlat = true;
                        }
                    }
                }
            }
        });

        loadData();

        return view;
    }

    private void getDataBeras(){
        page_numberAlat = 1;
        interfaceGet.getDataBeras(page_numberAlat).enqueue(new Callback<DataJual_respon>() {
            @Override
            public void onResponse(Call<DataJual_respon> call, Response<DataJual_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataJual_respon mDataJual_respon = (DataJual_respon) response.body();
                    if(mDataJual_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterListJual = new AdapterListJual(((DataJual_respon) response.body()).getData(), getContext(),"Beras");
                            listView.setAdapter(adapterListJual);
                        }else{
                            showInfo("Gagal terhubung keserver","Data beras kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataJual_respon.getMessage(),"Refresh");
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }
                }else{
                    showInfo("Gagal terhubung keserver","Data beras tidak dapat ditampilkan","Refresh");
                    dialog.dismiss();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataJual_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data beras tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getDataBibitPupuk(){
        page_numberBibitPupuk = 1;
        interfaceGet.getBibitPupuk(page_numberBibitPupuk).enqueue(new Callback<DataJual_respon>() {
            @Override
            public void onResponse(Call<DataJual_respon> call, Response<DataJual_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataJual_respon mDataJual_respon = (DataJual_respon) response.body();
                    if(mDataJual_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterListJual = new AdapterListJual(((DataJual_respon) response.body()).getData(), getContext(),"BibitPupuk");
                            listView.setAdapter(adapterListJual);
                        }else{
                            showInfo("Gagal terhubung keserver","Data bibit & pupuk kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataJual_respon.getMessage(),"Refresh");
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }
                }else{
                    showInfo("Gagal terhubung keserver","Data bibit & pupuk tidak dapat ditampilkan","Refresh");
                    dialog.dismiss();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataJual_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data bibit & pupuk tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void loadPageBibitPupuk(int page_number){
        swiperefresh.setRefreshing(true);
        interfaceGet.getBibitPupuk(page_number).enqueue(new Callback<DataJual_respon>() {
            @Override
            public void onResponse(Call<DataJual_respon> call, Response<DataJual_respon> response) {
                if(response.isSuccessful()) {
                    DataJual_respon mDataJual_respon = (DataJual_respon) response.body();
                    if(mDataJual_respon.isStatus()){
                        adapterListJual.addDataJual(mDataJual_respon.getData(),"BibitPupuk");
                        swiperefresh.setRefreshing(false);
                    }else{
                        swiperefresh.setRefreshing(false);
                    }
                }else{
                    Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataJual_respon> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getDataAlat(){
        page_numberAlat = 1;
        interfaceGet.getAlat(page_numberAlat).enqueue(new Callback<DataJual_respon>() {
            @Override
            public void onResponse(Call<DataJual_respon> call, Response<DataJual_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataJual_respon mDataJual_respon = (DataJual_respon) response.body();
                    if(mDataJual_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterListJual = new AdapterListJual(((DataJual_respon) response.body()).getData(), getContext(),"Alat");
                            listView.setAdapter(adapterListJual);
                        }else{
                            showInfo("Gagal terhubung keserver","Data alat kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataJual_respon.getMessage(),"Refresh");
                        swiperefresh.setRefreshing(false);
                    }
                }else{
                    showInfo("Gagal terhubung keserver","Data alat tidak dapat ditampilkan","Refresh");
                    dialog.dismiss();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataJual_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data alat tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void loadPageAlat(int page_number){
        swiperefresh.setRefreshing(true);
        interfaceGet.getAlat(page_number).enqueue(new Callback<DataJual_respon>() {
            @Override
            public void onResponse(Call<DataJual_respon> call, Response<DataJual_respon> response) {
                if(response.isSuccessful()) {
                    DataJual_respon mDataJual_respon = (DataJual_respon) response.body();
                    if(mDataJual_respon.isStatus()){
                        adapterListJual.addDataJual(mDataJual_respon.getData(),"Alat");
                        swiperefresh.setRefreshing(false);
                    }else{
                        swiperefresh.setRefreshing(false);
                    }
                }else{
                    Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataJual_respon> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Terhubung Keserver", Toast.LENGTH_SHORT).show();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void loadData(){
        dataJual.clear();
        switch (data){
            case "Beras" :
                dialog.show();
                getDataBeras();
                break;

            case "BibitPupuk" :
                dialog.show();
                getDataBibitPupuk();
                break;

            case "Alat" :
                dialog.show();
                getDataAlat();
                break;
        }
    }

    private void showInfo(String title, String subtitle, String button){
        constraint_info.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        tv_subtitle.setText(subtitle);
        btn_submit.setText(button);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }

}
