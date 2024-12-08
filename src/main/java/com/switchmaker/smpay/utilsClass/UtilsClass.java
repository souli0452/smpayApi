package com.switchmaker.smpay.utilsClass;

import com.switchmaker.smpay.dto.ResponseMessage;
import com.switchmaker.smpay.dto.ValidPaymentInformations;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class UtilsClass {


	/*-----------------------------------------------------------------------------/
	/*          Methode pour calculer le nouveau solde d'une application          /
	/*--------------------------------------------------------------------------*/

	public static int newBalanceOfPlatform(ValidPaymentInformations validPaymentInformations) {
		float number = validPaymentInformations.getPlatform().getBalance()+ (validPaymentInformations.getPayoutInformation().getAmountPayment() - validPaymentInformations.getPayoutInformation().getAmountPayment() * validPaymentInformations.getRateApplied() /100);
		int result = (int) Math.floor(number);
		return result;
	}



	/*-----------------------------------------------------------------------/
	/*          Methode pour couper la partie décimale d'un nombre          /
	/*--------------------------------------------------------------------*/

	public static int entier(float number) {
		int result = (int) Math.floor(number);
		return result;
	}


	/*------------------------------------------------------/
	/*         Methode pour calculer les commissions       /
	/*---------------------------------------------------*/

	public static int commission(int amount, float rate) {
		int result = (int) Math.round((amount*rate)/100);
		return result;
	}


	/*------------------------------------------------------------/
	/*          Methode pour gérer l'évolution du solde          /
	/*---------------------------------------------------------*/

	public static void solde(ArrayList<Double> solde, double montant) {
		if(solde.get(0)==0.0) {
      	  solde.remove(0);
      	  solde.add(0,montant);
        };
        if(solde.size() > 1) {
      	  for(int i=1 ; i < solde.size(); i++) {
      		  if(solde.get(i)==0.0) {
		        	  solde.remove(i);
		        	  solde.add(i,solde.get(i-1));
		          };
	          }
        }
	}



	/*------------------------------------/
	/*          Gestion erreurs          /
	/*---------------------------------*/

	public static ResponseMessage responseMessage(String code, String message) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setCode(code);
		responseMessage.setMessage(message);
		return responseMessage;
	}


	/*-------------------------------------------/
	/*           Generation des codes           /
	/*----------------------------------------*/

	public static String codeGenerate() {
		UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        String[] strings = randomUUIDString.split("-");
        String code = strings[0]+strings[1]+strings[2]+strings[3]+strings[4];
		return code;

	}



	/*--------------------------------/
	/*         Date format           /
	/*-----------------------------*/

	public static LocalDate dateConvert(Date date) {
		SimpleDateFormat formater = null;
		formater = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(formater.format(date));
		return localDate;

	}


	/*------------------------------------/
	/*        Méthode de hachage         /
	/*---------------------------------*/


	public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

