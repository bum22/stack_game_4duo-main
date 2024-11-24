package com.example.stack_forduo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nicknameInput;
    private Button startGameButton;
    private ImageView imageView;
    private LinearLayout modeLayout;
    private View overlayBackground;
    private TextView textViewStart, textViewStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 레이아웃 설정

        // 닉네임 입력 버튼 초기화
        nicknameInput = findViewById(R.id.nickname_input);
        startGameButton = findViewById(R.id.start_game_button);

        // 게임 시작 버튼 클릭 시
        startGameButton.setOnClickListener(v -> {
            String nickname = nicknameInput.getText().toString();  //닉네임 가져오기
            if (nickname.isEmpty()) {
                Toast.makeText(this, "Please enter a nickname!", Toast.LENGTH_SHORT).show();
                return;
            }

            //닉네임 입력 시 GameActivity로 이동
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("PLAYER_NICKNAME", nickname);  //닉네임 전달
            startActivity(intent);
        });

        // 전체 화면 설정 (앱바, 홈바 숨기기)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        // 애니메이션 적용
        textViewStart = findViewById(R.id.textViewStart);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.blink_animation);
        textViewStart.startAnimation(anim);

        // 버튼과 UI 요소 초기화
        imageView = findViewById(R.id.imageView);
        textViewStack = findViewById(R.id.textViewStack);
        modeLayout = findViewById(R.id.mode_layout);
        overlayBackground = findViewById(R.id.overlay_background);

        // 초기 상태에서 모드 선택 레이아웃 숨기기
        modeLayout.setVisibility(View.GONE);
        overlayBackground.setVisibility(View.GONE);

        Button singleModeButton = findViewById(R.id.singleMode_button);
        Button multiModeButton = findViewById(R.id.multiMode_button);

        // 싱글 모드 버튼 클릭 시
        singleModeButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("MODE", "SINGLE");
            startActivity(intent);
        });

        // 멀티 모드 버튼 클릭 시
        multiModeButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("MODE", "MULTI");
            startActivity(intent);
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            textViewStack.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);

            // 애니메이션 정지 및 숨기기
            textViewStart.clearAnimation();
            textViewStart.setVisibility(View.GONE);

            // 모드 선택 화면 표시
            overlayBackground.setVisibility(View.VISIBLE);
            modeLayout.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onTouchEvent(event);
    }
}
