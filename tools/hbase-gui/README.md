# 下拉框
    下拉框中选择原有类型，即存入HBase中的值是什么类型。
1. 十进制：
    可填入原有类型为十进制的值，如："\x00\x00\x00\x00ZE\xC8L"，这里会将十六进制转为byte，再将其余的ASCII码转为byte，再统一转为十进制。
    这里保存的类型为long，注意别越界！
2. 文本：
    可填入英文、数字和汉字（十六进制）混杂的值，如："123\xE7\x94\xB7age"，值为："123男age"。
3. 连续MAC：
    可填入连续的mac地址，这里会将十六进制保留，将其他的ASCII码转为十六进制，如果是多个mac拼接而成，中间不需加分隔符，否则会分割错误，示例：
 (n\xD4\x88\xDA\xF4\x18\xDE\xD8w}`  => 28:6E:D4:88:DA:F4,18:DE:D8:77:7D:60

4. 时间：
    单位为毫秒，时间这里会去掉前后多余的空格，而之前的内容，都不会去掉空格，因为空格也是ASCII码。

# 例子
\x00\x00\x00\x00ZE\xC8L 十进制：1514522700 （这里时间单位的秒）乘上1000，才能正确转为时间：2017-12-29 12:45:00。