package com.sporlif.activities.user.registFrg;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sporlif.R;
import com.sporlif.activities.user.ActRegist;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.json.JsonArray;

import static android.app.Activity.RESULT_OK;

public class FrgProfile extends Fragment {

    private EditText frgProfileNickName;
    private TextView frgProfileLstPosition;
    private ImageView frgProfileImgProfile;

    private Bitmap profilePhoto;
    private JsonArray postions;
    private boolean[] selectedPos;
    private ArrayList<Integer> mSelectedItems = new ArrayList<>();
    private ArrayList<String> mSelectedDes = new ArrayList<>();

    public static final int PICK_IMAGE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frg_profile, container, false);
        launchWidgets(view);
        launchEvents();

        postions = ((ActRegist) getActivity()).positions;
        selectedPos = new boolean[postions.size()];
        return view;
    }

    public void launchWidgets(View view) {

        frgProfileNickName = (EditText) view.findViewById(R.id.frgProfileNickName);
        frgProfileLstPosition = (TextView) view.findViewById(R.id.frgProfileLstPosition);
        frgProfileImgProfile = (ImageView) view.findViewById(R.id.frgProfileImgProfile);

    }

    private void launchEvents() {

        frgProfileLstPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] postionsDes = new String[postions.size()];
                for (int i = 0; i < postionsDes.length; i++) {
                    postionsDes[i] = postions.getJsonObject(i).getString("d2");
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(FrgProfile.this.getActivity());
                builder.setTitle(R.string.act_regist_lbl_position);
                builder.setMultiChoiceItems(postionsDes, selectedPos, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index, boolean isChecked) {

                        int selectedValue = postions.getJsonObject(index).getInt("d1");
                        String selectedDes = postions.getJsonObject(index).getString("d2");

                        if (isChecked) {
                            mSelectedItems.add(selectedValue);
                            mSelectedDes.add(selectedDes);
                        } else if (mSelectedItems.contains(selectedValue)) {
                            mSelectedItems.remove(selectedValue);
                            mSelectedDes.remove(selectedDes);
                        }

                        selectedPos[index] = isChecked;//cuando se borra todas las pocisiones, se borran también este arreglo

                    }
                });
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if (mSelectedItems.size() == 0) {
                            return;
                        }

                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mSelectedDes.size(); i++) {
                            sb.append(mSelectedDes.get(i) + (i == mSelectedDes.size() - 1 ? "" : ", "));
                        }
                        frgProfileLstPosition.setText(sb.toString());
                    }

                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.setCancelable(true);
                builder.show();
            }
        });

        frgProfileImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            try {

                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                profilePhoto = BitmapFactory.decodeStream(inputStream);

                frgProfileImgProfile.setPadding(0, 0, 0, 0);
                frgProfileImgProfile.setImageBitmap(profilePhoto);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public String getNickName() {
        return frgProfileNickName.getText().toString();
    }

    public Integer[] getPosition() {
        return mSelectedItems.toArray(new Integer[mSelectedItems.size()]);
    }

    public Drawable getImgProfile() {
        return frgProfileImgProfile.getDrawable();
    }

}
