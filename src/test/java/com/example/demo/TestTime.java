package com.example.demo;

import org.junit.Test;

import java.time.Duration;

/**
 * Created by zm on 2019/9/1.
 */
public class TestTime {

    @Test
    public void testParse(){
        System.out.println(Duration.parse("PT15M").toMinutes());
    }
}
