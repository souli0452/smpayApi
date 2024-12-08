package com.switchmaker.smpay.payout;
import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;

public class PayoutUrls {
	public static final String INITIATE_PAYOUT = ROOT_API+"/payout";
	public static final String INITIATE_MERCHANT_PAYOUT = ROOT_API+"/merchant/payout";
	public static final String GET_PAYOUT_STATUS = ROOT_API+"/payout/{code}";
}
