package com.melonbar.playground.module;

public abstract class AbstractModule implements Module {

    protected AbstractModule() {
        init();
    }

    abstract void init();
}
