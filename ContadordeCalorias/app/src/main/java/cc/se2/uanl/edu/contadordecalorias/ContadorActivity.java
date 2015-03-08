package cc.se2.uanl.edu.contadordecalorias;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ContadorActivity extends Activity {

    SparseArray<Group> groups = new SparseArray<Group>();
    RelativeLayout footerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        createData();
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(this,groups);
        View view = getLayoutInflater().inflate(R.layout.footer, listView, false);
        footerLayout = (RelativeLayout) view.findViewById(R.id.footer);
        listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        listView.addFooterView(footerLayout);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contador, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_perfil_contador:
                openPerfil();
                return true;
            case R.id.action_consulta_contador:
                openConsulta();
                return true;
            case R.id.action_salir_contador:
                salir();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openPerfil(){
        Toast.makeText(this, R.string.title_activity_perfil, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }

    public void openConsulta(){
        Toast.makeText(this, R.string.title_activity_consulta, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ConsultaActivity.class);
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


    public void createData() {

        Group desayuno = new Group(getString(R.string.desayuno));
        desayuno.children.add("");
        groups.append(0, desayuno);

        Group comida = new Group(getString(R.string.comida));
        comida.children.add("");
        groups.append(1, comida);

        Group cena = new Group(getString(R.string.cena));
        cena.children.add("");
        groups.append(2, cena);

        Group aperitivos = new Group(getString(R.string.aperitivos));
        aperitivos.children.add("");
        groups.append(3, aperitivos);

    }

}

  /* public void createData1() {
        for (int j = 0; j < 5; j++) {
            Group group = new Group("Test " + j);
            for (int i = 0; i < 5; i++) {
                group.children.add("Sub Item" + i);
            }
            groups.append(j, group);
        }
    }

   */