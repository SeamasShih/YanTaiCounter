package com.example.seamasshih.yantaicounter;

public class FloatPoint {
    public FloatPoint(){}
    public FloatPoint(FloatPoint floatPoint){
        this.x = floatPoint.x;
        this.y = floatPoint.y;
    }
    public float x;
    public float y;
    public void set(float x , float y){
        this.x = x;
        this.y = y;
    }
    public void set(FloatPoint floatPoint){
        this.x = floatPoint.x;
        this.y = floatPoint.y;
    }

    @Override
    public String toString() {
        return "x : " + x + " ; " +"y : " + y ;
    }
}
