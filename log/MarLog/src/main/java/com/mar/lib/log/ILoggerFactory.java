package com.mar.lib.log;

import com.mar.lib.log.slf4j.Logger;
public interface ILoggerFactory {
    public Logger getLogger(String name);
}
