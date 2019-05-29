package me.philcali.aws.global.infrastructure.ssm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import java.util.Objects;
import java.util.Optional;

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

import me.philcali.aws.global.infrastructure.api.RegionalAvailability;
import me.philcali.aws.global.infrastructure.api.Service;

@RunWith(MockitoJUnitRunner.class)
public class AWSServiceTest {
    @Mock
    private AWSSimpleSystemsManagement ssm;
    private Service s3;

    @Before
    public void setUp() {
        s3 = new AWSService("s3", ssm);
    }

    @Test
    public void testLongName() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/services/s3/longName")
                .withType(ParameterType.String)
                .withValue("Amazon Simple Storage Service"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/services/s3/longName")));
        assertEquals("Amazon Simple Storage Service", s3.longName());
    }

    @Test
    public void testRegion() {
        GetParameterResult result = new GetParameterResult();
        result.withParameter(new Parameter()
                .withName("/aws/service/global-infrastructure/services/s3/regions/us-west-2")
                .withType(ParameterType.String)
                .withValue("us-west-2"));
        doReturn(result).when(ssm).getParameter(eq(new GetParameterRequest()
                .withName("/aws/service/global-infrastructure/services/s3/regions/us-west-2")));
        final Optional<RegionalAvailability> availability = s3.region("us-west-2");
        assertTrue(availability.isPresent());
    }

    @Test
    public void testId() {
        assertEquals("s3", s3.id());
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash("s3"), s3.hashCode());
    }

    @Test
    public void testEquals() {
        assertNotEquals(s3, null);
        assertNotEquals(s3, "blue");
        assertNotEquals(s3, new AWSService("sqs", ssm));
        assertEquals(s3, new AWSService("s3", ssm));
    }
}
