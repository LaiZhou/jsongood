/**
 * 
 */
package com.github.jessyZu.jsongood.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jessyZu.jsongood.util.ClassGenerator;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;

import javax.validation.*;
import javax.validation.groups.Default;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalBeanServiceInvoker implements RpcInvoker, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(LocalBeanServiceInvoker.class);

    private ApplicationContext  applicationContext;
    private Validator           validator;

    /**
     * @param applicationContext the applicationContext to set
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final ConcurrentMap<String, Class<?>> Clazz_CACHE             = new ConcurrentHashMap<String, Class<?>>();
    private final ConcurrentMap<String, Method>   Signature_METHODS_CACHE = new ConcurrentHashMap<String, Method>();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void invoke(RpcContext rpcContext, RpcResult rpcResult) {
        if (validator == null) {
            validator = Validation.buildDefaultValidatorFactory().getValidator();
        }
        try {
            String classname = rpcContext.getClassName();
            Class<?> clazz = Clazz_CACHE.get(classname);
            if (clazz == null) {
                clazz = Class.forName(rpcContext.getClassName());
                Clazz_CACHE.put(classname, clazz);

            }

            Method m = findMethodByMethodSignature(clazz, rpcContext.getMethodName(), rpcContext.getParameters().size());
            Object bean = applicationContext.getBean(clazz);
            if (bean == null) {
                rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.SERVICE_BEAN_NOT_FOUND_ERROR);
            } else {

                // convert the parameters
                Object[] convertedParams = new Object[rpcContext.getParameters().size()];
                Class<?>[] parameterTypes = m.getParameterTypes();

                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();

                }
                for (int i = 0; i < parameterTypes.length; i++) {
                    convertedParams[i] = objectMapper.readValue(rpcContext.getParameters().get(i), parameterTypes[i]);
                    rpcContext.getParameterTypes().add(parameterTypes[i]);

                    //resolve element type of collecttion parameter from map
                    if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                        if (convertedParams[i] != null) {
                            Collection<?> collection = (Collection<?>) convertedParams[i];
                            Collection newCollection = (Collection) convertedParams[i].getClass()
                                    .newInstance();
                            if (collection.size() > 0) {
                                for (Object elementObject : collection) {
                                    if (elementObject != null) {
                                        ResolvableType type = ResolvableType.forMethodParameter(m, i);
                                        if (type.getGenerics().length == 1) {
                                            Class<?> elementClass = type.getGeneric(0).getRawClass();
                                            String jsonValue = objectMapper.writeValueAsString(elementObject);
                                            Object elementClassInstance = objectMapper.readValue(jsonValue,
                                                    elementClass);
                                            newCollection.add(elementClassInstance);
                                        }
                                    }
                                }
                                convertedParams[i] = newCollection;
                            }
                        }

                    }
                }
                validate(clazz, m.getName(), parameterTypes, convertedParams);

                // invoke the method
                Object result = m.invoke(bean, convertedParams);
                rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.SUCCESS);
                rpcResult.setData(result);
            }

        } catch (Exception e) {
            logger.error("{}", rpcContext);
            logger.error("{}", e);
            if (e instanceof ClassNotFoundException) {
                rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.CLASS_NOT_FOUND_ERROR);
            } else if (e instanceof NoSuchMethodException) {
                rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.METHOD_NOT_FOUND_ERROR);
            } else if (e instanceof ConstraintViolationException) {
                rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.VALIDATION_ERROR);
                Set<ConstraintViolation<?>> cvSet = ((ConstraintViolationException) e).getConstraintViolations();
                Map<String, String> validationResult = new HashMap<String, String>();
                for (ConstraintViolation<?> cv : cvSet) {
                    validationResult.put(cv.getPropertyPath().toString(), cv.getMessage());
                }
                rpcResult.setData(validationResult);
            } else {

                rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.METHOD_INVOKE_ERROR);
            }

        }
    }

    /**
     * 根据方法签名从类中找出方法。
     * 
     * @param clazz 查找的类。
     * @param methodName 方法签名，形如method1(int, String)。也允许只给方法名不参数只有方法名，形如method2。
     * @return 返回查找到的方法。
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws IllegalStateException 给定的方法签名找到多个方法（方法签名中没有指定参数，又有有重载的方法的情况）
     */
    public Method findMethodByMethodSignature(Class<?> clazz, String methodName, int parametersCount)
            throws NoSuchMethodException, ClassNotFoundException {
        String signature = methodName;
        if (parametersCount > 0) {
            signature = methodName + parametersCount;
        }
        Method method = Signature_METHODS_CACHE.get(signature);
        if (method != null) {
            return method;
        }
        List<Method> finded = new ArrayList<Method>();

        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(methodName) && m.getParameterTypes().length == parametersCount) {
                finded.add(m);
            }
        }

        if (finded.isEmpty()) {
            throw new NoSuchMethodException("No such method " + methodName + " in class " + clazz);
        }
        if (finded.size() > 1) {
            String msg = String.format("Not unique method for method name(%s) in class(%s), find %d methods.",
                    methodName, clazz.getName(), finded.size());
            throw new IllegalStateException(msg);
        }
        method = finded.get(0);

        Signature_METHODS_CACHE.put(signature, method);
        return method;
    }

    public void validate(Class clazz, String methodName, Class<?>[] parameterTypes, Object[] arguments)
            throws Exception {
        String methodClassName = clazz.getName() + "_" + toUpperMethoName(methodName);
        Class<?> methodClass = null;
        try {
            methodClass = Class.forName(methodClassName, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
        }
        Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();
        Method method = clazz.getMethod(methodName, parameterTypes);
        Object parameterBean = getMethodParameterBean(clazz, method, arguments);
        if (parameterBean != null) {
            if (methodClass != null) {
                violations.addAll(validator.validate(parameterBean, Default.class, clazz, methodClass));
            } else {
                violations.addAll(validator.validate(parameterBean, Default.class, clazz));
            }
        }
        for (Object arg : arguments) {
            validate(violations, arg, clazz, methodClass);
        }
        if (violations.size() > 0) {
            throw new ConstraintViolationException("Failed to validate service: " + clazz.getName() + ", method: "
                    + methodName + ", cause: " + violations, violations);
        }
    }

    private void validate(Set<ConstraintViolation<?>> violations, Object arg, Class<?> clazz, Class<?> methodClass) {
        if (arg != null && !isPrimitives(arg.getClass())) {
            if (Object[].class.isInstance(arg)) {
                for (Object item : (Object[]) arg) {
                    validate(violations, item, clazz, methodClass);
                }
            } else if (Collection.class.isInstance(arg)) {
                for (Object item : (Collection<?>) arg) {
                    validate(violations, item, clazz, methodClass);
                }
            } else if (Map.class.isInstance(arg)) {
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) arg).entrySet()) {
                    validate(violations, entry.getKey(), clazz, methodClass);
                    validate(violations, entry.getValue(), clazz, methodClass);
                }
            } else {
                if (methodClass != null) {
                    violations.addAll(validator.validate(arg, Default.class, clazz, methodClass));
                } else {
                    violations.addAll(validator.validate(arg, Default.class, clazz));
                }
            }
        }
    }

    private static boolean isPrimitives(Class<?> cls) {
        if (cls.isArray()) {
            return isPrimitive(cls.getComponentType());
        }
        return isPrimitive(cls);
    }

    private static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == String.class || cls == Boolean.class || cls == Character.class
                || Number.class.isAssignableFrom(cls) || Date.class.isAssignableFrom(cls);
    }

    private static Object getMethodParameterBean(Class<?> clazz, Method method, Object[] args) {
        if (!hasConstraintParameter(method)) {
            return null;
        }
        try {
            String upperName = toUpperMethoName(method.getName());
            String parameterSimpleName = upperName + "Parameter";
            String parameterClassName = clazz.getName() + "_" + parameterSimpleName;
            Class<?> parameterClass;
            try {
                parameterClass = (Class<?>) Class.forName(parameterClassName, true, clazz.getClassLoader());
            } catch (ClassNotFoundException e) {
                ClassPool pool = ClassGenerator.getClassPool(clazz.getClassLoader());
                CtClass ctClass = pool.makeClass(parameterClassName);
                ClassFile classFile = ctClass.getClassFile();
                classFile.setVersionToJava5();
                ctClass.addConstructor(CtNewConstructor.defaultConstructor(pool.getCtClass(parameterClassName)));
                // parameter fields
                Class<?>[] parameterTypes = method.getParameterTypes();
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> type = parameterTypes[i];
                    Annotation[] annotations = parameterAnnotations[i];
                    AnnotationsAttribute attribute = new AnnotationsAttribute(classFile.getConstPool(),
                            AnnotationsAttribute.visibleTag);
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType().isAnnotationPresent(Constraint.class)) {
                            javassist.bytecode.annotation.Annotation ja = new javassist.bytecode.annotation.Annotation(
                                    classFile.getConstPool(), pool.getCtClass(annotation.annotationType().getName()));
                            Method[] members = annotation.annotationType().getMethods();
                            for (Method member : members) {
                                if (Modifier.isPublic(member.getModifiers()) && member.getParameterTypes().length == 0
                                        && member.getDeclaringClass() == annotation.annotationType()) {
                                    Object value = member.invoke(annotation, new Object[0]);
                                    if (value != null && !value.equals(member.getDefaultValue())) {
                                        MemberValue memberValue = createMemberValue(classFile.getConstPool(),
                                                pool.get(member.getReturnType().getName()), value);
                                        ja.addMemberValue(member.getName(), memberValue);
                                    }
                                }
                            }
                            attribute.addAnnotation(ja);
                        }
                    }
                    String fieldName = method.getName() + "Argument" + i;
                    CtField ctField = CtField.make("public " + type.getCanonicalName() + " " + fieldName + ";",
                            pool.getCtClass(parameterClassName));
                    ctField.getFieldInfo().addAttribute(attribute);
                    ctClass.addField(ctField);
                }
                parameterClass = ctClass.toClass();
            }
            Object parameterBean = parameterClass.newInstance();
            for (int i = 0; i < args.length; i++) {
                Field field = parameterClass.getField(method.getName() + "Argument" + i);
                field.set(parameterBean, args[i]);
            }
            return parameterBean;
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
    }

    private static boolean hasConstraintParameter(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations != null && parameterAnnotations.length > 0) {
            for (Annotation[] annotations : parameterAnnotations) {
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().isAnnotationPresent(Constraint.class)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static String toUpperMethoName(String methodName) {
        return methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
    }

    // Copy from javassist.bytecode.annotation.Annotation.createMemberValue(ConstPool, CtClass);
    private static MemberValue createMemberValue(ConstPool cp, CtClass type, Object value) throws NotFoundException {
        MemberValue memberValue = javassist.bytecode.annotation.Annotation.createMemberValue(cp, type);
        if (memberValue instanceof BooleanMemberValue)
            ((BooleanMemberValue) memberValue).setValue((Boolean) value);
        else if (memberValue instanceof ByteMemberValue)
            ((ByteMemberValue) memberValue).setValue((Byte) value);
        else if (memberValue instanceof CharMemberValue)
            ((CharMemberValue) memberValue).setValue((Character) value);
        else if (memberValue instanceof ShortMemberValue)
            ((ShortMemberValue) memberValue).setValue((Short) value);
        else if (memberValue instanceof IntegerMemberValue)
            ((IntegerMemberValue) memberValue).setValue((Integer) value);
        else if (memberValue instanceof LongMemberValue)
            ((LongMemberValue) memberValue).setValue((Long) value);
        else if (memberValue instanceof FloatMemberValue)
            ((FloatMemberValue) memberValue).setValue((Float) value);
        else if (memberValue instanceof DoubleMemberValue)
            ((DoubleMemberValue) memberValue).setValue((Double) value);
        else if (memberValue instanceof ClassMemberValue)
            ((ClassMemberValue) memberValue).setValue(((Class<?>) value).getName());
        else if (memberValue instanceof StringMemberValue)
            ((StringMemberValue) memberValue).setValue((String) value);
        else if (memberValue instanceof EnumMemberValue)
            ((EnumMemberValue) memberValue).setValue(((Enum<?>) value).name());
        /* else if (memberValue instanceof AnnotationMemberValue) */
        else if (memberValue instanceof ArrayMemberValue) {
            CtClass arrayType = type.getComponentType();
            int len = Array.getLength(value);
            MemberValue[] members = new MemberValue[len];
            for (int i = 0; i < len; i++) {
                members[i] = createMemberValue(cp, arrayType, Array.get(value, i));
            }
            ((ArrayMemberValue) memberValue).setValue(members);
        }
        return memberValue;
    }

}
