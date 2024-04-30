package com.yuth.sql.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.yuth.sql.helper.model.AclNodeMModel;

public class Acl2SqlHelper {

    private static final String sep = ", ";

    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //


    public static void genSql(String path, long roleId) throws IOException {
        List<Long> menuIds = genMenuSql(path);
        genMenuRoleSql(menuIds, roleId);

    }

    /**
     * @param path
     * @return menu id
     * @throws IOException
     */
    public static List<Long> genMenuSql(String path) throws IOException {
        System.out.println("-- 菜单");
        List<Long> menuIds = new ArrayList<>();
        byte[] bs = Files.readAllBytes(Paths.get(path));
        List<AclNodeMModel> arr = JSON.parseArray(new String(bs), AclNodeMModel.class);
        long step = 1000L;
        long id = step;
        int level = 1;
        int menuType = 1;
        int btnType = 2;
        for (AclNodeMModel e : arr) {
            System.out.println(genMenuSql(id, 0L, e.getName(), e.getPath(), e.getMeta().getTitle(), level, menuType));
            // children
            if (e.getChildren() != null) {
                long cid = id + (step / 10);
                for (AclNodeMModel c : e.getChildren()) {
                    System.out.println(
                            genMenuSql(cid, id, c.getName(), c.getPath(), c.getMeta().getTitle(), level + 1, menuType));
                    menuIds.add(cid);

                    // 按钮
                    List<Long> ids = genMenuBtnSql(cid, cid, c.getName(), c.getPath(), level + 2, btnType);
                    menuIds.addAll(ids);
                    cid += (step / 10);
                }
            }

            menuIds.add(id);
            id += step;
        }

        return menuIds;
    }

    private static String genMenuSql(long id, long pid, String code, String url, String menuName, int level, int type) {
        StringBuilder t = new StringBuilder();
        t.append(
                "INSERT INTO `t_menu` (`menu_id`, `pid`, `pattern`, `code`, `menu_name`, `level`, `type`, `gmt_create`, `gmt_modified`) ");
        t.append("VALUES (");
        t.append(id).append(sep);
        t.append(pid).append(sep);
        t.append(wrapValue(url)).append(sep);
        t.append(wrapValue(code)).append(sep);
        t.append(wrapValue(menuName)).append(sep);
        t.append(level).append(sep);
        t.append(type).append(sep);
        t.append("CURRENT_TIMESTAMP").append(sep);
        t.append("CURRENT_TIMESTAMP");

        t.append(");");

        return t.toString();
    }

    private static void genMenuRoleSql(List<Long> menuIds, long roleId) {
        System.out.println("\n\n-- 菜单角色");
        long refId = 1;
        for (Long id : menuIds) {
            System.out.println(genMenuRoleSql(refId, id, roleId));
            refId++;
        }
    }

    private static String genMenuRoleSql(long refId, long menuId, long roleId) {

        StringBuilder t = new StringBuilder();
        t.append("INSERT INTO `t_menu_role` (`ref_id`, `menu_id`, `role_id`, `gmt_create`, `gmt_modified`) ");
        t.append("VALUES (");
        t.append(refId).append(sep);
        t.append(menuId).append(sep);
        t.append(roleId).append(sep);
        t.append("CURRENT_TIMESTAMP").append(sep);
        t.append("CURRENT_TIMESTAMP");

        t.append(");");

        return t.toString();
    }

    private static List<Long> genMenuBtnSql(long idStart, long pid, String code, String url, int level, int type) {
        System.out.println(
                genMenuSql(idStart + 1, pid, String.format("btn.%s.add", code), url + "/add", "添加", level, type));
        System.out.println(
                genMenuSql(idStart + 2, pid, String.format("btn.%s.update", code), url + "/update", "修改", level, type));
        System.out.println(
                genMenuSql(idStart + 3, pid, String.format("btn.%s.remove", code), url + "/remove", "删除", level, type));

        return Arrays.asList(idStart + 1, idStart + 2, idStart + 3);
    }

    private static String wrapValue(String s) {
        return "'" + s + "'";
    }

    public static void main(String[] args) throws IOException {
        genSql("./data/acl-1.json", 1);

    }

}
