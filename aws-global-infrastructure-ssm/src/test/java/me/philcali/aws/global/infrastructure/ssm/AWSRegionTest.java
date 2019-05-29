package me.philcali.aws.global.infrastructure.ssm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import java.util.Objects;

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

import me.philcali.aws.global.infrastructure.api.Region;

@RunWith(MockitoJUnitRunner.class)
public class AWSRegionTest {
    private Region region;
    @Mock
    private AWSSimpleSystemsManagement ssm;

    @Before
    public void setUp() {
        region = new AWSRegion("us-west-2", ssm);
    }

    @Test
    public void testId() {
        assertEquals("us-west-2", region.id());
    }

    @Test
    public void testLongName() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/regions/us-west-2/longName")
                .withType(ParameterType.String)
                .withValue("US N. Virginia"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/regions/us-west-2/longName")));
        assertEquals("US N. Virginia", region.longName());
    }

    @Test
    public void testDomain() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/regions/us-west-2/domain")
                .withType(ParameterType.String)
                .withValue("amazonaws.com"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/regions/us-west-2/domain")));
        assertEquals("amazonaws.com", region.domain());
    }

    @Test
    public void testGeolocationCountry() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/regions/us-west-2/geolocationCountry")
                .withType(ParameterType.String)
                .withValue("US"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/regions/us-west-2/geolocationCountry")));
        assertEquals("US", region.geolocationCountry());
    }

    @Test
    public void testGeolocationRegion() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/regions/us-west-2/geolocationRegion")
                .withType(ParameterType.String)
                .withValue("US-VA"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/regions/us-west-2/geolocationRegion")));
        assertEquals("US-VA", region.geolocationRegion());
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash("us-west-2"), region.hashCode());
    }

    @Test
    public void testEquals() {
        assertNotEquals(region, null);
        assertNotEquals(region, "blue42");
        assertNotEquals(region, new AWSRegion("us-east-1", ssm));
        assertEquals(region, new AWSRegion("us-west-2", ssm));
    }
}
