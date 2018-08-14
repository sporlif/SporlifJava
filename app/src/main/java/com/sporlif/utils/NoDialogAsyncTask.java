package com.sporlif.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sporlif.R;

public abstract class NoDialogAsyncTask<T> extends AsyncTask<T,Integer,T>{

    Context context;

    public NoDialogAsyncTask(Context context){
        this.context = context;
    }

    protected abstract T task();
    protected abstract void result(T res);

    protected void execute(){
        this.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected T doInBackground(T... params) {
        System.out.println("Ejecutando...");
        return task();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(T s) {
        super.onPostExecute(s);
        System.out.println("Terminó Ejecución...");
        result(s);
    }

    @Override
    protected void onCancelled(T s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        System.out.println("Ejecución Cancelada...");
        super.onCancelled();
    }
}
