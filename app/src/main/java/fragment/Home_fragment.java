package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Config.BaseURL;
import adapter.Home_slider_adapter;
import adapter.Home_suggetion_adapter;
import com.getpills.LoginActivity;
import com.getpills.Medical_productActivity;
import com.getpills.Prescription_listActivity;
import com.getpills.Producat_detailActivity;
import com.getpills.R;
import com.getpills.SearchActivity;
import com.getpills.Upload_prescriptionActivity;
import model.Medical_category_list_model;
import model.Slider_model;
import util.CommonAsyTask;
import util.ConnectivityReceiver;
import util.NameValuePair;
import util.RecyclerTouchListener;
import util.Session_management;



public class Home_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Home_fragment.class.getSimpleName();

    private List<Slider_model> slider_modelList = new ArrayList<>();
    private List<Medical_category_list_model> medical_category_list_modelList = new ArrayList<>();

    private RecyclerView rv_slider, rv_suggest;
    private TextView tv_upload, tv_prescription, tv_medical, tv_search;

    public Home_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tv_prescription = (TextView) view.findViewById(R.id.tv_home_prescription);
        tv_upload = (TextView) view.findViewById(R.id.tv_home_upload);
        tv_medical = (TextView) view.findViewById(R.id.tv_home_medical);
        tv_search = (TextView) view.findViewById(R.id.tv_home_search);
        rv_slider = (RecyclerView) view.findViewById(R.id.rv_home_slider);
        rv_suggest = (RecyclerView) view.findViewById(R.id.rv_home_suggested);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_slider.setLayoutManager(linearLayoutManager);
        rv_suggest.setLayoutManager(linearLayoutManager2);

        tv_search.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        tv_medical.setOnClickListener(this);
        tv_prescription.setOnClickListener(this);

        // check internet connection is available or not
        if (ConnectivityReceiver.isConnected()) {
            makeGetSlider();
            makeGetSuggest();
        } else {
            // display snackbar
            ConnectivityReceiver.showSnackbar(getActivity());
        }

        rv_slider.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_slider, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String url = slider_modelList.get(position).getSlider_url();

                if (!url.isEmpty()) {
                    Uri webpage = Uri.parse(url);
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        webpage = Uri.parse("http://" + url);
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(webpage);
                    startActivity(i);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        rv_suggest.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_suggest, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Medical_category_list_model model = medical_category_list_modelList.get(position);

                Intent i = new Intent(getActivity(), Producat_detailActivity.class);
                i.putExtra("id", model.getProduct_id());
                i.putExtra("product_name", model.getProduct_name());
                i.putExtra("product_image", model.getProduct_image());
                i.putExtra("description", model.getDescription());
                i.putExtra("category_id", model.getCategory_id());
                i.putExtra("in_stock", model.getIn_stock());
                i.putExtra("price", model.getPrice());
                i.putExtra("unit_value", model.getUnit_value());
                i.putExtra("unit", model.getUnit());
                i.putExtra("mfg_id", model.getMfg_id());
                i.putExtra("isgeneric", model.getIsgeneric());
                i.putExtra("group_name", model.getGroup_name());
                i.putExtra("discount", model.getDiscount());
                i.putExtra("stock", model.getStock());
                i.putExtra("title", model.getTitle());
                i.putExtra("mfg_name", model.getMfg_name());
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent i = null;

        if (id == R.id.tv_home_upload) {
            Session_management sessionManagement = new Session_management(getActivity());
            if (sessionManagement.isLoggedIn()) {
                i = new Intent(getActivity(), Upload_prescriptionActivity.class);
            } else {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                loginIntent.putExtra("setfinish", "true");
                startActivity(loginIntent);
            }
        } else if (id == R.id.tv_home_prescription) {
            i = new Intent(getActivity(), Prescription_listActivity.class);
        } else if (id == R.id.tv_home_medical) {
            i = new Intent(getActivity(), Medical_productActivity.class);
        } else if (id == R.id.tv_home_search) {
            i = new Intent(getActivity(), SearchActivity.class);
        }

        // check intent not null then start new activity
        if (i != null) {
            startActivity(i);
        }
    }

    private void makeGetSlider() {

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(new ArrayList<NameValuePair>(),
                BaseURL.GET_SLIDER_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e(TAG, response);

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Slider_model>>() {
                }.getType();

                slider_modelList = gson.fromJson(response, listType);

                Home_slider_adapter adapter = new Home_slider_adapter(slider_modelList);
                rv_slider.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void VError(String responce) {
                Log.e(TAG, responce);
            }
        }, true, getActivity());
        task.execute();

    }

    private void makeGetSuggest() {

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(new ArrayList<NameValuePair>(),
                BaseURL.GET_SUGGEST_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e(TAG, response);

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Medical_category_list_model>>() {
                }.getType();

                medical_category_list_modelList = gson.fromJson(response, listType);

                Home_suggetion_adapter adapter = new Home_suggetion_adapter(medical_category_list_modelList, getActivity());
                rv_suggest.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void VError(String responce) {
                Log.e(TAG, responce);
            }
        }, true, getActivity());
        task.execute();
    }

}
