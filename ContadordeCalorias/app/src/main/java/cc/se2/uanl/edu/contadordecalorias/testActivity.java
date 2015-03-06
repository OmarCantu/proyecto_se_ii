package cc.se2.uanl.edu.contadordecalorias;

/**
 * Created by Juanjo on 15/02/2015.
 */
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class testActivity extends ListActivity {

    private Cursor food;
    private MyDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new MyDatabase(this);
        food = db.getAlimentos(); // you would not typically call this on the main thread

        ListAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                food,
                new String[] {"Alimento","Calorias"},
                new int[] {android.R.id.text1,android.R.id.text2},0);

        getListView().setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        food.close();
        db.close();
    }

}