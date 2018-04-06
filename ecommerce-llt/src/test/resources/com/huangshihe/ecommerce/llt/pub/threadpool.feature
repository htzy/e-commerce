Feature: 线程池

  Scenario Outline: 线程池xml转bean
    Given 待转换的xml文件名为"<xml_file_name>"
    When 线程池xml转bean
    Then 线程池xml转bean成功
    Examples:
      | xml_file_name   |
      | simple_data.xml |


  Scenario Outline: 线程池解析xml并将任务提交到线程池中
    Given 待转换的xml文件名为"<xml_file_name>"
    When 线程池xml转bean
    And 新建线程池
    And 显式调用任务
    Then 任务运行在线程池中
    Examples:
      | xml_file_name   |
      | simple_task.xml |


  Scenario Outline: 线程池解析xml并将任务提交到线程池中，定时运行
    Given 待转换的xml文件名为"<xml_file_name>"
    When 线程池xml转bean
    And 新建线程池
    Then 任务在线程池中定时运行
    Examples:
      | xml_file_name       |
      | simple_schedule.xml |



