package com.example.appperguntasrespostas.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import android.database.MatrixCursor;
import java.util.Scanner;
import android.util.Log;

import org.json.JSONObject;


public class HistoricoProvider extends ContentProvider {
    // Endereço do contentProvider:
    // private static final String AUTHORITY = "com.example.appperguntasrespostas.provider";
    // transformar a String em um objeto URI(Padrão do content)
    // private static final Uri URI_HISTORICO = Uri.parse("content://" + AUTHORITY + "/historico");

    public HistoricoProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String[] colunas = {"jogador", "acertos", "totalPerguntas", "dificuldade"};

        MatrixCursor cursor = new MatrixCursor(colunas);

        try {
            Scanner leitor = new Scanner(
                    getContext().openFileInput("resultados.json")
            );
            // Transformando o JSON em um objeto pq tava dando NULL POINTER
            while (leitor.hasNextLine()) {

                Log.d("HISTORICO", "Quantidade de registros: " + cursor.getCount());
                JSONObject resultado = new JSONObject(leitor.nextLine());

                cursor.addRow(new Object[]{
                        resultado.getString("jogador"),
                        resultado.getInt("acertos"),
                        resultado.getInt("totalPerguntas"),
                        // OptString tem essa opção de colocar um valor padrão(FALLBACK), TAVA DANDO org.json.JSONException: No value for dificuldade
                        resultado.optString("dificuldade", "Offline"),
                });
            }

            leitor.close();

        } catch (Exception e) {
            Log.e("HistoricoProvider", "Erro ao consultar histórico", e);
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}