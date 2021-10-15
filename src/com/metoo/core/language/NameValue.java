package com.metoo.core.language;

public class NameValue {
	
	 private StringBuilder sb = new StringBuilder();
	 
	    public void add(String name, Object value) {
	        sb.append("&");
	        sb.append(name);
	        sb.append("=");
	        sb.append(value);
	    }
	 
	    @Override
	    public String toString() {
	        return sb.toString();
	    }
}
