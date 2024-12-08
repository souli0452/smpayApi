package com.switchmaker.smpay.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CodeGenerator {

  public static String generate() {
    Random generator = ThreadLocalRandom.current();
    int randomCode = generator.nextInt(10000); // Génère un nombre aléatoire entre 0 et 9999
    String code = String.format("%04d", randomCode); // Formatage pour assurer 4 chiffres
    return code;
  }
}
