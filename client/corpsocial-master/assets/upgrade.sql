#此文件用于数据库升级。配合system.properties中的databaseVer使用。
#格式说明：以#开始的行表示是注释
#         以version:xxx 开始的行表示版本号。其中xxx表示版本号，与system.properties中的databaseVer对应。
#         所有语句必须按顺序排列。即，1. 按版本号升序排列；2. 每个版本中的SQL语句按执行顺序排列。

#当system.properties中的databaseVer发生变化时，会自动触发，且只触发一次数据库升级。
#例如，当databaseVer从1变成3时，会执行(version:2及以后，version:4之前，即version2和3)的行，每行作为一个单独的SQL语句执行。

version：1

#version:2
#ALTER TABLE 'news' ADD COLUMN topFlag text;