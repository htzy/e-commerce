Feature: 配置类
  Scenario: 测试配置类
    Given 配置文件已存在
    When 查询"author"的配置值
    Then 查询结果不为空