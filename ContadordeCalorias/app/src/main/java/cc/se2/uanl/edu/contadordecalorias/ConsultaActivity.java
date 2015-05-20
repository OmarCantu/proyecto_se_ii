package cc.se2.uanl.edu.contadordecalorias;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


public class ConsultaActivity extends Activity {

    ImageButton imageButton;
    private Cursor food;
    private MyDatabase db;
    String [] pv;
    AutoCompleteTextView textView;
    private SharedPreferences preferencesPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        pickerValues();
        np.setMinValue(0);
        np.setMaxValue(pv.length-1);
        np.setDisplayedValues(pv);

        db = new MyDatabase(this);
        food = db.getAlimentos();

        crearLista();
        addListenerOnButton();
        addListenerOnTextView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_consulta, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_perfil_consulta:
                openPerfil();
                return true;
            case R.id.action_contador_consulta:
                openContador();
                return true;
            case R.id.action_salir_consulta:
                salir();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openPerfil() {
        Toast.makeText(this, R.string.title_activity_perfil, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }

    public void openContador() {
        Toast.makeText(this, R.string.title_activity_contador, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ContadorActivity.class);
        startActivity(intent);
    }

    public void salir() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void addListenerOnButton() {

        imageButton = (ImageButton) findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg) {

                EditText ed = (EditText) findViewById(R.id.cuanto);
                String buscar = ed.getText().toString();

                NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
                int cantidad = Integer.parseInt(pv[np.getValue()]);

                TextView tv = (TextView) findViewById(R.id.Consulta_Calorias);
                tv.setText(calcularCalorias(buscar,cantidad));

            }

        });

    }

    public void addListenerOnTextView()
    {
        AutoCompleteTextView text = (AutoCompleteTextView) findViewById(R.id.cuanto);

        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }

        });

    }


    public void crearLista()
    {
        int size = food.getCount();
        String[] alimentos = new String[size];

        if (food != null && food.moveToFirst()) {
            for (int i = 0; i < size; i++) {
                alimentos[i] = food.getString(1);
                food.moveToNext();
            }
        }
        textView = (AutoCompleteTextView) findViewById(R.id.cuanto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, alimentos);
        textView.setAdapter(adapter);

    }


    public String calcularCalorias(String buscar, int cantidad)
    {
        String totalCalorias;
        int cal = db.getCalorias(buscar);

        if(cal==-1)
        totalCalorias = getString(R.string.not_found);
        else
        totalCalorias = ""+(cantidad*cal)/100;

        return totalCalorias;
    }

    public void pickerValues()
    {
        int minValue = 50;
        int maxValue = 1000;
        int step = 50;

        pv = new String[maxValue/minValue];

        for (int i = minValue; i <= maxValue; i += step) {
            pv[(i/step)-1] = String.valueOf(i);
        }

    }


}
