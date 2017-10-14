<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <script type="text/javascript" src="../js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" href="../js/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" href="../js/easyui/themes/icon.css"/>

    <script type="text/javascript">
        //页面加载后完成
        $(function () {
            //对链接添加绑定事件
            $("#jcdasz").click(function () {
                //添加一个新的选项卡
                $("#mytabs").tabs('add', {
                    title: '基础档案设置',
                    content: '基础档案编号'
                });
            });
        });
    </script>
</head>

<body>

<body class="easyui-layout">
<div data-options="region:'north',split:true" style="height:100px;"></div>
<div data-options="region:'south',split:true" style="height:100px;"></div>
<div data-options="region:'east',iconCls:'icon-reload',title:'',split:true" style="width:200px;"></div>
<div data-options="region:'west',title:'菜单导航',split:true" style="width:250px;">
    <!--折叠面板-->
    <div class="easyui-accordion" data-options="fit:true">
        <div data-options="title:'基本功能'">
            <!---->
            <a href="javascript:void(0)" id="jcdasz">基础档案设置</a>
        </div>
        <div data-options="title:'系统管理'">面板二</div>
    </div>
</div>
<div data-options="region:'center',title:'中部'" style="padding:5px;background:#eee;">
    <div id="mytabs" class="easyui-tabs" data-options="fit:true">
        <div data-options="title:'选项卡面板一',closable:true">tab1</div>
        <div data-options="title:'选项卡面板二',closable:true">tab2</div>
    </div>

</div>

</body>

</body>

</html>