package jp.ac.soka.tkl.sss2017;

/*------------------------------*
 *  MainActivity.java           *
 *  ゲームの流れ                *
 *  last update : June 15, 2017 *
 *------------------------------*/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static boolean go = false;  // ゲームオーバーフラグ
    static String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);    // 画面のレイアウト設定
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(go) {
            setContentView(R.layout.activity_gameover);  // 画面のレイアウト設定（ゲームオーバー時）
            TextView counter = (TextView)findViewById(R.id.counter);
            counter.setText(time);
            Button back = (Button) findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go = false;
                    setContentView(R.layout.activity_start);    // ゲームオーバーのときはフラグをオフにしてスタート画面に戻す
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {    // 画面をタッチしたときの動作
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(!go) {
                Intent intent = new Intent(getApplication(), GameActivity.class);
                startActivity(intent);  // ゲームをスタートする
            }
        }

        return true;
    }

}
