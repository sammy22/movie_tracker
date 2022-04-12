package com.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Encrypt {

    public static String hashPassword(String plainTextPassword){
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}

    public static Boolean checkPass(String plainPassword, String hashedPassword) {
		if (BCrypt.checkpw(plainPassword, hashedPassword))
			return true;
		return false;
	}
}
