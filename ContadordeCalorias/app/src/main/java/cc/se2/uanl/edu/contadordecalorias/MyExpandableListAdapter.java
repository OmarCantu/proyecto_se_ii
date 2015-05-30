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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<Group>groups;
    private ArrayAdapter<String> adapter;
    public  LayoutInflater inflater;
    public  Activity activity;
    private String [] pv;
    private Cursor food;
    private MyDatabase db;
    private AutoCompleteTextView alimento;
    private Button addCalorias;
    private NumberPicker np;
    private TextView tvCaloriasContadas, tvCaloriasMeta;
    private String stAlimento;
    private int cantidad;
    private int caloriasAlimento = 0;
    private int caloriasMeta = 0;
    private int caloriasContadas = 0;
    private int meta;
    private SharedPreferences preferencesPerfil;


    public MyExpandableListAdapter(Activity act, SparseArray<Group> groups)
    {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
        db = new MyDatabase(activity);
        food = db.getAlimentos();
        preferencesPerfil = activity.getSharedPreferences(PerfilActivity.PREFS_NAME, 0);

        meta = preferencesPerfil.getInt("meta", 0);
        caloriasMeta = getCaloriasContador();
        caloriasContadas = getCaloriasMetaBase() - caloriasMeta;

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
            crearLista();

            addCalorias = (Button) convertView.findViewById(R.id.agregar_calorias);
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
        pv[(i/step)-1] = String.valueOf(i);

        np.setMinValue(0);
        np.setMaxValue(pv.length-1);
        np.setDisplayedValues(pv);
    }


    public void addListenerOnButton()
    {
            if(caloriasMeta!=0)
            {
                addCalorias.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View arg)
                    {
                        if (stAlimento != "")
                        {
                            caloriasAlimento = calcularCalorias(stAlimento, cantidad);
                            caloriasContadas += caloriasAlimento;
                            caloriasMeta -= caloriasAlimento;
                            setSignum();
                            setColor();
                            tvCaloriasContadas.setText(caloriasContadas+ " cal.");
                            Toast.makeText(activity, activity.getString(R.string.consumo_agregado),Toast.LENGTH_SHORT).show();
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


        alimento.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                InputMethodManager in = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    public void crearLista()
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
        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, alimentos);
        alimento.setAdapter(adapter);
    }


    public int calcularCalorias(String buscar, int cantidad)
    {
        int totalCalorias = 0;
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
        this.tvCaloriasContadas = tvCaloriasDiarias;

    }

    public void setCaloriasMeta(TextView tvCaloriasMeta)
    {
        this.tvCaloriasMeta = tvCaloriasMeta;

    }

    public void setFooter()
    {
        setColor();
        setSignum();
        tvCaloriasContadas.setText(caloriasContadas+ " cal.");
    }

    public void setColor()
    {
        switch (meta)
        {
            case R.id.perder_peso:
            {
                if(caloriasMeta>=0)
                    tvCaloriasMeta.setTextColor(activity.getResources().getColor(R.color.verde));
                else
                    tvCaloriasMeta.setTextColor(activity.getResources().getColor(R.color.rojo));
            }
            break;

            case R.id.mantener_peso:
            {
                if(caloriasMeta<getCaloriasMetaBase()*0.05&&caloriasMeta>getCaloriasMetaBase()*-0.05)
                    tvCaloriasMeta.setTextColor(activity.getResources().getColor(R.color.verde));
                else
                    tvCaloriasMeta.setTextColor(activity.getResources().getColor(R.color.rojo));
            }
            break;

            case R.id.ganar_peso:
            {
                if(caloriasMeta<0)
                    tvCaloriasMeta.setTextColor(activity.getResources().getColor(R.color.verde));
                else
                    tvCaloriasMeta.setTextColor(activity.getResources().getColor(R.color.rojo));
            }
            break;
        }

    }

    public void setSignum()
    {
        if(caloriasMeta>0)
            tvCaloriasMeta.setText("-"+caloriasMeta+" cal.");
        else
            tvCaloriasMeta.setText("+"+-1*caloriasMeta+" cal.");

    }

    public void desplegarAdvertencia(Context ctxt)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle(R.string.titulo_error_input_contador);
        alertDialog.setMessage(activity.getResources().getString(R.string.mensaje_error_input_contador));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        { public void onClick(DialogInterface dialog, int which) { } });
        alertDialog.setIcon(R.drawable.ic_warning_gray);
        alertDialog.show();

    }

    public void desplegarExcesoCalorico(Context ctxt)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle(R.string.titulo_error_input_contador);
        alertDialog.setMessage("Ha sobrepasado su meta diaria de: "+getCaloriasMetaBase()+" cal. \nExceso de: "+caloriasMeta*-1+ " cal.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        { public void onClick(DialogInterface dialog, int which) { } });
        alertDialog.setIcon(R.drawable.ic_warning_gray);
        alertDialog.show();

    }

    public void desplegarAdvertenciaCaloriasMeta(Context ctxt)
    {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle(R.string.titulo_error_input_contador);
        alertDialog.setMessage(activity.getString(R.string.advertencia_calorias_meta));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        { public void onClick(DialogInterface dialog, int which) { } });
        alertDialog.setIcon(R.drawable.ic_warning_gray);
        alertDialog.show();

    }

    public void setCaloriasContadas(int caloriasContadas)
    {
        this.caloriasContadas = caloriasContadas;
    }

    public void setCaloriasMeta(int caloriasMeta)
    {
        this.caloriasMeta = caloriasMeta;
    }

    public int getCaloriasMetaBase()
    {
        return preferencesPerfil.getInt("caloriasMeta", 0);
    }

    public int getCaloriasContador()
    {
        return preferencesPerfil.getInt("caloriasContador", 0);
    }

    public int getCaloriasMeta()
    {
        return caloriasMeta;
    }

}
