package me.philcali.aws.global.infrastructure.ssm;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;

import me.philcali.aws.global.infrastructure.api.RegionalAvailability;

@RunWith(MockitoJUnitRunner.class)
public class AWSRegionalAvailabiltiyTest {
    private RegionalAvailability availability;
    @Mock
    private AWSSimpleSystemsManagement ssm;

    @Before
    public void setUp() {
        availability = new AWSRegionalAvailabiltiy("us-east-1", "s3", ssm);
    }

    @Test
    public void testRegion() {
        assertEquals(availability.region(), new AWSRegion("us-east-1", ssm));
    }

    @Test
    public void testService() {
        assertEquals(availability.service(), new AWSService("s3", ssm));
    }
}
