package cc.se2.uanl.edu.contadordecalorias;

/**
 * Created by Juanjo on 15/02/2015.
 */


        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteQueryBuilder;

        import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "AlimentosDB.db";
    private static final int DATABASE_VERSION = 3;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

    }

    public Cursor getAlimentos() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"0 _id", "Alimento", "Calorias"};
        String sqlTables = "Alimentos";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);
        c.moveToFirst();
        return c;

    }


    public int getCalorias(String alimento)
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int cal = -1;

        if(alimento!=""&&alimento!=null&&alimento.length()>1) {
            String[] sqlSelect = {"0 _id", "Alimento", "Calorias"};
            String sqlTables = "Alimentos";
            alimento = alimento.substring(0, 1).toUpperCase() + alimento.substring(1);
            qb.setTables(sqlTables);
            qb.appendWhere("Alimento=" + "'" + alimento + "'");

            Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
            c.moveToFirst();

            if (c.moveToFirst() && c != null)
                cal = c.getInt(2);
            else
                cal = -1;
        }
        return cal;
    }


}