package com.switchmaker.smpay.security;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@Configuration
public class HttpsConfig {
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.requestFactory(() -> disableSSL())
				.build();
	}
	private HttpComponentsClientHttpRequestFactory disableSSL() {
		TrustStrategy acceptingTrustStrategy = new TrustStrategy() {

			@Override
			public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				return true;
			}
		};
		SSLContext sslContext = null;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		} catch (NoSuchAlgorithmException e) {
			// TODO: handle exception
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
		 CloseableHttpClient httpClient = HttpClients.custom()
                 .setSSLSocketFactory(csf)
                 .build();
		 HttpComponentsClientHttpRequestFactory requestFactory =
                 new HttpComponentsClientHttpRequestFactory();
		 requestFactory.setHttpClient(httpClient);

		 return requestFactory;
	}
}

