package com.svalero.ontimeapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePreference {

    /**
     * Métodos para salvar las preferencias y llamarlo desde cualquier sitio
     * https://developer.android.com/training/data-storage/shared-preferences?hl=es-419
     * @param key : Asignamos una key para poder recuperarla
     * @param value : Valor que le asigamos y va a sociado a la key para poder recuperarlo
     * @param context
     */
    public static void setSavePreference (String key, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getSavePreference (String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
