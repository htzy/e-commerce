Feature: hbase service
  Scenario: 每日新建原始表
    Given 每日新建原始表定时任务已设定
    When 触发新建原始表条件
    Then 创建原始表成功
