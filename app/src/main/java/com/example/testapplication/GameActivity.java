package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private Button button1, button2, button3, button4;
    private MediaPlayer currentTrack;
    private boolean track_paused = false;

    private MediaPlayer[] mediaPlayers;
    private String[] resourceNames;
    private String[] resourceViewNames;
    private int currentMediaPlayer = -1;

    private int roundsCount = -1;
    private int currentRound = 1;
    private int currentPlayer = 1;

    private int musicCount = 0;

    private TextView roundCountText;
    private TextView firstPlayerScoreText;
    private TextView secondPlayerScoreText;
    private Integer[] playerScoreCount;

    private Random random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        currentMediaPlayer = -1;
        roundsCount = -1;
        currentRound = 1;
        currentPlayer = 1;
        playerScoreCount = new Integer[2];
        playerScoreCount[0] = 0;
        playerScoreCount[1] = 0;
        random = new Random();

        // Получаем ресурсы
        Resources res = getResources();

        // Получаем идентификаторы всех mp3 файлов в папке raw
        TypedArray mp3Files = res.obtainTypedArray(R.array.mp3_files);
        musicCount = mp3Files.length();

        // Считаем количество раундов из расчета, что их должно быть четное количество и для каждого игрока в каждом раунде нужно 8 аудиозаписей
        roundsCount = musicCount/4;

        // Создание массива от 0 до N
        Integer[] array = new Integer[musicCount];

        for (int i = 0; i < musicCount; i++) {
            array[i] = i;
        }

        // Перемешивание элементов массива
        List<Integer> list = Arrays.asList(array);
        Collections.shuffle(list);
        list.toArray(array);

        // Выводим значения перемешанного массива в лог
        for (int i = 0; i < musicCount; i++) {
            Log.d("MyTag", "Resource ID at position " + i + ": " + array[i]);
        }

        //--------------------------------------------------

        // Инициализируем массивы MediaPlayer и строк для хранения имен ресурсов
        mediaPlayers = new MediaPlayer[musicCount];
        resourceNames = new String[musicCount];
        resourceViewNames = new String[musicCount];

        // Загружаем mp3 файлы из ресурсов в массив MediaPlayer
        for (int i = 0; i < musicCount; i++) {
            int resourceId = mp3Files.getResourceId(array[i], -1);
            if (resourceId != -1) {
                mediaPlayers[i] = MediaPlayer.create(this, resourceId);
                if (mediaPlayers[i] == null) {
                    Log.e("MediaPlayer", "Failed to create MediaPlayer for resource ID: " + resourceId);
                } else {
                    // Получаем имя ресурса
                    String resourceName = res.getResourceEntryName(resourceId);
                    resourceNames[i] = resourceName;

                    // Получаем идентификатор ресурса строки по его имени
                    int stringResourceId = res.getIdentifier(resourceName, "string", getPackageName());
                    // Получаем строку из ресурса имен по его идентификатору
                    resourceViewNames[i] = res.getString(stringResourceId);
                }
            }
        }

        // Освобождаем ресурсы
        mp3Files.recycle();

        //устанавливаем значения счетчика раундов;
        roundCountText = findViewById(R.id.rounds_count);
        firstPlayerScoreText = findViewById(R.id.first_player_score);
        secondPlayerScoreText = findViewById(R.id.second_player_score);
        updateRoundCount();

        //currentTrack = MediaPlayer.create(this, R.raw.baraban);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        // String resourceName = resourceNames[0]; // Получаем имя ресурса строки из массива resourceNames
        // Получаем идентификатор ресурса строки по его имени
        // int stringResourceId = res.getIdentifier(resourceName, "string", getPackageName());
        // Получаем строку из ресурса по его идентификатору
        // String myString = res.getString(stringResourceId);
    }

    public void onStart() {
        super.onStart();
        startRound(1);
    }

    public void startRound(int roundNumber){
        if(roundNumber <= roundsCount) {
            updateRoundCount();

            Log.d("MyTag", "roundNumber:  " + roundNumber);
            Log.d("MyTag", "currentPlayer:  " + currentPlayer);

            final int i = (roundNumber - 1) * 4;
            Log.d("MyTag", "i: " + i);
            if (currentMediaPlayer != -1) {
                playMusic(currentMediaPlayer);
            }
            int randomTrack = random.nextInt(4) + i;
            playMusic(randomTrack);

            Log.d("MyTag", "Track name: " + resourceViewNames[randomTrack]);
            Log.d("MyTag", "Track number:  " + randomTrack);
            button1.setText(resourceViewNames[i]);
            button2.setText(resourceViewNames[i + 1]);
            button3.setText(resourceViewNames[i + 2]);
            button4.setText(resourceViewNames[i + 3]);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(resourceViewNames[randomTrack] == button1.getText().toString()){
                        playerScoreCount[currentPlayer-1]++;
                    }
                    currentRound = currentPlayer == 2 ? currentRound+1 : currentRound;
                    currentPlayer = currentPlayer == 1 ? 2 : 1;
                    startRound(roundNumber + 1);
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(resourceViewNames[randomTrack] == button2.getText().toString()){
                        playerScoreCount[currentPlayer-1]++;
                    }
                    currentRound = currentPlayer == 2 ? currentRound+1 : currentRound;
                    currentPlayer = currentPlayer == 1 ? 2 : 1;
                    startRound(roundNumber + 1);
                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(resourceViewNames[randomTrack] == button3.getText().toString()){
                        playerScoreCount[currentPlayer-1]++;
                    }
                    currentRound = currentPlayer == 2 ? currentRound+1 : currentRound;
                    currentPlayer = currentPlayer == 1 ? 2 : 1;
                    startRound(roundNumber + 1);
                }
            });
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(resourceViewNames[randomTrack] == button4.getText().toString()){
                        playerScoreCount[currentPlayer-1]++;
                    }
                    currentRound = currentPlayer == 2 ? currentRound+1 : currentRound;
                    currentPlayer = currentPlayer == 1 ? 2 : 1;
                    startRound(roundNumber + 1);
                }
            });
        }else{
            cleanMediaPlayers();
            Intent intent = new Intent(this, WinActivity.class);
            if(playerScoreCount[0]>playerScoreCount[1]) {
                intent.putExtra("winText", "Победил игрок 1");
            }else if(playerScoreCount[0]<playerScoreCount[1]){
                intent.putExtra("winText", "Победил игрок 2");
            }else{
                intent.putExtra("winText", "НИЧЬЯ!");
            }
            startActivity(intent);
        }
    }

    public void updateRoundCount(){
        roundCountText.setText(String.format("%d(%d)/%d", currentRound, currentPlayer, roundsCount/2));
        firstPlayerScoreText.setText(String.format("%d", playerScoreCount[0]));
        secondPlayerScoreText.setText(String.format("%d", playerScoreCount[1]));
    }

    public void playMusic(int mpId){
        if(currentMediaPlayer < 0) {
            mediaPlayers[mpId].start();
            currentMediaPlayer = mpId;
        }else if (mpId == currentMediaPlayer){
            mediaPlayers[mpId].pause();
            mediaPlayers[mpId].seekTo(0);
        }else{
            mediaPlayers[currentMediaPlayer].pause();
            mediaPlayers[currentMediaPlayer].seekTo(0);
            currentMediaPlayer = mpId;
            if(mpId<musicCount)
                mediaPlayers[mpId].start();
            else{
                Log.d("MyTag", "Trash gavno:  " + mpId);
            }
        }
    }

    public void btnToStartScreen (View v){
        //currentTrack.stop();
        cleanMediaPlayers();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Метод для освобождения ресурсов MediaPlayer при завершении активности
    private void cleanMediaPlayers(){
        if (mediaPlayers != null) {
            for (MediaPlayer mediaPlayer : mediaPlayers) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanMediaPlayers();
    }
}