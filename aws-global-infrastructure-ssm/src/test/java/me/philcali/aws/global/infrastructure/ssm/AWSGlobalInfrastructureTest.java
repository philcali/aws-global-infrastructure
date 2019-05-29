package me.philcali.aws.global.infrastructure.ssm;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterType;

import me.philcali.aws.global.infrastructure.api.GlobalInfrastructure;
import me.philcali.aws.global.infrastructure.api.Region;
import me.philcali.aws.global.infrastructure.api.Service;
import me.philcali.aws.global.infrastructure.api.exception.RegionNotFoundException;
import me.philcali.aws.global.infrastructure.api.exception.ServiceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class AWSGlobalInfrastructureTest {
    @Mock
    private AWSSimpleSystemsManagement ssm;
    private GlobalInfrastructure infra;

    @Before
    public void setUp() {
        infra = new AWSGlobalInfrastructure(ssm);
    }

    @Test
    public void testRegion() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/regions/us-east-1")
                .withType(ParameterType.String)
                .withValue("us-east-1"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/regions/us-east-1")));
        Region region = infra.region("us-east-1");
        assertEquals("us-east-1", region.id());
    }

    @Test(expected = RegionNotFoundException.class)
    public void testRegionNotFoundException() {
        doThrow(ParameterNotFoundException.class).when(ssm).getParameter(any(GetParameterRequest.class));
        infra.region("bob-blart");
    }

    @Test
    public void testService() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/services/s3")
                .withType(ParameterType.String)
                .withValue("s3"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/services/s3")));
        Service s3 = infra.service("s3");
        assertEquals("s3", s3.id());
    }

    @Test(expected = ServiceNotFoundException.class)
    public void testServiceNotFoundException() {
        doThrow(ParameterNotFoundException.class).when(ssm).getParameter(any(GetParameterRequest.class));
        infra.service("charles-barkley");
    }
}
