package com.example.laboratorium3;

public class PostepInfo
{
    public static final int POBIERANIE_OK=0;
    public static final int POBIERANIE_TRWA=1;
    public static final int POBIERANIE_BLAD=2;
    public int mPobranychBajtow;

    public int mRozmiar;
    public int mWynik;
    public PostepInfo()
    {
        mPobranychBajtow=0;
        mRozmiar=0;
        mWynik=POBIERANIE_TRWA;
    }
}
