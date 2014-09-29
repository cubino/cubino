package ru.tetrasoft.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.tetrasoft.support.logix.Gbox;
import ru.tetrasoft.support.logix.Module;
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
	
    final static String H1 = "http://h1.tetra-soft.ru:8888/dashboard/";
    final static String H2 = "http://h2.tetra-soft.ru:8888/dashboard/";
    final static String H3 = "http://h3.tetra-soft.ru:8888/dashboard/";
    final static String H4 = "http://h4.tetra-soft.ru:8888/dashboard/";
    final static String H5 = "http://h5.tetra-soft.ru:8888/dashboard/";
    final static String H6 = "http://h6.tetra-soft.ru:8888/dashboard/";
    final static String H7 = "http://dashboard:iemo2Ugo@ubur.gtionline.ru:8888";
    final static String H8 = "http://novatek:8888/dashboard/";
	
    final static String RED = "fe1a00";
    final static String GREEN = "77d42a";
    final static String PINK = "fa665a";
    
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
    	ArrayList <Gbox> boxs = new ArrayList <Gbox>();
 
        
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
        	
    		String base64login = new String(Base64.encodeBase64("mrtg:tetraroot".getBytes()));
    		Document firstHetz = null;

			try {
				firstHetz = getDom (H1, base64login);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	
    		int status;
    		
    		for (Element el :firstHetz.getElementsByTag("tr")){
    			Gbox newGbox = new Gbox();
    			for (Element elChild : el.children()){
    				for (Element elCh : elChild.getAllElements()){
    					String tag = elCh.tag().getName(); 
    					if (tag.equals("td")){
    						if (elCh.ownText().equals("Database")){
    							newGbox.addDatabase(parseModule(elCh));
    						}
    						else if (elCh.ownText().equals("Reporting Share")){
    							newGbox.addReportingShare(parseModule(elCh));
    						}
    						else if (elCh.ownText().equals("Etc")){
    							newGbox.addEtc(parseModule(elCh));
    						}
    						else if (elCh.ownText().startsWith("Camera")){
    							newGbox.addCamera(parseModule(elCh));
    						}
    					}
    					else if (tag.equals("a")){
    						if (elCh.attr("href").endsWith(".conf")){ //тут ссылка на конфигурацию
    							newGbox.setName(elCh.ownText());
    							newGbox.setHrefConf(elCh.attr("href"));
    						}
    						else if (elCh.attr("href").endsWith("vpn.log")){
    							newGbox.setHrefVPN(elCh.attr("href"));
    						}
    					}
    					else if (tag.equals("span")){
    						if (elCh.ownText().startsWith("eth")){
    							newGbox.setNetworkAddress(elCh.ownText());
    						}
    					}
    					
    					else if (tag.equals("button")){
    						status = convertClassToId (elCh.attr("class"));
    						for (Element gboxInfo : elCh.children()){
    							if (gboxInfo.attr("style").equals("font-weight:bold;")){
    								String [] parts = gboxInfo.text().split("-");
    								newGbox.setNumber(parts[1]);
    								newGbox.setStatus(status);
    							}
    						}
    					}
    				}
    			}
    			boxs.add(newGbox);
    		}
			return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
        	List <String> title = new ArrayList<String>();
        	for (Gbox box :boxs){
        		title.add(box.getName());
        	}
            ListView list = (ListView) findViewById(R.id.lvMain);
            // создаем адаптер
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, title);
            
            // присваиваем адаптер списку
            list.setAdapter(adapter);
            
            mProgressDialog.dismiss();
        }
    }
    
	public static Module parseModule (Element elCh){
		Module module = new Module();
		if (elCh.attr("style").endsWith(RED))
			module.setStatus(Module.EPICFAIL);
		else if (elCh.attr("style").endsWith(GREEN))
			module.setStatus(Module.OK);
		else if (elCh.attr("style").endsWith(PINK))
			module.setStatus(Module.FAIL);
		else 
			module.setStatus(Module.UNKNOWN);
		for (Element databaseElement : elCh.getAllElements()){
			if (databaseElement.tag().getName().equals("span")){
				String ip = databaseElement.ownText();
				ip = ip.replaceAll("(\\(.*\\) )|( \\(.*\\))", "");
				module.setIp(ip);
			}
			else if (databaseElement.tag().getName().equals("a")){
				
				if (databaseElement.ownText().matches("(^No Route To Host)|(^\\d.*)")){
					module.setPing(databaseElement.ownText());
				}
				else if (!databaseElement.ownText().equals(""))
					module.setInformation(databaseElement.ownText());
			}
		}
		return module;
	}
	
	public static Document getDom (String site, String login) throws IOException{
		return Jsoup.connect(site).header("Authorization", "Basic " + login).get();
	}
	
	public static Elements getElements (Document doc, String key, String value) throws IOException{	
		return doc.getElementsByAttributeValue(key, value);
	}
	
	public static int convertClassToId (String atr){

		if (atr.equalsIgnoreCase("yellow")) return 1;
		else if (atr.equalsIgnoreCase("red")) return 2;
		else if (atr.equalsIgnoreCase("green")) return 3;
		else return 0;
	}
		
	public static String getHTTP(String site, String login){
		URL url;
		BufferedReader rd = null;
		String result = "";
		try {
			url = new URL(site);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "Basic " + login);
        conn.setRequestMethod("GET");
        String line;
        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         while ((line = rd.readLine()) != null) {
            result += line;
            result += "\n";
         }
		} catch (MalformedURLException e) {
			
		} catch (IOException e) {
		} finally {
			 try {
				if (rd != null)
				rd.close();
			} catch (IOException e) {}
		}
		return result;
	}
 }
