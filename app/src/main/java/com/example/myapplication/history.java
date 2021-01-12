package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class history extends AppCompatActivity {

    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    Animation atg;

    TextView user;
    ListView lv_history;
    private RecyclerView mFIrestoreList;
    private FirestoreRecyclerAdapter adapter;

    private java.util.List<String> nameList = new ArrayList<>();
    private List<String> List = new ArrayList<>();

    Double bmi1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        atg = AnimationUtils.loadAnimation(this, R.anim.atg);

        mFIrestoreList = findViewById(R.id.firestore_list);
        user = findViewById(R.id.userID);
        //lv_history = findViewById(R.id.lv_history);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        mFIrestoreList.startAnimation(atg);


        final DocumentReference documentReference = fStore.collection("users").document(userId);


        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                user.setText(documentSnapshot.getString("fName"));
            }
        });

        fStore.collection("users").document(userId).collection("History")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if ((task.isSuccessful())){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Log.d(TAG, documentSnapshot.getId() + "=>" + documentSnapshot.getData());
                            }
                        }else{
                            Log.d(TAG, "Error getting documents : ", task.getException());
                        }
                    }
                });

        //Query
        Query query = fStore.collection("users").document(userId).collection("History").orderBy("Tanggal", Query.Direction.ASCENDING);
        //RecycleOption
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull ProductsModel model) {
                holder.list_bmi.setText(model.getBMI());
                holder.list_status.setText(model.getStatus());
                String getchar = model.getTanggal();
                String key = getchar.substring(0,10);
                holder.list_tanggal.setText(key);
            }
        };
        //ViewHolderClass
        mFIrestoreList.setHasFixedSize(true);
        mFIrestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFIrestoreList.setAdapter(adapter);

        GetChart();
    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder{

        private TextView list_bmi;
        private TextView list_status;
        private TextView list_tanggal;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_bmi = itemView.findViewById(R.id.list_bmi);
            list_status = itemView.findViewById(R.id.list_status);
            list_tanggal = itemView.findViewById(R.id.list_tanggal);
        }

    }
    private void GetChart() {
        fStore.collection("users").document(userId).collection("History").orderBy("Tanggal", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener< QuerySnapshot >() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task< QuerySnapshot > task) {
                        if ((task.isSuccessful())){
                            ArrayList<String> lisTanggal = new ArrayList<String>();
                            ArrayList<Double> lisTanggal1 = new ArrayList<Double>();
//                            if (lisTanggal.size()>0){
//                                lisTanggal.clear();
//                            }
//                            if (lisTanggal1.size()>0){
//                                lisTanggal1.clear();
//                            }
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String tanggal = (String) documentSnapshot.getData().get("Tanggal");
                                lisTanggal.add(tanggal);
                            }
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String tanggal1 = (String) documentSnapshot.getData().get("BMI");
                                bmi1 = Double.parseDouble(tanggal1);
                                lisTanggal1.add(bmi1);
                            }
                            AnyChartView anyChartView = findViewById(R.id.any_chart_view);
                            anyChartView.setProgressBar(findViewById(R.id.progress_bar));

                            Cartesian cartesian = AnyChart.line();

                            cartesian.animation(true);

                            cartesian.padding(10d, 20d, 5d, 20d);

                            cartesian.crosshair().enabled(true);
                            cartesian.crosshair()
                                    .yLabel(true)
                                    // TODO ystroke
                                    .yStroke((Stroke) null, null, null, (String) null, (String) null);

                            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

                            cartesian.title("Grafik perhitungan BMI Anda berdasarkan History yang telah dimasukkan");

                            cartesian.yAxis(0).title("Bilangan angka");
                            cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

                            Double[] jumlah = {6.5,2.5,4.2,1.5,10.1,11.50,12.89,78.9,76.90,87.90};
                            Log.d(TAG, "Jumlah >="+jumlah);

                            List< DataEntry > seriesData = new ArrayList<>();
                            for (int i = 0; i < lisTanggal.size(); i++){
                                seriesData.add(new ValueDataEntry(lisTanggal.get(i), lisTanggal1.get(i)));
                            }
//                            for (int i = 0; i < status.length; i++){
//                                seriesData.add(new ValueDataEntry(status[i], jumlah[i]));
//                            }
                            Set set = Set.instantiate();
                            set.data(seriesData);

                            Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");


                            Line series2 = cartesian.line(series2Mapping);
                            series2.name("BMI");
                            series2.hovered().markers().enabled(true);
                            series2.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series2.tooltip()
                                    .position("right")
                                    .anchor(Anchor.CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);

                            //leftcenter
                            cartesian.legend().enabled(true);
                            cartesian.legend().fontSize(10d);
                            cartesian.legend().padding(0d, 0d, 10d, 0d);

                            anyChartView.setChart(cartesian);
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
