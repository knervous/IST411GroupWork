/**
 * 
 *
 * Generated by <a href="http://enunciate.webcohesion.com">Enunciate</a>.
 */
package com.google.gson;

/**
 * (no documentation provided)
 */
public class JsonNull extends com.google.gson.JsonElement {

  protected JsonNull() {
  }

  /**
   * Create an instance of JsonNull from JSON text.
   */
  public static native JsonNull fromJson(String json) /*-{
    return eval('(' + json + ')');
  }-*/;
}
