package com.juaracoding.gamekuis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.juaracoding.gamekuis.kuis.QuisModel;
import com.juaracoding.gamekuis.service.APIClient;
import com.juaracoding.gamekuis.service.APIInterfacesRest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoalQuis extends AppCompatActivity implements View.OnClickListener {

    TextView txtSoal, txtJawaban;
    Button btnA, btnB, btnC, btnD;
    RadioGroup radioGroup;
    ImageView imageSoal;

    ArrayList<String> jumlah = new ArrayList<>();

    int sizeArr;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSoal = findViewById(R.id.txtSoal);
        txtJawaban = findViewById(R.id.txtJawaban);
        imageSoal = findViewById(R.id.imageSoall);
        radioGroup = findViewById(R.id.radioGroup);


        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        btnA.setId(Integer.parseInt("1"));
        btnB.setId(Integer.parseInt("2"));
        btnC.setId(Integer.parseInt("3"));
        btnD.setId(Integer.parseInt("4"));

        callSoalQuiz();


        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);


    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;


    public void callSoalQuiz() {

        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(SoalQuis.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        Call<QuisModel> call3 = apiInterface.getSoalQuiz();
        call3.enqueue(new Callback<QuisModel>() {
            @Override
            public void onResponse(Call<QuisModel> call, Response<QuisModel> response) {
                progressDialog.dismiss();
                QuisModel data = response.body();
                //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                if (data != null) {

                    sizeArr = data.getData().getSoalQuizAndroid().size();

                    if (i < sizeArr) {

                            txtSoal.setText(data.getData().getSoalQuizAndroid().get(i).getPertanyaan());
                            String gambar = data.getData().getSoalQuizAndroid().get(i).getGambar();
                            Picasso.get().load(gambar).into(imageSoal);

                            btnA.setText(data.getData().getSoalQuizAndroid().get(i).getA());
                            btnB.setText(data.getData().getSoalQuizAndroid().get(i).getB());
                            btnC.setText(data.getData().getSoalQuizAndroid().get(i).getC());
                            btnD.setText(data.getData().getSoalQuizAndroid().get(i).getD());

                        }
                        i++;



                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(SoalQuis.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(SoalQuis.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<QuisModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });


    }


    @Override
    public void onClick(View v) {
        jumlah.add(String.valueOf(v.getId()));
        Toast.makeText(SoalQuis.this, String.valueOf(jumlah), Toast.LENGTH_LONG).show();
        callSoalQuiz();

        if (i == 3) {


            Intent i = new Intent(SoalQuis.this, ResultNilai.class);
            i.putStringArrayListExtra("jumlah", jumlah);
            startActivity(i);
            finish();


        }
    }
}


