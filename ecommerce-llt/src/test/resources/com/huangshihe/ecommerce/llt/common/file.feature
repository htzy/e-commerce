Feature: 文件工具类

  Scenario Outline: 获取文件夹下的所有文件
    Given 给定的文件夹路径为"<file_path>"
    When 获取文件夹下的所有文件
    Then 获取文件夹下的所有文件数目为"<nums>"
    Examples:
      | file_path | nums |
      | data      | 1    |