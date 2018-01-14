Feature: 数字工具类

  Scenario Outline: 16进制转int
    Given 待转换十六进制字符串为"<str_16>"
    When 十六进制字符串转为int
    Then 转换结果为"<result_int>"
    Examples:
      | str_16     | result_int |
      | W\x5C5\x80 | 1465660800 |