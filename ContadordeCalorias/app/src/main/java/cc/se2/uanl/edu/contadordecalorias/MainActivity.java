package cc.se2.uanl.edu.contadordecalorias;

import android.content.Intent;
import android.app.Activity;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;

// public class MainActivity extends ActionBarActivity {
public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "edu.uanl.se2.cc.MESSAGE";

    /** Called when the user clicks the Send button */
   /* public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/

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
            case R.id.action_salir:
                salir();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openPerfil(){
        Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }

    public void openConsulta(){
        Toast.makeText(this, "Consulta", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ConsultaActivity.class);
        startActivity(intent);
    }

    public void openContador(){
        Toast.makeText(this, "Contador", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ContadorActivity.class);
        startActivity(intent);
    }

    public void salir() {
        finish();
        System.exit(0);
    }

    public void readFile(View view) {
        //InputStream in = (Activity)getContext().getAssets().open("my_file.txt");
        try {

            AssetManager assetManager = getAssets();
            InputStream input = assetManager.open("testfile.txt");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            String text = new String(buffer);

            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*AssetDatabaseOpenHelper adb = new AssetDatabaseOpenHelper(this.getApplicationContext());
        SQLiteDatabase db  =adb.openDatabase();

        String      query  = "SELECT Alimento FROM Alimentos where Alimento = 'pizza' " ;

        Cursor cursor  = db.rawQuery(query, null);
        Toast.makeText(this, cursor.getString(0), Toast.LENGTH_SHORT).show();*/


    }
}
