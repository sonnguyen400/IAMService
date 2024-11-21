insert into "IAM_Service".public.permission
values (1,'SUPER_ADMIN'),
       (2,'ADMIN'),
        (3,'USER');
insert into "IAM_Service".public.permission_detail(id,name,permission_id)
values
    (1,'GRAND_PERMISSION',1),
    (2,'CHANGE_USER_PROFILE',2),
    (3,'CHANGE_USER_PASSWORD',2),
    (4,'CHANGE_PROFILE',3),
    (5,'CHANGE_PASSWORD',3);
