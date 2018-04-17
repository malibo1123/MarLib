package com.mar.lib.log;

public interface Logger {
    public void trace(String msg);
    public void trace(String tag,String msg);
    public void debug(String msg);
    public void debug(String tag,String msg);
    public void info(String msg);
    public void info(String tag,String msg);
    public void warn(String msg);
    public void warn(String tag,String msg);
    public void warn(String tag,Throwable e);
    public void warn(String tag,Object... objects);
    public void error(String msg);
    public void error(String tag,String msg);
    public void error(String tag,Throwable e);
    public void error(String tag,Object... objects);
}
