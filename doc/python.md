
# matplotlib
## 不支持中文
1. 下载字体：http://www.font5.com.cn/font_download.php?id=151&part=1237887120
2. 拷贝到如下目录中：/Users/huangshihe/anaconda/lib/python2.7/site-packages/matplotlib/mpl-data/fonts/ttf
3. 删除字体缓存：rm -rf ~/.matplotlib/*.cache
4. 如下进行使用：
```python

from matplotlib import pyplot
pyplot.rcParams['font.sans-serif'] = ['simhei']
pyplot.ylabel(u"中文y坐标")

```
# 参考
[matplotlib图例中文乱码?](https://www.zhihu.com/question/25404709)  
[numpy读写文件](https://blog.csdn.net/qq_24330285/article/details/51576912)  



