package cc.se2.uanl.edu.contadordecalorias;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class PerfilActivity extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private String estatura;
    private String peso;
    private String edad;
    private int sexo;
    private int meta;
    private int actividad;
    private int caloriasMeta;
    private EditText inputEstatura;
    private EditText inputPeso;
    private EditText inputEdad;
    private RadioGroup radioSexo;
    private RadioGroup radioMeta;
    private RadioGroup radioActividad;
    private Button botonGuardar;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        botonGuardar = (Button)findViewById(R.id.perfil_guardar);

        desplegarDatosGuardados();

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarVariables();
                if (validarInputs()) {
                    calcularCaloriasMeta();
                    desplegarCaloriasMeta(PerfilActivity.this);
                    /*Toast.makeText(PerfilActivity.this, "Tu meta: " + caloriasMeta + " cal/día",
                        Toast.LENGTH_LONG).show();*/




                } else {
                    desplegarAdvertencia(PerfilActivity.this);
                }
            }
        });
    }

    public void desplegarDatosGuardados(){
        preferences = getSharedPreferences(PREFS_NAME, 0);

        // Obtener datos guardados
        estatura = preferences.getString("estatura", "");
        peso = preferences.getString("peso", "");
        edad = preferences.getString("edad", "");
        sexo = preferences.getInt("sexo", 0);
        meta = preferences.getInt("meta", 0);
        actividad = preferences.getInt("actividad", 0);
       // caloriasMeta = preferences.getInt("caloriasMeta", 0);

        // Instanciar inputs
        inputEstatura = (EditText) findViewById(R.id.perfil_cm);
        inputPeso = (EditText) findViewById(R.id.perfil_kg);
        inputEdad = (EditText) findViewById(R.id.perfil_años);
        radioSexo = (RadioGroup) findViewById(R.id.radio_sexo);
        radioMeta = (RadioGroup) findViewById(R.id.radio_meta);
        radioActividad = (RadioGroup) findViewById(R.id.radio_actividad);
        //saveCalorias = (TextView)findViewById(R.id.calorias_meta);


        // Mostrar datos
        inputEstatura.setText(estatura);
        inputPeso.setText(peso);
        inputEdad.setText(edad);
        radioSexo.check(sexo);
        radioMeta.check(meta);
        radioActividad.check(actividad);
      //  saveCalorias.setText(caloriasMeta+"");
    }

    public void actualizarVariables() {
        // Instanciar inputs
        inputEstatura = (EditText) findViewById(R.id.perfil_cm);
        inputPeso = (EditText) findViewById(R.id.perfil_kg);
        inputEdad = (EditText) findViewById(R.id.perfil_años);
        radioSexo = (RadioGroup) findViewById(R.id.radio_sexo);
        radioMeta = (RadioGroup) findViewById(R.id.radio_meta);
        radioActividad = (RadioGroup) findViewById(R.id.radio_actividad);
      //  saveCalorias = (TextView)findViewById(R.id.calorias_meta);

        // Asignar datos de inputs a variables
        estatura = inputEstatura.getText().toString();
        peso = inputPeso.getText().toString();
        edad = inputEdad.getText().toString();
        sexo = radioSexo.getCheckedRadioButtonId();
        meta = radioMeta.getCheckedRadioButtonId();
        actividad = radioActividad.getCheckedRadioButtonId();
      //  caloriasMeta = Integer.parseInt(saveCalorias.getText().toString());
    }

    public void calcularCaloriasMeta() {
        double tasaMetabolicaBasal;
        double caloriasDiarias = 0.0d;

        if (sexo == R.id.perfil_masculino) {
            tasaMetabolicaBasal = (10 * Double.parseDouble(peso)) +
                    (6.25 * Double.parseDouble(estatura)) - (5 * Integer.parseInt(edad)) + 5;
        } else {
            tasaMetabolicaBasal = (10 * Double.parseDouble(peso)) +
                    (6.25 * Double.parseDouble(estatura)) - (5 * Integer.parseInt(edad)) - 161;
        }

        switch(actividad) {
            case R.id.act_fisica_ninguna:
                caloriasDiarias = tasaMetabolicaBasal * 1.2;
                break;
            case R.id.act_fisica_ligera:
                caloriasDiarias = tasaMetabolicaBasal * 1.375;
                break;
            case R.id.act_fisica_moderada:
                caloriasDiarias = tasaMetabolicaBasal * 1.55;
                break;
            case R.id.act_fisica_intensa:
                caloriasDiarias = tasaMetabolicaBasal * 1.725;
                break;
            case R.id.act_fisica_muy_intensa:
                caloriasDiarias = tasaMetabolicaBasal * 1.9;
                break;
        }

        switch (meta) {
            case R.id.perder_peso:
                caloriasMeta = (int) Math.round(caloriasDiarias * 0.75);
                break;
            case R.id.mantener_peso:
                caloriasMeta = (int) Math.round(caloriasDiarias);
                break;
            case R.id.ganar_peso:
                caloriasMeta = (int) Math.round(caloriasDiarias * 1.25);
                break;
        }
    }

    public boolean validarInputs() {
        if (estatura.equals(null) || estatura.equals("0") || estatura.equals("")
                || peso.equals(null) || peso.equals("0") || peso.equals("")
                || edad.equals(null) || edad.equals("0") || edad.equals("")
                || sexo <= 0 || meta <= 0 || actividad <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public void desplegarCaloriasMeta(Context ctxt) {
        new AlertDialog.Builder(ctxt)
            .setTitle(R.string.titulo_calorias_meta)
            .setMessage(getString(R.string.mensaje_calorias_meta) + " " + caloriasMeta + " cal.")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // No hacer nada.
                }
            })
            .show();
        preferences = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("caloriasMeta", Integer.parseInt("" + Math.round(caloriasMeta)));
        editor.commit();

            /*.setIcon(R.drawable.ic_accept) // android.R.drawable.ic_xxxxx
            .show();*/
    }

    public void desplegarAdvertencia(Context ctxt) {
        new AlertDialog.Builder(ctxt)
                .setTitle(R.string.titulo_error_input_perfil)
                .setMessage(R.string.mensaje_error_input_perfil)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada.
                    }
                })
                .setIcon(R.drawable.ic_warning_gray)
                .show();
    }

    @Override
    protected void onStop(){
        super.onStop();
        persistirDatos();
    }

    public void persistirDatos() {
        preferences = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("estatura", estatura);
        editor.putString("peso", peso);
        editor.putString("edad", edad);
        editor.putInt("sexo", sexo);
        editor.putInt("meta", meta);
        editor.putInt("actividad", actividad);
        editor.putInt("caloriasMeta", Integer.parseInt("" + Math.round(caloriasMeta)));
        editor.commit();
    }


    public int getCaloriasMeta()
    {

        return caloriasMeta;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_consulta_perfil:
                openConsulta();
                return true;
            case R.id.action_contador_perfil:
                openContador();
                return true;
            case R.id.action_salir_perfil:
                salir();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openConsulta(){
        Toast.makeText(this, R.string.title_activity_consulta, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ConsultaActivity.class);
        startActivity(intent);
    }

    public void openContador(){
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
//        finish();
//        System.exit(0);
    }
}
