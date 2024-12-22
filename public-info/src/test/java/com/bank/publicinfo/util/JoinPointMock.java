package com.bank.publicinfo.util;

import com.bank.publicinfo.util.enums.EntityTypeEnum;
import com.bank.publicinfo.util.enums.OperationTypeEnum;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

@Getter
public class JoinPointMock implements JoinPoint {
    private static final Long EMPTY_ARGS = -1L;
    private static final Long NOT_VALID_ARGS = -2L;
    private final Signature signature;
    private final Object[] args;

    private JoinPointMock(Signature signature, Object[] args) {
        this.signature = signature;
        this.args = args;
    }

    public String toShortString() {
        return "";
    }

    public String toLongString() {
        return "";
    }

    public Object getThis() {
        return null;
    }

    public Object getTarget() {
        return null;
    }

    public SourceLocation getSourceLocation() {
        return null;
    }

    public String getKind() {
        return "";
    }

    public JoinPoint.StaticPart getStaticPart() {
        return null;
    }

    public static JoinPointMock createJoinPointMock(EntityTypeEnum entityType, OperationTypeEnum operationType, Long id) {
        Signature signature1 = SignatureMock.createSignature(entityType, operationType);
        Object[] argsResult;
        if (EMPTY_ARGS.equals(id)) {
            argsResult = new Object[0];
        } else if (NOT_VALID_ARGS.equals(id)) {
            String notValid = "Not Valid";
            argsResult = new Object[]{notValid};
        } else {
            argsResult = new Object[]{id};
        }

        return new JoinPointMock(signature1, argsResult);
    }
}
