package com.example.myapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class target_berat extends AppCompatActivity {

    Animation atg, atg2, atgup2;

    TextView edit_target, target_berat, berat_sekarang, kebutuhan_kalori;

    LinearLayout target_berat_anda, card_kalori, set_pengingat;

    private String myText;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String status;
    String set_alarm;
    String nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_berat);

        atg = AnimationUtils.loadAnimation(this, R.anim.atg);
        atg2 = AnimationUtils.loadAnimation(this, R.anim.atg2);
        atgup2 = AnimationUtils.loadAnimation(this, R.anim.atgup2);

        target_berat_anda = findViewById(R.id.target_berat_anda);
        card_kalori = findViewById(R.id.card_kalori);
        edit_target = findViewById(R.id.edit_target);
        target_berat = findViewById(R.id.target_berat);
        berat_sekarang = findViewById(R.id.berat_sekarang);
        kebutuhan_kalori = findViewById(R.id.kebutuhan_kalori);
        set_pengingat = findViewById(R.id.set_pengingat);

        target_berat_anda.startAnimation(atg);
        card_kalori.startAnimation(atg2);
        set_pengingat.startAnimation(atgup2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                kebutuhan_kalori.setText(documentSnapshot.getString("keperluan_kalori"));
                berat_sekarang.setText(documentSnapshot.getString("Berat_Badan"));
                status = documentSnapshot.getString("status");
                nama = documentSnapshot.getString("fName");
                target_berat.setText(documentSnapshot.getString("Target"));
            }
        });

    }

    public void edit_target(View view) {

        Dialog dialog;

        edit_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(target_berat.this);
                mydialog.setTitle("Masukkan Berat Banda Anda (kg)");

                final EditText input = new EditText(target_berat.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                mydialog.setView(input);

                mydialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Double myText = Double.parseDouble(input.getText().toString());
                        Double nilai = Double.parseDouble(berat_sekarang.getText().toString());

                        if (status.equals("KEKURUSAN")) {
                            if (myText < nilai) {
                                Toast.makeText(target_berat.this, "Maaf Jangan Turunkan Berat Badan", Toast.LENGTH_SHORT).show();
                            } else if (myText > nilai) {
                                Toast.makeText(target_berat.this, "Target Berat Anda " + myText, Toast.LENGTH_SHORT).show();
                                target_berat.setText("" + myText);
                                String hasil_target = String.valueOf(myText);
                                fStore.collection("users").document(userId)
                                        .update("Target", hasil_target)
                                        .addOnCompleteListener(new OnCompleteListener< Void >() {
                                            @Override
                                            public void onComplete(@NonNull Task< Void > task) {
                                                Toast.makeText(target_berat.this, "Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(target_berat.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(target_berat.this, "Maaf Tidak Terjadi Perubahan Target", Toast.LENGTH_SHORT).show();
                            }
                        } else if (status.equals("NORMAL")) {
                            if (myText < nilai) {
                                Toast.makeText(target_berat.this, "Anda Tidak Perlu Merubah Apapun", Toast.LENGTH_SHORT).show();
                            } else if (myText > nilai) {
                                Toast.makeText(target_berat.this, "Anda Tidak Perlu Merubah Apapun", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(target_berat.this, "Anda Tidak Perlu Merubah Apapun", Toast.LENGTH_SHORT).show();
                            }
                        } else if (status.equals("GEMUK")) {
                            if (myText < nilai) {
                                Toast.makeText(target_berat.this, "Berat Anda " + myText, Toast.LENGTH_SHORT).show();
                                target_berat.setText("" + myText);
                                String hasil_target = String.valueOf(myText);
                                fStore.collection("users").document(userId)
                                        .update("Target", hasil_target)
                                        .addOnCompleteListener(new OnCompleteListener< Void >() {
                                            @Override
                                            public void onComplete(@NonNull Task< Void > task) {
                                                Toast.makeText(target_berat.this, "Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(target_berat.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (myText > nilai) {
                                Toast.makeText(target_berat.this, "Maaf Itu Sama Saja Menaikkan Berat Badan (Tidak Sehat) " + myText, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(target_berat.this, "Maaf Tidak Terjadi Perubahan Target", Toast.LENGTH_SHORT).show();
                            }
                        } else if (status.equals("OBESITAS I")) {
                            if (myText < nilai) {
                                Toast.makeText(target_berat.this, "Berat Anda " + myText, Toast.LENGTH_SHORT).show();
                                target_berat.setText("" + myText);
                                String hasil_target = String.valueOf(myText);
                                fStore.collection("users").document(userId)
                                        .update("Target", hasil_target)
                                        .addOnCompleteListener(new OnCompleteListener< Void >() {
                                            @Override
                                            public void onComplete(@NonNull Task< Void > task) {
                                                Toast.makeText(target_berat.this, "Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(target_berat.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (myText > nilai) {
                                Toast.makeText(target_berat.this, "Maaf Itu Sama Saja Menaikkan Berat Badan (Tidak Sehat) " + myText, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(target_berat.this, "Maaf Tidak Terjadi Perubahan Target", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (myText < nilai) {
                                Toast.makeText(target_berat.this, "Berat Anda " + myText, Toast.LENGTH_SHORT).show();
                                target_berat.setText("" + myText);
                                String hasil_target = String.valueOf(myText);
                                fStore.collection("users").document(userId)
                                        .update("Target", hasil_target)
                                        .addOnCompleteListener(new OnCompleteListener< Void >() {
                                            @Override
                                            public void onComplete(@NonNull Task< Void > task) {
                                                Toast.makeText(target_berat.this, "Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(target_berat.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (myText > nilai) {
                                Toast.makeText(target_berat.this, "Maaf Itu Sama Saja Menaikkan Berat Badan (Tidak Sehat) " + myText, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(target_berat.this, "Maaf Tidak Terjadi Perubahan Target", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                mydialog.show();
            }
        });

    }

    public void set_pengingat(View view) {

        final CharSequence[] items = {"1 Minggu Kedepan", "2 Minggu Kedepan", "3 Minggu Kedepan", "4 Minggu Kedepan"};

        AlertDialog.Builder builder = new AlertDialog.Builder(target_berat.this);
        builder.setTitle("Setel Pengingat Anda");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                set_alarm = (String) items[item];
            }
        });

        createNotificationChannel();

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (set_alarm.equals("1 Minggu Kedepan")) {
                            Toast.makeText(target_berat.this, nama + " Berhasil Mengatur " + set_alarm, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(target_berat.this, ReminderBroadcast.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(target_berat.this, 0, intent, 0);

                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                            //Calendar calendar = Calendar.getInstance();
                            //calendar.set(Calendar.SECOND, 0);
                            //calendar.set(Calendar.MINUTE, 0);
                            //calendar.set(Calendar.HOUR, 0);
                            long timeAtButtonClick = System.currentTimeMillis();
                            long tenSeconds = 1000 * 10; //604800

                            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10,pendingIntent);

                            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSeconds, pendingIntent);
                        } else if (set_alarm.equals("2 Minggu Kedepan")) {
                            Toast.makeText(target_berat.this, nama + " Berhasil Mengatur " + set_alarm, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(target_berat.this, ReminderBroadcast.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(target_berat.this, 0, intent, 0);

                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                            //Calendar calendar = Calendar.getInstance();
                            //calendar.set(Calendar.SECOND, 0);
                            //calendar.set(Calendar.MINUTE, 0);
                            //calendar.set(Calendar.HOUR, 0);
                            long timeAtButtonClick = System.currentTimeMillis();
                            long tenSeconds = 1000 * 20; //604800 * 2

                            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10,pendingIntent);

                            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSeconds, pendingIntent);

                        } else if (set_alarm.equals("3 Minggu Kedepan")) {
                            Toast.makeText(target_berat.this, nama + " Berhasil Mengatur " + set_alarm, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(target_berat.this, ReminderBroadcast.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(target_berat.this, 0, intent, 0);

                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                            //Calendar calendar = Calendar.getInstance();
                            //calendar.set(Calendar.SECOND, 0);
                            //calendar.set(Calendar.MINUTE, 0);
                            //calendar.set(Calendar.HOUR, 0);
                            long timeAtButtonClick = System.currentTimeMillis();
                            long tenSeconds = 1000 * 30; //604800 * 3

                            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10,pendingIntent);

                            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSeconds, pendingIntent);

                        } else {
                            Toast.makeText(target_berat.this, nama + " Berhasil Mengatur " + set_alarm, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(target_berat.this, ReminderBroadcast.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(target_berat.this, 0, intent, 0);

                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                            //Calendar calendar = Calendar.getInstance();
                            //calendar.set(Calendar.SECOND, 0);
                            //calendar.set(Calendar.MINUTE, 0);
                            //calendar.set(Calendar.HOUR, 0);
                            long timeAtButtonClick = System.currentTimeMillis();
                            long tenSeconds = 1000 * 40; //604800 * 4

                            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10,pendingIntent);

                            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSeconds, pendingIntent);
                        }

                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(target_berat.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
