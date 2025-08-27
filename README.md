# 智能数据KPI管理系统

## 项目简介

智能数据KPI管理系统是一个基于Spring Boot的团队KPI数据采集、分析和报表生成系统。该系统主要用于医疗健康行业的团队绩效指标管理，支持多种数据源的集成和Excel报表导出功能。

## 技术架构

### 后端技术栈
- **框架**: Spring Boot 2.3.1.RELEASE
- **数据库**: MySQL 5.1.34
- **ORM框架**: MyBatis 2.1.3
- **连接池**: C3P0 0.9.5.2
- **模板引擎**: Thymeleaf 3.0.0
- **Java版本**: JDK 1.8
- **构建工具**: Maven

### 前端技术栈
- **UI框架**: Bootstrap 3.0.2
- **图标库**: Font Awesome
- **JavaScript库**: jQuery
- **图表库**: ECharts
- **日期选择器**: DateRangePicker

## 项目结构

```
src/
├── main/
│   ├── java/com/jky/znys/team_kpi/
│   │   ├── config/          # 配置类
│   │   ├── controller/      # 控制器
│   │   ├── dao/            # 数据访问层
│   │   ├── entity/         # 实体类
│   │   ├── service/        # 业务逻辑层
│   │   ├── utils/          # 工具类
│   │   └── TeamKpiApplication.java  # 主启动类
│   ├── resources/
│   │   ├── mapper/         # MyBatis映射文件
│   │   ├── application.properties  # 配置文件
│   │   └── mybatis-config.xml     # MyBatis配置
│   └── webapp/             # Web资源
│       ├── css/            # 样式文件
│       ├── js/             # JavaScript文件
│       ├── img/            # 图片资源
│       └── index.html      # 主页面
```

## 核心功能

### 1. KPI数据采集
- **手动数据录入**: 支持用户手动输入KPI指标数据
- **友盟数据集成**: 自动获取友盟平台的数据
- **实时数据更新**: 支持数据的增删改查操作

### 2. 数据源集成
- **内部系统API**: 集成120.net、快问健康等内部系统
- **第三方平台**: 支持友盟、百度等第三方数据源
- **财务系统**: 集成财务数据统计接口

### 3. 报表生成
- **Excel导出**: 支持多维度数据的Excel报表生成
- **数据可视化**: 集成ECharts图表展示
- **定时任务**: 支持定时生成和发送报表

### 4. 通知系统
- **钉钉通知**: 集成钉钉机器人发送KPI数据
- **消息推送**: 支持多种通知方式
- **异常告警**: 系统异常自动通知

## 主要实体类

### KPIManual (手动KPI数据)
- 媒体阅读数
- 直播相关指标
- 视频PV数据
- 支付相关数据
- 用户活跃度指标

### KPIUmeng (友盟数据)
- 用户行为数据
- 应用使用统计
- 渠道分析数据

### KPIExcelConfig (Excel配置)
- 报表模板配置
- 数据字段映射
- 计算公式定义

## API接口

### 主要接口
- `POST /AddData` - 添加KPI数据
- `POST /AddUMData` - 添加友盟数据
- `POST /GetExcel` - 生成Excel报表
- `POST /GetYLExcel` - 生成医疗相关Excel报表

## 配置说明

### 数据库配置
```properties
jdbc.url=jdbc:mysql://jumpserver.120.net:33061/nekpi
jdbc.username=e0ae524c-61ea-4fd8-b023-43a788d7b5a9
jdbc.password=txdXHR2gjvT0FVVX
```

### 服务器配置
```properties
server.port=18080
spring.mvc.view.prefix=/
spring.mvc.view.suffix=.jsp
```

## 部署说明

### 环境要求
- JDK 1.8+
- MySQL 5.1+
- Maven 3.6+
- Tomcat 8.5+

### 部署步骤
1. 克隆项目到本地
2. 配置数据库连接信息
3. 执行Maven构建: `mvn clean package`
4. 将生成的WAR包部署到Tomcat
5. 启动应用

### 打包方式
项目支持WAR包部署，继承`SpringBootServletInitializer`以支持传统Web容器部署。

## 特色功能

### 1. 智能数据解析
- 支持多种数据格式的自动解析
- 智能数据清洗和验证
- 灵活的数据映射配置

### 2. 多维度报表
- 支持按时间、团队、指标等维度生成报表
- 自定义报表模板
- 批量数据导出

### 3. 实时监控
- 实时数据采集和更新
- 异常数据告警
- 性能监控和统计

## 开发团队

- **项目名称**: 智能数据KPI管理系统
- **开发语言**: Java
- **项目类型**: 企业级Web应用
- **应用领域**: 医疗健康行业团队管理

## 许可证

本项目为内部项目，仅供内部使用。

## 联系方式

如有问题请联系开发团队或系统管理员。

---

*最后更新时间: 2024年*
