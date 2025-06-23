-- 检查是否已存在管理员账户
MERGE INTO users (username, password, email, role, active, created_at) 
KEY(username)
VALUES (
    'admin',                                    -- 用户名
    'admin123',                                -- 密码（实际项目中应该使用加密密码）
    'admin@example.com',                       -- 邮箱
    'ADMIN',                                   -- 角色
    true,                                      -- 账户状态
    CURRENT_TIMESTAMP()                        -- 创建时间
); 