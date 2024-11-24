package com.example.stack_forduo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GameOverActivity extends AppCompatActivity {
    private boolean isSingleMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSingleMode = getIntent().getBooleanExtra("MODE", false);

        if (!isSingleMode) {
            // 멀티 모드일 경우
            setContentView(R.layout.activity_gameover_multi);

            String winner = getIntent().getStringExtra("WINNER");
            int player1Score = getIntent().getIntExtra("PLAYER1_SCORE", 0);
            int player2Score = getIntent().getIntExtra("PLAYER2_SCORE", 0);

            TextView winnerTextView = findViewById(R.id.winner);
            winnerTextView.setText(winner);

            TextView player1ScoreView = findViewById(R.id.player1_score_view);
            TextView player2ScoreView = findViewById(R.id.player2_score_view);

            player1ScoreView.setText("Player 1 Score: " + player1Score);
            player2ScoreView.setText("Player 2 Score: " + player2Score);

            // RecyclerView 설정
            RecyclerView recyclerView = findViewById(R.id.scoreboard_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // 데이터베이스에서 점수 불러오기
            loadTopScores(recyclerView);

        } else {
            // 싱글 모드일 경우
            setContentView(R.layout.activity_gameover_single);

            int player1Score = getIntent().getIntExtra("PLAYER1_SCORE", 0);

            TextView player1ScoreView = findViewById(R.id.player1_score_view);
            player1ScoreView.setText(String.valueOf(player1Score));
        }

        // "Play Again" 버튼 클릭 시
        Button playAgainButton = findViewById(R.id.playagain);
        playAgainButton.setOnClickListener(v -> onClickPlayAgain());

        // "Quit" 버튼 클릭 시
        Button quitButton = findViewById(R.id.quitButton);
        quitButton.setOnClickListener(v -> onClickQuit());
    }

    private void loadTopScores(RecyclerView recyclerView) {
        // 점수 데이터 읽기
        GameDatabaseHelper dbHelper = new GameDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nickname, score FROM scores ORDER BY score DESC LIMIT 10", null);

        List<Score> scores = new ArrayList<>();
        while (cursor.moveToNext()) {
            scores.add(new Score(cursor.getString(0), cursor.getInt(1)));
        }
        cursor.close();

        // RecyclerView에 데이터 표시
        ScoreAdapter adapter = new ScoreAdapter(scores);
        recyclerView.setAdapter(adapter);
    }

    public void onClickPlayAgain() {
        Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
        intent.putExtra("MODE", isSingleMode ? "SINGLE" : "MULTI");
        startActivity(intent);
    }

    public void onClickQuit() {
        Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
