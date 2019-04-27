package me.philcali.aws.global.infrastructure.ssm;

import java.util.Arrays;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.StringJoiner;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;

public interface ParameterMixin {
    default Optional<String> parameter(final String name, final AWSSimpleSystemsManagement ssm) {
        try {
            final Parameter parameter = ssm.getParameter(new GetParameterRequest().withName(name)).getParameter();
            return Optional.ofNullable(parameter).map(Parameter::getValue);
        } catch (ParameterNotFoundException nfe) {
            return Optional.empty();
        }
    }

    default Stream<Parameter> stream(final String namespace, final AWSSimpleSystemsManagement ssm) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                new ParameterIterator(namespace, ssm), Spliterator.NONNULL), false);
    }

    default String namespace(final String...parts) {
        return Arrays.stream(parts).reduce(new StringJoiner("/").add(Constants.VENDOR_NAMESPACE),
                (left, right) -> left.add(right),
                (left, right) -> left).toString();
    }
}
