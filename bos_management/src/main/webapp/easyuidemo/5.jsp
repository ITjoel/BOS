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
            // 警告窗口
//				 $.messager.alert("阿耀","你是秒男么?","warning");
//				 $.messager.alert("阿耀","你是秒男么?","error");
//				 $.messager.alert("阿耀","你是秒男么?","info");
//				 $.messager.alert("阿耀","你是秒男么?","question");

            //确认窗口
//				 $.messager.confirm("删除提示","确定删除吗？",function(result){
//					if(result){
//						alert("执行删除...");
//					}else{
//						alert("取消删除...");
//					}
//				});

            // 输入窗口

            /*$.messager.prompt("智障儿童统计","你叫什么名字？",function(val){
             alert("你好," + val);
             });*/

            //显示进度消息
            $.messager.progress({interval: 500});
            $.messager.progress('close');


            //右下角窗口
            $.messager.show({
                title: "标题",
                msg: "减价大促销 ， <a href='#'>请猛戳这里</a>",
                timeout: 5000 // 5秒后自动消失
            });
        })

    </script>
</head>

<body>

</body>

</html>