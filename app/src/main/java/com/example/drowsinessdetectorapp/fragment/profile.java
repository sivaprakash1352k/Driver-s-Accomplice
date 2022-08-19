package com.example.drowsinessdetectorapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drowsinessdetectorapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {
    private static final String TAG = "profile";
    private EditText txtview1,txtview2,txtview3,txtview4;
    private Button Reg;
    private ImageView imgselect;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,"View is to be Created");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences prof = getContext().getSharedPreferences("MySharedPref",MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = prof.edit();

        txtview1 = (EditText) view.findViewById(R.id.txtview1);
        txtview2 = (EditText) view.findViewById(R.id.txtview2);
        txtview3 = (EditText) view.findViewById(R.id.txtview3);
        txtview4 = (EditText) view.findViewById(R.id.txtview4);
        Reg = (Button) view.findViewById(R.id.Reg);
        imgselect = (ImageView) view.findViewById(R.id.imgselect);
        txtview1.setText(prof.getString("name",""));
        txtview2.setText(prof.getString("reg",""));
        txtview3.setText(prof.getString("no",""));
        txtview4.setText(prof.getString("route",""));

        Reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                if(validate_data()){
                    String txt1 = txtview1.getText().toString();
                    //Toast.makeText(getActivity(),txt1,Toast.LENGTH_LONG).show();
                    String txt2 = txtview2.getText().toString();
                    String txt3 = txtview3.getText().toString();
                    String txt4 = txtview4.getText().toString();
                    myEdit.putString("name",txt1);
                    myEdit.putString("reg",txt2);
                    myEdit.putString("no",txt3);
                    myEdit.putString("route",txt4);                }

                myEdit.commit();


            }
            private boolean validate_data(){
                if(txtview1.getText().toString().equals("")){
                    return false;
                }
                if(txtview2.getText().toString().equals("")){
                    return false;
                }
                if(txtview3.getText().toString().equals("")){
                    return false;
                }
                if(txtview4.getText().toString().equals("")){
                    return false;
                }
                return true;



            }
        });
        Log.i(TAG,"Widgets are to be Initialized");
        return view;
    }
}




