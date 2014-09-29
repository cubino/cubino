package ru.tetrasoft.support.logix;

import java.util.LinkedList;
import java.util.List;

public class Module {
	
	public final static int UNKNOWN = 0;
	public final static int OK = 1;
	public final static int FAIL = 2;
	public final static int EPICFAIL = 3;
	
	//private long timeTogleState = 0;
	private String ip = "UNKNOWN";
	private String ping = "UNKNOWN";
	private Integer state = UNKNOWN;
	private List <String> moreInformation = new LinkedList<String>();
	
	
	public String getPing() {
		return ping;
	}

	public void setPing(String ping) {
		this.ping = ping;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	
	public void setStatus (int status){
		switch (status) {
		case UNKNOWN: state = UNKNOWN;	break;
		case OK: state = OK;	break;
		case FAIL: state = FAIL;	break;
		case EPICFAIL: state = EPICFAIL;	break;
		default: state = UNKNOWN;
		}
	}
	
	public int getStatus (){
		return state;
	}

	public List<String> getInformation() {
		return moreInformation;
	}

	public void setInformation(String info) {
		moreInformation.add(info);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		addLine(sb, "ip", ip);
		addLine(sb, "state", state.toString());
		addLine(sb, "ping", ping);
		if (!moreInformation.isEmpty()){
			sb.append("info: \n");
			for (String s : moreInformation){
				sb.append(s);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	private void addLine (StringBuilder sb, String name, String value){
		sb.append(name);
		sb.append(" = ");
		sb.append(value);
		sb.append("\n");
	}
}
