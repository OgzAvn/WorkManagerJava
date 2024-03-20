package com.oguz.javaworkmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Data data = new Data.Builder().putInt("intKey",1).build();

        //Bazı şartlar koyabiliyoruz. Örneğin : Şarj ederken çalıştır, internete bağlıyken çalıştır gibi

        Constraints constraints = new Constraints.Builder()
                //.setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build();

        /*
        //İş isteği oluşturacağız
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDataBase.class) // Bir defa arka planda yap onetimerequest
                .setConstraints(constraints)
                .setInputData(data)
                //.setInitialDelay(5, TimeUnit.HOURS)
                //.addTag("myTag") //Birden fazla workRequest imiz varsa Tag lerine göre ayrıştırabilirz ya da kendisi zaten bir ID atıyor ona göre ayrıştırabiliriz.
                .build();


        WorkManager.getInstance(this).enqueue(workRequest); //WorkRequest i işleme alıyoruz
         */

        WorkRequest workRequest = new PeriodicWorkRequest.Builder(RefreshDataBase.class,15,TimeUnit.MINUTES) //15 dk altı bir sürede çalışmaz
                .setConstraints(constraints)
                .setInputData(data)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);

        //Eğer bir şekilde yapılan work ün ne durumda olduğunu görmek istiyorsanız.Bildirim gelsin istiyorsanız ve bu bildirime göre işlem yapmak istiyorsanız
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo.getState() == WorkInfo.State.RUNNING) {//işin şuanki durumunu alıyorum
                    System.out.println("running");
                }else if (workInfo.getState() == WorkInfo.State.SUCCEEDED){
                    System.out.println("Succeded");
                }else if (workInfo.getState()== WorkInfo.State.FAILED){
                    System.out.println("Failed");
                }
            }
        });

        //Nasıl iptal edebiliriz

        //WorkManager.getInstance(this).cancelWorkById(workRequest.getId());

        /*
        //Chaining - Zincirleme

        //Periyodik iş isteklerini birbirine bağlayamıyoruz. Sadece bir defa yaptıklarımızı bağlıyabiliyoruz.
        //Arka planda arka arkaya sırasıyla bir iş yaptırmak istediğimizde bunu kullanabiliriz.
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefreshDataBase.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest) //3 tane farklı data, constraints lere sahip olan workrequest olduklarını düşünün
                .then(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .enqueue();

         */

    }


}