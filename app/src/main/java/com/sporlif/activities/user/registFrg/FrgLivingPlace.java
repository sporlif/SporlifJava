package com.sporlif.activities.user.registFrg;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sporlif.R;
import com.sporlif.utils.ClientHttpRequest;
import com.sporlif.utils.NoDialogAsyncTask;
import com.sporlif.utils.UtilsForViews;

import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class FrgLivingPlace extends Fragment {

    private EditText frgLivingPlaceEtSearch;
    private ListView frgLivingPlaceLstPlaces;
    private ProgressBar frgLivingPlacePbSearching;
    private TextView frgLivingPlaceTxtError;
    private searchPlace searchPlaceTask;
    private ImageView frgLivingPlaceBtnClear;

    private int selectedPosId = 0;

    JsonArray plcResult;

    public FrgLivingPlace() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_living_place, container, false);

        frgLivingPlaceEtSearch = (EditText) view.findViewById(R.id.frgLivingPlaceEtSearch);
        frgLivingPlaceLstPlaces = (ListView) view.findViewById(R.id.frgLivingPlaceLstPlaces);
        frgLivingPlacePbSearching = (ProgressBar) view.findViewById(R.id.frgLivingPlacePbSearching);
        frgLivingPlaceTxtError = (TextView) view.findViewById(R.id.frgLivingPlaceTxtError);
        frgLivingPlaceBtnClear = (ImageView) view.findViewById(R.id.frgLivingPlaceBtnClear);

        frgLivingPlaceEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().length() == 0) {
                    return;
                }

                if (searchPlaceTask != null) {
                    searchPlaceTask.cancel(true);
                    FrgLivingPlace.this.frgLivingPlacePbSearching.setVisibility(View.GONE);
                }

                FrgLivingPlace.this.frgLivingPlacePbSearching.setVisibility(View.VISIBLE);
                searchPlaceTask = new searchPlace(FrgLivingPlace.this.getActivity(), s.toString());
                searchPlaceTask.execute();
            }

        });

        frgLivingPlaceBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frgLivingPlaceEtSearch.setText("");
            }
        });

        return view;
    }

    public int getSelectedPosId() {
        return selectedPosId;
    }

    private class searchPlace extends NoDialogAsyncTask<String> {

        String param;

        public searchPlace(Context context, String param) {
            super(context);
            this.param = param;
        }

        @Override
        protected String task() {

            int tries = 0;
            do {
                try {

                    ClientHttpRequest request = new ClientHttpRequest(new URL("https://wsporlif-project.herokuapp.com/?mod=user&op=getC&filter=" + param).openConnection());//luigar
                    request.setConnectTimeout(ClientHttpRequest.CONNECT_TIMEOUT);
                    plcResult = Json.createReader(request.post()).readArray();

                    break;

                } catch (Exception e) {
                    e.printStackTrace();
                    tries++;
                    if (tries == 3) {
                        return "TIMEOUT";
                    }
                }
            } while (tries < 3);

            return "OK";
        }

        @Override
        protected void result(String res) {

            if (res == "OK") {
                UtilsForViews.setListItems(FrgLivingPlace.this.getActivity(), plcResult, frgLivingPlaceLstPlaces);

                frgLivingPlaceLstPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                        JsonObject selectedObj = plcResult.getJsonObject(position);
                        frgLivingPlaceEtSearch.setText(selectedObj.getString("d2"));
                        FrgLivingPlace.this.setPosId(selectedObj.getInt("d1"));
                    }
                });

            }

            frgLivingPlacePbSearching.setVisibility(View.GONE);

        }
    }

    public void setPosId(int posId) {
        this.selectedPosId = posId;
    }


}
