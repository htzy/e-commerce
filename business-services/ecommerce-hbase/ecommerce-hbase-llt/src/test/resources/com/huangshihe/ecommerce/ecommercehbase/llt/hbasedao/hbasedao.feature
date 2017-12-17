Feature: HBaseDao

  Scenario Outline:  新建hbase表成功后删除该表
    Given 创建hbase连接成功
    And 要创建表的familyNames是"<familyNames>"
    And 要创建表的columnFamily老化时间都是"<ttl>"
    When 创建"<tableName>"表
    Then 创建"<tableName>"表成功
    And 删除"<tableName>"表
    Then 删除"<tableName>"表成功
    Examples:
      | tableName | familyNames | ttl     |
      | llt-test  | name        | 7776000 |


  Scenario Outline: 通过rowkey查询
    Given 创建hbase连接成功
    And 要创建表的familyNames是"<familyNames>"
    And 要创建表的columnFamily老化时间都是"<ttl>"
    And 数据表"<tableName>"创建成功
    When 在表"<tableName>"中通过rowKey"<rowKey>"查询
    Then 查询该rowKey共有"<cellsCount>"条记录
    And 删除"<tableName>"表

    Examples:
      | tableName | familyNames | ttl     | rowKey | cellsCount |
      | llt-test  | name        | 7776000 | 10001  | 0          |

