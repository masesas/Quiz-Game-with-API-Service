package com.juaracoding.gamekuis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.juaracoding.gamekuis.kuis.QuisModel;
import com.juaracoding.gamekuis.service.APIClient;
import com.juaracoding.gamekuis.service.APIInterfacesRest;

import org.json.JSONObject;

import java.net.Inet4Address;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultNilai extends AppCompatActivity  {

    Button btnFinish, btnRepeat;
    TextView txtNilai;
    ArrayList<String> tmp = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_nilai);

        txtNilai = findViewById(R.id.txtNilai);

        btnFinish = findViewById(R.id.btnFinish);
        btnRepeat = findViewById(R.id.btnRepeat);


        Intent arry = getIntent();
        tmp = arry.getStringArrayListExtra("jumlah");


        callInputData();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResultNilai.this, "Thanks!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent repeat = new Intent(ResultNilai.this, SoalQuis.class);
                startActivity(repeat);

            }
        });
    }


    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;

    public void callInputData(){

        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(ResultNilai.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        Call<QuisModel> call3 = apiInterface.getSoalQuiz();
        call3.enqueue(new Callback<QuisModel>() {
            @Override
            public void onResponse(Call<QuisModel> call, Response<QuisModel> response) {
                progressDialog.dismiss();
                QuisModel data = response.body();

                if (data !=null) {


                    Integer jumlahPoint = 0;
                    Integer nilaiSoal = 0;

                    for (int i = 0; i < data.getData().getSoalQuizAndroid().size(); i++) {

                        ArrayList<String> tambah = new ArrayList<String>(3);
                        tambah.add("4");
                        tambah.add("1");
                        tambah.add("1");

                        if (tmp.get(i).equalsIgnoreCase(tambah.get(i))) {
                            jumlahPoint = Integer.parseInt(data.getData().getSoalQuizAndroid().get(i).getPoint());
                            nilaiSoal += jumlahPoint;
                        }


                        txtNilai.setText(String.valueOf(nilaiSoal));

                    }



                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(ResultNilai.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(ResultNilai.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<QuisModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });




    }




}
