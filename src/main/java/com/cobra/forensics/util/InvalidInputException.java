package com.cobra.forensics.util;

/**
 * This class is used to capture failures related to invalid input.
 */
public class InvalidInputException extends CobraException {
  public InvalidInputException(String str){
    super(str);
  }
}
