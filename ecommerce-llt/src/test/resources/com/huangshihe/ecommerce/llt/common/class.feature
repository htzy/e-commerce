Feature: classKit
  Scenario: 通过私有构造方法创建类的实例——多参
    Given 简单类已存在且构造方法存在多个参数
    When 通过多参私有构造方法创建类实例
    Then 创建类实例成功

    Scenario: 通过私有构造方法创建类的实例——无参
      Given 简单类已存在且构造方法存在无参数
      When 通过无参私有构造方法创建类实例
      Then 创建类实例成功