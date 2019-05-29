package me.philcali.aws.global.infrastructure.ssm;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterType;

import me.philcali.aws.global.infrastructure.api.Reachability;

@RunWith(MockitoJUnitRunner.class)
public class AWSReachabilityTest {
    private Reachability reachability;
    @Mock
    private AWSSimpleSystemsManagement ssm;

    @Before
    public void setUp() {
        reachability = new AWSReachability("us-east-1", "s3", ssm);
    }

    @Test
    public void testEndpoint() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/regions/us-east-1/services/s3/endpoint")
                .withType(ParameterType.String)
                .withValue("s3.amazonaws.com"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/regions/us-east-1/services/s3/endpoint")));
        assertEquals("s3.amazonaws.com", reachability.endpoint());
    }

    @Test
    public void testProtocols() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/regions/us-east-1/services/s3/protocols")
                .withType(ParameterType.StringList)
                .withValue("HTTP,HTTPS"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/regions/us-east-1/services/s3/protocols")));
        assertEquals(Arrays.asList("HTTP", "HTTPS"), reachability.protocols());
    }
}
