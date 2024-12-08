package com.switchmaker.smpay.security;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class NoopHostnameVerifier implements HostnameVerifier {
	public static final NoopHostnameVerifier INSTANCE = new NoopHostnameVerifier();

	@Override
	public boolean verify(final String s, final SSLSession session) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public final String toString () {
		// TODO Auto-generated method stub
		return "NO_OP";
	}

}
