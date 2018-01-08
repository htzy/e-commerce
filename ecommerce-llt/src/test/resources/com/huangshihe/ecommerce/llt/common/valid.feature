Feature: 校验工具类

  Scenario Outline: 匹配data中的数据是否存在于param中
    Given param和data数据已存在
    And json数据为"<jsonData>"
    And 数据项为"<dataField>"
    And json参数为"<jsonParam>"
    And 参数数据项为"<paramField>"
    When 判断是否存在
    Then 结果为"<result>"
    Examples:
      | jsonData                 | dataField | jsonParam                            | paramField | result |
      | {'id':666,'name':'htzy'} | id        | {'ids':'666,888','names':'htzy,xyz'} | ids        | true   |
      | {'id':666,'name':'htzy'} | name      | {'ids':'666,888','names':'htzy,xyz'} | names      | true   |
      | {'id':123,'name':'htzy'} | id        | {'ids':'666,888','names':'htzy,xyz'} | ids        | false  |
      | {'i':666,'name':'htzy'}  | id        | {'ids':'666,888','names':'htzy,xyz'} | ids        | false  |
      | {'id':666,'name':'htzy'} | ids       | {'ids':'666,888','names':'htzy,xyz'} | ids        | false  |
      | {'id':'','name':'htzy'}  | id        | {'ids':'666,888','names':'htzy,xyz'} | ids        | false  |
      | {'id':'','name':'htzy'}  | id        | {'ids':'666,888','names':'htzy,xyz'} | idss       | false  |
      | {'id':'','name':'htzy'}  | id        |                                      | ids        | false  |

  Scenario Outline: 匹配param中的数据是否在data数据范围内
    Given param和data数据已存在
    And json数据为"<jsonData>"
    And 数据项为"<dataField>"
    And json参数为"<jsonParam>"
    And 参数起始数据项为"<startParamField>"
    And 参数终止数据项为"<stopParamField>"
    When 判断是否在范围内
    Then 结果为"<result>"
    Examples:
      | jsonData | dataField | jsonParam            | startParamField | stopParamField | result |
      | {'id':6} | id        | {'start':5,'stop':8} | start           | stop           | true   |
      | {'id':5} | id        | {'start':5,'stop':8} | start           | stop           | true   |
      | {'id':8} | id        | {'start':5,'stop':8} | start           | stop           | true   |
      | {'id':6} | id        | {'stop':8}           | start           | stop           | true   |
      | {'id':6} | id        | {'start':5,'stop':8} | start           | end            | true   |
      | {'id':6} | id        | {'start':5,'stop':8} | start           |                | true   |
      | {'id':6} | id        | {'start':5}          | start           | stop           | true   |
      | {'id':6} | id        | {'start':5,'stop':8} | begin           | stop           | true   |
      | {'id':6} | id        | {'start':5,'stop':8} |                 | stop           | true   |

      | {'id':4} | id        | {'start':5,'stop':8} | start           | stop           | false  |
      | {'id':9} | id        | {'start':5,'stop':8} | start           | stop           | false  |

      | {'id':6} | i         | {'start':5,'stop':8} | start           | stop           | false  |
      | {'i':6}  | id        | {'start':5,'stop':8} | start           | stop           | false  |
      |          | id        | {'start':5,'stop':8} | start           | stop           | false  |
      | {'id':6} |           | {'start':5,'stop':8} | start           | stop           | false  |
      | {'id':6} | id        |                      | start           | stop           | false  |
