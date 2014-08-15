package ru.tetrasoft.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
 
public class MainActivity extends Activity {
 
    // URL Address
     
	ArrayList <String> listOfServers = new ArrayList<String>();
	
    final static String H1 = "http://mrtg:tetraroot@h1.tetra-soft.ru:8888/dashboard/";
    final static String H2 = "http://mrtg:tetraroot@h2.tetra-soft.ru:8888/dashboard/";
    final static String H3 = "http://mrtg:tetraroot@h3.tetra-soft.ru:8888/dashboard/";
    final static String H4 = "http://mrtg:tetraroot@h4.tetra-soft.ru:8888/dashboard/";
    final static String H5 = "http://mrtg:tetraroot@h5.tetra-soft.ru:8888/dashboard/";
    final static String H6 = "http://mrtg:tetraroot@h6.tetra-soft.ru:8888/dashboard/";
    final static String H7 = "http://dashboard:iemo2Ugo@ubur.gtionline.ru:8888";
    final static String H8 = "http://mrtg:tetraroot@novatek:8888/dashboard/";
    
    ProgressDialog mProgressDialog;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        // Locate the Buttons in activity_main.xml
        Button titlebutton = (Button) findViewById(R.id.titlebutton);

        // Capture button click
        titlebutton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // Execute Title AsyncTask
                new Title().execute();
            }
        });
    }
 
    // Title AsyncTask
    private class Title extends AsyncTask<Void, Void, Void> {
       List <String> title = new ArrayList<String>();
 
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Android Basic JSoup Tutorial");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
        	// response;
       // 	try {        
            	
               //Connection.Response loginForm = Jsoup.connect(H1).method(Connection.Method.GET).execute();
        	 Response response = Jsoup.connect("http://mrtg:tetraroot@h4.tetra-soft.ru:8888/dashboard/")
            		  .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            		  .method(Method.GET).response();
        	 
        	 Map<String, String> cookies = response.cookies();        	 
        	 Connection connection = Jsoup.connect("http://mrtg:tetraroot@h4.tetra-soft.ru:8888/dashboard/");

        	 for (Entry<String, String> cookie : cookies.entrySet()) 
        	     connection.cookie(cookie.getKey(), cookie.getValue());
        	 
        	 
        	 try {
				Document document = connection.get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       /*     } catch (IOException e) {
                e.printStackTrace();
                try {
					response = Jsoup.connect("http://h3.tetra-soft.ru:8888/dashboard/")
						    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
						    .data("username", "mrtg")
						    .data("password", "tetraroot")
						    .method(Method.POST).execute();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }*/
            return null;
            

               
            		//Map<String, String> cookies = response.cookies();
           
                // Connect to the web site
          /*      Document document = Jsoup.connect("http://h3.tetra-soft.ru:8888/dashboard/")
                		//.cookies(loginForm.cookies())
                		.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36")
                		.data("mrtg:tetraroot")
                	//	.data("password", "tetraroot")
                		.get();
                // Using Elements to get the class data
                Elements elem = document.getElementsByTag("a");*/
                // Locate the src attribute
                
              //  for (Element e : elem)
             //   	title.add(e.absUrl("href"));
                

        }
 
        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            ListView list = (ListView) findViewById(R.id.lvMain);
            
            // создаем адаптер
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, title);
            
            // присваиваем адаптер списку
            list.setAdapter(adapter);

            mProgressDialog.dismiss();
            
            
            
        }
    }
 }
