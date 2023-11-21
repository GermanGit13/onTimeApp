package com.svalero.ontimeapp.adapter;

import static android.app.ProgressDialog.show;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
public class SignAdapter extends RecyclerView.Adapter<SignAdapter.SignHolder> implements SignDeleteContract.View {

    private Context context;
    private List<Sign> signsList;
    private String department;
    private Sign sign;
    private View snackBarView;
    private SignDeletePresenter presenter;

    /**
     * 1) Constructor que creamos para pasarle los datos que queremos que pinte
     * el contexto y la lista de fichajes
     * @param dataList Lista de fichajes que le pasamos
     */
    public SignAdapter(Context context, List<Sign> dataList) {
        this.context = context;
        this.signsList = dataList;
        presenter = new SignDeletePresenter(this);
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
        holder.signUsername.setText(String.valueOf(signsList.get(position).getUserInSign().getUsername()));
        holder.date.setText(String.valueOf(signsList.get(position).getDay()));
        holder.signIn.setText(signsList.get(position).getIn_time());
        holder.signOut.setText(signsList.get(position).getOut_time());
        holder.modality.setText(signsList.get(position).getModality());
        holder.incidenceIn.setText(signsList.get(position).getIncidence_in());
        holder.incidenceOut.setText(signsList.get(position).getIncidence_out());
        String urlPhoto = signsList.get(position).getUserInSign().getPhoto();
        Log.d("List Sign", "Llamada desde el adapter: " + urlPhoto); //Para depurar errores y ver si avanza o donde se para
        Glide.with(context)
            .load(signsList.get(position).getUserInSign().getPhoto())
            .error(R.drawable.notphoto)
            .into(holder.signPhoto);
//        if ((signsList.get(position).getUserInSign().getPhoto()).equals("") ){
//            holder.signPhoto.setImageResource(R.drawable.notphoto);
//        } else{
//            holder.signPhoto.setImageURI(Uri.parse(signsList.get(position).getUserInSign().getPhoto()));
//        }
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
        public ShapeableImageView signPhoto;

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
            date = view.findViewById(R.id.tv_card_date);
            signIn = view.findViewById(R.id.tv_card_in);
            signOut = view.findViewById(R.id.tv_card_out);
            modality = view.findViewById(R.id.tv_card_modality);
            incidenceIn = view.findViewById(R.id.tv_card_incidence_in);
            incidenceOut = view.findViewById(R.id.tv_card_incidence_out);
            signPhoto = view.findViewById(R.id.rv_card_photo);

            modifySignButton = view.findViewById(R.id.bt_list_all_find);
            deleteSignButton = view.findViewById(R.id.bt_list_all_clear);
            detailsSignButton = view.findViewById(R.id.bt_card_details);

            // TODO añadir opción que realizarán los botones
            deleteSignButton.setOnClickListener( v -> deleteSign(getAdapterPosition()));
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showMessage(String message) {

    }

    /**
     * Métodos de los botones del layout para recoger el id y registrar una inspection
     */
    // TODO añadir los métodos que son llamados por los botones
    private void deleteSign(int position) {
        Sign sign = signsList.get(position);
        Log.d("Delete Sign", "Desde Aviso de Borrar:" + sign.getId());

//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage(R.string.delete_sign)
//                .setTitle("Delete select Sign")
//                .setPositiveButton(R.string.yes, (dialog, id) -> {
//                    presenter.deleteSign(String.valueOf(sign.getId()));
//
//                    signsList.remove(position);
//                    notifyItemRemoved(position);
//                })
//                .setNegativeButton(R.string.not, (dialog, id) -> dialog.dismiss());
//        AlertDialog dialog = builder.create();
//        dialog.show();

        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.delete_select_sign)
                .setMessage(context.getString(R.string.the_sign_dated) + sign.getDay() + context.getString(R.string.will_be_deleted))
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.deleteSign(String.valueOf(sign.getId()));

                        signsList.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .setNegativeButton(R.string.not , ((dialog, which) -> dialog.dismiss()))
                .show();
    }
}
