package com.metoo.module.app.test.httpclient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class TrustAnyHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		// TODO Auto-generated method stub
		return true;
	}
	

}
