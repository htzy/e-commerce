Feature: json工具类
  Scenario: 对象转json字符串
    Given 待转换对象已存在
    When 对象转字符串
    Then 转换字符串正确

  Scenario: json字符串转对象
    Given 待转换json字符串已存在
    When 字符串转对象
    Then 转换对象正确

  Scenario: json字符串转Tree
    Given 待转换json字符串已存在
    When 字符串转Tree
    Then 转换Tree正确

    Scenario: 对象转Tree
      Given 待转换对象已存在
      When 对象转Tree
      Then 转换Tree正确