package com.cobra.forensics.util;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Formatter;

/**
 *
 *Provides utility function to convert between data format types.
 */
public class DataConverter {

  public static byte[] base64ToBinary(String str) {
    Base64.Decoder decoder = Base64.getDecoder();
    return decoder.decode(str);
  }

  public static String binaryToBase64(byte[] bytes) {
    Base64.Encoder encoder = Base64.getEncoder();
    return encoder.encodeToString(bytes);
  }

  public static byte[] asciiToBinary(String str) {
    return str.getBytes(StandardCharsets.UTF_8);
  }

  public static String binaryToAscii(byte[] bytes) {
    return new String(bytes, StandardCharsets.UTF_8);
  }

  public static byte[] hexToBinary(String str) throws InvalidInputException {
    // generate an array of hex strings where each entry represent a byte
    String[] strings = genArrayofHex(str);
    return hexStringsToByteArray(strings);
  }

  public static byte[] longToBigEndianByteArray(long i, int size) {
    byte[] bytes = new byte[size];

    int index = 0;
    for (int shift = size -1; shift > 0 ;shift--){
      bytes[index++] = (byte) (i  >> (8 * shift));
    }
    bytes[index] = (byte) i;

    return bytes;
  }

  public static byte[] intToBigEndianByteArray(int i, int size) {
    byte[] bytes = new byte[size];

    int index = 0;
   for (int shift = size -1; shift > 0 ;shift--){
        bytes[index++] = (byte) (i  >> (8 * shift));
    }
    bytes[index] = (byte) i;

    return bytes;
  }

  public static int bigEndianByteArrayToInt(byte[] bytes) {

    int shift = (bytes.length - 1)  * 8;
    int value = 0;
    
    for (byte b : bytes){
      int tmp = Byte.toUnsignedInt(b);
      value |= (tmp << shift);
      shift -= 8;
    }
        
    return value;
  }

  public static long bigEndianByteArrayToLong(byte[] bytes) {

    int shift = (bytes.length - 1)  * 8;
    long value = 0;

    for (byte b : bytes){
      int tmp = Byte.toUnsignedInt(b);
      value |= (tmp << shift);
      shift -= 8;
    }

    return value;
  }

  public static byte[] gmtUnixTimeAsBytes() {
    int t = (int) (System.currentTimeMillis() / 1000L);
    return intToBigEndianByteArray(t,4);
  }


  public static String binaryToHexForDisplay(byte[] bytes) {
    StringBuilder buffer = new StringBuilder();
    String hexString = binaryToHex(bytes);

    for (int i = 0; i < hexString.length(); i++) {
      if (i != 0 && (i % 32 == 0)) {
        buffer.append(System.lineSeparator());
      }
      else if (i != 0 && (i % 2 == 0)) {
        buffer.append(' ');
      }
      buffer.append(hexString.charAt(i));
    }
    return buffer.toString();
  }

  public static String binaryToHex(byte[] bytes) {
    Formatter formatter = new Formatter();

    for (byte b : bytes) {
      formatter.format("%02x", b);
    }
    return formatter.toString();
  }

  public static String base64ToHex(String base64Str) {
    byte[] bytes = base64ToBinary(base64Str);
    return binaryToHex(bytes);
  }

  public static String base64ToHexForDisplay(String base64Str) {
    byte[] bytes = base64ToBinary(base64Str);
    return binaryToHexForDisplay(bytes);
  }

  public static String hexToBase64(String hex) throws Exception {
    byte[] bytes = hexToBinary(hex);
    return binaryToBase64(bytes);
  }

  /**
   * Helper method to extract an array of 1 or 2 hex digits (i.e a byte) from a string catering for
   * a flexible input representation, where the hex string can be a sequence of hex characters with
   * no space, or a sequence of multiple hex characters with space. Typically input should be hex
   * characters with no space or sequences of at most two hex characters separated by space. This
   * function will handle both types of inputs.
   *
   * @param string the hex string
   * @return array of hex values
   */
  private static String[] genArrayofHex(String string) {
    // First remove extra returns and spaces from the string
    StringBuilder buffer = new StringBuilder();

    boolean lastCharIsNotWhiteSpace = false;
    char ch;
    int charsInCurrentHex = 0;

    for (int i = 0; i < string.length(); i++) {
      ch = string.charAt(i);
      if (Character.isWhitespace(ch)) {
        if (lastCharIsNotWhiteSpace
          && (i != string.length() - 1)) {
          buffer.append(' ');
          charsInCurrentHex = 0;
        }
        lastCharIsNotWhiteSpace = false;
      }
      else {
        if (charsInCurrentHex == 2) {
          buffer.append(' ');
          charsInCurrentHex = 0;
        }
        buffer.append(ch);
        charsInCurrentHex++;
        lastCharIsNotWhiteSpace = true;
      }
    }

    return buffer.toString().split(" ");
  }

  /**
   * Converts an array of strings (with hex values) to an array of bytes.
   *
   * @param strings an array of strings
   * @return an array of bytes
   */
  private static byte[] hexStringsToByteArray(String[] strings)
    throws InvalidInputException {
    byte[] bytes = new byte[strings.length];

    for (int i = 0; i < strings.length; i++) {
      if (strings[i].length() < 1 || strings[i].length() > 2) {
        throw new InvalidInputException(
          "Invalid length of hex string. Expect one or two charcaters.");
      }
      bytes[i] = hexStringToByte(strings[i]);
    }

    return bytes;
  }

  /**
   * Converts a string with hex values to a byte.
   *
   * @param str a hex string
   * @return the byte value
   */
  private static byte hexStringToByte(String str) {
    int b;

    b = Integer.parseInt(str, 16);

    return (byte) b;
  }

  /**
   * Replace non-printable character with a period.
   *
   * @param message ascii string
   * @return modified "printable" string
   */
  public static String getPrintableAsciiOnly(String message) {

    StringBuilder buffer = new StringBuilder(message);
    for (int i = 0; i < buffer.length(); i++) {
      char ch = buffer.charAt(i);
      // replace non-printable characters, execpt LF (10) and CR(13).
      if ((ch < 32 || ch > 126) && ch != 10 && ch != 13) {
        buffer.setCharAt(i, '.');
      }
    }
    return buffer.toString();
  }
}
