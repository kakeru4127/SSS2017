package jp.ac.soka.tkl.sss2017;

/*------------------------------*
 *  GameView.java           *
 *  ゲームの描画                *
 *  last update : June 22, 2017 *
 *------------------------------*/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {
    private Paint paint;
    private Boolean viewflg;
    private Bitmap character = null;
    private Bitmap background = null;
    private Bitmap ground = null;
    private int c_w;
    private int c_h;
    private int c_y;
    private int c_n;
    private int bg_w;
    private int bg_x;
    private int g_x;
    private int g_y;
    private int max;
    private boolean jump = false;
    private Timer timer1;
    private final Handler handler = new Handler();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        viewflg = true;
        character = BitmapFactory.decodeResource(getResources(), R.drawable.c1);
        c_n = 1;
        background = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        ground = BitmapFactory.decodeResource(getResources(),R.drawable.bg);

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display dp = wm.getDefaultDisplay();
        Point size = new Point();
        dp.getSize(size);

        c_w = size.y/5;
        c_h = size.y/5;
        bg_w = size.x * 2;
        int bg_h = size.y/10 * 7;
        int g_h = size.y/10 *3;

        c_y = size.y/2;
        bg_x = 0;
        g_x = 0;
        g_y = size.y/10 * 7;

        max = c_y - 250;

        character = Bitmap.createScaledBitmap(character, c_w, c_h, true);
        background = Bitmap.createScaledBitmap(background, bg_w, bg_h, true);
        ground = Bitmap.createScaledBitmap(ground, bg_w, g_h, true);
    }

    public void showCanvas(boolean flg){
        viewflg = flg;
        invalidate();
    }

    public void animation(boolean  flg) {
        if(flg) {
            timer1 = new Timer();
            timer1.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            if(GameActivity.jumpflg){
                                if(!jump) {
                                    c_y -= 25;
                                    if(c_y == max) jump = true;
                                } else {
                                    c_y += 25;
                                    if(c_y == max + 250) {
                                        jump = false;
                                        GameActivity.jumpflg = false;
                                    }
                                }
                            }else if (c_n == 1) {
                                c_n = 2;
                                character = BitmapFactory.decodeResource(getResources(), R.drawable.c2);
                                character = Bitmap.createScaledBitmap(character, c_w, c_h, true);
                            } else if (c_n == 2) {
                                c_n = 3;
                            } else if (c_n == 3) {
                                c_n = 4;
                                character = BitmapFactory.decodeResource(getResources(), R.drawable.c1);
                                character = Bitmap.createScaledBitmap(character, c_w, c_h, true);
                            } else if (c_n == 4) {
                                c_n = 1;
                            } else if (!GameActivity.jumpflg) c_n = 1;

                            g_x -= 20;
                            if(g_x + bg_w < 0) g_x = 0;
                            bg_x -= 5;
                            if(bg_x + bg_w < 0) bg_x = 0;

                            invalidate();
                        }
                    });
                }
            }, 0, 100);
        }
        else {
            timer1.cancel();
            timer1 = null;
        }
    }

    public void jump() {
        c_n = 3;
        character = BitmapFactory.decodeResource(getResources(), R.drawable.c3);
        character = Bitmap.createScaledBitmap(character, c_w, c_h, true);
        invalidate();
    }

    public void clear() {
        viewflg = false;
        invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if(viewflg){
            canvas.drawBitmap(background, bg_x, 0, paint);
            canvas.drawBitmap(background, bg_x + bg_w, 0, paint);
            canvas.drawBitmap(ground, g_x, g_y, paint);
            canvas.drawBitmap(ground, g_x + bg_w, g_y, paint);
            canvas.drawBitmap(character, 100, c_y, paint);
        }

    }
}
