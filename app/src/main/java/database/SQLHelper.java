package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import model.Item;
import model.Livro;

public class SQLHelper extends SQLiteOpenHelper {

    /**  ATRIBUTOS DA CLASSE DE CONNECTION  **/

    private static final  String DB_NAME = "libri";
    private static final int DB_VERSION = 2;
    private static SQLHelper INSTANCE ;


    /* Método de verificar se a conexão esta aberta  */

    public static SQLHelper getInstance(Context context){

        if (INSTANCE == null){

            INSTANCE = new SQLHelper(context);

        }

        return  INSTANCE;
    }

    /* Método construtor: Recebe os valores iniciais de abertura da conexão  */

    public SQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE tbl_usuario" +
                "(cod_usuario INTEGER PRIMARY KEY," +
                "nome TEXT," +
                "sobrenome TEXT," +
                "email TEXT," +
                "login TEXT," +
                "senha TEXT," +
                "created_date DATETIME)");

        Log.d("SQLITE-", "BANCO DE DADOS CRIADO! ˜" + DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

//        sqLiteDatabase.execSQL("CREATE TABLE tbl_livro" +
//                "(cod_livro INTEGER PRIMARY KEY," +
//                "cod_usuario INTEGER," +
//                "titulo TEXT," +
//                "descricao TEXT," +
//                "foto TEXT," +
//                "created_date DATETIME,"+
//                "FOREIGN KEY (cod_usuario) REFERENCES tbl_usuario(cod_usuario))");

        Log.d("SQLITE-", "BANCO DE DADOS CRIADO! ˜" + DB_VERSION);

    }

    public boolean addUser(String nome, String sobrenome, String email, String login, String senha, String created_date){

        //Configura o SQLITE para escrita:

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        try{
            sqLiteDatabase.beginTransaction();

            ContentValues values = new ContentValues();

            values.put("nome", nome);
            values.put("sobrenome", sobrenome);
            values.put("email", email);
            values.put("login", login);
            values.put("senha", senha);
            values.put("created_date", created_date);

            sqLiteDatabase.insertOrThrow("tbl_usuario", null, values);
            sqLiteDatabase.setTransactionSuccessful();

            return  true;
        }

        catch (Exception e){
           Log.d("SQLITE˜", e.getMessage());
           return  false;
        }

        finally {

            if (sqLiteDatabase.isOpen()){
                sqLiteDatabase.endTransaction();
            }

        }


    }

    //INSERÇÃO DE LIVROS

    public boolean addBook( int cod_usuario, String titulo,  String descricao, String foto, String created_date){

        //Configura o SQLITE para escrita:

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int cod_livro = 0;

        try{
            sqLiteDatabase.beginTransaction();

            ContentValues values = new ContentValues();

            values.put("cod_usuario", cod_usuario);
            values.put("titulo", titulo);
            values.put("descricao", descricao);
            values.put("foto", foto);
            values.put("created_date", created_date);

            sqLiteDatabase.insertOrThrow("tbl_livro", null, values);
            sqLiteDatabase.setTransactionSuccessful();

            return  true;
        }

        catch (Exception e){
            Log.d("SQLITE˜", e.getMessage());
            return  false;
        }

        finally {

            if (sqLiteDatabase.isOpen()){
                sqLiteDatabase.endTransaction();
            }

        }


    }

    //REALIZAR LOGIN

    public int login(String login, String senha){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM tbl_usuario WHERE login = ? AND senha = ?",
                new String[]{login,senha}
        );
        int cod_usuario = 0;
        try{

            if (cursor.moveToFirst()){

                cod_usuario = cursor.getInt(cursor.getColumnIndex("cod_usuario"));
                return cod_usuario;
            }

            return 0;

        }catch (Exception e){

            Log.d("SQLITE˜", e.getMessage());

        }finally{
lo
            if (cursor != null && !cursor.isClosed()){

                cursor.close();
            }

        }
        return 0;

    }


    public List<Item> listBook(){

        List<Item> items = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tbl_livro WHERE cod_usuario = ? ", new String[]{"1"});
        try{

            if (cursor.moveToFirst()){

                do {

                    Livro livro = new Livro(
                            cursor.getString(cursor.getColumnIndex("titulo")),
                            cursor.getString(cursor.getColumnIndex("descricao"))
                    );

                    items.add(new Item(0, livro));

                } while(cursor.moveToNext());

            }

        }catch (Exception e){

            Log.d("SQLITERROR˜", e.getMessage());

        }finally {

            if (cursor != null && !cursor.isClosed()){

                cursor.close();
            }


        }

        return items;

    }




}
