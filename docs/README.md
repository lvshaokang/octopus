# octopus
metadata and data monitor

## 元数据管理

功能:
元数据定义
元数据存储
元数据采集
元数据血缘
元数据探索
访问权限

### 元数据定义

元模型管理

模型：概念 + 实体

概念父类：数据集和流程

元模型 ：


实体分为：数据集 和 流程



主要采用的CMW元模型,跟Atlas 类型系统结构相似

存储结构

主表：ID,code,name,type,description,version,create_time,update_time,create_by,update_by

---
属性表：ID,code,key,value,leaf_type（叶子类型）


关系表：当前节点，子节点，深度

---
属性表：ID,code,key,value,leaf_type（叶子类型），parent_code


概念：

枚举：

｜名称｜类型｜描述｜版本｜

| 名称 |     类型 |  描述  | 版本  |     |     |
|----|-------:| :----: |-----|-----|-----|
| 测试状态 |   Enum |       | 1.0 |     |     |
| pg_db | Entity |   6    |     |     |     |
| 草莓 |     $1 |   7    |     |     |     |

测试状态，枚举，元素[{测试不通过,NOT},{测试通过,NOT}]

{
"name":"TEST_STATUS",
"type":"Enum",
"description": "测试状态",
"version":1
"enumElements":[
{"value":"TEST_SUCCESS","description": "测试通过", "ordinal": 1},
{"value":"TEST_FAILED","description": "测试失败", "ordinal": 2},
{"value":"TEST_NOT","description": "未测试", "ordinal": 3}
]
}
{
"name": "pg_db",
"type":"Enum",
"description": "测试状态",
"version":1
"attributes":[
{"value":"TEST_SUCCESS","description": "测试通过", "ordinal": 1},
{"value":"TEST_FAILED","description": "测试失败", "ordinal": 2},
{"value":"TEST_NOT","description": "未测试", "ordinal": 3}
]
}
}




### 元数据存储

* 数据存储：PostgreSQL
* 关系存储：GraphDB
* 索引存储：ES

> 元数据主要存储在关系型数据库，通过配置开启关系功能和高级搜索功能时才需要引入外部图数据库和搜索引擎

### 元数据采集

同时支持推和拉模式

1.推模式

* API
* 消息系统插件化-默认是kafka

2.拉模式

* 定时采集数据上传到消息系统
* Hook 钩子采集变更数据上传到消息系统
  支持多源：HIVE,HBASE,JDBC...

### 元数据血缘
1.字段血缘

### 元数据探索


## 数据监控
数据可观测性


## 任务分配

1.元数据定义

* 模型类型和实体定义，转换，验证
* 模型和数据库交互
* hive 元数据定义
* pulsar 元数据定义
* 多数据源元数据定义（优先级低）

2.元数据采集

* API 存储元数据
* 消息系统接收元数据，存储元数据
* hive 定时采集或者Hook(推荐使用Hook) 采集变更数据上传到消息系统
* pulsar 元数据手动录入
* 定时采集主要针对RDMS(优先级低)
* 对接dolphinscheduler采集元数据
* 多种数据源对接

3.元数据血缘
* 字段级血缘？？？

4.元数据探索
* 基础查询
* 生成索引数据
* 高级搜索接口
4