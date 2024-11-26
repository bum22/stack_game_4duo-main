package com.example.stack_forduo;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private Player player1, player2;
    private boolean isSingleMode;  // 싱글 모드인지 여부를 저장

    public boolean isSingleMode() {
        return isSingleMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 모드에 맞게 게임 시작
        Intent intent = getIntent();
        String mode = intent.getStringExtra("MODE");
        isSingleMode = "SINGLE".equals(mode);

        // 멀티모드일 때, player2 추가
        if (!isSingleMode) {
            setContentView(R.layout.activity_game_multi);

            player2 = new Player("Player 2");

            // 플레이어 점수
            EditText player2ScoreView = findViewById(R.id.player2_score);
            FrameLayout player2Layout = findViewById(R.id.player2);

            // GameView 인스턴스 생성 및 FrameLayout에 추가
            GameView player2GameView = new GameView(this, player2, player2ScoreView);
            player2Layout.addView(player2GameView);
        } else {
            setContentView(R.layout.activity_game_single);
        }

        player1 = new Player("Player 1");
        EditText player1ScoreView = findViewById(R.id.player1_score);
        FrameLayout player1Layout = findViewById(R.id.player1);
        GameView player1GameView = new GameView(this, player1, player1ScoreView);
        player1Layout.addView(player1GameView);

        // 스크린 전체화면 설정 (appbar, homebar 숨기기)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

    // 게임 종료 시 호출되는 메서드
    public void gameOver() {
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);

        // 승자와 각 플레이어의 점수를 가져옴
        String winner = player1.getScore() > (player2 != null ? player2.getScore() : 0)
                ? player1.getName() : (player2 != null ? player2.getName() : "Player 1");
        int player1Score = player1.getScore();
        int player2Score = (player2 != null) ? player2.getScore() : 0;

        // 승자와 점수 정보를 GameOverActivity로 전달
        intent.putExtra("WINNER", winner);
        intent.putExtra("PLAYER1_SCORE", player1Score);
        if (player2 != null) {
            intent.putExtra("PLAYER2_SCORE", player2Score);
        }
        intent.putExtra("MODE", isSingleMode); // isSingleMode 플래그 추가

        // 점수 저장
        String nickname = getIntent().getStringExtra("PLAYER_NICKNAME");
        GameDatabaseHelper dbHelper = new GameDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO scores (nickname, score) VALUES (?, ?)",
                new Object[]{nickname, player1Score}); // Player 1의 점수 저장 (싱글 모드에만 적용)

        // GameOverActivity로 이동
        startActivity(intent);
    }
}
