package me.philcali.aws.global.infrastructure.ssm;

import java.util.Iterator;
import java.util.Objects;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;

public class ParameterIterator implements Iterator<Parameter> {
    private static final int MAX_RESULTS = 10;
    private final AWSSimpleSystemsManagement ssm;
    private GetParametersByPathRequest request;
    private Iterator<Parameter> currentPage;

    public ParameterIterator(final String namespace, final AWSSimpleSystemsManagement ssm) {
        this.ssm = ssm;
        this.request = new GetParametersByPathRequest()
                .withPath(namespace + "/")
                .withMaxResults(MAX_RESULTS)
                .withWithDecryption(false)
                .withRecursive(false);
    }

    private void fillPage() {
        final GetParametersByPathResult result = ssm.getParametersByPath(request);
        request.setNextToken(result.getNextToken());
        currentPage = result.getParameters().iterator();
    }

    @Override
    public boolean hasNext() {
        if (Objects.isNull(currentPage) || (!currentPage.hasNext() && Objects.nonNull(request.getNextToken()))) {
            fillPage();
        }
        return currentPage.hasNext();
    }

    @Override
    public Parameter next() {
        return currentPage.next();
    }
}
