package cc.se2.uanl.edu.contadordecalorias;

import android.app.Activity;
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
    private int edad;
    private int sexo;
    private int meta;
    private int actividad;
    private EditText inputEstatura;
    private EditText inputPeso;
    private EditText inputEdad;
    private RadioGroup radioSexo;
    private RadioGroup radioMeta;
    private RadioGroup radioActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Restore the saved data
        Button botonGuardar = (Button)findViewById(R.id.perfil_guardar);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);

        estatura = preferences.getString("estatura", "");
        peso = preferences.getString("peso", "");
        edad = preferences.getInt("edad", 0);
        sexo = preferences.getInt("sexo", 0);
        meta = preferences.getInt("meta", 0);
        actividad = preferences.getInt("actividad", 0);

        inputEstatura = (EditText) findViewById(R.id.perfil_cm);
        inputPeso = (EditText) findViewById(R.id.perfil_kg);
        inputEdad = (EditText) findViewById(R.id.perfil_años);
        radioSexo = (RadioGroup) findViewById(R.id.radio_sexo);
        radioMeta = (RadioGroup) findViewById(R.id.radio_meta);
        radioActividad = (RadioGroup) findViewById(R.id.radio_actividad);

        inputEstatura.setText(estatura);
        inputPeso.setText(peso);
        inputEdad.setText(""+edad);
        radioSexo.check(sexo);
        radioMeta.check(meta);
        radioActividad.check(actividad);

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEstatura = (EditText) findViewById(R.id.perfil_cm);
                inputPeso = (EditText) findViewById(R.id.perfil_kg);
                inputEdad = (EditText) findViewById(R.id.perfil_años);
                radioSexo = (RadioGroup) findViewById(R.id.radio_sexo);
                radioMeta = (RadioGroup) findViewById(R.id.radio_meta);
                radioActividad = (RadioGroup) findViewById(R.id.radio_actividad);

                estatura = inputEstatura.getText().toString();
                peso = inputPeso.getText().toString();
                edad = Integer.parseInt(inputEdad.getText().toString());
                sexo = radioSexo.getCheckedRadioButtonId();
                meta = radioMeta.getCheckedRadioButtonId();
                actividad = radioActividad.getCheckedRadioButtonId();

                //Toast.makeText(PerfilActivity.this, peso, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("estatura", estatura);
        editor.putString("peso", peso);
        editor.putInt("edad", edad);
        editor.putInt("sexo", sexo);
        editor.putInt("meta", meta);
        editor.putInt("actividad", actividad);

        editor.commit();
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
