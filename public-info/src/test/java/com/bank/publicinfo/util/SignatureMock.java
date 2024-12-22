package com.bank.publicinfo.util;

import com.bank.publicinfo.util.enums.EntityTypeEnum;
import com.bank.publicinfo.util.enums.OperationTypeEnum;
import lombok.Getter;
import org.aspectj.lang.Signature;

@Getter
public class SignatureMock implements Signature {
    private static final String ADD_BANK_DETAILS = "addBankDetails";
    private static final String UPDATE_BANK_DETAILS = "updateBankDetails";
    private static final String ADD_ATM = "addAtm";
    private static final String UPDATE_ATM = "updateAtm";
    private static final String ADD_BRANCH = "addBranch";
    private static final String UPDATE_BRANCH = "updateBranch";
    private static final String ADD_CERTIFICATE = "addCertificate";
    private static final String UPDATE_CERTIFICATE = "updateCertificate";
    private static final String ADD_LICENSE = "addLicense";
    private static final String UPDATE_LICENSE = "updateLicense";
    private static final String UNSUPPORTED_OPERATION = "UnsupportedOperationBankDetails";
    private static final String UNSUPPORTED_ENTITY = "addNotExistEntity";
    private final String name;

    private SignatureMock(String name) {
        this.name = name;
    }

    public String toShortString() {
        return "";
    }

    public String toLongString() {
        return "";
    }

    public int getModifiers() {
        return 0;
    }

    public Class getDeclaringType() {
        return null;
    }

    public String getDeclaringTypeName() {
        return "";
    }

    public static Signature createSignature(EntityTypeEnum entityType, OperationTypeEnum operationType) {
        if (EntityTypeEnum.UNSUPPORTED_ENTITY.equals(entityType)) {
            return new SignatureMock("addNotExistEntity");
        } else if (OperationTypeEnum.UNSUPPORTED_OPERATION.equals(operationType)) {
            return new SignatureMock("UnsupportedOperationBankDetails");
        } else {
            String name;
            switch (entityType) {
                case BANK_DETAILS:
                    if (OperationTypeEnum.CREATE.equals(operationType)) {
                        name = "addBankDetails";
                    } else {
                        name = "updateBankDetails";
                    }
                    break;
                case ATM:
                    if (OperationTypeEnum.CREATE.equals(operationType)) {
                        name = "addAtm";
                    } else {
                        name = "updateAtm";
                    }
                    break;
                case BRANCH:
                    if (OperationTypeEnum.CREATE.equals(operationType)) {
                        name = "addBranch";
                    } else {
                        name = "updateBranch";
                    }
                    break;
                case CERTIFICATE:
                    if (OperationTypeEnum.CREATE.equals(operationType)) {
                        name = "addCertificate";
                    } else {
                        name = "updateCertificate";
                    }
                    break;
                case LICENSE:
                    if (OperationTypeEnum.CREATE.equals(operationType)) {
                        name = "addLicense";
                    } else {
                        name = "updateLicense";
                    }
                    break;
                default:
                    throw new RuntimeException("Ошибка при создании заглушки Signature");
            }

            return new SignatureMock(name);
        }
    }
}
