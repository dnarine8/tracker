package com.cobra.tracker.util;

//https://docs.oracle.com/javase/9/docs/specs/security/standard-names.html#messagedigest-algorithms

public class Constants {

    public static final String AES = "AES";
    public static final String AES_GCM_NOPADDING = "AES/GCM/NoPadding";
    public static final String SHA256 = "SHA256";

    public static final int GCM_IV_LENGTH = 96;
    public static final int GCM_TAG_LENGTH = 128;
    public static final int AES_256_IN_BYTES = 32;
    public static final int GCM_IV_LENGTH_IN_BYTES = 12;


}
