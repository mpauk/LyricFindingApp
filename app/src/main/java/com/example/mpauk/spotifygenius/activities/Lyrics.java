package com.example.mpauk.spotifygenius.activities;


import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mpauk on 6/13/2017.
 */

public class Lyrics extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... params) {
        String url = "https://www.google.com/search?q=%20genius%20lyrics";
        Scanner artistScanner = new Scanner(params[0]);
        while(artistScanner.hasNext()){
            url+="%20"+artistScanner.next();
        }
        Scanner trackScanner = new Scanner(params[1]);
        while(trackScanner.hasNext()){

            url+="%20"+trackScanner.next();
        }
        Document doc = null;
        try {
            doc = Jsoup.connect(url).ignoreHttpErrors(true).userAgent("Mozilla").get();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Elements link = doc.select("h3.r > a");
        String newUrl = link.attr("href");
        int endIndex=0;
        for(int i = 0;i<newUrl.length();i++){
            if(newUrl.charAt(i)=='&'){
                endIndex = i;
                break;
            }
        }
        newUrl = newUrl.substring(7,endIndex);
        Document doc2 = null;
        Log.d("URL",newUrl);
        try {
            doc2 = Jsoup.connect(newUrl).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements lyrics = doc2.select(".lyrics");
        String initialText = "";
        String finalText = "";
        initialText = lyrics.text();
        Scanner scan = new Scanner(initialText);
        int counter = 0;
        while(scan.hasNext()){
            String temp = scan.next();
            if(temp.charAt(0)=='['){
                finalText+="\n"+"\n";
                while(!temp.contains("]")){
                    finalText+= temp+" ";
                    temp = scan.next();
                }
                finalText+=temp+"\n";
                counter=0;
            }
            else if(counter<9){
                finalText+=temp+" ";
                counter++;
            }
            else{
                finalText+=temp+"\n";
                counter=0;
            }

        }
        return finalText;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}