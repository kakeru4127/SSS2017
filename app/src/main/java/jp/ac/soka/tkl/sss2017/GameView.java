package jp.ac.soka.tkl.sss2017;

/*------------------------------*
 *  GameView.java           *
 *  ゲームの描画                *
 *  last update : June 29, 2017 *
 *------------------------------*/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
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
    private int x;
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
    private Obstacles obstacle1 = null;
    private Obstacles obstacle2 = null;
    private Obstacles obstacle3 = null;
    private int appear = 0;
    
    //private Obstacles[] obstacles = null;
    //private final int obsSize = 3;
    private int collision = 0;
    private final int invincibleTime = 20;

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

        x = size.x;
        int y = size.y;

        c_w = y /5;
        c_h = y /5;
        bg_w = x * 2;
        int bg_h = y /10 * 7;
        int g_h = y /10 * 3;

        c_y = y /2;
        bg_x = 0;
        g_x = 0;
        g_y = y /10 * 7;

        max = c_y - 250;

        character = Bitmap.createScaledBitmap(character, c_w, c_h, true);
        background = Bitmap.createScaledBitmap(background, bg_w, bg_h, true);
        ground = Bitmap.createScaledBitmap(ground, bg_w, g_h, true);
        //obstacles = new Obstacles[obsSize];

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

                            if(collision != 0) collision--;

                            g_x -= 20;
                            if(g_x + bg_w < 0) g_x = 0;
                            bg_x -= 5;
                            if(bg_x + bg_w < 0) bg_x = 0;

                            if((obstacle1 == null || obstacle2 == null || obstacle3 == null) && appear == 0 && RandomGenerator.rand(15) == 0){
                                if(obstacle1 == null) {
                                    obstacle1 = new Obstacles(getContext());
                                    obstacle1.posX = x;
                                    if(RandomGenerator.rand(3) == 0) obstacle1.posY = g_y - obstacle1.h*3/2;
                                    else obstacle1.posY = g_y - obstacle1.h/2;
                                    appear = 30;
                                }
                                else if(obstacle2 == null) {
                                    obstacle2 = new Obstacles(getContext());
                                    obstacle2.posX = x;
                                    if(RandomGenerator.rand(3) == 0) obstacle2.posY = g_y - obstacle2.h*3/2;
                                    else obstacle2.posY = g_y - obstacle2.h/2;
                                    appear = 30;
                                }
                                else if(obstacle3 == null) {
                                    obstacle3 = new Obstacles(getContext());
                                    obstacle3.posX = x;
                                    if(RandomGenerator.rand(3) == 0) obstacle3.posY = g_y - obstacle3.h*3/2;
                                    else obstacle3.posY = g_y - obstacle3.h/2;
                                    appear = 30;
                                }
                            }
                            /*
                            if(appear==0&&RandomGenerator.rand(15)==0){
                                for(int i = 0; i<obsSize; i++){
                                    if(obstacles[i]==null){
                                        obstacles[i] = new Obstacles(getContext());
                                        obstacles[i].posX = x;
                                        if(RandomGenerator.rand(3) == 0) obstacles[i].posY = g_y - obstacles[i].h*3/2;
                                        else obstacles[i].posY = g_y - obstacles[i].h/2;
                                        appear = 30;
                                    }
                                }
                            }
                            */
                            if(obstacle1 != null && obstacle1.end) obstacle1 = null;
                            if(obstacle2 != null && obstacle2.end) obstacle2 = null;
                            if(obstacle3 != null && obstacle3.end) obstacle3 = null;
                            checkCollision();
                            /*
                            for(int i = 0; i<obsSize; i++){
                                if(obstacles[i] != null && obstacles[i].end) obstacles[i] = null;
                            }
                            */

                            if(appear > 0) appear--;

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
            if(collision%2==0) canvas.drawBitmap(character, 100, c_y, paint);
            if(obstacle1 != null ) canvas.drawBitmap(obstacle1.shape,obstacle1.posX,obstacle1.posY,paint);
            if(obstacle2 != null ) canvas.drawBitmap(obstacle2.shape,obstacle2.posX,obstacle2.posY,paint);
            if(obstacle3 != null ) canvas.drawBitmap(obstacle3.shape,obstacle3.posX,obstacle3.posY,paint);

            /*
            for (Obstacles obs:obstacles){
                if(obs!=null)canvas.drawBitmap(obs.shape, obs.posX, obs.posY, paint);
            }
            */
        }
    }

    public void checkCollision(){
        if(collision != 0) return;
        if( obstacle1 != null && obstacle1.isColliding(c_w, c_h, 100, c_y) ) {
            Log.d("d",Integer.toString(obstacle1.h)+" "+Integer.toString(c_h));
            collision = invincibleTime;
        }
        if( obstacle2 != null && obstacle2.isColliding(c_w, c_h, 100, c_y) ) {
            Log.d("d",Integer.toString(obstacle2.h)+" "+Integer.toString(c_h));
            collision = invincibleTime;
        }
        if( obstacle3 != null && obstacle3.isColliding(c_w, c_h, 100, c_y) ) {
            Log.d("d",Integer.toString(obstacle3.h)+" "+Integer.toString(c_h));
            collision = invincibleTime;
        }
        /*
        for (Obstacles obs:obstacles) {
            if( obs != null && obs.isColliding(c_w, c_h, 100, c_y) ){
                collision = invincibleTime;
                break;
            }
        }*/
    }
}
