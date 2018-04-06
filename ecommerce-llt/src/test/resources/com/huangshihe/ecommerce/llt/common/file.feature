Feature: 文件工具类

  Scenario Outline: 获取文件夹下的所有文件
    Given 给定的文件夹路径为"<file_path>"
    When 获取文件夹下的所有文件
    Then 获取文件夹下的所有文件数目为"<nums>"
    Examples:
      | file_path | nums |
      | data      | 4    |

  Scenario Outline: 获取文件夹下符合规则的所有文件
    Given 给定的文件夹路径为"<file_path>"
    And 给定的匹配符为"<file_name_pattern>"
    When 获取文件夹下符合规则的所有文件
    Then 获取文件夹下的所有文件数目为"<nums>"
    Examples:
      | file_path           | file_name_pattern | nums |
      | data                | (.*)cfg\\.xml     | 0    |
      | data                | (.*)\\.xml        | 3    |
      | data/pub/threadpool | (.*)\\.xml        | 3    |
      | data/pub/threadpool | (.*)\\.ml         | 0    |
      | data/pub/threadpool | \\.xml            | 0    |
      | data/pub/threadpool | xml               | 0    |
