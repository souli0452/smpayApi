package com.switchmaker.smpay.constant.urls;
import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;

public class PaymentUrls {
	public static final String GET_TRANSACTION = ROOT_API+"/transaction/{id}";
	public static final String GET_PLATFORM_TRANSACTIONS = ROOT_API+"/transactions/platform/{platformId}/limit/{limit}/offset/{offset}";
	public static final String GET_CLIENT_TRANSACTIONS = ROOT_API+"/transactions/client/{clientId}/limit/{limit}/offset/{offset}";
	public static final String GET_PLATFORM_PERIOD_TRANSACTIONS = ROOT_API+"/transactions/period/platform/{platformId}/debut/{debut}/fin/{fin}";
	public static final String GET_CLIENT_PERIOD_TRANSACTIONS = ROOT_API+"/transactions/period/client/{clientId}/debut/{debut}/fin/{fin}";
	public static final String GET_PERIOD_TRANSACTIONS_BY_STATUS = ROOT_API+"/transactions/status/{status}/period/debut/{debut}/fin/{fin}";
	public static final String GET_TRANSACTIONS = ROOT_API+"/all/transactions/status/{status}/limit/{limit}/offset/{offset}";
	public static final String GET_AMOUNT_TRANSACTION_PLATFORM = ROOT_API+"/amount/transactions";
	public static final String CLIENT_TRANSACTION_VERSEMENT_PER_MONTH = ROOT_API+"/transaction/month/client/{id}";
	public static final String CLIENT_SOLDE_VERSEMENT_PER_MONTH = ROOT_API+"/solde_versement/month/client/{id}/{value}";
	public static final String PLATFORM_TRANSACTION_VERSEMENT_SOLDE_PER_MONTH = ROOT_API+"/transaction/month/client/{client}/platform/{platform}/{value}";
	public static final String PLATFORM_TRANSACTION_PER_MONTH = ROOT_API+"/trans/month/client/{client}/platform/{platform}/{value}";
	public static final String PLATFORM_VOLUME_PER_MONTH = ROOT_API+"/volume/month/client/{client}/platform/{platform}/{value}";
	public static final String PLATFORM_VERSEMENT_PER_MONTH = ROOT_API+"/versement/month/client/{client}/platform/{platform}/{value}";
	public static final String PLATFORM_VERSEMENT_SOLDE_PER_MONTH = ROOT_API+"/solde/versement/month/client/{client}/platform/{platform}/{value}";
	public static final String TRANSACTION_VOLUME_PER_MONTH = ROOT_API+"/transaction_volume";
	public static final String CLIENT_TRANSACTION_PER_MONTH = ROOT_API+"/trans/month/client/{id}/{value}";
	public static final String ADMIN_VIEW_TRANSACTION_VOLUME_PER_MONTH = ROOT_API+"/transaction/volume/month/year/{value}";
	public static final String ADMIN_VIEW_TRANSACTION_PER_MONTH = ROOT_API+"/trans/month/year/{value}";
	public static final String ADMIN_VIEW_VOLUME_PER_MONTH = ROOT_API+"/volume/month/year/{value}";
	public static final String CLIENT_SOLDE_PER_MONTH = ROOT_API+"/solde/month/client/{id}/{value}";
	public static final String CLIENT_VERSEMENT_PER_MONTH = ROOT_API+"/versement/month/client/{id}/{value}";
}
