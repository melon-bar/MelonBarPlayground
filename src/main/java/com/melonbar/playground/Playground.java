package com.melonbar.playground;

import com.melonbar.playground.module.DemoModule0;
import com.melonbar.playground.module.Module;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Playground {

    private static final Module demoModule0 = new DemoModule0();

    public static void main(String[] args) {
        demoModule0.run();
    }
}
