package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //tworze tablice dwuwymiarową
    private final Button[][] buttons = new Button[3][3];
    //Definiuje zmienna
    private boolean player1Turn = true;
    private boolean turn1 = true;

    private int round;
    private int p1Points;
    private int p2Points;

    private TextView tvPlayers;
    private TextView tvPlayer1Points;
    private TextView tvPlayer2Points;

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPlayers = findViewById(R.id.tvPlayer);
        tvPlayer1Points = findViewById(R.id.tvPlayer1Points);
        tvPlayer2Points = findViewById(R.id.tvPlayer2Points);
        loadTurn();
        //Uzupełnia tablice dwu wymiarową
        for (int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                String ButtonID = "button_" + i + j;
                int resID = getResources().getIdentifier(ButtonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        //Przycisk resetu
        Button buttonReset = findViewById(R.id.btnReset);
        buttonReset.setOnClickListener(v -> resetGame());
    }

    @Override
    public void onClick(View v) {
        //Sprawdza czy klikany button jest pusty
        if(!((Button) v).getText().toString().equals("")){
            return;
        }
        //pozwala odpowiediemu graczowi na ruch
        if (player1Turn){
            ((Button) v).setText("X");
        }else{
            ((Button) v).setText("o");
        }
        //Zwięlsza licznik rund
        round++;
        //sprawdza który gracz wygrał-->czy był remis-->zmienia ture
        if(checkForWin()){
            if(player1Turn){
                player1Wins();
            }else{
                player2Wins();
            }
        }else if(round == 9){
            draw();
        }else{
            player1Turn = !player1Turn;
            whosRound();
        }
    }
    private boolean checkForWin(){
        String[][] field = new String[3][3];
        //Sprawdza czy w danej rundzie wygrał jakis gracz
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0;i<3;i++){
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")){
                return true;
            }
        }

        for (int i = 0;i<3;i++){
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")){
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")){
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")){
            return true;
        }
        return false;
    }
    //Co się dzieje jak wygra gracz 1
    private void player1Wins(){
        p1Points++;
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
        player1Turn = true;
        turn1 = true;
        saveTurn();
        whosRound();

    }
    //Co się dzieje jak wygra gracz 2
    private void player2Wins(){
        p2Points++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
        player1Turn = false;
        turn1 = false;
        saveTurn();
        whosRound();

    }
    //co się dzieje jak jest remis
    private void draw(){
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
        player1Turn = !player1Turn;
        turn1 = !turn1;
        saveTurn();
        whosRound();
    }

    /**
     * Aktualizuje ilość punktów
     */
    private void updatePointsText(){
        tvPlayer1Points.setText("P1 Points: "+ p1Points);
        tvPlayer2Points.setText("P2 Points: "+ p2Points);

    }

    /**
     * Resetuje tablice
     */
    private void resetBoard(){
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                buttons[i][j].setText("");
            }
        }

        round = 0;
    }

    /**
     * Sprawdza czyja jest runda a następnie zmienia tekst
     */
    private void whosRound(){
        if(player1Turn){
            tvPlayers.setText("Gracz 1");
        }else{
            tvPlayers.setText("Gracz 2");
        }
    }
    /**
     *Funckja przycisku resetującego gre
     * Resetuje on ilość punktów i wykonuje funkcje
     * updatePointsText();
     * resetBoard();
     */
    private void resetGame(){
        p1Points = 0;
        p2Points = 0;
        updatePointsText();
        resetBoard();
    }
    /**
     *Funckja zapisująca dane pomiędzy włączeniem i wyłączeniem gry

     */
    private void saveTurn(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("czyjaTura", turn1);
        editor.apply();
    }
    /**
     *Funckja wczytująca dane pomiędzy włączeniem i wyłączeniem gry

     */
    private void loadTurn(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        player1Turn = prefs.getBoolean("czyjaTura", true);//
        whosRound();
    }

}