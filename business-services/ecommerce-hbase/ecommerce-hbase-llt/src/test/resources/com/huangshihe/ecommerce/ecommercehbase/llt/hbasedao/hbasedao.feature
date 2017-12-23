Feature: HBaseDao

  Scenario Outline:  新建hbase表成功后删除该表
    Given 创建hbase连接成功
    And 要创建表的familyNames是"<familyNames>"
    And 要创建表的columnFamily老化时间都是"<ttl>"
    And 表名为"<tableName>"
    When 创建表
    Then 创建表成功
    And 删除表
    Then 删除表成功
    Examples:
      | tableName | familyNames | ttl     |
      | llt-test  | name        | 7776000 |


  Scenario Outline: 通过rowkey查询
    Given 创建hbase连接成功
    And 要创建表的familyNames是"<familyNames>"
    And 要创建表的columnFamily老化时间都是"<ttl>"
    And 表名为"<tableName>"
    And 数据表创建成功
    When 在表中通过rowKey"<rowKey>"查询
    Then 查询该rowKey共有"<cellsCount>"条记录
    And 删除表
    Examples:
      | tableName | familyNames | ttl     | rowKey | cellsCount |
      | llt-test  | name        | 7776000 | 10001  | 0          |


  Scenario Outline: 删除不存在的表
    Given 创建hbase连接成功
    And 表名为"<tableName>"
    When 删除不存在的表
    Then 删除表成功
    Examples:
      | tableName           |
      | llt-test-not-exists |


  Scenario Outline: 插入数据
    Given 创建hbase连接成功
    # TODO 整改familyNames为families
    And 要创建表的familyNames是"<familyNames>"
    And 表名为"<tableName>"
    And 数据表创建成功
    And 要插入数据的columns为"<columns>"
    When 在表中随机生成"<rowNums>"条rowKey和随机值插入
    Then 查询表中共有"<rowNums>"条rowKey
    And 删除表
    Examples:
      | tableName | familyNames | columns        | rowNums |
      | llt-test  | t           | col1,col2,col3 | 5       |
