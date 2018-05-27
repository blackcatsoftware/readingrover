package net.jdkr.readingrover;

import org.jimmutable.cloud.ApplicationId;
import org.jimmutable.cloud.CloudExecutionEnvironment;
import org.junit.BeforeClass;

import net.jdkr.readingrover.TypeNameRegister;

public abstract class StubTest
{
    @BeforeClass
    public static void setUp()
    {
        try
        {
            CloudExecutionEnvironment.startupStubTest(new ApplicationId("stub"));
        }
        catch (Exception e) {}
        
        TypeNameRegister.registerAllTypes();
    }
}
