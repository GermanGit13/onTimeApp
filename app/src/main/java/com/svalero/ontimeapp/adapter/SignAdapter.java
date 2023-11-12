package com.svalero.ontimeapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignListContract;
import com.svalero.ontimeapp.domain.Sign;

import java.io.File;
import java.util.List;

/**
 * SignAdapter: Es la clase en la que le explicamos a Android como pintar cada elemento en el RecyclerView
 * Patron Holder: 1) Constructor - 2) onCreateViewHolder - 3) onBindViewHolder - 4) getItemCount - 5) Y la estructura SuperheroHolder
 * al extender de la clase RecyclerView los @Override los añadira automáticamente para el patron Holder, solo añadiremos nosotros el 5)
 * implements TeamDeleteContract.View porque hace las funciones de view para implentar sus metodos
 */
public class SignAdapter extends RecyclerView.Adapter<SignAdapter.SignHolder> {
    //TODO Falta implementar SignDeleteContract.View

    private Context context;
    private List<Sign> signsList;
    private Sign sign;
    private View snackBarView;

    /**
     * 1) Constructor que creamos para pasarle los datos que queremos que pinte
     * el contexto y la lista de fichajes
     * @param dataList Lista de fichajes que le pasamos
     */
    public SignAdapter(Context context, List<Sign> dataList) {
        this.context = context;
        this.signsList = dataList;
//        presenter = new SignDeletePresenter(this);
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
                .inflate(R.layout.signs_item, parent, false); // Layout con la card para cada fichaje
        return new SignHolder(view); // Creamos un holder para cada una de las estructuras que infla el layout
    }

    /**
     * 5)
     * Metodo que estamos obligados para hacer corresponder los valores de la lista y pintarlo en cada elemento de layout
     * es para poder recorrer en el bucle por cada elemento de la lista y poder pintarlo
     */
    @Override
    public void onBindViewHolder(SignHolder holder, int position) {
        holder.signUsername.setText(String.valueOf(signsList.get(position).getUser().getUsername()));
        holder.signIn.setText(signsList.get(position).getIn_time());
        holder.signOut.setText(signsList.get(position).getOut_time());
        holder.signPhoto.setImageURI(Uri.fromFile(new File(signsList.get(position).getUser().getPhoto()))); // Todo REVISAR
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
        public ImageView signPhoto;

        public Button modifySignButton;
        public Button deleteSignButton;
        public Button detailsSignButton;

        private View parentView; // vista padre - como el recyclerView

        /**
         * 3) Constructor del Holder
         */
        public SignHolder(View view) {
            super(view); // Vista padre
            parentView = view; // Guardamos el componente padre

            signUsername = view.findViewById(R.id.tv_card_username);
            signIn = view.findViewById(R.id.tv_card_in);
            signOut = view.findViewById(R.id.tv_card_out);
            signPhoto = view.findViewById(R.id.iv_card_photo);

            modifySignButton = view.findViewById(R.id.bt_card_modify);
            deleteSignButton = view.findViewById(R.id.bt_card_delete);
            detailsSignButton = view.findViewById(R.id.bt_card_details);

            // TODO añadir opción que realizarán los botones
        }
    }

    /**
     * Métodos de los botones del layout para recoger el id y registrar una inspection
     */
    // TODO añadir los métodos que son llamados por los botones
}
