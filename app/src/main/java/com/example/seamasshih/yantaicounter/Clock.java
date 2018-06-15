package com.example.seamasshih.yantaicounter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Clock extends View {
    public Clock(Context context , AttributeSet attributeSet){
        super(context,attributeSet);

        for (int i = 0 ; i < 10 ; i ++){
            for (int j = 0 ; j < 7 ; j++){
                number[i][j] = new FloatPoint();
            }
        }

        number[0][0].set(0,0);
        number[0][1].set(1,0);
        number[0][2].set(1,1);
        number[0][3].set(1,2);
        number[0][4].set(0,2);
        number[0][5].set(0,1);
        number[0][6].set(0,0);

        number[1][0].set(1,0);
        number[1][1].set(1,0);
        number[1][2].set(1,1);
        number[1][3].set(1,1);
        number[1][4].set(1,1);
        number[1][5].set(1,2);
        number[1][6].set(1,2);

        number[2][0].set(0,0);
        number[2][1].set(1,0);
        number[2][2].set(1,1);
        number[2][3].set(1,1);
        number[2][4].set(0,1);
        number[2][5].set(0,2);
        number[2][6].set(1,2);

        number[3][0].set(0,0);
        number[3][1].set(1,0);
        number[3][2].set(1,1);
        number[3][3].set(0,1);
        number[3][4].set(1,1);
        number[3][5].set(1,2);
        number[3][6].set(0,2);

        number[4][0].set(0,0);
        number[4][1].set(0,1);
        number[4][2].set(1,1);
        number[4][3].set(1,0);
        number[4][4].set(1,1);
        number[4][5].set(1,1);
        number[4][6].set(1,2);

        number[5][0].set(1,0);
        number[5][1].set(0,0);
        number[5][2].set(0,1);
        number[5][3].set(0,1);
        number[5][4].set(1,1);
        number[5][5].set(1,2);
        number[5][6].set(0,2);

        number[6][0].set(1,0);
        number[6][1].set(0,0);
        number[6][2].set(0,1);
        number[6][3].set(0,2);
        number[6][4].set(1,2);
        number[6][5].set(1,1);
        number[6][6].set(0,1);

        number[7][0].set(0,1);
        number[7][1].set(0,0);
        number[7][2].set(0,0);
        number[7][3].set(1,0);
        number[7][4].set(1,1);
        number[7][5].set(1,1);
        number[7][6].set(1,2);

        number[8][0].set(1,1);
        number[8][1].set(1,0);
        number[8][2].set(0,0);
        number[8][3].set(0,2);
        number[8][4].set(1,2);
        number[8][5].set(1,1);
        number[8][6].set(0,1);

        number[9][0].set(1,0);
        number[9][1].set(0,0);
        number[9][2].set(0,1);
        number[9][3].set(1,1);
        number[9][4].set(1,0);
        number[9][5].set(1,2);
        number[9][6].set(0,2);

        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);

        paintText.setTextSize(100);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setColor(Color.RED);
        Paint.FontMetrics fontMetrix = paintText.getFontMetrics();
        adjustTextY = (fontMetrix.bottom - fontMetrix.top)/2 - fontMetrix.bottom;
        adjustTextBottom = adjustTextY + fontMetrix.bottom;


        for (int i = 0 ; i < hour.length ; i++){
            day[i] = new Number();
            hour[i] = new Number();
            minute[i] = new Number();
            second[i] = new Number();
            day[i].initialPoints();
            hour[i].initialPoints();
            minute[i].initialPoints();
            second[i].initialPoints();
            day[i].matrix.postScale(length*0.55f,length*0.55f);
            day[i].matrix.postTranslate(-length*0.55f-edge + (length*0.55f+edge)*i,edge*2);
            day[i].setPath();
            hour[i].matrix.postScale(length*0.7f,length*0.7f);
            hour[i].matrix.postTranslate(-length*0.7f-edge+ (length*0.7f+edge)*i,length*2+edge);
            hour[i].setPath();
            minute[i].matrix.postScale(length*0.85f,length*0.85f);
            minute[i].matrix.postTranslate(-length*0.85f-edge+ (length*0.85f+edge)*i,(length*2+edge)*2);
            minute[i].setPath();
            second[i].matrix.postScale(length,length);
            second[i].matrix.postTranslate(-length-edge+ (length+edge)*i,(length*2+edge)*3);
            second[i].setPath();
            hour[i].max = 6 + i*4;
            minute[i].max = 6 + i*4;
            second[i].max = 6 + i*4;
        }

        for (int i = 0 ; i < meal.length ; i++){
            meal[i] = new Number();
            meal[i].initialPoints();
            meal[i].matrix.postScale(length,length);
            meal[i].matrix.postTranslate(-length/2 - length - edge + (length+edge)*(i),-length/2);
            meal[i].setPath();
            meal[i].max = 10;
        }

        for (int i = 0 ; i < money.length ; i++){
            money[i] = new Number();
            money[i].initialPoints();
            money[i].matrix.postScale(length,length);
            money[i].matrix.postTranslate(-length/2 - length*2 - edge*2 + (length+edge)*(i),-length/2);
            money[i].setPath();
            money[i].max = 10;
            money[i].setAnimatorCW();
        }

        for (int i = 0 ; i < acmday.length ; i++){
            acmday[i] = new Number();
            acmday[i].initialPoints();
            acmday[i].matrix.postScale(length,length);
            acmday[i].matrix.postTranslate(-edge/2 - length + (length+edge)*(i),-length/2);
            acmday[i].setPath();
            acmday[i].max = 10;
            acmday[i].setAnimatorCW();
        }

        for (int i = 0 ; i < percent.length ; i++){
            percent[i] = new Number();
            percent[i].initialPoints();
            if (i < 2) {
                percent[i].matrix.postScale(length, length);
                percent[i].matrix.postTranslate(-edge/2 - length - (length + edge) + (length+edge)*(i),-length/2);
            }
            else {
                percent[i].matrix.postScale(length * 2 / 3, length * 2 / 3);
                percent[i].matrix.postTranslate( edge/2+ (length+edge)*(i-2)*2/3,length/6);
            }
            percent[i].setPath();
            percent[i].max = 10;
            percent[i].setAnimatorCW();
        }
    }
    private Resources resources = this.getResources();
    private DisplayMetrics dm = resources.getDisplayMetrics();
    private int screenWidth = dm.widthPixels;
    private int screenHeight = dm.heightPixels;
    private float adjustTextY;
    private float adjustTextBottom;
    FloatPoint[][] number = new FloatPoint[10][7];
    Number[] day = new Number[2];
    Number[] hour = new Number[2];
    Number[] minute = new Number[2];
    Number[] second = new Number[2];
    Number[] meal = new Number[3];
    Number[] money = new Number[5];
    Number[] acmday = new Number[2];
    Number[] percent = new Number[6];
    Paint paint = new Paint();
    Paint paintText = new Paint();
    float length = 150;
    MyThread myThread = new MyThread();
    Clock clock = this;
    float edge = 40;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    Date curDate = new Date(System.currentTimeMillis());
    MainActivity mainActivity;
    final int CAL_DAY = 1000 * 60 * 60 * 24;
    final int CAL_HOUR = 1000 * 60 * 60;
    final int CAL_MINUTE = 1000 * 60;
    final int CAL_SECOND = 1000;
    final int MODE_DHMS = 0;
    final int MODE_MEAL = 1;
    final int MODE_MONEY = 2;
    final int MODE_ACMDAY = 3;
    final int MODE_PERCENT = 4;
    final String stringMEAL = "餐";
    final String stringMEALTOP = "還有...";
    final String stringMONEYTOP = "累積賺到...";
    final String stringMONEY = "港幣";
    final String stringMONEYBOTTOM = "點擊選擇出差日期";
    final String stringACMDAYTOP = "累積出差...";
    final String stringACMDAY = "天";
    final String stringPERCENTTOP = "累積進度...";
    final String stringPERCENTRIGHT = "%";
    int comeYear;
    int comeMonth;
    int comeDate;
    boolean isFinish = false;
    int mode = MODE_DHMS;
    int yearInDHMS;
    int monthInDHMS;
    int dateInDHMS;
    int mealInMEAL = 0;
    int moneyInMONEY = 0;
    int dayInACMDAY = 0;
    float percentInPERCENT = 0f;

    void setMode(int mode){
        this.mode = mode;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN && (mode == MODE_MONEY || mode == MODE_ACMDAY || mode == MODE_PERCENT) && event.getY() > screenHeight*3/4 && event.getX() < screenWidth/2 + 300 && event.getX() > screenWidth/2 - 300){
            mainActivity.comeDatePickerDialog.show();
            return true;
        }
        else if (event.getActionMasked() == MotionEvent.ACTION_DOWN && !(event.getX() < screenWidth*3/4 &&  event.getX() > screenWidth/4 && event.getY() < screenHeight*3/4 && event.getY() > screenHeight/4)){
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mode){
            case MODE_DHMS:
                canvas.scale(1.4f,1.4f,screenWidth/2,0);
                canvas.translate(screenWidth/2+edge/2,0);
                for (int i = 0 ; i < hour.length ; i++){
                    canvas.drawPath(day[i].path,paint);
                    canvas.drawPath(hour[i].path,paint);
                    canvas.drawPath(minute[i].path,paint);
                    canvas.drawPath(second[i].path,paint);
                }
                break;
            case MODE_MEAL:
                canvas.translate(screenWidth/2 , screenHeight/2 - length);
                canvas.scale(1.5f,1.5f);
                for (int i = 0 ; i < meal.length ; i++){
                    canvas.drawPath(meal[i].path,paint);
                }
                canvas.translate(0,length*3);
                canvas.drawText(stringMEAL,0,adjustTextY,paintText);
                canvas.translate(0,-length*5);
                canvas.drawText(stringMEALTOP,0,adjustTextY,paintText);
                break;
            case MODE_MONEY:
                canvas.translate(screenWidth/2 , screenHeight/2 - length);
                for (int i = 0 ; i < money.length ; i++){
                    canvas.drawPath(money[i].path,paint);
                }
                canvas.translate(0,-length*2);
                canvas.drawText(stringMONEYTOP,0,adjustTextY,paintText);
                canvas.translate(0,length*5);
                canvas.drawText(stringMONEY,0,adjustTextY,paintText);
                canvas.translate(0,length*3);
                paintText.setTextSize(50);
                canvas.drawText(stringMONEYBOTTOM,0,adjustTextY,paintText);
                paintText.setTextSize(100);
                break;
            case MODE_ACMDAY:
                canvas.translate(screenWidth/2 , screenHeight/2 - length);
                for (int i = 0 ; i < acmday.length ; i++){
                    canvas.drawPath(acmday[i].path,paint);
                }
                canvas.translate(0,-length*2);
                canvas.drawText(stringACMDAYTOP,0,adjustTextY,paintText);
                canvas.translate(0,length*5);
                canvas.drawText(stringACMDAY,0,adjustTextY,paintText);
                canvas.translate(0,length*3);
                paintText.setTextSize(50);
                canvas.drawText(stringMONEYBOTTOM,0,adjustTextY,paintText);
                paintText.setTextSize(100);
                break;
            case MODE_PERCENT:
                canvas.translate(screenWidth/2 , screenHeight/2 - length);
                for (int i = 0 ; i < percent.length ; i++){
                    canvas.drawPath(percent[i].path,paint);
                }
                canvas.drawText(".",0,adjustTextBottom+length,paintText);
                canvas.translate(0,-length*2);
                canvas.drawText(stringPERCENTTOP,0,adjustTextY,paintText);
                canvas.translate(0,length*8);
                paintText.setTextSize(50);
                canvas.drawText(stringMONEYBOTTOM,0,adjustTextY,paintText);
                paintText.setTextSize(100);
                canvas.translate(0,-length*3);
                canvas.drawText(stringPERCENTRIGHT,0,adjustTextY,paintText);
                break;
        }
    }

    public String getDate(){
        return simpleDateFormat.format(curDate).trim();
    }
    public boolean setDate(int year , int month , int date , int mode){
        myThread.isHalt = true;
        Date backDate = new Date();
        String s = String.valueOf(year) + (month > 10 ? (month) : "0"+(month)) + (date > 10 ? (date) : "0"+(date)) + "185000";
        try {
            backDate = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        curDate = new Date(System.currentTimeMillis());
        long diff = backDate.getTime() - curDate.getTime();

        if (diff < 0)
            return false;

        yearInDHMS = year;
        monthInDHMS = month;
        dateInDHMS = date;
        this.mode = mode;

        long days = diff/CAL_DAY;
        diff -= days*CAL_DAY;
        long hours = diff / CAL_HOUR;
        diff -= hours*CAL_HOUR;
        long minutes = diff/CAL_MINUTE;
        diff -= minutes*CAL_MINUTE;
        long seconds = diff/CAL_SECOND;

        switch (mode){
            case MODE_DHMS:
                day[0].now = (int)days/10;
                day[1].now = (int)days%10;
                hour[0].now = (int)hours/10;
                hour[1].now = (int)hours%10;
                minute[0].now = (int)minutes/10;
                minute[1].now = (int)minutes%10;
                second[0].now = (int)seconds/10;
                second[1].now = (int)seconds%10;
                for (int i = 0 ; i < hour.length ; i++){
                    day[i].setPoints();
                    hour[i].setPoints();
                    minute[i].setPoints();
                    second[i].setPoints();
                    day[i].setPath();
                    hour[i].setPath();
                    minute[i].setPath();
                    second[i].setPath();
                }
                break;
            case MODE_MEAL:
                mealInMEAL = (int)days*3;
                if (hours >= 11) mealInMEAL += 3;
                else if (hours >= 6) mealInMEAL += 2;
                else if (hours >= 1) mealInMEAL += 1;
                meal[0].now = mealInMEAL/100;
                meal[1].now = (mealInMEAL%100)/10;
                meal[2].now = mealInMEAL%10;
                for (int i = 0 ; i < meal.length ; i++){
                    meal[i].setPoints();
                    meal[i].setPath();
                }
                break;
            case MODE_MONEY:
                Date arriveDate = new Date();
                String s2 = String.valueOf(comeYear) + (comeMonth > 10 ? (comeMonth) : "0"+(comeMonth)) + (comeDate > 10 ? (comeDate) : "0"+(comeDate)) + "000000";
                try {
                    arriveDate = simpleDateFormat.parse(s2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long total = backDate.getTime() - arriveDate.getTime();
                diff = curDate.getTime() - arriveDate.getTime();

                int totalMoney = (int)((total / CAL_DAY)+1) * 250;
                int totalTime = (int) (total/CAL_SECOND);
                int throughTime = (int) (diff / CAL_SECOND);

                moneyInMONEY =(int) ( (float)totalMoney * (float)throughTime / (float)totalTime);

                money[0].now = moneyInMONEY/10000;
                money[1].now = (moneyInMONEY%10000)/1000;
                money[2].now = (moneyInMONEY%1000)/100;
                money[3].now = (moneyInMONEY%100)/10;
                money[4].now = moneyInMONEY%10;
                for (int i = 0 ; i < money.length ; i++){
                    money[i].setPoints();
                    money[i].setPath();
                }
                break;
            case MODE_ACMDAY:
                Date arrDate = new Date();
                String s3 = String.valueOf(comeYear) + (comeMonth > 10 ? (comeMonth) : "0"+(comeMonth)) + (comeDate > 10 ? (comeDate) : "0"+(comeDate)) + "000000";
                try {
                    arrDate = simpleDateFormat.parse(s3);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                diff = curDate.getTime() - arrDate.getTime();
                dayInACMDAY = (int)(diff/CAL_DAY) + 1;

                if (dayInACMDAY > 99) return false;

                acmday[0].now = dayInACMDAY /10;
                acmday[1].now = dayInACMDAY %10;
                for (int i = 0 ; i < acmday.length ; i++){
                    acmday[i].setPoints();
                    acmday[i].setPath();
                }
                break;
            case MODE_PERCENT:
                Date arDate = new Date();
                String s4 = String.valueOf(comeYear) + (comeMonth > 10 ? (comeMonth) : "0"+(comeMonth)) + (comeDate > 10 ? (comeDate) : "0"+(comeDate)) + "000000";
                try {
                    arDate = simpleDateFormat.parse(s4);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                diff = curDate.getTime() - arDate.getTime();
                long ttl  = backDate.getTime() - arDate.getTime();
                percentInPERCENT = (float)(diff/10000000) / (float) (ttl/10000000) * 100f;
                percent[0].now = (int)percentInPERCENT /10;
                percent[1].now = (int)percentInPERCENT %10;
                percent[2].now = (int)(percentInPERCENT*10) %10;
                percent[3].now = (int)(percentInPERCENT*10*10) %10;
                percent[4].now = (int)(percentInPERCENT*10*10*10) %10;
                percent[5].now = (int)(percentInPERCENT*10*10*10*10) %10;
                for (int i = 0 ; i < percent.length ; i++){
                    percent[i].setPoints();
                    percent[i].setPath();
                }
                break;
        }
        myThread = new MyThread();
        myThread.start();
        invalidate();
        return true;
    }

    boolean setComeDate(int comeYear , int comeMonth , int comeDate){
        Date arriveDate = new Date();
        String s = String.valueOf(comeYear) + (comeMonth > 10 ? (comeMonth) : "0"+(comeMonth)) + (comeDate > 10 ? (comeDate) : "0"+(comeDate)) + "000000";
        try {
            arriveDate = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        curDate = new Date(System.currentTimeMillis());
        long diff = curDate.getTime() - arriveDate.getTime();

        if (diff < 0)
            return false;

        this.comeYear = comeYear;
        this.comeMonth = comeMonth;
        this.comeDate = comeDate;

        return true;
    }

    public class Number {
        public Number(){
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        for (int i = 0 ; i < points.length ; i++)
                            points[i].set(number[(now+1)%max][i].x + value * (number[now][i].x - number[(now+1)%max][i].x),number[(now+1)%max][i].y + value * (number[now][i].y - number[(now+1)%max][i].y));
                        setPath();
                        postInvalidate();
                    }
                });
                animatorS.setInterpolator(new LinearInterpolator());
                animatorS.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        for (int i = 0 ; i < points.length ; i++)
                            points[i].set(number[0][i].x + value * (number[now][i].x - number[0][i].x),number[0][i].y + value * (number[now][i].y - number[0][i].y));
                        setPath();
                        postInvalidate();
                    }
                });
        }
        public Path path = new Path();
        public Matrix matrix = new Matrix();
        public ValueAnimator animator = ValueAnimator.ofFloat(0,1).setDuration(500);
        public ValueAnimator animatorS = ValueAnimator.ofFloat(0,1).setDuration(500);
        public int now = 0;
        public int max = 10;
        public FloatPoint points[] = new FloatPoint[7];
        public void setPath(){
            path.reset();
            path.moveTo(points[0].x,points[0].y);
            for (int i = 1 ; i < points.length ; i++)
                path.lineTo(points[i].x,points[i].y);
            path.transform(matrix);
        }
        public void initialPoints(){
            for (int i = 0; i < points.length ; i++){
                points[i] = new FloatPoint();
                points[i].set(number[now][i]);
            }
        }
        public void setPoints(){
            for (int i = 0; i < points.length ; i++){
                Log.w("Seamas","i " +  i+ " " + now);
                points[i].set(number[now][i]);
            }
        }
        void setAnimatorCW(){
            animator.removeAllUpdateListeners();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    for (int i = 0 ; i < points.length ; i++)
                        points[i].set(number[(now-1+max)%max][i].x + value * (number[now][i].x - number[(now-1+max)%max][i].x),number[(now-1+max)%max][i].y + value * (number[now][i].y - number[(now-1+max)%max][i].y));
                    setPath();
                    postInvalidate();
                }
            });
        }
    }

    public class MyThread extends Thread{

        Number calculusTime(Number number1 , Number number2){
            if (number2.now == number2.max-1) {
                number1.now = (number1.now + number1.max -1) % number1.max;
                return number1;
            }
            else return null;
        }

        public boolean isHalt = false;

        @Override
        public void run() {
            switch (mode){
                case MODE_DHMS:
                    while (!isHalt) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (isHalt) return;
                        second[1].now = (second[1].now + 9) % 10;
                        clock.post(new Runnable() {
                            @Override
                            public void run() {
                                second[1].animator.start();
                            }
                        });
                        if (calculusTime(second[0],second[1]) != null) {
                            clock.post(new Runnable() {
                                @Override
                                public void run() {
                                    second[0].animator.start();
                                }
                            });
                            if (calculusTime(minute[1],second[0]) != null) {
                                clock.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        minute[1].animator.start();
                                    }
                                });
                                if (calculusTime(minute[0],minute[1]) != null) {
                                    clock.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            minute[0].animator.start();
                                        }
                                    });
                                    if (calculusTime(hour[1],minute[0]) != null) {
                                        clock.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                hour[1].animator.start();
                                            }
                                        });
                                        if (calculusTime(hour[0],hour[1]) != null) {
                                            clock.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    hour[0].animator.start();
                                                }
                                            });
                                            if (calculusTime(day[1],hour[0]) != null) {
                                                clock.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        day[1].animator.start();
                                                    }
                                                });
                                                if (calculusTime(day[0],day[1]) != null) {
                                                    clock.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            day[0].animator.start();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case MODE_MEAL:
                    while (!isHalt) {
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Date backDate = new Date();
                        String s = String.valueOf(yearInDHMS) + (monthInDHMS > 10 ? (monthInDHMS) : "0"+(monthInDHMS)) + (dateInDHMS > 10 ? (dateInDHMS) : "0"+(dateInDHMS)) + "185000";
                        try {
                            backDate = simpleDateFormat.parse(s);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        curDate = new Date(System.currentTimeMillis());
                        long diff = backDate.getTime() - curDate.getTime();

                        if (diff < 0) {
                            for (int i = 0 ; i < meal.length ; i++){
                                meal[i].now = 0;
                                meal[i].setPoints();
                                meal[i].setPath();
                            }
                            isFinish = true;
                            postInvalidate();
                            return;
                        }

                        long days = diff/CAL_DAY;
                        diff -= days*CAL_DAY;
                        long hours = diff / CAL_HOUR;

                        mealInMEAL = (int)days*3;
                        if (hours >= 11) mealInMEAL += 3;
                        else if (hours >= 6) mealInMEAL += 2;
                        else if (hours >= 1) mealInMEAL += 1;
                        if (meal[0].now != mealInMEAL/100) {
                            meal[0].now = mealInMEAL/100;
                            clock.post(new Runnable() {
                                @Override
                                public void run() {
                                    meal[0].animator.start();
                                }
                            });
                        }
                        if (meal[1].now != (mealInMEAL%100)/10) {
                            meal[1].now = (mealInMEAL%100)/10;
                            clock.post(new Runnable() {
                                @Override
                                public void run() {
                                    meal[1].animator.start();
                                }
                            });
                        }
                        if (meal[2].now != mealInMEAL%10) {
                            meal[2].now = mealInMEAL%10;
                            clock.post(new Runnable() {
                                @Override
                                public void run() {
                                    meal[2].animator.start();
                                }
                            });
                        }
                    }
                    break;
                case MODE_MONEY:
                    while (!isHalt) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Date arriveDate = new Date();
                        String s = String.valueOf(comeYear) + (comeMonth > 10 ? (comeMonth) : "0"+(comeMonth)) + (comeDate > 10 ? (comeDate) : "0"+(comeDate)) + "000000";
                        try {
                            arriveDate = simpleDateFormat.parse(s);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        curDate = new Date(System.currentTimeMillis());
                        long diff = curDate.getTime() - arriveDate.getTime();

                        if (diff < 0) {
                            for (int i = 0 ; i < money.length ; i++){
                                money[i].now = 0;
                                money[i].setPoints();
                                money[i].setPath();
                            }
                            isFinish = true;
                            postInvalidate();
                            return;
                        }

                        Date backDate = new Date();
                        String s2 = String.valueOf(yearInDHMS) + (monthInDHMS > 10 ? (monthInDHMS) : "0"+(monthInDHMS)) + (dateInDHMS > 10 ? (dateInDHMS) : "0"+(dateInDHMS)) + "185000";
                        try {
                            backDate = simpleDateFormat.parse(s2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long total = backDate.getTime() - arriveDate.getTime();

                        int totalMoney = (int)(int)((total / CAL_DAY)+1) * 250;
                        int totalTime = (int) (total/CAL_SECOND);
                        int throughTime = (int) (diff / CAL_SECOND);

                        moneyInMONEY =(int) ( (float)totalMoney * (float)throughTime / (float)totalTime);

                        if (money[0].now != moneyInMONEY/10000) {
                            money[0].now = moneyInMONEY/10000;
                            clock.post(new Runnable() {
                                @Override
                                public void run() {
                                    money[0].animator.start();
                                }
                            });
                        }
                        if (money[1].now != (moneyInMONEY%10000)/1000) {
                            money[1].now = (moneyInMONEY%10000)/1000;
                            clock.post(new Runnable() {
                                @Override
                                public void run() {
                                    money[1].animator.start();
                                }
                            });
                        }
                        if (money[2].now != (moneyInMONEY%1000)/100) {
                            money[2].now = (moneyInMONEY%1000)/100;
                            clock.post(new Runnable() {
                                @Override
                                public void run() {
                                    money[2].animator.start();
                                }
                            });
                        }
                        if (money[3].now != (moneyInMONEY%100)/10) {
                            money[3].now = (moneyInMONEY%100)/10;
                            clock.post(new Runnable() {
                                @Override
                                public void run() {
                                    money[3].animator.start();
                                }
                            });
                        }
                        if (money[4].now != moneyInMONEY%10) {
                            money[4].now = moneyInMONEY%10;
                            clock.post(new Runnable() {
                                @Override
                                public void run() {
                                    money[4].animator.start();
                                }
                            });
                        }
                    }
                    break;
                case MODE_PERCENT:
                    while (!isHalt) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Date arriveDate = new Date();
                        String s = String.valueOf(comeYear) + (comeMonth > 10 ? (comeMonth) : "0" + (comeMonth)) + (comeDate > 10 ? (comeDate) : "0" + (comeDate)) + "000000";
                        try {
                            arriveDate = simpleDateFormat.parse(s);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        curDate = new Date(System.currentTimeMillis());
                        long diff = curDate.getTime() - arriveDate.getTime();

                        if (diff < 0) {
                            for (int i = 0; i < money.length; i++) {
                                money[i].now = 0;
                                money[i].setPoints();
                                money[i].setPath();
                            }
                            isFinish = true;
                            postInvalidate();
                            return;
                        }

                        Date backDate = new Date();
                        String s2 = String.valueOf(yearInDHMS) + (monthInDHMS > 10 ? (monthInDHMS) : "0" + (monthInDHMS)) + (dateInDHMS > 10 ? (dateInDHMS) : "0" + (dateInDHMS)) + "185000";
                        try {
                            backDate = simpleDateFormat.parse(s2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long total = backDate.getTime() - arriveDate.getTime();

                        percentInPERCENT = (float) (diff / 10000000) / (float) (total / 10000000) * 100f;

                        for (int i = 0; i < percent.length; i++) {
                            if (percent[i].now != (int) (percentInPERCENT * Math.pow(10, i - 1)) % 10) {
                                percent[i].now = (int) (percentInPERCENT * Math.pow(10, i - 1)) % 10;
                                final int k = i;
                                clock.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        percent[k].animator.start();
                                    }
                                });
                            }
                        }
                    }
                    break;
            }
        }
    }
}
