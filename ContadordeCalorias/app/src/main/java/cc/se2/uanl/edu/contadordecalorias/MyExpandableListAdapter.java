package cc.se2.uanl.edu.contadordecalorias;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import static cc.se2.uanl.edu.contadordecalorias.R.color.rojo;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<Group>groups;
    public LayoutInflater inflater;
    public Activity activity;
    private String [] pv;
    private Cursor food;
    private MyDatabase db;
    private  AutoCompleteTextView alimento;
    private ImageButton buscar;
    private NumberPicker np;
    private TextView tvCaloriasDiarias, tvCaloriasMeta;
    private String stAlimento;
    private int cantidad;
    private int caloriasDiarias = 0;
    private int caloriasMeta = 0;
    private SharedPreferences preferencesPerfil, preferencesContador;
    public static final String PREFS_NAME2 = "MyPrefsFile2";

    public MyExpandableListAdapter(Activity act, SparseArray<Group> groups)
    {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
        db = new MyDatabase(activity);
        food = db.getAlimentos();

        preferencesPerfil = activity.getSharedPreferences(PerfilActivity.PREFS_NAME, 0);
        caloriasMeta = preferencesPerfil.getInt("caloriasMeta", 0);

        preferencesContador = activity.getSharedPreferences(PREFS_NAME2, 0);
        SharedPreferences.Editor editor = preferencesContador.edit();
        editor.putInt("caloriasMeta", caloriasMeta);
        editor.commit();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {



        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listrow_details, null);

            np = (NumberPicker) convertView.findViewById(R.id.picker_contador);
            cantidad = 50;
            pickerValues();
            addListenerOnNumberPicker();

            alimento = (AutoCompleteTextView) convertView.findViewById(R.id.alimento);
            stAlimento = "";
            addListenerOnEditText();
            crearLista(alimento);

            buscar = (ImageButton) convertView.findViewById(R.id.buscar_alimento);
            addListenerOnButton();
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }


    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        Group group = (Group) getGroup(groupPosition);
        ((TextView) convertView).setText(group.string);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void pickerValues()
    {
        int minValue = 50;
        int maxValue = 1000;
        int step = 50;

        pv = new String[maxValue/minValue];

        for (int i = minValue; i <= maxValue; i += step)
        {
            pv[(i/step)-1] = String.valueOf(i);
        }

        np.setMinValue(0);
        np.setMaxValue(pv.length-1);
        np.setDisplayedValues(pv);
    }


    public void addListenerOnButton()
    {
            if(caloriasMeta!=0) {
                buscar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg) {
                        if (stAlimento != "") {


                            caloriasDiarias = calcularCalorias(stAlimento, cantidad);
                            tvCaloriasDiarias.setText("" + caloriasDiarias + " cal.");
                            caloriasMeta -= caloriasDiarias;
                            tvCaloriasMeta.setText("" + caloriasMeta + " cal.");


                            if (caloriasMeta < 0) {
                                // tvCaloriasMeta.setTextColor(activity.getResources().getColor(R.color.rojo));
                                desplegarExcesoCalorico(activity);
                                caloriasMeta = getCaloriasMetaBase();
                                setFooter();
                            }

                        }
                    }
                });
            }
            else
            {
                desplegarAdvertenciaCaloriasMeta(activity);
            }
    }

    public void addListenerOnEditText()
    {
        alimento.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {   }

            @Override
            public void afterTextChanged(Editable s)
            {
                stAlimento = s.toString();
            }
        });

    }

    public void addListenerOnNumberPicker()
    {

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                cantidad = Integer.parseInt(pv[newVal]);
            }
        });

    }

    public void crearLista(AutoCompleteTextView textView)
    {
        int size = food.getCount();
        String[] alimentos = new String[size];

        if (food != null && food.moveToFirst())
        {
            for (int i = 0; i < size; i++)
            {
                alimentos[i] = food.getString(1);
                food.moveToNext();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, alimentos);
        textView.setAdapter(adapter);
    }


    public int calcularCalorias(String buscar, int cantidad)
    {
        int totalCalorias=0;
        int cal = db.getCalorias(buscar);

        if(cal==-1)
        {
            desplegarAdvertencia(activity);
        }
        else
            totalCalorias=(cantidad*cal)/100;

        return totalCalorias;
    }


    public void setCaloriasDiarias(TextView tvCaloriasDiarias)
    {
        this.tvCaloriasDiarias = tvCaloriasDiarias;

    }

    public void setCaloriasMeta(TextView tvCaloriasMeta)
    {
        this.tvCaloriasMeta = tvCaloriasMeta;

    }

    public void setFooter()
    {
        //tvCaloriasMeta.setTextColor(activity.getResources().getColor(R.color.verde));
        tvCaloriasDiarias.setText("0 cal.");
        tvCaloriasMeta.setText(caloriasMeta+" cal.");

    }

    public int getCaloriasMeta()
    {

        return caloriasMeta;

    }

    public void desplegarAdvertencia(Context ctxt)
    {
        new AlertDialog.Builder(ctxt)
                .setTitle(R.string.titulo_error_input_contador)
                .setMessage(R.string.mensaje_error_input_contador)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada.
                    }
                })
                .setIcon(R.drawable.ic_warning_gray)
                .show();
    }

    public void desplegarExcesoCalorico(Context ctxt)
    {


        new AlertDialog.Builder(ctxt)
                .setTitle("Exceso Calórico")
                .setMessage("Ha sobrepasado su meta diaria de: "+getCaloriasMetaBase()+" cal. \nExceso de: "+caloriasMeta*-1+ " cal.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada.
                    }
                })
                .setIcon(R.drawable.ic_warning_gray)
                .show();
    }

    public void desplegarAdvertenciaCaloriasMeta(Context ctxt)
    {
        new AlertDialog.Builder(ctxt)
                .setTitle(R.string.titulo_error_input_contador)
                .setMessage("Debe primero establecer sus calorías Meta antes de empezar con su plan calórico")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada.
                    }
                })
                .setIcon(R.drawable.ic_warning_gray)
                .show();
    }

    public int getCaloriasMetaBase()
    {
        preferencesContador = activity.getSharedPreferences(PREFS_NAME2, 0);

        return preferencesPerfil.getInt("caloriasMeta", 0);
    }


}