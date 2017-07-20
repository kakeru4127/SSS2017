package jp.ac.soka.tkl.sss2017;

/*------------------------------*
 *  GameActivity.java           *
 *  ゲームの制御                *
 *  last update : July 20, 2017 *
 *------------------------------*/

import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    AlertDialog dlg;
    TextView timerText;
    Timer timer;
    Handler handler = new Handler();
    long count = 0;
    private GameView gv;
    static boolean jumpflg = false;
    boolean pauseflg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game); // 画面のレイアウト設定
        View decor = this.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        gv = (GameView) findViewById(R.id.gv);
        gv.showCanvas(true);
        gv.animation(true);

        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("終了しますか？");
        alertDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                gv.clear();
                MainActivity.go = true;
                MainActivity.time = timerText.getText().toString();
                GameActivity.this.finish();
            }
        });
        alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                gv.animation(true);
                startTimer();
                pauseflg = false;
            }
        });
        dlg = alertDlg.create();
        dlg.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        Button pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pauseflg) {
                    pauseflg = true;
                    gv.animation(false);
                    timer.cancel();
                    timer = null;
                    dlg.show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        timerText = (TextView)findViewById(R.id.timer);
        timerText.setText("00:00.0");
        count = 0;
        startTimer();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {    // 画面をタッチしたときの動作
        if(event.getAction() == MotionEvent.ACTION_DOWN) {    // 画面に触れたとき
            if(!pauseflg) {
                if (!jumpflg) {
                    jumpflg = true;
                    gv.jump();
                }
            }
        }

        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        count++;
                        long mm = count * 100 / 1000 / 60;
                        long ss = count * 100 / 1000 % 60;
                        long ms = (count * 100 - ss * 1000 - mm * 1000 * 60) / 100;
                        timerText.setText(String.format("%1$02d:%2$02d.%3$01d", mm, ss, ms));
                    }
                });
            }
        }, 0, 100);
    }

}
