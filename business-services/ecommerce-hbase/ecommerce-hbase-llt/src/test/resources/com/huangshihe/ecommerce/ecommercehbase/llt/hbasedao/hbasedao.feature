Feature: HBaseDao

  Background: 每一次都会调用下面的语句，但这里连接只在第一次创建。除非测试删除表，否则每次运行后都会自动删除表名为tableName的表
    Given 创建hbase连接成功

  Scenario Outline:  新建hbase表成功后删除该表
    Given 表名为"<tableName>"
    And 要创建表的familyNames是"<familyNames>"
    And 要创建表的columnFamily老化时间都是"<ttl>"
    When 创建表
    Then 创建表成功
    And 删除表
    Then 删除表成功
    Examples:
      | tableName | familyNames | ttl     |
      | llt-test  | name        | 7776000 |


  Scenario Outline: 删除不存在的表
    Given 表名为"<tableName>"
    When 删除不存在的表
    Then 删除表成功
    Examples:
      | tableName           |
      | llt-test-not-exists |

  Scenario Outline: 通过rowkey查询
    Given 表名为"<tableName>"
    And 要创建表的familyNames是"<familyNames>"
    And 要创建表的columnFamily老化时间都是"<ttl>"
    And 数据表创建成功
    When 在表中通过rowKey"<rowKey>"查询
    Then 查询该rowKey共有"<cellsCount>"条记录
    Examples:
      | tableName | familyNames | ttl     | rowKey | cellsCount |
      | llt-test  | name        | 7776000 | 10001  | 0          |


  Scenario Outline: 插入数据
    Given 表名为"<tableName>"
    And 要创建表的familyNames是"<familyNames>"
    And 数据表创建成功
    And 要插入数据的qualifiers为"<qualifiers>"
    When 在表中随机生成"<rowNums>"条rowKey和随机值插入
    Then 查询表中共有"<rowNums>"条rowKey
    Examples:
      | tableName | familyNames | qualifiers     | rowNums |
      | llt-test  | t           | col1,col2,col3 | 5       |

  Scenario Outline: 插入特定数据并查询该数据
    Given 表名为"<tableName>"
    And 要创建表的familyNames是"<familyNames>"
    And 数据表创建成功
    And 要插入数据的qualifiers为"<qualifiers>"
    And 要创建数据的rowKey为"<insertRowKey>"
    When 在表中插入随机值
    Then 查询rowKey为"<queryRowKey>"
    Examples:
      | tableName | familyNames | qualifiers     | insertRowKey | queryRowKey |
      | llt-test  | t           | col1,col2,col3 | 10001        | 10001       |


  Scenario Outline: 根据表名查询所有数据
    Given 表名为"<tableName>"
    And 要创建表的familyNames是"<familyNames>"
    And 数据表创建成功
    And 要插入数据的qualifiers为"<qualifiers>"
    And 要创建数据的rowKeys为"<insertRowKeys>"
    And 在表中插入多个rowKeys随机值
    When 根据表名查询所有数据
    Then 查询到所有的数据
    Examples:
      | tableName | familyNames | qualifiers     | insertRowKeys     |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 |

