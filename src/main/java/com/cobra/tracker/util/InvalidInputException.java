package com.cobra.tracker.util;

import com.cobra.tracker.util.CobraException;

/**
 * This class is used to capture failures related to invalid input.
 */
public class InvalidInputException extends CobraException {
  public InvalidInputException(String str){
    super(str);
  }
}
