<div style="text-align: center;">
	<h1>Wow-Spring</h1>
</div>

mini版spring框架，手写实现了IoC、DI、AOP、MVC，整体基于纯注解形式，本项目是为了更好的
学习和理解spring底层原理，纯属娱乐。

## 整体结构

```text
framework                                     
├── Utils                                     
│   └── Strings.java                          
├── annotation                                
│   ├── WAutowired.java                       
│   ├── WController.java                      
│   ├── WRequestMapping.java                  
│   ├── WRequestParam.java                    
│   └── WService.java                         
├── beans                                     
│   ├── WBeanWrapper.java                     
│   ├── config                                
│   │   ├── WBeanDefinition.java              
│   │   └── WBeanPostProcessor.java           
│   └── support                               
│       ├── IWBeanDefinitionReader.java       
│       └── WBeanDefinitionReader.java        
├── context                                   
│   ├── WAbstractApplicationContext.java      
│   ├── WApplicationContext.java              
│   ├── WApplicationContextAware.java         
│   └── support                               
│       └── WDefaultListableBeanFactory.java  
├── core                                      
│   └── WBeanFactory.java                     
└── webmvc                                    
    ├── WHandlerAdapter.java                  
    ├── WHandlerMapping.java                  
    ├── WModelAndView.java                    
    ├── WView.java                            
    ├── WViewResolver.java                    
    └── servlet                               
        └── WDispatcherServlet.java           
```

## 启动

当前为webapp项目，导入idea后部署到tomcat下直接启动即可。

## 参考
- spring源码
- spring 5核心原理
