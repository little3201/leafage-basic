# abeille-basic-assets
assets module of abeille basic

系统存放经验、学习文章总结，旅行心得，旅拍照片等，

每一条记录都有一个business_id，其遵循规则如下：

1. 参照字节码访问标志来存储博文的所属分类;
2. 前两位为标示位，后四位为随机混淆位，中间2-3位为创建的日期，月份占一位，使用16进制表示；


