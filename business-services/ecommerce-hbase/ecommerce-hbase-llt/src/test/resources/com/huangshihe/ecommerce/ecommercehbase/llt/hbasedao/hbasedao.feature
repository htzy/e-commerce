Feature: HBaseDao

  Scenario Outline:  新建hbase表成功
    Given 创建hbase连接成功
    And 要创建表的familyNames是"<familyNames>"
    And 要创建表的columnFamily老化时间都是"<ttl>"
    When 创建"<tableName>"表
    Then 创建"<tableName>"表成功
    Examples:
      | tableName | familyNames | ttl     |
      | llt-test  | name        | 7776000 |

