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
        $(function () {
            //窗口
            $("#mybot").click(function () {
                $("#mywindow").window('open');
            });
        })
    </script>
</head>

<body>
<input id="mybot" type="button" value="输入"/><input type="text" value="请输入名字"/>
<div id="mywindow" class="easyui-window" data-options="title:'窗口',collapsible:false,modal:true"
     style="width: 500px; height: 400px;">浮动窗口
</div>
</body>

</html>