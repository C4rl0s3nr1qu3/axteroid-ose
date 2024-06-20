package com.axteroid.ose.server.tools.exception;

public class DetailException{
  protected String message;
  
  public DetailException() {}
  
  public String getMessage(){
    return message;
  }
  
  public void setMessage(String value){
    message = value;
  }
  
  public String toString(){
    return "ExceptionDetail [message=" + message + "]";
  }
}
