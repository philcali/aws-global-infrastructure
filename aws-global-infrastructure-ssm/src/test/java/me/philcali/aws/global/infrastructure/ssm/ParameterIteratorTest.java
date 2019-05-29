package me.philcali.aws.global.infrastructure.ssm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterType;

@RunWith(MockitoJUnitRunner.class)
public class ParameterIteratorTest {
    private ParameterIterator iterator;
    @Mock
    private AWSSimpleSystemsManagement ssm;

    @Before
    public void setUp() {
        iterator = new ParameterIterator("/aws/service/global-infrastructure/regions", ssm);
    }

    @Test
    public void testIterator() {
        GetParametersByPathRequest req = new GetParametersByPathRequest()
                .withPath("/aws/service/global-infrastructure/regions/")
                .withMaxResults(10)
                .withRecursive(false)
                .withWithDecryption(false);
        GetParametersByPathRequest req2 = new GetParametersByPathRequest()
                .withPath("/aws/service/global-infrastructure/regions/")
                .withNextToken("abc-123")
                .withMaxResults(10)
                .withRecursive(false)
                .withWithDecryption(false);
        GetParametersByPathResult firstPage = new GetParametersByPathResult()
                .withNextToken("abc-123")
                .withParameters(Arrays.asList(
                        new Parameter()
                        .withName("/aws)/service/global-infrastructure/regions/us-east-1")
                        .withValue("us-east-1")
                        .withType(ParameterType.String),
                        new Parameter()
                        .withName("/aws)/service/global-infrastructure/regions/us-east-2")
                        .withValue("us-east-2")
                        .withType(ParameterType.String)));
        GetParametersByPathResult secondPage = new GetParametersByPathResult()
                .withParameters(Arrays.asList(
                        new Parameter()
                        .withName("/aws)/service/global-infrastructure/regions/us-west-1")
                        .withValue("us-west-1")
                        .withType(ParameterType.String),
                        new Parameter()
                        .withName("/aws)/service/global-infrastructure/regions/us-west-2")
                        .withValue("us-west-2")
                        .withType(ParameterType.String)));
        doReturn(firstPage).when(ssm).getParametersByPath(eq(req));
        doReturn(secondPage).when(ssm).getParametersByPath(eq(req2));
        assertTrue(iterator.hasNext());
        assertEquals("us-east-1", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("us-east-2", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("us-west-1", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("us-west-2", iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }
}
