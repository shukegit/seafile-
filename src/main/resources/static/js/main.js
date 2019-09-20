<script>
    $(function () {
        $("#right2").hide();
        $("#right3").hide();
        $("#right4").hide();
        $("#right5").hide();
        $("#right6").hide();

        $("#mybase").on("click",function () {
            $("#right1").toggle();
            $("#right2").hide();
            $("#right3").hide();
            $("#right4").hide();
            $("#right5").hide();
            $("#right6").hide();
        })

        $("#enjoyGroup").on("click",function () {

            $("#right1").hide();
            $("#right2").toggle();
            $("#right3").hide();
            $("#right4").hide();
            $("#right5").hide();
            $("#right6").hide();
        })

        $("#enjoybase").on("click",function () {
            $("#right1").hide();
            $("#right2").hide();
            $("#right3").toggle();
            $("#right4").hide();
            $("#right5").hide();
            $("#right6").hide();
        })

        $("#ziliaoku").on("click",function () {
            $("#right1").hide();
            $("#right2").hide();
            $("#right3").hide();
            $("#right4").toggle();
            $("#right5").hide();
            $("#right6").hide();
        })


        $("#wenjianjia").on("click",function () {
            $("#right1").hide();
            $("#right2").hide();
            $("#right3").hide();
            $("#right4").hide();
            $("#right5").toggle();
            $("#right6").hide();
        })

        $("#lianjie").on("click",function () {
            $("#right1").hide();
            $("#right2").hide();
            $("#right3").hide();
            $("#right4").hide();
            $("#right5").hide();
            $("#right6").toggle();
        })
    })

    function addTr(){

        var gList = [
            { "name": "001", "size": "camera1", "time": "20" ,"del":"删除"},

        ]

        var tableObject = document.getElementById("added");
        for (var i = 0; i < gList.length; i++) {
            var rows = tableObject.insertRow(i + 1);
            var name = rows.insertCell(0);
            var size= rows.insertCell(1);
            var time = rows.insertCell(2);
            var del = rows.insertCell(3)

            name.innerHTML = gList[i].name;
            size.innerHTML = gList[i].size;
            time.innerHTML = gList[i].time;
            del.innerHTML = gList[i].del;

            name.innerHTML.align = "center";
        }
    }
</script>