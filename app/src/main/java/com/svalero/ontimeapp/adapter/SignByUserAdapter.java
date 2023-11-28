package com.svalero.ontimeapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignDeleteContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.presenter.SignDeletePresenter;

import java.util.List;

/**
 * SignAdapter: Es la clase en la que le explicamos a Android como pintar cada elemento en el RecyclerView
 * Patron Holder: 1) Constructor - 2) onCreateViewHolder - 3) onBindViewHolder - 4) getItemCount - 5) Y la estructura SuperheroHolder
 * al extender de la clase RecyclerView los @Override los añadira automáticamente para el patron Holder, solo añadiremos nosotros el 5)
 * implements TeamDeleteContract.View porque hace las funciones de view para implentar sus metodos
 */
public class SignByUserAdapter extends RecyclerView.Adapter<SignByUserAdapter.SignHolder> {

    private Context context;
    private List<Sign> signsList;
    private String department;
    private Sign sign;
    private View snackBarView;


    /**
     * 1) Constructor que creamos para pasarle los datos que queremos que pinte
     * el contexto y la lista de fichajes
     * @param dataList Lista de fichajes que le pasamos
     */
    public SignByUserAdapter(Context context, List<Sign> dataList) {
        this.context = context;
        this.signsList = dataList;
    }

    public Context getContext() {
        return context;
    }

    /**
     * 4)
     * Metodo con el que Android va a inflar, va a crear cada estructura del layout donde irán los datos de cada equipo.
     * Vista detalle de cada equipo
     */
    @Override
    public SignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.signs_item_user, parent, false); // Layout con la card para cada fichaje
        return new SignHolder(view); // Creamos un holder para cada una de las estructuras que infla el layout
    }

    /**
     * 5)
     * Metodo que estamos obligados para hacer corresponder los valores de la lista y pintarlo en cada elemento de layout
     * es para poder recorrer en el bucle por cada elemento de la lista y poder pintarlo
     */
    @Override
    public void onBindViewHolder(SignHolder holder, int position) {
        holder.date.setText(String.valueOf(signsList.get(position).getDay()));
        holder.signIn.setText(signsList.get(position).getIn_time());
        holder.signOut.setText(signsList.get(position).getOut_time());
        holder.modality.setText(signsList.get(position).getModality());
        holder.incidenceIn.setText(signsList.get(position).getIncidence_in());
        holder.incidenceOut.setText(signsList.get(position).getIncidence_out());
    }

    /**
     * 2)
     * Metodo que estamos obligados a hacer para que devuelva el número de elementos y android pueda hacer sus calculos y pintar xtodo en base a esos calculos
     */
    @Override
    public int getItemCount() {
        return signsList.size(); // devolvemos el tamaño de la list
    }

    /**
     * 2) OJO me hace falta crearlo de lo primero
     * 5) Holder son las estructuras que contienen los datos y los rellenan luego
     * Creamos todos los componentes que tenemos
     */
    public class SignHolder extends RecyclerView.ViewHolder {
        public TextView signUsername;
        public TextView signIn;
        public TextView signOut;
        public TextView modality;
        public TextView date;
        public TextView incidenceIn;
        public TextView incidenceOut;

        public Button modifySignButton;
        public Button detailsSignButton;

        private View parentView; // vista padre - como el recyclerView

        /**
         * 3) Constructor del Holder
         */
        public SignHolder(View view) {
            super(view); // Vista padre
            parentView = view; // Guardamos el componente padre

            date = view.findViewById(R.id.tv_card_date_user);
            signIn = view.findViewById(R.id.tv_card_in_user);
            signOut = view.findViewById(R.id.tv_card_out_user);
            modality = view.findViewById(R.id.tv_card_modality_user);
            incidenceIn = view.findViewById(R.id.tv_card_incidence_in_user);
            incidenceOut = view.findViewById(R.id.tv_card_incidence_out_user);

            modifySignButton = view.findViewById(R.id.bt_card_modify_user);
            detailsSignButton = view.findViewById(R.id.bt_card_details_user);

            // TODO añadir opción que realizarán los botones

        }
    }
}
