package com.cobra.tracker.util;

//https://docs.oracle.com/javase/9/docs/specs/security/standard-names.html#messagedigest-algorithms

public class Constants {

    public static final String AES = "AES";
    public static final String AES_GCM_NOPADDING = "AES/GCM/NoPadding";
    // Message Digest
    public static final String MD_MD5 = "MD5";
    public static final String MD_SHA1 = "SHA-1";
    public static final String MD_SHA256 = "SHA-256";
    public static final String MD_SHA384 = "SHA-384";
    public static final String MD_SHA512 = "SHA-512";


    public static final int GCM_IV_LENGTH = 96;
    public static final int GCM_TAG_LENGTH = 128;
    public static final int AES_256_IN_BYTES = 32;
    public static final int GCM_IV_LENGTH_IN_BYTES = 12;


}
