-- Insert predefined roles only if they don't exist
INSERT INTO role (role_name)
SELECT 'admin'
    WHERE NOT EXISTS (SELECT 1 FROM role WHERE role_name = 'admin');

INSERT INTO role (role_name)
SELECT 'teammember'
    WHERE NOT EXISTS (SELECT 1 FROM role WHERE role_name = 'teammember');

INSERT INTO role (role_name)
SELECT 'client'
    WHERE NOT EXISTS (SELECT 1 FROM role WHERE role_name = 'client');