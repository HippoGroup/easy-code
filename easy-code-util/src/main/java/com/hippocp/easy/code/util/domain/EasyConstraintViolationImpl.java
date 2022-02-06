package com.hippocp.easy.code.util.domain;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.ValidationException;
import javax.validation.metadata.ConstraintDescriptor;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 一个自定义的{@link ConstraintViolation}，它允许获取有关违反约束的附加信息。<br>
 * 注意：仅 message、argName、value 会被校验器赋值，其它值都为null<br>
 * 注意：以上三个参数在自定义校验器中将由调用者负责赋值
 *
 * @author ZhouYifan
 * @date 2022/1/11
 */
public class EasyConstraintViolationImpl<T> implements ConstraintViolation<T>, Serializable {

    /**
     * 该属性会被校验器赋值，但在自定义校验器中将由调用者负责赋值
     */
    private final String message;

    private String messageTemplate;

    private T rootBean;

    private Class<T> rootBeanClass;

    private Object leafBeanInstance;

    private Object[] executableParameters;

    private Object executableReturnValue;

    private Path propertyPath;

    /**
     * 该属性会被校验器赋值，但在自定义校验器中将由调用者负责赋值
     */
    private final String argName;

    /**
     * 该属性会被校验器赋值，但在自定义校验器中将由调用者负责赋值
     */
    private final Object value;

    private ConstraintDescriptor<?> constraintDescriptor;


    /**
     * 构建{@link EasyConstraintViolationImpl}
     *
     * @param message 约束违反信息
     * @param argName 约束违反参数名
     * @param value   约束违反参数值
     * @param <T>     JavaBean
     * @return {@link EasyConstraintViolationImpl}
     */
    public static <T> EasyConstraintViolationImpl<T> forBeanValidation(String message,
                                                                       String argName,
                                                                       Object value) {
        return new EasyConstraintViolationImpl<>(message,
                argName,
                value);
    }


    public static <T> EasyConstraintViolationImpl<T> forBeanValidation(String message,
                                                                       String messageTemplate,
                                                                       T rootBean,
                                                                       Class<T> rootBeanClass,
                                                                       Object leafBeanInstance,
                                                                       Object[] executableParameters,
                                                                       Object executableReturnValue,
                                                                       Path propertyPath,
                                                                       String argName,
                                                                       Object value,
                                                                       ConstraintDescriptor<?> constraintDescriptor) {
        return new EasyConstraintViolationImpl<>(
                message,
                messageTemplate,
                rootBean,
                rootBeanClass,
                leafBeanInstance,
                executableParameters,
                executableReturnValue,
                propertyPath,
                argName,
                value,
                constraintDescriptor);
    }


    public EasyConstraintViolationImpl(String message, String argName, Object value) {
        this.message = message;
        this.argName = argName;
        this.value = value;
    }

    public EasyConstraintViolationImpl(String message, String messageTemplate, T rootBean, Class<T> rootBeanClass, Object leafBeanInstance, Object[] executableParameters, Object executableReturnValue, Path propertyPath, String argName, Object value, ConstraintDescriptor<?> constraintDescriptor) {
        this.message = message;
        this.messageTemplate = messageTemplate;
        this.rootBean = rootBean;
        this.rootBeanClass = rootBeanClass;
        this.leafBeanInstance = leafBeanInstance;
        this.executableParameters = executableParameters;
        this.executableReturnValue = executableReturnValue;
        this.propertyPath = propertyPath;
        this.argName = argName;
        this.value = value;
        this.constraintDescriptor = constraintDescriptor;
    }

    public String getArgName() {
        return argName;
    }

    @Override
    public final String getMessage() {
        return message;
    }

    @Override
    public final String getMessageTemplate() {
        return messageTemplate;
    }

    @Override
    public final T getRootBean() {
        return rootBean;
    }

    @Override
    public final Class<T> getRootBeanClass() {
        return rootBeanClass;
    }

    @Override
    public final Object getLeafBean() {
        return leafBeanInstance;
    }

    @Override
    public final Object getInvalidValue() {
        return value;
    }

    @Override
    public final Path getPropertyPath() {
        return propertyPath;
    }

    @Override
    public final ConstraintDescriptor<?> getConstraintDescriptor() {
        return this.constraintDescriptor;
    }

    @Override
    public <C> C unwrap(Class<C> type) {
        // 保持向后兼容性
        if (type.isAssignableFrom(ConstraintViolation.class)) {
            return type.cast(this);
        }
        throw new ValidationException("EasyConstraintViolationImpl 不支持该调用");
    }

    @Override
    public Object[] getExecutableParameters() {
        return executableParameters;
    }

    @Override
    public Object getExecutableReturnValue() {
        return executableReturnValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EasyConstraintViolationImpl{");
        sb.append("message='").append(message).append('\'');
        sb.append(", messageTemplate='").append(messageTemplate).append('\'');
        sb.append(", rootBean=").append(rootBean);
        sb.append(", rootBeanClass=").append(rootBeanClass);
        sb.append(", leafBeanInstance=").append(leafBeanInstance);
        sb.append(", executableParameters=").append(Arrays.toString(executableParameters));
        sb.append(", executableReturnValue=").append(executableReturnValue);
        sb.append(", propertyPath=").append(propertyPath);
        sb.append(", argName='").append(argName).append('\'');
        sb.append(", value=").append(value);
        sb.append(", constraintDescriptor=").append(constraintDescriptor);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EasyConstraintViolationImpl<?> that = (EasyConstraintViolationImpl<?>) o;
        return Objects.equals(message, that.message) && Objects.equals(messageTemplate, that.messageTemplate) && Objects.equals(rootBean, that.rootBean) && Objects.equals(rootBeanClass, that.rootBeanClass) && Objects.equals(leafBeanInstance, that.leafBeanInstance) && Arrays.equals(executableParameters, that.executableParameters) && Objects.equals(executableReturnValue, that.executableReturnValue) && Objects.equals(propertyPath, that.propertyPath) && Objects.equals(argName, that.argName) && Objects.equals(value, that.value) && Objects.equals(constraintDescriptor, that.constraintDescriptor);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(message, messageTemplate, rootBean, rootBeanClass, leafBeanInstance, executableReturnValue, propertyPath, argName, value, constraintDescriptor);
        result = 31 * result + Arrays.hashCode(executableParameters);
        return result;
    }
}


