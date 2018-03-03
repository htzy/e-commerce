Feature: 线程池

  Scenario Outline: 线程池xml转bean
    Given 待转换的xml文件名为"<xml_file_name>"
    When 线程池xml转bean
    Then 线程池xml转bean成功
    Examples:
      | xml_file_name   |
      | simple_data.xml |
