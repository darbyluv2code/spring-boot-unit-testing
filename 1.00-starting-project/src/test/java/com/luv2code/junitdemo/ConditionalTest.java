package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

public class ConditionalTest {

    @Test
    @Disabled("Don't run until JIRA#123 is resolved")
    void basicTest(){
        //execute method and perform assets
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindowsOnly(){
        //execute method and perform assets
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testForLinuxOnly(){
        //execute method and perform assets
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testForMacOnly(){
        //execute method and perform assets
    }

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testOnlyForJava17(){
        //execute method and perform assets
    }

    @Test
    @EnabledOnJre(JRE.JAVA_13)
    void testOnlyForJava13(){
        //execute method and perform assets
    }
    
    @Test
    @EnabledForJreRange(min=JRE.JAVA_13, max=JRE.JAVA_18)
    void testOnlyForJavaRange(){
        //execute method and perform assets
    }

        
    @Test
    @EnabledForJreRange(min=JRE.JAVA_11)
    void testOnlyForJavaRangeMin(){
        //execute method and perform assets
    }

    @Test
    @EnabledIfSystemProperty(named="LUV2CODE_SYS_PROP",matches="CI_CD_DEPLOY")
    void testEnabledIfSystemProperties(){

    }

    
    @Test
    @EnabledIfEnvironmentVariable(named="LUV2DOCE_ENV", matches="DEV")
    void testEnabledIfEnviromentVariable(){
        
    }
}
