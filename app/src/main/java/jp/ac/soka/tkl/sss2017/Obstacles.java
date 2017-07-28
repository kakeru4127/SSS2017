package jp.ac.soka.tkl.sss2017;

/*------------------------------*
 *  Obstacles.java              *
 *  障害物のクラス              *
 *  last update : July 28, 2017 *
 *------------------------------*/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

class Obstacles extends View{
    Bitmap shape = null;
    int posX, posY, w, h;
    boolean end = false;
    Timer timer;
    Handler handler = new Handler();

    public Obstacles(Context context) {
        super(context);
        int num = RandomGenerator.rand(3);
        if(num == 0) shape = BitmapFactory.decodeResource(getResources(),R.drawable.maru);
        else if(num == 1) shape = BitmapFactory.decodeResource(getResources(),R.drawable.sankaku);
        else if(num == 2) shape = BitmapFactory.decodeResource(getResources(),R.drawable.shikaku);
        posX = 0;
        posY = 0;
        w = shape.getWidth();
        h = shape.getHeight();
        shape = Bitmap.createScaledBitmap(shape, w/2, h/2, true);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        posX = posX - 20;
                        if(posX < 0 - w/2) {
                            end = true;
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 100);

    }

    public boolean isColliding( int c_w, int c_h, int c_x, int c_y ){
        if( c_x + c_w > posX && posX + w/2 > c_x ) {
            if ( c_y + c_h > posY && posY + h/2 > c_y ) {
                return true;
            }
        }
        return false;
    }




}
