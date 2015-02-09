package cc.se2.uanl.edu.contadordecalorias;

import android.content.Intent;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

// public class MainActivity extends ActionBarActivity {
public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "edu.uanl.se2.cc.MESSAGE";

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
        // return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_perfil:
                openPerfil();
                return true;
            case R.id.action_consulta:
                openConsulta();
                return true;
            case R.id.action_contador:
                openContador();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openPerfil(){
        Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(this, PerfilActivity.class);
        *//*EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*//*
        startActivity(intent);*/
    }

    public void openConsulta(){
        Toast.makeText(this, "Consulta", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(this, ConsultaActivity.class);
        *//*EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*//*
        startActivity(intent);*/
    }

    public void openContador(){
        Toast.makeText(this, "Contador", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(this, ContadorActivity.class);
        *//*EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*//*
        startActivity(intent);*/
    }
}
