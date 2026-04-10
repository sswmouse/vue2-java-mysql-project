-- 将characters表的avatar_url字段从VARCHAR(500)改为TEXT类型
-- 以支持更长的图片URL链接

ALTER TABLE characters MODIFY COLUMN avatar_url TEXT COMMENT '角色外观图片URL';

-- 如果有错误，请检查表名和字段名是否正确
-- 在MySQL中，TEXT类型可以存储最多65,535个字符，足够存储长URL