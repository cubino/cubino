package ru.tetrasoft.support.logix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Gbox {
	
	private String number = "0";
	private String name;
	private int status;
	private String hrefConf;
	private String hrefVPN;
	private Map<String, String> networkAddress = new TreeMap<String, String>();
	private Module db;
	private Module reportShare;
	private Module etc;
	private ArrayList <Module> cameras;
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public void setNetworkAddress(String address) {
		String [] network = address.split(" ");		
		for (int i = 1; i < network.length; i += 2){
			networkAddress.put(network[i-1], network[i]);
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getHrefConf() {
		return hrefConf;
	}

	public void setHrefConf(String href) {
		this.hrefConf = href;
	}
	
	public String getHrefVPN() {
		return hrefVPN;
	}

	public void setHrefVPN(String href) {
		this.hrefVPN = href;
	}
	
	public void addDatabase(Module db){
		this.db = db;
	}
	
	public Module getDatabase(){
		return db;
	}
	
	public boolean haveDb(){
		return (db != null);
	}

	public void addReportingShare (Module reportShare){
		this.reportShare = reportShare;
	}
	
	public Module getReportingShare(){
		return reportShare;
	}
	
	public boolean haveReportingShare(){
		return (reportShare != null);
	}
	
	public void addEtc (Module etc){
		this.etc = etc;
	}
	
	public Module getEtc(){
		return etc;
	}
	
	public boolean haveEtc(){
		return (etc != null);
	}
	
	public void addCamera (Module camera){
		if (cameras == null)
			cameras = new ArrayList<Module>();
		cameras.add(camera);		
	}
	
	public List <Module> getCameras(){
		return cameras;
	}
	
	public boolean haveCamera(){
		return !cameras.isEmpty();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Gbox " + number);
		sb.append("." + name);
		sb.append("\nNetwork:\n");
		for(Entry<String, String> entry : networkAddress.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + " " + value + "\n");
		}
			
		sb.append("Статус = " + status);
		sb.append("\nhrefConf = " + hrefConf);
		sb.append("\nhrefVPN = " + hrefVPN);
		sb.append("\nhave Db = " + haveDb() + "\n");
		
		if (haveDb()){
			sb.append(db);
		}
		
		sb.append("\nhave Reporting Share = " + haveReportingShare() + "\n");
		
		if (haveReportingShare()){
			sb.append(reportShare);
		}
		
		sb.append("\nhave Etc = " + haveEtc() + "\n");
		
		if (haveEtc()){
			sb.append(etc);
		}
		
		sb.append("\nhave Camera = " + haveCamera() + "\n");
		
		if (haveCamera()){
			for (int i = 0; i < cameras.size(); i++){
			sb.append("Camera #");	
			sb.append(i);	
			sb.append("\n");
			sb.append(cameras.get(i));
			}
		}
		
		return sb.toString();
	}
	
	
}
