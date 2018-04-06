Feature: aop
  Scenario: 简单的aop
    Given 对简单类进行增强
    When 调用简单类的方法
    Then 方法被拦截