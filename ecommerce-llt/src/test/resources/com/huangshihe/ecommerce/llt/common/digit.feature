Feature: 数字工具类

  Scenario Outline: 16进制转long
    Given 待转换十六进制字符串为"<str_16>"
    When 十六进制字符串转为long
    Then 转换long结果为"<result_long>"
    Examples:
      | str_16     | result_long |
      | W\x5C5\x80 | 1465660800  |

  Scenario Outline: 含汉字的16进制转字符串
    Given 待转换十六进制字符串为"<str_16>"
    When 含汉字的十六进制转字符串
    Then 转换str结果为"<result_str>"
    Examples:
      | str_16                   | result_str |
      | 123\xE7\x94\xB7age       | 123男age    |
      | \xe4\xb8\xad\xe6\x96\x87 | 中文         |

  Scenario Outline: 计算utf8字符串的长度
    Given 待计算的字符串为"<str>"
    When 计算utf8字符串长度
    Then 计算utf8字符串长度的结果为"<result_int>"
    Examples:
      | str                      | result_int |
      | 123\xE7\x94\xB7age       | 7          |
      | \xe4\xb8\xad\xe6\x96\x87 | 2          |
      |                          | 0          |
