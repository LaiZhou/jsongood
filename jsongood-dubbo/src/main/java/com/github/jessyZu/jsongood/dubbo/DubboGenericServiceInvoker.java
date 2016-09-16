/**
 *
 */
package com.github.jessyZu.jsongood.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.registry.RegistryService;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jessyZu.jsongood.core.RpcContext;
import com.github.jessyZu.jsongood.core.RpcInvoker;
import com.github.jessyZu.jsongood.core.RpcResult;
import com.github.jessyZu.jsongood.core.RpcResultCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DubboGenericServiceInvoker implements RpcInvoker, InitializingBean {

    private final Logger logger = LoggerFactory
            .getLogger(DubboGenericServiceInvoker.class);

    private ApplicationConfig application;
    private List<RegistryConfig> registryList;

    private ObjectMapper objectMapper;

    private ConcurrentHashMap<String, ReferenceConfig<GenericService>> REFERENCECONFIG_CACHE = new ConcurrentHashMap<String, ReferenceConfig<GenericService>>();

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }

    public void setRegistryList(List<RegistryConfig> registryList) {
        this.registryList = registryList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connectRegistry();
    }

    public void connectRegistry() {
        Long s = System.currentTimeMillis();
        ReferenceConfig<RegistryService> reference = new ReferenceConfig<RegistryService>();
        reference.setApplication(application);
        reference.setRegistries(registryList); // 多个注册中心可以用setRegistries()
        reference.setInterface(RegistryService.class); // 弱类型接口名
        reference.setRetries(0);
        reference.setTimeout(5000);
        RegistryService registryService = reference.get();
        Long t = System.currentTimeMillis() - s;
        logger.info("connectRegistry success ,spend time=" + t + "ms");

    }

    @Override
    public void invoke(RpcContext rpcContext, RpcResult rpcResult) {

        try {
            String referenceCacheKey = new StringBuilder()
                    .append(rpcContext.getClassName()).append("-")
                    .append(rpcContext.getServiceVersion()).toString();
            ReferenceConfig<GenericService> reference = REFERENCECONFIG_CACHE
                    .get(referenceCacheKey);
            if (reference == null) {
                // 获取泛化引用
                reference = new ReferenceConfig<GenericService>();
            }
            reference.setApplication(application);
            reference.setRegistries(registryList); // 多个注册中心可以用setRegistries()
            reference.setGeneric(true); // 声明为泛化接口
            reference.setInterface(rpcContext.getClassName()); // 弱类型接口名
            reference.setVersion(rpcContext.getServiceVersion());
            reference.setRetries(0);
            Long s = System.currentTimeMillis();
            GenericService genericService = reference.get(); // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用
            Long t = System.currentTimeMillis() - s;

            logger.info("reference.get() success ,spend time=" + t + "ms"+","+rpcContext);

            if (genericService == null) {
                rpcResult
                        .setWithRpcResultCodeEnum(RpcResultCodeEnum.SERVICE_BEAN_NOT_FOUND_ERROR);
            } else {
                // 缓存dubbo reference
                REFERENCECONFIG_CACHE.put(referenceCacheKey, reference);
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();

                }
                List<?> parameters = objectMapper.readValue(rpcContext
                        .getRpcRequest().getParameters(), ArrayList.class);
                // 基本类型以及Date,List,Map等不需要转换，直接调用
                Object result = genericService.$invoke(
                        rpcContext.getMethodName(), null, parameters.toArray());
                rpcResult
                        .setWithRpcResultCodeEnum(RpcResultCodeEnum.SUCCESS);
                rpcResult.setData(result);
            }

        } catch (Exception e) {
            logger.error("{}", rpcContext);
            logger.error("{}", e);
            String messageString = e.getMessage();
            if (messageString != null && messageString.contains("ConstraintViolation")) {
                rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.VALIDATION_ERROR);
                Map<String, String> vaidationResultMap = buildValidationResult(messageString);
                rpcResult.setData(vaidationResultMap);
            } else {
                rpcResult.setWithRpcResultCodeEnum(RpcResultCodeEnum.METHOD_INVOKE_ERROR);
            }


        }

    }

    public static Map<String, String> buildValidationResult(String messageString) {
        Map<String, String> vaidationResultMap = new HashMap<String, String>();
        StringBuilder messageBuilder = new StringBuilder(messageString.trim());
        messageBuilder.delete(0, messageBuilder.indexOf("cause: [ConstraintViolationImpl{")
                + "cause: [ConstraintViolationImpl{".length());
        messageBuilder.delete(messageBuilder.indexOf("}]"), messageBuilder.length());
        String content = messageBuilder.toString().trim();
        // System.out.println(content);
        String[] contentArray = content.split("\\}, ConstraintViolationImpl\\{");

        String inValidPropKey = null;
        String inValidPropMessage = null;
        for (String string : contentArray) {
            // System.out.println(string);
            String[] stringArray = string.trim().split(",");
            for (String string2 : stringArray) {
                String[] kvPair = string2.trim().split("=");//注意message里不要出现等号
                // System.out.println(kvPair[0] + " " + kvPair[1]);
                if (kvPair[0].equals("propertyPath")) {
                    inValidPropKey = kvPair[1];
                }

                if (kvPair[0].equals("interpolatedMessage")) {
                    inValidPropMessage = kvPair[1].subSequence(1, kvPair[1].length() - 1).toString();
                }
            }
            if (inValidPropKey != null) {
                vaidationResultMap.put(inValidPropKey, inValidPropMessage);
            }

        }

        return vaidationResultMap;

    }
}
