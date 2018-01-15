Feature: 数字工具类

  Scenario Outline: 16进制转int
    Given 待转换十六进制字符串为"<str_16>"
    When 十六进制字符串转为int
    Then 转换int结果为"<result_int>"
    Examples:
      | str_16     | result_int |
      | W\x5C5\x80 | 1465660800 |

  Scenario Outline: 含汉字的16进制转字符串
    Given 待转换十六进制字符串为"<str_16>"
    When 含汉字的十六进制转字符串
    Then 转换str结果为"<result_str>"
    Examples:
      | str_16                   | result_str |
      | 123\xE7\x94\xB7age       | 123男age    |
      | \xe4\xb8\xad\xe6\x96\x87 | 中文         |
