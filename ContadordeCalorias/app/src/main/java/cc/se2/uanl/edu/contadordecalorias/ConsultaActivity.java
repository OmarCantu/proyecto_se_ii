package cc.se2.uanl.edu.contadordecalorias;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class ConsultaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void openPerfil(){
        this.finish(); // Checar si esta linea es necesaria
        Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }

    public void openContador(){
        this.finish(); // Checar si esta linea es necesaria
        Toast.makeText(this, "Contador", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ContadorActivity.class);
        startActivity(intent);
    }

    public void salir() {
        /*finish();
        System.exit(0);*/

        /*android.os.Process.killProcess(android.os.Process.myPid());*/

        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
