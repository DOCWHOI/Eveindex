# API文档说明

## 概述

本项目使用Knife4j生成和展示API文档，Knife4j是基于OpenAPI 3.0规范的增强版Swagger UI，提供了更加美观和功能丰富的API文档界面。

## 访问方式

### Knife4j文档界面
- **地址**: http://localhost:8100/api/doc.html
- **特点**: 
  - 美观的中文界面
  - 支持接口调试
  - 支持参数校验
  - 支持文档导出
  - 支持离线文档

### OpenAPI JSON格式
- **地址**: http://localhost:8100/api/v3/api-docs
- **用途**: 
  - 提供标准的OpenAPI 3.0格式文档
  - 可用于代码生成工具
  - 可用于第三方工具集成

## 文档特性

### 1. 中文界面
Knife4j提供了完全中文化的界面，更符合国内开发者的使用习惯。

### 2. 接口分组
所有接口按照功能模块进行分组：
- 产品管理
- 市场管理
- 竞品管理
- HS编码管理
- 法规关键词管理
- 风险评分管理
- 监控数据管理
- 告警规则管理
- 数据源管理
- 统计管理

### 3. 详细描述
每个接口都包含：
- 接口概述（summary）
- 详细描述（description）
- 请求参数说明
- 响应结果说明
- 示例数据

### 4. 在线调试
可以直接在文档界面中：
- 填写请求参数
- 发送API请求
- 查看响应结果
- 复制curl命令

## 配置说明

### 1. 依赖配置（pom.xml）
```xml
<!-- Knife4j API文档 -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-spring-boot-starter</artifactId>
    <version>4.5.0</version>
</dependency>
```

### 2. 应用配置（application.yml）
```yaml
# Knife4j API文档配置
knife4j:
  enable: true
  setting:
    language: zh_cn
    enable-version: true
    enable-reload-cache-parameter: true
    enable-after-script: true
    enable-filter-multipart-api-method-type: POST
    enable-request-cache: true
    enable-host-text: localhost:8100
  openapi:
    title: 风险监控系统API文档
    description: 医疗器械风险监控系统后端API接口文档
    version: 1.0.0
    concat-contact: true
    contact:
      name: 开发团队
      email: dev@example.com
    group:
      default:
        group-name: 默认分组
        api-rule: package
        api-rule-resources:
          - com.eveindex.controller
```

### 3. Java配置类（Knife4jConfig.java）
```java
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("风险监控系统API文档")
                        .description("医疗器械风险监控系统后端API接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
```

## 注解使用

### 1. 控制器类注解
```java
@Tag(name = "产品管理", description = "产品信息的增删改查接口")
@RestController
@RequestMapping("/product")
public class ProductController {
    // ...
}
```

### 2. 接口方法注解
```java
@Operation(summary = "分页查询产品列表", description = "支持关键词搜索和排序的分页查询")
@PostMapping("/page")
public BaseResponse<Page<Product>> page(@RequestBody PageRequest pageRequest) {
    // ...
}
```

### 3. 参数注解
```java
@Operation(summary = "根据ID查询产品")
public BaseResponse<Product> getById(
    @Parameter(description = "产品ID", required = true) @PathVariable Integer id
) {
    // ...
}
```

## 最佳实践

### 1. 接口命名规范
- 使用清晰的中文描述
- summary简洁明了
- description提供详细说明

### 2. 参数说明
- 为所有参数添加description
- 标明必填和可选参数
- 提供参数示例

### 3. 响应说明
- 说明响应数据结构
- 提供成功和失败示例
- 说明错误码含义

### 4. 分组管理
- 按业务模块分组
- 保持分组名称一致
- 合理安排接口顺序

## 常见问题

### 1. 文档不显示
- 检查knife4j.enable是否为true
- 确认包扫描路径是否正确
- 查看控制台是否有错误信息

### 2. 接口调试失败
- 检查跨域配置
- 确认接口地址是否正确
- 查看网络请求是否正常

### 3. 中文乱码
- 确认language设置为zh_cn
- 检查浏览器编码设置
- 确认服务器字符集配置

## 扩展功能

### 1. 文档导出
Knife4j支持导出多种格式的文档：
- Word文档
- HTML文档
- PDF文档
- Markdown文档

### 2. 离线文档
可以生成离线版本的API文档，方便在没有网络的环境下使用。

### 3. 自定义主题
支持自定义UI主题，可以根据公司品牌进行定制。

### 4. 权限控制
可以配置文档访问权限，保护内部API不被外部访问。

## 总结

Knife4j为项目提供了功能强大、界面美观的API文档解决方案，相比传统的Swagger UI有以下优势：

1. **更好的用户体验** - 中文界面，操作更直观
2. **更丰富的功能** - 支持文档导出、离线使用等
3. **更好的性能** - 优化了加载速度和响应性能
4. **更强的扩展性** - 支持插件和自定义功能

通过合理配置和使用，Knife4j可以大大提高API文档的质量和开发效率。