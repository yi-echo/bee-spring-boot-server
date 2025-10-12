-- 简化的数据初始化脚本

-- 插入角色数据
INSERT INTO roles (id, name, code, description) VALUES
(1, 'ROLE_ADMIN', 'SUPER_ADMIN', '超级管理员'),
(2, 'ROLE_USER', 'TEST', '测试用户');

-- 插入权限数据
INSERT INTO permissions (id, name, code, description, resource, action, is_active) VALUES
('permission_create', 'permission-create', 'permission:create', '创建权限', 'permission', 'create', true),
('permission_read', 'permission-read', 'permission:read', '查看权限', 'permission', 'read', true),
('permission_update', 'permission-update', 'permission:update', '更新权限', 'permission', 'update', true),
('permission_delete', 'permission-delete', 'permission:delete', '删除权限', 'permission', 'delete', true);

-- 插入简单的菜单数据
INSERT INTO menus (id, name, code, parent_id, path, icon, sort_order, is_active, type, component, caption, info, auth, hidden, disabled, external_link) VALUES
('group_dashboard', 'sys.nav.dashboard', 'dashboard', '', '', '', 1, true, 0, NULL, NULL, NULL, NULL, false, false, NULL),
('workbench', 'sys.nav.workbench', 'workbench', 'group_dashboard', '/workbench', 'local:ic-workbench', 1, true, 2, '/pages/dashboard/workbench', NULL, NULL, NULL, false, false, NULL);

-- 插入用户数据
INSERT INTO users (id, username, email, password, avatar, nickname, phone, is_active) VALUES
(1, 'admin', 'admin@slash.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'https://avatars.githubusercontent.com/u/15075420', '系统管理员', '13800138000', true),
(2, 'test', 'test@slash.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'https://avatars.githubusercontent.com/u/15075421', '测试用户', '13800138001', true);

-- 插入用户角色关联数据
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin用户 -> 超级管理员角色
(2, 2); -- test用户 -> 测试用户角色

-- 插入角色权限关联数据
INSERT INTO role_permissions (role_id, permission_id) VALUES
-- 超级管理员拥有所有权限
(1, 'permission_create'),
(1, 'permission_read'),
(1, 'permission_update'),
(1, 'permission_delete'),
-- 测试用户只有读取和更新权限
(2, 'permission_read'),
(2, 'permission_update');

-- 插入角色菜单关联数据
INSERT INTO role_menus (role_id, menu_id) VALUES
-- 超级管理员角色拥有所有菜单
(1, 'group_dashboard'),
(1, 'workbench'),
-- 测试用户角色只有部分菜单
(2, 'group_dashboard'),
(2, 'workbench');
