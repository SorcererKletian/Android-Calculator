package com.example.kletian.slidingtest;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText edtCalculo;
    int oldValor = 1;
    int newValor;
    int d;
    Button delButton;
    int abreParCount = 0;
    int fechaParCount = 0;
    TextView textView;
    StringBuilder resultado = new StringBuilder();
    CalculAssistance calculAssistance;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtCalculo = (EditText) findViewById(R.id.edtCalculo);
        delButton = (Button) findViewById(R.id.delButton);
        textView = (TextView) findViewById(R.id.textView1);
        delButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                edtCalculo.setText("");
                edtCalculo.setTextSize(45);
                textView.setText(R.string.strCalculo);
                resultado.delete(0, resultado.length());
                return true;

            }
        });
        edtCalculo.setFocusable(false);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public boolean searchOp(String s) {
        char c = s.charAt(s.length() - 1);
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public boolean searchFunc(String s) {
        char c = s.charAt(s.length() - 1);
        return c == 'z' || c == 'n' || c == 's' || c == 'g';
    }

    public boolean searchNumber(String s) {
        char c = s.charAt(s.length() - 1);
        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
    }

    public static boolean isNumber(String n) {
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(n);
        return m.find();
    }

    //method for clicking button. All the buttons on the calculator share this same method,
    //so every information on how to behave when a button is clicked is described here
    public void button_Click(View v) {
        Button b = (Button) v;
        String s = b.getText().toString();


        //* DEL button
        if (s.equals("Del")) {
            if (edtCalculo.getText().length() > 0) {
                removeLast();
            } else {
                edtCalculo.setText("");
            }

        } else {

            // " = " Button
            if (s.equals("=")) {
                if (abreParCount != fechaParCount) {
                    edtCalculo.setText(R.string.Erro_de_expressao);
                    abreParCount = 0;
                    fechaParCount = 0;
                } else {
                    calculAssistance = new CalculAssistance();
                    calculAssistance.setResult(resultado.toString());
                    String res = calculAssistance.getResult();
                    edtCalculo.setText(res);
                    resultado.delete(0, resultado.length());
                    resultado.append(res);
                }
            } else {
                // check if there are no numbers or operations on screen
                d = edtCalculo.getText().length();
                if (d == 0) {
                    if (searchNumber(s)) {
                        edtCalculo.setText(s);
                        resultado.append(s);
                    }
                    if (searchFunc(s)) {
                        edtCalculo.setText(String.format("%s%s", s, getString(R.string.Abre_par)));
                        resultado.append(s);
                        resultado.append(" ");
                        resultado.append("(");
                        abreParCount++;
                    }
                    if (s.equals("(")) {
                        edtCalculo.setText(s);
                        resultado.append(s);
                        abreParCount++;
                    }
                    if (s.equals("!") || s.equals("%") || s.equals("^")) {
                        edtCalculo.setText("");
                    }
                    if (s.equals(")")) {
                        edtCalculo.setText(s);
                        resultado.append(s);
                        fechaParCount++;
                    }
                    if (s.equals("Pi")) {
                        edtCalculo.setText(R.string.Pi);
                        resultado.append("3.14159265");
                    }
                }
                // if there are digits/functions on screen
                if (d > 0 && isNumber(s) && (searchNumber(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '.')) {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), s));
                    resultado.append(s);
                }
                if (d > 0 && isNumber(s) && (searchOp(edtCalculo.getText().toString()) || searchFunc(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '(' || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '^')) {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), s));
                    resultado.append(" ");
                    resultado.append(s);
                }
                if (d > 0 && searchOp(s) && searchNumber(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == ')') {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), s));
                    resultado.append(" ");
                    resultado.append(s);
                }
                if (d > 0 && searchOp(s) && searchOp(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '(' || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '^' || searchFunc(edtCalculo.getText().toString())) {
                    edtCalculo.setText(edtCalculo.getText());
                }
                if (d > 0 && searchFunc(s) && searchOp(edtCalculo.getText().toString())) {
                    edtCalculo.setText(String.format("%s%s%s", edtCalculo.getText(), s, getString(R.string.Abre_par)));
                    resultado.append(" ");
                    resultado.append(s);
                    resultado.append(" ");
                    resultado.append("(");
                    abreParCount++;
                }
                if (d > 0 && searchFunc(s) && searchNumber(edtCalculo.getText().toString())) {
                    edtCalculo.setText(String.format("%s%s%s%s", edtCalculo.getText(), getString(R.string.Multiply), s, getString(R.string.Abre_par)));
                    resultado.append(" ");
                    resultado.append("*");
                    resultado.append(" ");
                    resultado.append(s);
                    resultado.append(" ");
                    resultado.append("(");
                    abreParCount++;
                }
                if (d > 0 && s.equals("!") && searchNumber(edtCalculo.getText().toString())) {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), s));
                    resultado.append(" ");
                    resultado.append(s);
                }
                if (d > 0 && s.equals("!") && (searchFunc(edtCalculo.getText().toString()) || searchOp(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '(' || edtCalculo.getText().charAt(edtCalculo.length() - 1) == ')')) {
                    edtCalculo.setText(edtCalculo.getText());
                }
                if (d > 0 && s.equals("(") && searchNumber(edtCalculo.getText().toString())) {
                    edtCalculo.setText(String.format("%s%s%s", edtCalculo.getText(), getString(R.string.Multiply), s));
                    abreParCount++;
                    resultado.append(" ");
                    resultado.append("*");
                    resultado.append(" ");
                    resultado.append(s);
                }
                if (d > 0 && s.equals("(") && searchOp(edtCalculo.getText().toString())) {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), s));
                    abreParCount++;
                    resultado.append(" ");
                    resultado.append(s);
                }
                if (d > 0 && s.equals(")") && searchNumber(edtCalculo.getText().toString())) {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), s));
                    fechaParCount++;
                    resultado.append(" ");
                    resultado.append(s);
                }
                if (d > 0 && s.equals(")") && searchOp(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '^') {
                    edtCalculo.setText(edtCalculo.getText());
                }
                if (d > 0 && s.equals("^") && (searchNumber(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == ')')) {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), getString(R.string.Potencia)));
                    resultado.append(" ");
                    resultado.append("^");
                }
                if (d > 0 && s.equals("^") && searchOp(edtCalculo.getText().toString()) || searchFunc(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '(') {
                    edtCalculo.setText(edtCalculo.getText());
                }
                if (d > 0 && s.equals("%") && searchNumber(edtCalculo.getText().toString())) {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), s));
                    resultado.append(" ");
                    resultado.append(s);
                }
                if (d > 0 && s.equals("%") && (searchFunc(edtCalculo.getText().toString()) || searchOp(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '(' || edtCalculo.getText().charAt(edtCalculo.length() - 1) == ')')) {
                    edtCalculo.setText(edtCalculo.getText());
                }
                if (d > 0 && s.equals(".") && searchNumber(edtCalculo.getText().toString())) {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), s));
                    resultado.append(s);
                }
                if (d > 0 && s.equals(".") && !searchNumber(edtCalculo.getText().toString())) {
                    edtCalculo.setText(edtCalculo.getText());
                }
                if (d > 0 && s.equals("Pi") && (searchOp(edtCalculo.getText().toString()) || searchFunc(edtCalculo.getText().toString()) || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '(' || edtCalculo.getText().charAt(edtCalculo.length() - 1) == '^')) {
                    edtCalculo.setText(String.format("%s%s", edtCalculo.getText(), getString(R.string.Pi)));
                    resultado.append(" ");
                    resultado.append(getString(R.string.Pi));
                }
                if (d > 0 && s.equals("Pi") && searchNumber(edtCalculo.getText().toString())) {
                    edtCalculo.setText(edtCalculo.getText());
                }
                if (s.equals("C")) {
                    if (edtCalculo.getText().length() > 0) {
                        while (resultado.length() > 0) {
                            resultado.deleteCharAt(resultado.length() - 1);
                        }
                        edtCalculo.setText("");
                    }
                }
            }
            reduceText();
        }
    }

    // method used for "Del" Button
    public void removeLast() {
        String s;
        String r = "";

        s = edtCalculo.getText().toString();
        if (searchFunc(s)) {
            if (s.charAt(s.length() - 1) == 'z') {
                r = s.substring(0, s.length() - 4);
                edtCalculo.setText(r);
                if (resultado.length() == 4) {
                    resultado.deleteCharAt(resultado.length() - 1);
                    resultado.deleteCharAt(resultado.length() - 1);
                    resultado.deleteCharAt(resultado.length() - 1);
                    resultado.deleteCharAt(resultado.length() - 1);
                } else {
                    if (resultado.length() > 4) {
                        resultado.deleteCharAt(resultado.length() - 1);
                        resultado.deleteCharAt(resultado.length() - 1);
                        resultado.deleteCharAt(resultado.length() - 1);
                        resultado.deleteCharAt(resultado.length() - 1);
                        resultado.deleteCharAt(resultado.length() - 1);
                    }
                }
            }
            if (s.charAt(s.length() - 1) == 'n' || s.charAt(s.length()) == 's' || s.charAt(s.length()) == 'g') {
                r = s.substring(0, s.length() - 3);
                edtCalculo.setText(r);
                if (resultado.length() == 3) {
                    resultado.deleteCharAt(resultado.length() - 1);
                    resultado.deleteCharAt(resultado.length() - 1);
                    resultado.deleteCharAt(resultado.length() - 1);
                } else {
                    if (resultado.length() > 3) {
                        resultado.deleteCharAt(resultado.length() - 1);
                        resultado.deleteCharAt(resultado.length() - 1);
                        resultado.deleteCharAt(resultado.length() - 1);
                        resultado.deleteCharAt(resultado.length() - 1);
                    }
                }
            }
        } else {
            if (s.length() > 0) {
                r = s.substring(0, s.length() - 1);
                if (resultado.length() == 1) {
                    resultado.deleteCharAt(0);
                } else {
                    if (resultado.length() > 1) {
                        resultado.deleteCharAt(resultado.length() - 1);
                        resultado.deleteCharAt(resultado.length() - 1);
                    }
                }
            }
            if (s.length() == 0) {
                r = "";
                resultado.setLength(0);
            }
            edtCalculo.setText(r);
        }
    }

    public void arrangeLines() {
        double t = edtCalculo.getTextSize();
        t = t * 0.5;
        edtCalculo.setTextSize((float) (t));
    }

    //
    // the Method reduceText works together with arrangeLines for reducing the text font in case there are
    // too much content on the text box
    //
    public void reduceText() {
        newValor = edtCalculo.getLineCount();
        if (newValor > oldValor) {
            arrangeLines();
            oldValor = newValor;
        }
    }

    //* Method for searching difference in parenthesis count that could return calc error with shunting yard
    //* used only as a debugger
    public void changeTextView(int a, int b) {
        StringBuilder sb = new StringBuilder();
        sb.append(a);
        sb.append(" + ");
        sb.append(b);
        sb.append(", ");
        textView.setText(sb.toString());
        sb.delete(0, sb.length());
    }

    //override for saving the content of variables used for shunting yard
    //without this, on rotating the screen  all the information stored are lost and there'll be errors
    //while trying to execute the ShuntingYard algorithm
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ABREPARCOUNT", abreParCount);
        outState.putInt("FECHAPARCOUNT", fechaParCount);
        outState.putInt("OLDVALOR", oldValor);
        outState.putInt("NEWVALOR", newValor);
        outState.putString("STRINGRESULTADO", resultado.toString());
        outState.putInt("D", d);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        abreParCount = savedInstanceState.getInt("ABREPARCOUNT");
        fechaParCount = savedInstanceState.getInt("FECHAPARCOUNT");
        oldValor = savedInstanceState.getInt("OLDVALOR");
        newValor = savedInstanceState.getInt("NEWVALOR");
        resultado.append(savedInstanceState.getString("STRINGRESULTADO"));
        d = savedInstanceState.getInt("D");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.kletian.slidingtest/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.kletian.slidingtest/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
