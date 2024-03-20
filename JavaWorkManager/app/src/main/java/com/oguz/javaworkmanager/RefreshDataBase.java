package com.oguz.javaworkmanager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.DataKt;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RefreshDataBase extends Worker {

    Context myContext;
    public RefreshDataBase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.myContext=context;
    }

    @NonNull
    @Override
    public Result doWork() { //Workmanager in ne yapacağını buraya yazıyoruz.

        Data data = getInputData(); //Bize bir girdi (input) verilecek onu al demek.
        int myNumber = data.getInt("intKey",0);
        refreshDatabase(myNumber);
        return Result.success();
    }

    private void refreshDatabase(int myNumber){ //SharedPrefenrence içerisine bi numara kaydediceğim istediğim periyot da bunu değiştireceğim.

        //Activity den context alman lazım sharedpreferences da
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("com.oguz.javaworkmanager",Context.MODE_PRIVATE);
        int mySavedNumber = sharedPreferences.getInt("MyNumber",0);
        mySavedNumber = mySavedNumber + myNumber;
        System.out.println(mySavedNumber);
        sharedPreferences.edit().putInt("MyNumber",mySavedNumber).apply(); //Sharedpreferences a kaydediyoruz mySavedNumber ı

    }
}
