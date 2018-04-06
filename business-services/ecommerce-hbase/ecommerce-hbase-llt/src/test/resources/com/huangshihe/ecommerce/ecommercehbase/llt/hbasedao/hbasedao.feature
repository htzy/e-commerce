Feature: HBaseDao

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


  Scenario Outline: 分页查询
    Given 表名为"<tableName>"
    And 要创建表的familyNames是"<familyNames>"
    And 数据表创建成功
    And 要插入数据的qualifiers为"<qualifiers>"
    And 要创建数据的rowKeys为"<insertRowKeys>"
    And 在表中插入多个rowKeys随机值
    And 要查询的startRowKey为"<startRowKey>"
    And 要查询的stopRowKey为"<stopRowKey>"
    And 要查询的pageSize为"<pageSize>"
    When 分页查询数据
    Then 查询到"<resultCount>"条数据
    Examples:
      | tableName | familyNames | qualifiers     | insertRowKeys     | startRowKey | stopRowKey | pageSize | resultCount |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 | 10001       | 10004      | 3        | 3           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 | 10003       | 10004      | 3        | 1           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 | 10001       | 10003      | 3        | 2           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 | 10000       | 10004      | 3        | 3           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 | 10000       | 10003      | 3        | 2           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 | 10001       | 10004      | 2        | 2           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 | 10001       | 10004      | 5        | 3           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 |             | 10004      | 3        | 3           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 | 10001       |            | 3        | 3           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 | 10004       |            | 3        | 0           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 |             |            | 3        | 3           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 |             |            | 2        | 2           |
      | llt-test  | t           | col1,col2,col3 | 10001,10002,10003 |             |            | 5        | 3           |

  Scenario Outline: 前缀模糊和时间范围查询
    Given 表名为"<tableName>"
    And 要创建表的familyNames是"<familyNames>"
    And 数据表创建成功
    And 要插入数据的qualifiers为"<qualifiers>"
    And 要创建数据的rowKeys的前缀为"<prefix>"
    And 要创建数据的rowKeys的时间为"<insertTime>"
    And 在表中插入一个前缀rowKeys随机值
    And 要查询的startTime为"<startTime>"
    And 要查询的stopTime为"<stopTime>"
    When 前缀模糊和时间范围查询数据
    Then 查询到"<resultCount>"条数据
    Examples:
      | tableName | familyNames | qualifiers | prefix | insertTime    | startTime     | stopTime      | resultCount |
      | llt-test  | t           | col1       | (      | 1514522700000 | 1514522600000 | 1514522800000 | 1           |
      | llt-test  | t           | col1       |        | 1514522700000 | 1514522600000 | 1514522800000 | 1           |
      | llt-test  | t           | col1       | (      | 1514522600000 | 1514522600000 | 1514522800000 | 1           |
      | llt-test  | t           | col1       | (      | 1514522800000 | 1514522600000 | 1514522800000 | 0           |
      | llt-test  | t           | col1       | ()     | 1514522700000 | 1514522600000 | 1514522800000 | 1           |
