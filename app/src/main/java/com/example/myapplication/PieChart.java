package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PieChart extends AppCompatActivity {

    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    AnyChartView anyChartView;
    TextView jmlStatus;

    Animation atg, atgup2;


    String userId;
    String[] status = {"OBESITAS II","NORMAL","GEMUK","OBESITAS I","KEKURUSAN"};
    int jml_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        atgup2 = AnimationUtils.loadAnimation(this, R.anim.atgup2);
        atg = AnimationUtils.loadAnimation(this, R.anim.atg);

        anyChartView = findViewById(R.id.any_chart_view);
        jmlStatus = findViewById(R.id.txtJml);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userId);

        status();

        anyChartView.startAnimation(atgup2);
        jmlStatus.startAnimation(atg);

    }

    public void status(){
        fStore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if ((task.isSuccessful())) {
                            List< Boolean > boolTrue = new ArrayList<>(),
                                    boolFalse = new ArrayList<>(),
                                    boolObs = new ArrayList<>(),
                                    boolNor = new ArrayList<>(),
                                    boolT = new ArrayList<>(),
                                    boolF = new ArrayList<>(),
                                    boolGemuk = new ArrayList<>(),
                                    boolGem = new ArrayList<>(),
                                    boolG = new ArrayList<>(),
                                    boolObsI = new ArrayList<>(),
                                    boolO = new ArrayList<>(),
                                    boolOf = new ArrayList<>(),
                                    boolKurus = new ArrayList<>(),
                                    boolKur = new ArrayList<>(),
                                    boolK = new ArrayList<>();

                            int iTrue = 0, iFalse = 0, iT = 0, iF = 0, iGemuk = 0,iG = 0, iObsI = 0, iO = 0, iKurus = 0, iK = 0;
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                boolObs.add(documentSnapshot.getData().get("status").equals("OBESITAS II"));
                            }

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                boolNor.add(documentSnapshot.getData().get("status").equals("NORMAL"));
                            }

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                boolGemuk.add(documentSnapshot.getData().get("status").equals("GEMUK"));
                            }

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                boolObsI.add(documentSnapshot.getData().get("status").equals("OBESITAS I"));
                            }

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                boolKurus.add(documentSnapshot.getData().get("status").equals("KEKURUSAN"));
                            }

                            for (int i = 0; i < boolObs.size(); i++) {
                                if (boolObs.get(i)) {
                                    boolTrue.add(boolObs.get(i));
                                }
                                if (!boolObs.get(i)) {
                                    boolFalse.add(boolObs.get(i));
                                }
                            }

                            for (int i = 0; i < boolNor.size(); i++) {
                                if (boolNor.get(i)) {
                                    boolT.add(boolNor.get(i));
                                }
                                if (!boolNor.get(i)) {
                                    boolF.add(boolNor.get(i));
                                }
                            }

                            for (int i = 0; i < boolGemuk.size(); i++) {
                                if (boolGemuk.get(i)) {
                                    boolGem.add(boolGemuk.get(i));
                                }
                                if (!boolGemuk.get(i)) {
                                    boolG.add(boolGemuk.get(i));
                                }
                            }

                            for (int i = 0; i < boolObsI.size(); i++) {
                                if (boolObsI.get(i)) {
                                    boolO.add(boolObsI.get(i));
                                }
                                if (!boolObsI.get(i)) {
                                    boolOf.add(boolObsI.get(i));
                                }
                            }

                            for (int i = 0; i < boolKurus.size(); i++) {
                                if (boolKurus.get(i)) {
                                    boolKur.add(boolKurus.get(i));
                                }
                                if (!boolKurus.get(i)) {
                                    boolK.add(boolKurus.get(i));
                                }
                            }

                            iTrue = boolTrue.size();
                            iFalse = boolFalse.size();
                            iT = boolT.size();
                            iF = boolF.size();
                            iGemuk = boolGem.size();
                            iG = boolG.size();
                            iObsI = boolO.size();
                            iO = boolOf.size();
                            iKurus = boolKur.size();
                            iK = boolK.size();

                            jml_status = iTrue + iT + iGemuk + iObsI + iKurus;

                            Log.d(TAG, "onComplete 1: " + boolObs.toString());
                            Log.d(TAG, "onComplete OBESITSAS 2: true: " + iTrue + ", false: " + iFalse);
                            Log.d(TAG, "onComplete Normal: true: " + iT + ", false: " + iF);
                            Log.d(TAG, "onComplete Gemuk: true: " + iGemuk + ", false: " + iG);
                            Log.d(TAG, "onComplete Obesitas 1: true: " + iObsI + ", false: " + iO);
                            Log.d(TAG, "onComplete Kurus: true: " + iKurus + ", false: " + iK);

                            jmlStatus.setText("JUMLAH = " + jml_status);

                            int[] jumlah = {iTrue, iT, iGemuk, iObsI, iKurus};

                            Pie pie = AnyChart.pie();
                            List<DataEntry> dataEntries = new ArrayList<>();

                            for (int i = 0; i < status.length; i++){
                                dataEntries.add(new ValueDataEntry(status[i], jumlah[i]));
                            }
                            pie.data(dataEntries);
                            anyChartView.setChart(pie);

                        } else {
                            Log.d(TAG, "Error getting documents : ", task.getException());
                        }
                    }
                });
    }
}
