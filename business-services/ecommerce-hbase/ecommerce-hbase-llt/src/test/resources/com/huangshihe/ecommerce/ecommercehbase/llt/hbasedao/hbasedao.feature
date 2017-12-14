Feature: HBaseDao

  Scenario Outline:  新建hbase表成功
    Given 创建hbase连接成功
    When 创建"<tableName>"表
    And 该表的familyNames是"<familyNames>"
    And 该表的columnFamily老化时间都是"<ttl>"
    Then 创建hbase表成功
    Examples:
      | tableName | familyNames | ttl     |
      | llt-test  | name        | 7776000 |

