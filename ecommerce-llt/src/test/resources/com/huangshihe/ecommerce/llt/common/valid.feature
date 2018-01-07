Feature: 校验工具类

  Scenario Outline: 匹配param中的数据是否存在于data中
    Given param和data数据已存在
    And json数据为"<jsonData>"
    And 数据项为"<dataField>"
    And json参数为"<jsonParam>"
    And 参数数据项为"<paramField>"
    When 判断是否存在
    Then 结果为"<result>"
    Examples:
      | jsonData                             | dataField | jsonParam                | paramField | result |
      | {'ids':'666,888','names':'htzy,xyz'} | ids       | {'id':666,'name':'htzy'} | id         | true   |
      | {'ids':'666,888','names':'htzy,xyz'} | names     | {'id':666,'name':'htzy'} | name       | true   |
      | {'ids':'666,888','names':'htzy,xyz'} | ids       | {'id':123,'name':'htzy'} | id         | false  |
      | {'ids':'666,888','names':'htzy,xyz'} | ids       | {'i':666,'name':'htzy'}  | id         | false  |
      | {'ids':'666,888','names':'htzy,xyz'} | ids       | {'id':666,'name':'htzy'} | ids        | false  |
      | {'ids':'666,888','names':'htzy,xyz'} | ids       | {'id':'','name':'htzy'}  | id         | false  |
      | {'ids':'666,888','names':'htzy,xyz'} | idss      | {'id':'','name':'htzy'}  | id         | false  |
      |                                      | ids       | {'id':'','name':'htzy'}  | id         | false  |

